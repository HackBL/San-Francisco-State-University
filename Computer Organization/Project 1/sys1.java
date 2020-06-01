/*
 *  The program is written by java
 *
 *  Instructions:
 *
 *      - Compile source file:
 *          javac sys1.java
 *
 *      - Run the program:
 *          java Sys1 <trace file>
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

class Sys1
{
    public static void simulate( InputStream incomingStream, PrintStream outputStream) throws Exception
    {
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
            
            if (!TNnotBranch.equals("-")) {
                branches++; // number of branches
                
                if ( targetAddressTakenBranch - instructionAddress > 0 ) {
                    forward_branches++;    // number of forward branches
                    
                    if ( TNnotBranch.equals("T") ) {
                        forward_taken_branches++; // number of forward taken branches
                        mispredictions++; // number of misprediction
                    }
                    
                    
                } else if ( targetAddressTakenBranch - instructionAddress < 0 ) {
                    backward_branches++; // number of backward branches
                    
                    if ( TNnotBranch.equals("T") )
                        backward_taken_branches++; // number of backward taken branches
                    
                    if ( TNnotBranch.equals("N") )
                        mispredictions++; // number of mispredictions
                }
            }
        }
        
        // predictions rate
        predictionsRate = (double)mispredictions / (double)branches;
        
        outputStream.format( "Number of branches = %d\n", branches );
        outputStream.format( "Number of forward branches = %d\n", forward_branches );
        outputStream.format( "Number of forward taken branches = %d\n", forward_taken_branches );
        outputStream.format( "Number of backward branches = %d\n", backward_branches );
        outputStream.format( "Number of backward taken branches = %d\n", backward_taken_branches );
        outputStream.format( "Number of mispredictions = %d %f\n", mispredictions, predictionsRate );
    }
    
    public static void main(String[] args) throws Exception
    {
        InputStream inputStream = System.in;
        PrintStream outputStream = System.out;
        
        if ( args.length >= 1 ) {
            inputStream = new FileInputStream( args[0] );
        }
        
        if ( args.length >= 2 ) {
            outputStream = new PrintStream(args[1]);
        }
        
        Sys1.simulate(inputStream, outputStream);
    }
}

