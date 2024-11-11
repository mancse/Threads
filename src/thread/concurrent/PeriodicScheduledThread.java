package thread.concurrent;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeriodicScheduledThread 
{
	static final int CORE_POOL_SIZE=2;
	public static void main(String args[])
	{	
		int i=0;
		//Schduling tasks periodially at fixed rate once task is schduled after a delay.
		while(true)
		{
			RunnableTask task = new RunnableTask(i++);
			scheduleTaskPeriodically(task,4,5);
		}
	}
	
	private static void scheduleTaskPeriodically(Runnable task,int delay, int period)
	{
		ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
		ScheduledFuture<?> result = scheduledPool.scheduleWithFixedDelay(task, delay, period, TimeUnit.SECONDS);
		
		try 
		{
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
		scheduledPool.shutdown();
	}
}

class RunnableTask implements Runnable
{
	int task;
	public RunnableTask(int task)
	{
		this.task = task;
	}
	public void run() 
	{
		System.out.println(Thread.currentThread()+" Task: "+task+" is scheduled at time- "+new Date());
	}
}
