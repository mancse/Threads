package thread.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
class Task implements Runnable
{
	int i=0;
	public Task(int i)
	{
		this.i = i;
	}
	public void run()
	{
		System.out.println("Task Executed: "+i+" by thread: "+Thread.currentThread());
	}
}

class WorkerThread implements Runnable
{
	BlockingQueue<Runnable> queue = null;
	private boolean stop = false;
	private Thread thread = null;
	
	public WorkerThread(BlockingQueue<Runnable> queue)
	{
		this.queue = queue;
	}
	
	public void run()
	{
		this.thread = Thread.currentThread();
		while(!stop)
		{
			try
			{
				Runnable task = queue.take();
				task.run();
			}
			catch(InterruptedException  e)
			{
				 this.thread.interrupt();
			}
		}
	}
	
	public synchronized void stop()
	{
		stop = true;
		this.thread.interrupt();
	}
}
public class ThreadPool 
{
	BlockingQueue<Runnable> queue = null;
	List<WorkerThread> workerThreads = new ArrayList<WorkerThread>();
	public ThreadPool(int poolSize, int numThreads)
	{
		queue = new ArrayBlockingQueue<Runnable>(poolSize);
		for (int i=0; i < numThreads; i++)
		{
			WorkerThread worker = new WorkerThread(queue);
			workerThreads.add(worker);
		}
		
		for (WorkerThread worker : workerThreads)
		{
			new Thread(worker).start();
		}
	}
	
	public synchronized void execute(Runnable task) throws Exception
	{
	    this.queue.offer(task);
	}
	
	public synchronized void stop()
	{
		for (WorkerThread worker : workerThreads)
		{
			worker.stop();
		}
	}
	
	public synchronized void waitUntilAllTaskFinished()
	{
		while(this.queue.size() > 0)
		{
			try
			{
				Thread.sleep(1);
			}
			catch(Exception e)
			{
				//Log
			}
		}
	}

    public static void main(String args[])
    {
    	ThreadPool pool = new ThreadPool(100,3);
    	for (int i=0 ; i < 100; i++)
    	{
    		Task work  = new Task(i);
    		try 
    		{
				pool.execute(work);
			} 
    		catch (Exception e) 
    		{
				e.printStackTrace();
			}
    	}
    	pool.waitUntilAllTaskFinished();
    	pool.stop();
    }
}
