package thread.util;

/**
 * Purpose of CountDownLatch is to deal a situation where a given thread need to wait
 * until another thread or group of threads completes certain set of operations. 
 * 
 * Real use case could be: Big system turns to be up and running only when certain set
 * of prerequisite operations gets completed like initialization of DB connection, initialization
 * of caches, networks components etc.
 * @author Manoj.K
 *
 */
class CountDownLatch 
{
    private int count;
    
    public CountDownLatch(int cnt)
    {
    	count = cnt;
    }
    
    public synchronized void await() throws InterruptedException
    {
    	while (count > 0)
    	{
    		this.wait();
    	}
    }
    
    public synchronized void countdown()
    {
    	count--;
    	
    	if (count ==0 )
    	{
    		this.notifyAll();
    	}
    }
    
    public static void main(String args[]) throws InterruptedException
    {
    	CountDownLatch latch = new CountDownLatch(5);
    	
    	new Thread(new MyRunnable(latch)).start();
    	/**
    	 * Main thread will wait until coutdown latch reaches to 0;
    	 */
    	latch.await();
    	
    	 System.out.println("count has reached zero, "+
                 Thread.currentThread().getName()+" thread has ended");
    }
}

class MyRunnable implements Runnable
{
	CountDownLatch latch;
	
	public MyRunnable(CountDownLatch latch)
	{
		this.latch = latch;
	}
	public void run()
	{
		for (int i=4; i>=0 ; i--)
		{
			latch.countdown();
			
			System.out.println("Thread "+Thread.currentThread().getName()+" reduced latch count to "+i);
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}