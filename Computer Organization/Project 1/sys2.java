/*
 *  The program is written by java
 *
 *  Instructions:
 *
 *      - Compile source file:
 *          javac sys2.java
 *
 *      - Run the program:
 *          non-verbose mode:
 *              java Sys2 <trace file> N M
 *
 *          verbose mode:
 *              java Sys2 <trace file> N M -v
 */

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
    String tag;
    int valid;
    
    public Target( String tag, int valid )
    {
        this.tag = tag;
        this.valid = valid;
    }
}

class Sys2
{
    public static boolean takenPrediction( int val )
    {
        if ( val == 2 || val == 3 )
            return true;
        return false;
    }
    
    public static boolean notTakenPrediction( int val )
    {
        if ( val == 0 || val == 1 )
            return true;
        return false;
    }
    
    public static int predictorState( int val ) {
        if  ( val < 0 )
            return 0;
        if ( val > 3 )
            return 3;
        return val;
    }
    
    public static void simulate( InputStream incomingStream, PrintStream outputStream, final int PB_buffer_size, final int BTB_buffer_size, boolean verbose ) throws Exception
    {
        // offsets length
        int PB_offset_length = (int)( Math.log( PB_buffer_size ) / Math.log( 2 ) );
        int BTB_offset_length = (int)( Math.log( BTB_buffer_size ) / Math.log( 2 ) );
        
        // offsets
        String PB_offset;
        String BTB_offset;
        
        // buffers
        int PB_buffer[] = new int[PB_buffer_size];
        Target BTB_buffer[] = new Target[BTB_buffer_size];
        
        // initialize PB_buffer
        for ( int i = 0; i < PB_buffer_size; i++ )
            PB_buffer[i] = 1;
        
        // See the documentation to understand what these variables mean.
        long instructionAddress;
        String TNnotBranch;
        long targetAddressTakenBranch;
        
        int branches = 0;
        int forward_branches = 0;
        int forward_taken_branches = 0;
        int backward_branches = 0;
        int backward_taken_branches = 0;
        int mispredictions = 0;
        double predictionsRate = 0.0;
        double missesRate = 0.0;
        
        String binaryPC;
        
        boolean taken = false;
        boolean notTaken = false;
        
        int order = 0;
        int hit = 0;
        int access = 0;
        int miss = 0;
        
        BufferedReader r = new BufferedReader( new InputStreamReader( incomingStream ) );
        String line;
        
        while ( true ) {
            line = r.readLine();
            if ( line == null ) {
                break;
            }
            String [] tokens = line.split("\\s+");
            
            instructionAddress = Long.parseLong( tokens[1], 16 );
            TNnotBranch = tokens[6];
            targetAddressTakenBranch = Long.parseLong( tokens[11], 16 );
            
            // binary of PC
            binaryPC = Long.toBinaryString( instructionAddress );
            
            PB_offset = binaryPC.substring( binaryPC.length() - PB_offset_length, binaryPC.length() );
            BTB_offset = binaryPC.substring( binaryPC.length() - BTB_offset_length, binaryPC.length() );
            
            if ( !TNnotBranch.equals("-") ) {
                boolean correct = false;
                int PB_index; //temp assign
                int PB_cur_state; // temp assign
                int PB_next_state = 1;
                int BTB_index; // temp assign
                String BTB_cur_branch_tag; //temp assign
                
                branches++; // number of branches
                order = branches;
                
                PB_index = Integer.parseInt( PB_offset, 2 ); // branch's index of PB buffer
                PB_cur_state = PB_buffer[PB_index];    // get prediction for current branch
                
                BTB_index = Integer.parseInt( BTB_offset, 2 ); // branch's index of BTB buffer
                BTB_cur_branch_tag = String.format( "%x",Integer.parseInt( binaryPC.substring( 0, binaryPC.length() - BTB_offset_length ), 2 ) );
                
                
                // get prediction for B
                taken = takenPrediction( PB_cur_state );    // predicted taken
                notTaken = notTakenPrediction( PB_cur_state ); // predicted not taken
                
                if ( taken ) {
                    if ( BTB_buffer[BTB_index].valid == 1 && BTB_buffer[BTB_index].tag.equals( BTB_cur_branch_tag ) )
                        hit++;
                    else
                        miss++;
                } else if ( notTaken ){
                    // do nothing
                }
                
                access = hit + miss;    // number of access
                
                // correct if prediction == actual behavior, else is wrong
                if ( TNnotBranch.equals("T") ) {
                    if ( taken )
                        correct = true;
                    else
                        mispredictions++;
                }
                
                if ( TNnotBranch.equals("N") ) {
                    if ( notTaken )
                        correct = true;
                    else
                        mispredictions++;
                }
                
                // update prediction
                if ( TNnotBranch.equals("T") ) {
                    PB_next_state = predictorState( PB_cur_state + 1 );
                    PB_buffer[PB_index] = PB_next_state;
                }
                
                if ( TNnotBranch.equals("N") ) {
                    PB_next_state = predictorState( PB_cur_state - 1 );
                    PB_buffer[PB_index] = PB_next_state;
                }
                
                // store tag and valid into BTB buffer
                if ( TNnotBranch.equals("T") )
                    BTB_buffer[BTB_index] = new Target( BTB_cur_branch_tag, 1 );
                
                if ( verbose )
                    outputStream.format( "%d %d %d %d %d %s %d %d\n\n", order-1, PB_index, PB_cur_state, PB_next_state, BTB_index, BTB_cur_branch_tag, access, miss );
                
                if ( targetAddressTakenBranch - instructionAddress > 0 ) {
                    forward_branches++;    // number of forward branches
                    if ( TNnotBranch.equals("T") )
                        forward_taken_branches++; // number of forward taken branches
                    
                } else if ( targetAddressTakenBranch - instructionAddress < 0 ) {
                    backward_branches++; // number of backward branches
                    if ( TNnotBranch.equals("T") )
                        backward_taken_branches++; // number of backward taken branches
                }
            }
        }
        
        // predictions rate
        predictionsRate = (double)mispredictions / (double)branches;
        
        // misses rate
        missesRate = (double)miss / (double)access;
        
        outputStream.format( "Number of branches = %d\n", branches );
        outputStream.format( "Number of forward branches = %d\n", forward_branches );
        outputStream.format( "Number of forward taken branches = %d\n", forward_taken_branches );
        outputStream.format( "Number of backward branches = %d\n", backward_branches );
        outputStream.format( "Number of backward taken branches = %d\n", backward_taken_branches );
        outputStream.format( "Number of mispredictions = %d %f\n", mispredictions, predictionsRate );
        outputStream.format( "Number of BTB misses = %d %f\n", miss, missesRate );
    }
    
    public static void main(String[] args) throws Exception
    {
        InputStream inputStream = System.in;
        PrintStream outputStream = System.out;
        int pb = 0;
        int btb = 0;
        
        if ( args.length >= 1 ) {
            inputStream = new FileInputStream( args[0] );
        }
        
        if ( args.length >= 2 ) {
            pb = Integer.parseInt( args[1] );
        }
        
        if ( args.length >= 3 ) {
            btb = Integer.parseInt( args[2] );
        }
        
        if ( args.length >= 4 ) {
            if ( args[3].equals("-v") )
                Sys2.simulate( inputStream, outputStream, pb, btb, true);
        } else
            Sys2.simulate( inputStream, outputStream, pb, btb, false);
    }
}
