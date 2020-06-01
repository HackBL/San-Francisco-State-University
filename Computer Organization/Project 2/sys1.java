/*
 *  The program is written by java
 *
 *  Instructions:
 *      - Compile source file:
 *              javac sys1.java
 *
 *      - Run the program:
 *              non-verbose mode:
 *                    java Sys1 tracefile cachesize
 *
 *              verbose mode:
 *                    java Sys1 tracefile cachesize -v ic1 ic2
**/


import java.lang.Exception;
import java.lang.Long;
import java.lang.String;
import java.lang.System;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.math.*;

class Target
{
    int valid;
    int dirty;
    String tag;
    
    public Target( int valid, int dirty, String tag )
    {
        this.valid = valid;
        this.dirty = dirty;
        this.tag = tag;
    }
}

class Sys1
{
    public static boolean powerOfTwo( int number ){
        int square = 1;
        while( number >= square ){
            if( number == square ){
                return true;
            }
            square = square*2;
        }
        return false;
    }
    
    public static void simulate( InputStream incomingStream, PrintStream outputStream, final int cache_size, boolean verbose, int start, int end ) throws Exception
    {
        int block_byte = 16;
        int cache_buf_size = cache_size * 1024 / block_byte;    // number of blocks of cache
        int offset_length = (int) ( Math.log( block_byte ) / Math.log( 2 ) );    // number of offset of data
        int index_length = (int) ( Math.log( cache_buf_size ) / Math.log( 2 ) );    // number of index of data
        
        Target cache[] = new Target[cache_buf_size];
        
        // verbose data
        String index_binary;
        String index_hex;
        String tag_binary;
        String binary_data_addr;
        String tag;
        int reference = 0;
        int index;
        
        // collected statistics
        int reads = 0;
        int writes = 0;
        int accesses = 0;
        int read_misses = 0;
        int write_misses = 0;
        int total_misses = 0;
        int dirty_read_misses = 0;
        int dirty_write_misses = 0;
        int total_dirty_misses = 0;
        int read_from_mem = 0;
        int write_to_mem = 0;
        int read_cycle = 0;
        int write_cycle = 0;
        int total_cycle = 0;    // total access time
        double miss_rate = 0.0;
        
        // See the documentation to understand what these variables mean.
        long instructionAddress;
        String loadStore;
        long addressForMemoryOp;
        
        // init cache
        for ( int i = 0; i < cache_buf_size; i++ )
            cache[i] = new Target( 0, 0, "0" );
        
        
        BufferedReader r = new BufferedReader( new InputStreamReader( incomingStream ) );
        String line;
        
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            String [] tokens = line.split("\\s+");
            
            instructionAddress = Long.parseLong( tokens[1], 16 );
            loadStore = tokens[7];
            addressForMemoryOp = Long.parseLong( tokens[9], 16 );
            
            if ( !loadStore.equals("-") ) {
                // verbose data
                int hit_miss = 0;
                String case_num = "";
                
                // binary of data address
                binary_data_addr = Long.toBinaryString( addressForMemoryOp );

                // binary of index
                index_binary = binary_data_addr.substring(  binary_data_addr.length() - ( index_length + offset_length ), binary_data_addr.length() - offset_length );
                
                // hex of index
                index_hex = Long.toHexString( Long.parseLong( index_binary, 2 ) );
                
                // decimal of index
                index = Integer.parseInt( index_binary, 2 );
                
                // binary of tag
                tag_binary = binary_data_addr.substring( 0, binary_data_addr.length() - ( index_length + offset_length ) );
                
                // hex of tag
                tag = Long.toHexString( Long.parseLong( tag_binary, 2 ) );
                
                // verbose mode
                if (verbose) {
                    if (reference >= start && reference <= end)  // for testing range
                        outputStream.format( "%d %s %s %s %d %s %d ", reference,
                                            Long.toHexString( addressForMemoryOp ),
                                            index_hex,
                                            tag,
                                            cache[index].valid,
                                            cache[index].tag,
                                            cache[index].dirty );
                }
                
                // Case number according to Detailed Operation
                if ( tag.equals( cache[index].tag ) ) {    // case 1
                    if ( loadStore.equals("L") )    // read
                        // no state change, 1 cycle
                        read_cycle++;
                    
                    else {  // write
                        cache[index].dirty = 1;
                        write_cycle++;
                    }
                    
                    cache[index].valid = 1;
                    hit_miss = 1; // hit
                    case_num = "1";
                    
                } else { // block containing A is not found in data cache
                    if ( cache[index].tag.equals("0") || cache[index].dirty == 0 ) { // case 2a
                        if ( loadStore.equals("L") ) {  // read
                            cache[index].tag = tag;
                            cache[index].dirty = 0;
                            read_misses++;
                            read_cycle += 81;
                            
                        } else if ( loadStore.equals("S") ) {   // write
                            cache[index].tag = tag;
                            cache[index].dirty = 1;
                            write_misses++;
                            write_cycle += 81;
                        }
                        
                        cache[index].valid = 1;
                        hit_miss = 0;
                        case_num = "2a";
                        
                    } else if ( cache[index].dirty == 1 ) {     // case 2b
                        if ( loadStore.equals("L") ) {  // read
                            cache[index].tag = tag;
                            cache[index].dirty = 0;
                            read_misses++;
                            dirty_read_misses++;
                            read_cycle += 161;
                            
                        } else if ( loadStore.equals("S") ) {   // write
                            cache[index].tag = tag;
                            cache[index].dirty = 1;
                            write_misses++;
                            dirty_write_misses++;
                            write_cycle += 161;
                        }
                        
                        cache[index].valid = 1;
                        hit_miss = 0;
                        case_num = "2b";
                    }
                }
                
                // verbose mode
                if ( verbose ) {
                    if ( reference >= start && reference <= end )
                        outputStream.format( "%d %s\n", hit_miss, case_num );
                }
                
                reference++;

                if ( loadStore.equals("L") )
                    reads++;
                if ( loadStore.equals("S") )
                    writes++;
            }
        }
        
