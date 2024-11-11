package thread.util;

/**
 * ReaderWriter lock mechanism works on following two concepts.
 * 1. Read Access: Reader thread can get access to critical section only when there is no 
 *    writer thread accessing it nor any writer thread has made request for access to it.
 * 2. Write Access: Writer thread can get access to critical section only when neither any
 *    reader nor any writer thread is accessing it.
 * @author Manoj.K
 *
 */
public class ReaderWriterLock 
{
    int readers =0;
    int writers =0;
    int writeRequests = 0;
    
    public synchronized void lockRead() throws InterruptedException
    {
    	while(writers > 0 || writeRequests > 0)
    	{
    		this.wait();
    	}
    	readers++;
    }
    
    public synchronized void unlockRead()
    {
    	readers--;
    	this.notifyAll();
    }
    
    public synchronized void lockWrite() throws InterruptedException
    {
    	writeRequests++;
    	while(writers > 0 || readers > 0)
    	{
    		this.wait();
    	}
    	writers++;
    	writeRequests--;
    }
    
    public synchronized void unlockWrite()
    {
    	writers--;
    	this.notifyAll();
    }
}
