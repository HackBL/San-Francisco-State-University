#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

void menu();
void process();

int main()
{
	process();		
	return 0;
}

void menu() //display selection
{
	printf("1. Hello World!\n2. List files\n3. Exit\n");
	printf("Please select: ");
}

void process()	//access selection input
{
	pid_t pid;   
	int selection;
        char userInput[10];

	menu();
	fgets( userInput, 10, stdin );
	selection = atoi( userInput );

	do { 
		if ( selection == 1 ) { //Hello World!
			pid = fork(); 

			if ( pid < 0 ){	
				fprintf(stderr, "Fork failed!\n");	
			}
			else if ( pid == 0 ){
				printf("Hello World!\n");
				//exit child process
				exit( 0 );
			}
			else {
				wait( NULL );
				process();
			}
		}
		else if ( selection == 2 ) { //list files
			pid = fork(); 

			if ( pid < 0 ){	
				fprintf(stderr, "Fork failed!\n");	
			}
			else if ( pid == 0 ){
				execlp("/bin/ls", "ls", NULL);
			}
			else {
				wait( NULL );
				process();
			}
		} 
		else if ( selection == 3 ) { //exit
			exit( 0 );
		}
		else {	//invalid choice
			printf("Invalid choice!\n");
			process();
		}
	
	} while( true );	
}


