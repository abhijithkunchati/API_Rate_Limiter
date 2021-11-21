
 ##  Understanding Rate Limits
API rate limiting is simply controlling how many requests or calls an API
consumer can make to an API. It is not only essential for the efficient access of
backend resources but also to safeguard from DoS and DDoS attacks.
With rate limiting, we can enforce a limit on the number of requests a client can send
over a period of time. When the limit is exceeded we can throttle the requests to
prevent the backend services from over-loading.
Rate limiting can also be applied on the client-side but since we can't always
predict how the user might use the APIs, it is usually done on the server-side.

 ## The architecture of this solution

<kbd>
 <img src="Attachments/Architechture.png?raw=true">
</kbd></br></br>
It starts with the manager class, we use it for managing all the
User class objects. Each of the User objects represents a single
client, we can use this object to handle the APIs associated with this
user.
The Api class represents the API's and here is where the actual
tracking of the user’s API calls takes place. Each User may have
multiple API objects and we may have multiple User objects in the
manager class.
Furthermore, the ApiIndex class, you can think of it as a
table, where you have the information related to the API, for which
APIs you are applying rate limiting, and their default rate limit.
The User and Api objects have an ID field that differentiates them
from other User and Api objects.
The throttle class works as a call-back. we pass it along with the
request and the appropriate function will be called in the throttle
object, based on whether the user has exceeded his limit or not.
It is declared as abstract so that the methods can be configured
according to the requirements during the actual implementation.
 
 ##  Working
We first need to create a class that extends the Throttle and
implements the abstract methods. Then we can make a reference to
the manager class, add the API IDs to the ApiIndex and then we
can start processing the requests. For each request, we send the
userID, an ApiID, and a throttle to the manager.
If an object for a specific user is not present, then the manager
adds it implicitly and forwards the request to that object, the User
object will check for the respective Api object, if the api is not
present, it will look into the ApiIndex and creates a new Api object,
with its default limit and forwards the request to it.
The Api objects will calculate the number of requests and checks
if the limit has been exceeded or not and then sends that
information to the throttle object and calls the respective method
in it.
#### NOTE
For more detailed information on each class and the
methods involved please open the “allclasses-index.html” file
present in the “Javadoc” folder from there you can navigate to
each class and look at the detailed information. 
# My approach for the solution and design considerations
## Algorithm:
There are many algorithms present for tracking the API calls, each with its
pros and cons. I considered a few of them, Fixed-window, Sliding logs,
and Sliding window.
Fixed-window is the easiest to implement, we break the time into fixed
frames of some size and count the number of requests the user sends in
that time frame and compare them against the limit. The main issue with
this approach is that the user sends all the requests at the end of a time
frame and also during the start of the next frame. They can send double the
number of requests allowed and it will cause spikes which may affect the
services.
Sliding logs is the most accurate implementation. you keep track of every
request in the form of logs and keep removing old logs. For each request,
we check the number of logs and compare them against the limit. The
problem with this approach is it takes much memory for storing the logs
and a lengthy processing time for removing old logs.
I implemented the sliding window algorithm in my solution. Sliding
window algorithms combine the best of Fixed Window and Sliding Log
algorithms. A counter for a time period is used, similar to the fixed window
algorithm but here I also consider the number of requests in the previous
window to help smooth outbursts of traffic.
we take the number of requests in the current window and add some weightage
from the number of requests in the previous window and compare the final sum
against the given limit and take necessary actions. This is both memory efficient
and also simple to implement.
### Boolean variable vs Throttle class :
I first considered returning just a Boolean value for indicating if the limit
is exceeded or not.
But, since creating a class for a callback and providing necessary methods
gives a lot more functionality and flexibility during implementation, I went
with creating an abstract class Throttle, which can be extended and
configured as per the needs.
### Single-Threaded vs Multi-Threaded :
Although a single-threaded design is clean and simple, if the request takes
some time to get processed, other API calls will get blocked, to achieve
lower latency between requests, I implemented the throttle class in a
multi-threaded fashion, so that after checking the API rate limit the required
actions will be done in a separate thread.
For heavy loads, even checking the rate limit in a single-threaded way may
cause delays, so I made the necessary critical methods as synchronized
and used thread-safe objects where ever possible. So now we can process
each request in a separate thread without causing any delays.
### Fail open vs Fail close:
What happens when a client requests a new, never seen API?
I can either block the request or allow the request. Here I took a more
optimistic approach and made it as fail open. So if I get a request for a
new API it will be assumed that no rate limiting is applied to that API and
that request will be allowed. Only the Api present in the Api Index are
assumed to have rate limiting.
