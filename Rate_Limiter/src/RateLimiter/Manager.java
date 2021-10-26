package RateLimiter;
import java.util.concurrent.ConcurrentHashMap;

/**
* This singleton class is used for managing all the users.
*/

public final class Manager {

	private Manager() {}                                          //making the constructor private for implementing singleton class.
	static final Manager instance = new Manager();
	private ConcurrentHashMap <String, User> userMap = new ConcurrentHashMap<>();
	private ApiIndex apiIndex = ApiIndex.instance;
     

	public void addUser( String userID )                          //Method for adding the user explicitly.
	{
		userMap.put( userID, new User( userID ));
	}
	

	public void removeUser( String userID )
	{
	   userMap.remove(userID);
	}
	

	public void removeAll()
	{
		userMap.clear();
	}
	

    public User getUser( String userID )
    {
    	return userMap.get( userID );
    }
    
 
    public boolean containsUser( String userID )
    {
    	return userMap.containsKey( userID );
    }
    
   
    public void addApi( String apiID )                         //This method will add a new api to the table with default limit.
    {
    	apiIndex.addApi( apiID );
    }
    
  
    public void addApi( String apiID, long defaultLimit )      //This overloaded method will add new api along with a specific default limit for it.
    {
    	apiIndex.addApi( apiID, defaultLimit );                
    }
    
    
    public void requested( String userID, String apiID, Throttle throttle )  //New user will be added implicitly ,if not present and
    {                                                                        //the request will be sent to user object.                                                        
    	if( !userMap.containsKey(userID) )
    		addUser( userID );
    	userMap.get( userID ).requested( apiID, throttle );
    }
   
    
}
