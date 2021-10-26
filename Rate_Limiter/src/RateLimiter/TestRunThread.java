package RateLimiter;

/** 
 * This class is for demo and testing purposes, this class used for simulation of requests on multiple threads.
 */

public final class TestRunThread extends Thread{
	/**
	 * We send requests to manager object.
	 */
	Manager  manager;
	/**
	 * This is used for call back.
	 */
	Throttle throttle;
	

	TestRunThread( Manager manager, Throttle throttle )
	{
		this.manager = manager;
		this.throttle = throttle;
	}
	
	/**
	 * Will send requests to manager object on a seperate thread.
	 */
	public void run()
	{
		 manager.requested( "abhi", "/dev", throttle );
	}
	
	
}
