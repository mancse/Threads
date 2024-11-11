package thread.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool 
{
	public static final int CORE_POOL_SIZE= 2;
	public static final int MAX_POOL_SIZE=2;
	public static final int KEEP_ALIVE_TIME = 10;
	
    public static void main(String args[])
    {
    	boolean isResultRequired = false;
    	
    	if(!isResultRequired)
    	    executeTask();
    	
    	isResultRequired = true;
    	
    	if (isResultRequired)
    	{
    		submitTask();
    	}
    }
    
    public static void submitTask()
    {
    	ThreadPoolExecutor executorPool = null;
    	try
    	{
    		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);
    		List<Future<String>> futureList= new ArrayList<Future<String>>();
    		RejectedExecutionHandler rejectedHandler = new RejectedExecutionHandlerImpl();
    		executorPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,queue,rejectedHandler);
    		MonitorThread monitor = new MonitorThread(executorPool,5);
    		Thread monitorThread = new Thread(monitor);
    		monitorThread.start();
    	
    		for (int i=0; i< 20; i++)
    		{
    			Callable task = new CallableTask(" "+i);
    			Future<String> future = executorPool.submit(task);
    			futureList.add(future);
    		}
    	
  
    		for (Future future : futureList)
    		{
    			String result;
    			if (future.isDone())
    			{
    				result = (String)future.get();
    				System.out.println(" Result Received for "+result);
    			}
    		}
    		executorPool.shutdown();
    		while(!executorPool.isTerminated());
    		System.out.println("All threads finished exution!!!");
    	}
    	catch(InterruptedException e)
    	{
    		e.printStackTrace();
    		executorPool.shutdownNow();
    	}
    	catch (ExecutionException e)
    	{
    		e.printStackTrace();
    		executorPool.shutdownNow();
    	}
    }
    public static void executeTask()
    {
    	ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(2);
    	RejectedExecutionHandler rejectedHandler = new RejectedExecutionHandlerImpl();
    	ThreadPoolExecutor executorPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,queue,rejectedHandler);
    	MonitorThread monitor = new MonitorThread(executorPool,5);
    	Thread monitorThread = new Thread(monitor);
    	monitorThread.start();
    	
    	for (int i=0; i< 20; i++)
    	{
    		Runnable task = new Task(" "+i);
    		executorPool.execute(task);
    	}
    	executorPool.shutdown();
    	while(!executorPool.isTerminated());
    	
    	System.out.println("All threads finished exution!!!");
    }
}


class CallableTask implements Callable<String>
{
	String task;
	
	public CallableTask(String task)
	{
		this.task = task;
	}
	
	public String call()
	{
		System.out.println(Thread.currentThread().getName()+" "+" executed");
		return Thread.currentThread().getName()+ "  and task " +task;
	}
}
class Task implements Runnable
{
	String task;
	
	public Task(String task)
	{
		this.task = task;
	}
	
	public void run()
	{
		System.out.println(Thread.currentThread().getName()+" start "+task);
		System.out.println(Thread.currentThread().getName()+" ends task "+task);
	}
}
class RejectedExecutionHandlerImpl implements RejectedExecutionHandler
{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) 
	{
		System.out.println(r.toString()+" is rejected hence resubmitted to pool");
		executor.submit(r);
	}
}

class MonitorThread implements Runnable
{
    ThreadPoolExecutor executor;
    int delay;
    public MonitorThread(ThreadPoolExecutor executor, int delay)
    {
    	this.executor = executor;
    	this.delay = delay;
    }
    
    public void run()
    {
    	while(true)
    	{
    		System.out.println(String.format("Current PoolSize: [%d], " +
    				"Core PoolSize[%d], " +
    				"Active Task Count[%d]" +
    				"Completed Task Count[%d]" +
    				"Task Count[%d]" +
    				"IsShutdown[%s]" +
    				"IsTerminated[%s]", 
    				executor.getPoolSize(),executor.getCorePoolSize(),
    				executor.getActiveCount(),executor.getCompletedTaskCount(),
    				executor.getTaskCount(),executor.isShutdown(),executor.isTerminated()));
    		
    		try
    		{
    			Thread.sleep(delay*1000);	
    		}
    		catch(InterruptedException e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
}