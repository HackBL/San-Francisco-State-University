#include <pthread.h> 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define THREAD_NUM 8 // number of child threads 
#define BUFFER_SIZE 65536 // size of buffer 

char buffer[BUFFER_SIZE];
int summation[128]; // ASCII character counter for all child threads

struct Section
{
	int count[128]; // ASCII character counter for each child thread
	int start;
	int end;
};

void *runner( void *param ); 

int main( int argc, char *argv[] )
{
	struct Section section[THREAD_NUM]; // divide process into groups as each child thread
	int size; // size of the file
	FILE *fp;
	
	fp = fopen ( argv[1], "r" ); // open exist file and read only

	if ( argc != 2 ) { // check command argument
		fprintf( stderr, "usage: ./a.out <file>.txt\n" );
		return -1;
    	}	

	if ( fp == NULL ) { // check file exists
		fprintf( stderr, "failed to open %s\n", argv[1] );
		return -1;
	}

	fseek( fp, 0, SEEK_END ); // seek to end of file
	size = ftell( fp ) - 1;   // retrieve size of the file	       
	fseek( fp, 0, SEEK_SET ); // seek back to beginning of the file
	
	if( size + 1 <= BUFFER_SIZE ) // check whether size of file overload the buffer  

		fread( buffer, sizeof( char), BUFFER_SIZE, fp );
	else {
		fprintf( stderr, "Size of file cannot be stored into buffer\n" );
		return -1;
	}

	for ( int i = 0; i < THREAD_NUM; i++ ) {  // initialize partition of each child thread
            	section[i].start = size / THREAD_NUM * i;
		
		if ( i < THREAD_NUM - 1 )
			section[i].end = size / THREAD_NUM * (i+1) - 1; 
		else
			section[i].end = size;
	}	

	pthread_t tid[THREAD_NUM]; // thread identifiers for child threads

	pthread_attr_t attr; 	
	pthread_attr_init( &attr ); 
	
	for ( int i = 0; i < THREAD_NUM; i++ ) // create child threads 
		pthread_create( &tid[i], &attr, runner, &section[i] );

	for ( int i = 0; i < THREAD_NUM; i++ ) { // wait for child threads to exit 	
		pthread_join( tid[i], NULL );

		for ( int j = 0; j < 128; j++ ) // sum all threads 
			summation[j] += section[i].count[j];
	}

	for ( int i = 0; i < 128; i++ ) { // print result
		if ( i < 33 )	// hex character 
			printf( "%d occurrences of 0x%x\n", summation[i], i );
		else		// ASCII character
			printf( "%d occurrences of '%c'\n", summation[i], i );		
	}

	fclose( fp );	

	return 0;
}


void *runner( void *param )
{
	struct Section *section;
	int start, end;	
	int *count;	

	section = param;
	start = section->start;
	end = section->end;
	count = section->count;

	for ( int i = 0; i < 128; i++ )  //initialize counter of each ASCII character is 0
		count[i] = 0;
	
	for ( int i = start; i <= end; i++ )  // count occurrences of each ASCII char in each child thread
		count[(int) buffer[i]] += 1;
	

	pthread_exit(0);
}
