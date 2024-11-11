package thread.util;
/**
 * Purpose of CyclicBarrier is to deal with certain use cases where each thread
 * needs to wait for other thread to complete their task. Once all threads complete
 * their task (i.e when every thread calls this.await() method) then barrier is broken
 * and then following things happens.
 * 1. All waiting threads are released.
 * 2. Event can be triggered.
 * 3. All thread can again set new barrier point to reach it all together or non.
 * 
 * Now, if any of the thread is interrupted in between, then all of the thread exit from 
 * barrier race.
 * @author Manoj.K
 *
 */
public class CyclicBarrier 
{
    int intialiparties;//Total parties
    int partiesawait;//Parties yet to arrive
    
    Runnable barrierEvent;
    public CyclicBarrier(int parties, Runnable barrierEvent)
    {
    	intialiparties = parties;
    	partiesawait = parties;
    	this.barrierEvent = barrierEvent;
    }
    
    public synchronized void await() throws InterruptedException
    {
    	partiesawait--;
    	
    	if (partiesawait > 0)
    	{
    		this.wait();
    	}
    	else
    	{
    		/**
    		 * There is no more thread which is yet to call await() method.
    		 * If it is the case then we can reset the partiesawait with total
    		 * intial parties once again for next barrier race. 
    		 */
    		partiesawait = intialiparties;
    		
    		this.notifyAll();
    		
    		/**
    		 * Invoke barrier event when all thread reaches to barrier point.
    		 */
    		
    		barrierEvent.run();
    	}
    }
    
    public static void main(String args[])
    {
    	
    	/*
         * Create CyclicBarrier with 3 parties, when all 3 parties
         * will reach common barrier point BarrrierEvent will be
         * triggered i.e. run() method of CyclicBarrrierEvent will be called
         */
    	CyclicBarrier barrier = new CyclicBarrier(3, new BarrierEvent());
    	
    	CustomTask task = new CustomTask(barrier);
    	
    	new Thread(task,"Thread1").start();
    	new Thread(task,"Thread2").start();
    	new Thread(task,"Thread3").start();
    }
}

class CustomTask implements Runnable
{
	CyclicBarrier barrier;
	public CustomTask(CyclicBarrier barrier)
	{
		this.barrier = barrier;
	}
	public void run()
	{
		try 
		{
			 System.out.println(Thread.currentThread().getName() +
                     " is waiting for all other threads to reach common barrier point");
			Thread.sleep(1000);
			
			/*
             * when all 3 parties will call await() method (i.e. common barrier point)
             * CyclicBarrrierEvent will be triggered and all waiting threads will
             * be released.
             */
			barrier.await();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
	}
}
class BarrierEvent implements Runnable
{
	public void run()
	{
		System.out.println("All threads has reached to barrier point hence barrier event is triggered");
	}
}
