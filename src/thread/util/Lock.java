package thread.util;

public class Lock 
{
    boolean isLocked = false;
    
    public synchronized void lock() throws InterruptedException
    {
    	while(isLocked)
    	{
    		this.wait();
    	}
    	
    	isLocked = true;
    }
    
    public synchronized void unlock()
    {
    	isLocked = false;
    	this.notifyAll();
    }
}
