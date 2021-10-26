package RateLimiter;
/**
 * This class is used for call back to inform the application if the user has exceeded the limit or not.
 */
 public abstract class Throttle implements Runnable{
	 
	
	 private boolean  statusCode = true;           //false indicates request limit is crossed
	 protected long timeStamp;
	 protected long requests;
	 protected long limit;
	 public abstract void notExceeded();           //should implement these methods according to requirement
	 public abstract void    Exceeded();
	 
	 public void status( boolean statusCode, long currentTime, long requests, long limit )
	 {                                              
		 this.statusCode = statusCode;
		 this.timeStamp = currentTime;
		 this.requests = requests;
		 this.limit = limit;
	 }
	  
	 public void run()
	 {                                              //will run either method based on weather the limit has been exceeded or not
		 if(statusCode) notExceeded();
		 else              Exceeded();
	 }
    
}
