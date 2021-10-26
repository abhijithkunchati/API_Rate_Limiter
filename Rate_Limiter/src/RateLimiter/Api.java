package RateLimiter;

/**
 * This is the class where the tracking of requests is implemented.
 */

public final class Api {
	
	private String   apiID;                       //unique for each API
	private long     frameLength = 1000L;         //limit may be in requests per second or per minute, default is 1 second
	private long     limit;                       //max number of requests for a time frame
	private long     previousRequests = 0L;       //number of requests in previous time frame
	private long     currentRequests  = 0L;       //number of requests in current time frame
	private long     currentFrameStart;           //time stamp of the start of current time frame


	Api( String apiID, long limit )             
	{  
		this.apiID = apiID;
		this.limit = limit;
		this.currentFrameStart = System.currentTimeMillis();
	}
	
	Api( String apiID, long limit, long frameLength )
	{
		this.apiID = apiID;
		this.limit = limit;
		this.frameLength = frameLength;	
		this.currentFrameStart = System.currentTimeMillis();
	}
	

	public synchronized void requested(Throttle throttle)
	{
	
		long currentTime = System.currentTimeMillis();             
		long timeElapsed = currentTime - currentFrameStart;        //time between now and start of time frame
		
		if( timeElapsed > ( 2 * frameLength) )
		{
			previousRequests = 0L;                                  //new time frame
			currentRequests = 1L;
			currentFrameStart = currentTime;
			throttle.status( true, currentTime, 1l, limit );
			new Thread( throttle ).start();
		}
		else if( timeElapsed > frameLength )
		{                                                           //new time frame
			previousRequests = currentRequests;
			currentRequests = 1L;
			currentFrameStart += frameLength;
			throttle.status( true, currentTime, 1l, limit );
			new Thread( throttle ).start();
		}
		else
		{                                                           //same time frame
			
			long weightage = ( ( frameLength - timeElapsed ) * previousRequests ) / frameLength;
			long requests = currentRequests + weightage;
			
			if(requests < limit )
			{
				currentRequests++;                                    //////within limit
				throttle.status( true, currentTime, currentRequests, limit );
				new Thread( throttle ).start();
			}
			else
			{                                                         /////limit exceeded          
				throttle.status( false, currentTime, currentRequests, limit );
				new Thread( throttle ).start();    
			}
		
		}
		
	}
	
	
	public long getLimit()
	{
		return limit;
	}

	
	public long getCurrentRequests()
	{
		return currentRequests;
	}
	
	
	public void incrementLimit()
	{
		limit += 100;
	} 
	

	public void incrementLimit( long inc )
	{
		limit += inc;
	}
	
	
	public void decrementLimit()
	{
		if( limit <= 100L ) limit = 1;
		else           limit -= 100;
	}
	

	public void decrementLimit( long dec )
	{
	    if( dec >= limit ) limit = 0;
	    else            limit -= dec;
	}
	
	public void setLimit( long limit )
	{
		if( limit >= 0L )
		this.limit = limit;
	}
	

	public void setFrameLength( long frameLength )
	{
		if( frameLength >= 0L )
		this.frameLength = frameLength;
	}
	

	public String getApiID()
	{
		return apiID;
	}
	
	
}
