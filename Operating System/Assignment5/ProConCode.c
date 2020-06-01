#include <pthread.h> 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>

#define BUFFER_SIZE 16

void *consumer( void *param );
void *producer( void *param );

struct Producer_section
{
	int thread_index;
	int num_produced;
};

struct Consumer_section
{
	int thread_index;
	int num_consumed;
};


pthread_mutex_t mutex;
sem_t empty;
sem_t full;

int buffer[BUFFER_SIZE];
int in, out;
int producer_num, consumer_num;
int total_items;

int main( int argc, char *argv[] ) 
{
	/* check valid of Command-Line arguments */
	if ( argc != 4 )	// command-line arguments should have 4 parameters
	{	
		fprintf( stderr, "usage: ./ProConCode.out <integer value> <integer value> <integer value>\n" );
		return -1;
    	}	

	for ( int i = 1; i <= 3; i++ )	// each command line argument has to be greater than 0
	{	
		if ( atoi( argv[i] ) <= 0 ) 
		{
			fprintf( stderr, "%d must be > 0\n", atoi( argv[i] ) );
			return -1;
		}
	}

	/* initialization of data */
	producer_num = atoi( argv[1] );
	consumer_num = atoi( argv[2] );

	in = 0, out = 0;	
	sem_init( &empty, 0, BUFFER_SIZE );	
	sem_init( &full, 0, 0 );		
	
	pthread_mutex_init( &mutex, NULL ); 
	pthread_attr_t attr; 	
	pthread_attr_init( &attr ); 


	/* create producer child-thread */	
	struct Producer_section producerSection[producer_num];

	for ( int i = 0; i < producer_num; i++ )	// split buffer to each producer child thread
	{	
		producerSection[i].thread_index = i;
		producerSection[i].num_produced = atoi( argv[3] );	// number of items produced by each producer

		total_items += producerSection[i].num_produced;		// total items produced by all producers
	}
	
	pthread_t producer_tid[producer_num];	 // producer thread identifiers for child threads

	for ( int i = 0; i < producer_num; i++ ) 	// create producer child threads 
		pthread_create( &producer_tid[i], &attr, producer, &producerSection[i] );
	

	/* create consumer child-tread */
	struct Consumer_section consumerSection[consumer_num];

	for ( int i = 0; i < consumer_num; i++ )	// split buffer to each consumer child thread
	{	
		consumerSection[i].thread_index = i;
		consumerSection[i].num_consumed = total_items / consumer_num;	// number of items consumed by each consumer
	}
	
	pthread_t consumer_tid[consumer_num];	// consumer thread identifiers for child threads
	
	for ( int i = 0; i < consumer_num; i++ )	// create consumer child threads 
		pthread_create( &consumer_tid[i], &attr, consumer, &consumerSection[i] );


	/* join producer */
	for ( int i = 0; i < producer_num; i++ )	// wait for child threads to exit 	
		pthread_join( producer_tid[i], NULL );

	/* join consumer */
	for ( int i = 0; i < consumer_num; i++ )	// wait for child threads to exit 	
		pthread_join( consumer_tid[i], NULL );

	return 0;
}


void *producer( void *param )
{
	struct Producer_section *section;
	int thread_index, num_produced;

	int next_produced;	// item produced by producer
	section = param;
	thread_index = section->thread_index;
	num_produced = section->num_produced; 

	/* produce items into buffer */
	for ( int counter = 0; counter < num_produced; counter++ ) 
	{		
		/* entry section */
		sem_wait( &empty );
		pthread_mutex_lock( &mutex );

		/* critical section */			
		next_produced = thread_index * num_produced + counter;
		buffer[in] = next_produced;
		in = ( in + 1 ) % BUFFER_SIZE;		

		/* exit section */
		pthread_mutex_unlock( &mutex );
		sem_post( &full );
	} 
	
	pthread_exit(0);
}


void *consumer( void *param )
{
	struct Consumer_section *section;
	int thread_index, num_consumed;

	int next_consumed;	// item consumed by consumer
	section = param;
	thread_index = section->thread_index;
	num_consumed = section->num_consumed; 
		
	/* consume items from buffer */
	for ( int i = 0; i < num_consumed; i++ ) 
	{
		/* entry section */
		sem_wait( &full );
		pthread_mutex_lock( &mutex );

		/* critical section */
		next_consumed = buffer[out];
		printf( "Consumer %d consumes %d\n", thread_index, next_consumed);				
		out = ( out + 1 ) % BUFFER_SIZE; 

		/* exit section */
		pthread_mutex_unlock( &mutex );
		sem_post( &empty );	
	}	

	pthread_exit(0);
}
