package RateLimiter;
/**
 * This class is for demo and testing purposes, this class extends Throttle class and it takes necessary action based on if a request 
 * has exceeded the limit or not.
 */

public final class TestThrottle extends Throttle {

	public void notExceeded()                       //this method will be executed when limit is not exceeded
	{
		System.out.println( timeStamp + " " + requests + " within limit" );
	}

	public void Exceeded()                         //this method will be executed when requests are within the limit
	{
		System.out.println( timeStamp + " " + requests + " limit exceeded" );
	}

}
