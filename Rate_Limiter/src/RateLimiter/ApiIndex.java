package RateLimiter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This singleton class is used for storing information about which Api's the rate limit is applied and their default limit.
 */

public final class ApiIndex {
	
	private ApiIndex() {}                                  //making the constructor as private for implementing singleton class.
	public static final ApiIndex instance = new ApiIndex();
	private long defaultLimit = 100;
	private ConcurrentHashMap <String, Long> indexMap = new ConcurrentHashMap<>();
	
	public boolean contains( String apiID )
	{                                                       //method for checking if a specific api is present in table
		if( indexMap.containsKey( apiID ) ) 
			return true;
		
		return false;
	}
	
	public long getDefaultLimit( String apiID )
	{
		return indexMap.get( apiID );                      //will return default limit for a specific api   
	}
	
	public long getDefaultLimit()
	{
		return defaultLimit;                               //will return default limit for general api
	}
	
	public void addApi( String apiID )
	{                                                      //if limit for each api is not specified then general default limit is assumed
		indexMap.put( apiID, defaultLimit );
	}
	
	public void addApi( String apiID, long specificLimit ) // api is added with a specific default limit
	{
		indexMap.put( apiID, specificLimit );
	}
	
	public void setDefaultLimit( long limit )
	{
		defaultLimit = limit;
	}
	

}
