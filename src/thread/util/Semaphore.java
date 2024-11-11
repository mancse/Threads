package thread.util;

public class Semaphore
{
	int count = 0;
	int bound;
    public Semaphore(int bound)
    {
    	this.bound = bound;
    }
    
    public synchronized void take() throws InterruptedException
    {
    	while(count == bound)
    	{
    		this.wait();
    	}
    	
    	count++;
    	this.notifyAll();
    }
    
    public synchronized void release() throws InterruptedException
    {
    	while(count == 0)
    	{
    		this.wait();
    	}
    	
    	count--;
    	this.notifyAll();
    }
}
