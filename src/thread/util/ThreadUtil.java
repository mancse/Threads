package thread.util;

public class ThreadUtil 
{
    boolean isSignal = false;
    
    public void doWait()
    {
        synchronized(this)
        {
        	while(!isSignal)
        	{
        		try 
        		{
					this.wait();
				}
        		catch (InterruptedException e) 
				{	
					e.printStackTrace();
				}
        	}
        	/*
        	 * Here, current thread sets the signal flag to false so that it will block 
        	 * other thread to enter critical section.
        	 */
        	isSignal = false;
        }
    }
    
    public void doNotify()
    {
    	synchronized(this)
    	{
    		/*
    		 * Set the sinal flag to true so that it will allow any one thread to enter
    		 * into critical section 
    		 */
    		isSignal = true;
    		this.notifyAll();
    	}
    }
    
    public void sleep(long ms) 
	{
		try 
		{
			Thread.sleep(ms);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
