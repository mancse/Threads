package thread.concurrent;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledThread 
{
	static final int CORE_POOL_SIZE=2;
	public static void main(String args[])
	{
		ScheduleTask task1 = new ScheduleTask("Task 1");
		ScheduleTask task2 = new ScheduleTask("Task 2");
		ScheduleTask task3 = new ScheduleTask("Task 3");
		
		//One time schduleing of following tasks after certain delay.
		//while(true)
		{
		   scheduleTask(task1,5);
		   scheduleTask(task2,5);
		   scheduleTask(task3,5);
		}

	}
	
	private static void scheduleTask(Callable<String> task,int delay)
	{
		ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
		ScheduledFuture<String> future = scheduledPool.schedule(task,delay,TimeUnit.SECONDS);
		
		if (future.isDone())
		{
			try 
			{
			
				String result = future.get();
				System.out.println(" Scheduled Task return: "+result);
			} 
			catch (InterruptedException | ExecutionException e) 
			{
				e.printStackTrace();
			}
		}
	    
		scheduledPool.shutdown();
		//System.out.println("Is ScheduledThreadPool shutting down? "+scheduledPool.isShutdown());
		
		try 
		{
			scheduledPool.awaitTermination(1, TimeUnit.DAYS);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}


class ScheduleTask implements Callable<String>
{
	String task;
	public ScheduleTask(String task)
	{
		this.task = task;
	}
	public String call() throws Exception 
	{
		System.out.println(Thread.currentThread()+" Running task "+task+" at time: "+new Date());
		return "Task  "+task;
	}
}