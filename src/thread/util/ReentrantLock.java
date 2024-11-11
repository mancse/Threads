package thread.util;

public class ReentrantLock 
{
    boolean isLocked = false;
    int lockCnt = 0;
    Thread lockedByThread =  null;
    
    public synchronized void lock() throws InterruptedException
    {
    	Thread currThread = Thread.currentThread();
    	while(isLocked && currThread != lockedByThread)
    	{
    		this.wait();
    	}
    	
    	isLocked = true;
    	lockCnt++;
    	lockedByThread = currThread;
    }
    
    public synchronized void unlock()
    {
    	if (Thread.currentThread() == lockedByThread)
    	{
    		lockCnt--;
    		
    		if (lockCnt == 0)
    		{
    			isLocked = false;
    			this.notifyAll();
    		}
    	}
    }
}
