#include <stdio.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/stat.h> 
#include <fcntl.h> 

#define BUFFER_SIZE 1024

int main( int argc, char *argv[] )
{
	char buffer[BUFFER_SIZE];
	int fd_read, fd_write;
	ssize_t in = 0, out = 0, total_bytes = 0;

	if ( argc != 3 ) { // check command argument
		fprintf( stderr, "usage: ./mycp.out <file1> <file2>\n" );
		return -1;
    	}	

	fd_read = open ( argv[1], O_RDONLY ); // read file1
	
	if ( fd_read < 0 )	
	{
		perror( "Failed to open file1.\n" );
		return -1;
	}	

	fd_write = open( argv[2], O_WRONLY | O_CREAT | O_EXCL, S_IRWXU ); // create and write file2

	if ( fd_write < 0 )
	{
		perror( "Failed to open file2.\n" );
		return -1;	
	}	
	
	in = read( fd_read, buffer, BUFFER_SIZE ); // pointer to buffer of reading file1

	for ( ; in > 0;  in = read( fd_read, buffer, BUFFER_SIZE ) )
	{
		out = write( fd_write, buffer, (ssize_t) in ); // pointer to buffer of writting file2

		if ( in != out )
		{
			perror( "Error to copy.\n" );
			return -1;		
		}
	
		total_bytes += out;
	}

	// close files
	close( fd_read );
	close( fd_write );	

	printf( "copied %li bytes\n", total_bytes );

	return 0;
}