        // calculate statistics
        accesses = reads + writes;
        total_misses = read_misses + write_misses;
        total_dirty_misses = dirty_read_misses + dirty_write_misses;
        read_from_mem = total_misses * block_byte;
        write_to_mem = total_dirty_misses * block_byte;
        total_cycle = read_cycle + write_cycle;
        miss_rate = (double) total_misses / (double) accesses;
        
        // display collected statistics
        outputStream.format( "\ndirect-mapped, writeback, size = %dKB\n", cache_size );
        outputStream.format( "number of data reads = %d\n", reads );
        outputStream.format( "number of data writes = %d\n", writes );
        outputStream.format( "number of data accesses = %d\n", accesses );
        outputStream.format( "number of total data read misses = %d\n", read_misses );
        outputStream.format( "number of total data write misses = %d\n", write_misses );
        outputStream.format( "number of data misses = %d\n", total_misses );
        outputStream.format( "number of dirty data read misses = %d\n", dirty_read_misses );
        outputStream.format( "number of dirty write misses = %d\n", dirty_write_misses );
        outputStream.format( "number of bytes read from memory = %d\n", read_from_mem );
        outputStream.format( "number of bytes written to memory = %d\n", write_to_mem );
        outputStream.format( "the total access time (in cycles) for reads = %d\n", read_cycle );
        outputStream.format( "the total access time (in cycles) for writes = %d\n", write_cycle  );
        outputStream.format( "the total access time (in cycles) = %d\n", total_cycle  );
        outputStream.format( "the overall data cache miss rate = %f\n", miss_rate );
    }
    
    public static void main( String[] args ) throws Exception
    {
        InputStream inputStream = System.in;
        PrintStream outputStream = System.out;
        int cache_size = 0;
        int start = 0, end = 0;
        
        if ( args.length >= 1 )
            inputStream = new FileInputStream( args[0] );
        
        if ( args.length >= 2 ) {
            // cache size is power of 2
            if ( powerOfTwo( Integer.parseInt( args[1] ) ) )
                cache_size = Integer.parseInt( args[1] );
            else {
                System.out.println("Cache size has to be power of 2");
                return ;
            }
        }
        
        if ( args.length >= 5 ) {   // verbose mode arguments
            if ( args[2].equals("-v") ) {
                start = Integer.parseInt( args[3] );
                end = Integer.parseInt( args[4] );
                Sys1.simulate( inputStream, outputStream, cache_size, true, start, end );
            }
            
        } else
            Sys1.simulate( inputStream, outputStream, cache_size, false, start, end );
    }
}
