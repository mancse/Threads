package thread.util;

import java.util.LinkedList;
import java.util.List;

public class BlockingQueue 
{
    private List<Object> queue = new LinkedList<>();
    private int capacity=10;
    
    public BlockingQueue(int capacity)
    {
    	this.capacity = capacity;
    }
    
    public  synchronized void enqueue(Object object) throws InterruptedException
    {
    	while(queue.size() == capacity)
    	{
    		this.wait();
    	}
    	
    	queue.add(object);
    	
    	if (queue.size() == 1)
    	{
    		this.notifyAll();
    	}
    }
    
    public synchronized Object dequeue() throws InterruptedException
    {
    	while(queue.size() == 0)
    	{
    		this.wait();
    	}
    	
    	if (queue.size() == capacity)
    	{
    		this.notifyAll();
    	}
    	
    	Object obj = queue.remove(0);
    	return obj;
    }
    
    
    /**
     * 
     * Following is the code to use the BlockingQueue in two different threads i.e producer and consumer threads.
     *
     */
    static class ProducerThread extends Thread
    {
    	BlockingQueue queue;
    	int threadId; 
    	int count = 0;
    	public ProducerThread(int id, BlockingQueue queue)
    	{
    		this.threadId = id;
    		this.queue = queue;
    	}
    	public void run()
    	{
    		for(int i=0; i<100; i++)
    		{
    			System.out.println("Producer thread: "+ threadId+" Added in queue: "+count++);
    			try 
    			{
					queue.enqueue(i);
				} 
    			catch (InterruptedException e) 
    			{
				}
    		}
    	}
    }
    
    static class ConsumerThread extends Thread
    {
    	BlockingQueue queue;
    	int threadId; 
    	
    	public ConsumerThread(int id, BlockingQueue queue)
    	{
    		this.threadId = id;
    		this.queue = queue;
    	}
    	public void run()
    	{
    		for(int i=0; i<100; i++)
    		{
    			try 
    			{
					int count = (int) queue.dequeue();
					System.out.println("Consumer thread: "+ threadId+" Removed from queue: "+count);
				} 
    			catch (InterruptedException e) 
    			{
				}
    		}
    	}
    }
    public static void main(String args[])
    {
    	BlockingQueue queue = new BlockingQueue(5);
    	Thread t1 = new ProducerThread(1, queue);
    	Thread t2 = new ConsumerThread(2,queue);
    	Thread t3 = new ProducerThread(3, queue);
    	Thread t4 = new ConsumerThread(4,queue);
    	//Thread t5 = new ProducerThread(5, queue);
    	//Thread t6 = new ConsumerThread(6,queue);
    	t1.start();
    	t2.start();
    	t3.start();
    	t4.start();
    	//t5.start();
    	//t6.start();
    }
}
