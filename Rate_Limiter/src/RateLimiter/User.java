package RateLimiter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a user, where we will track all the api's associated with the user.
 */

public final class User {
	
	private String userID;
	private ConcurrentHashMap <String, Api> apiMap = new ConcurrentHashMap<>();
	
	public User( String userID )
	{ 
		this.userID = userID;
	}
	
	public void addApi( String apiID )
	{                                                                             //for adding an Api with the default limit for api
		 if( ApiIndex.instance.contains( apiID ) )
			{
				long defaultlimit = ApiIndex.instance.getDefaultLimit( apiID );
				apiMap.put( apiID, new Api( apiID, defaultlimit ) );
			}
	}
	
	public void addApi( String apiID, long specificLimit )
	{                                                                            // for adding an Api with user specific limit (requests/ second)
		if( ApiIndex.instance.contains( apiID ) )	
			apiMap.put( apiID, new Api( apiID, specificLimit ) );
		
	}
	
	public void addApi( String apiID, long specificLimit, long frameLength )
	{                                                                           // for adding an Api with different limit units(requests/minute or requests/5seconds)
		if(ApiIndex.instance.contains(apiID))	
			apiMap.put( apiID, new Api( apiID, specificLimit, frameLength ) );	
	}
	
	public void removeApi( String apiID )
	{
		apiMap.remove( apiID );
	}

	public void removeAll()
	{
		apiMap.clear();
	}
	
	public synchronized void requested( String apiID, Throttle throttle ) 
	{
		if( apiMap.containsKey( apiID ) )                                // If the api is already added.
		{                        
			apiMap.get( apiID ).requested( throttle );
		}
		else if(ApiIndex.instance.contains( apiID ) )                   // If the api is not added explicitly then it will add the api with its default limit.
		{
			addApi( apiID );
			apiMap.get( apiID ).requested( throttle );	
		}
		else
		{                                                               //If the api is not present in the index then we are assuming no rate limiting is 
			throttle.status( true, System.currentTimeMillis(), 0, 0 );  //applied to the api.
			new Thread( throttle ).start();
		}
	}
	
	public Api getApi( String apiID )
	{
		return apiMap.get( apiID );
	}
    
	public String getUserID()
	{
		return userID;
	}
	
}
