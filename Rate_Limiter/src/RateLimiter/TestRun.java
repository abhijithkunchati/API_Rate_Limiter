package RateLimiter;

/**
 * This class is for demo and testing purposes, used for sending the requests. 
 */

public final class TestRun {

	/**
	 * We run this method for simulating the user requests
	 */
	
	public static void main( String args[] ) throws InterruptedException
	{
	   Manager manager = Manager.instance;
	   manager.addUser( "abhi" );                                          //adding the user and api.
	   manager.addApi( "/dev", 10 );
	 
	   int  noOfRequests = 20;                                             //number of requests for simulation
	   long timeBwRequests = 50;                                           //time between each requests
	  
	   for(int i=0; i < noOfRequests; i++)
	   {
		    manager.requested( "abhi", "/dev", new TestThrottle() );       //this is a single thread implementation.
		   
		    //new TestRunThread( manager, new TestThrottle() ).start();    //this is multi-threaded implementation look into 
		                                                                   //TestRunThread class for more details.
		    Thread.sleep( timeBwRequests );
	   }
	   
	}
	
}
