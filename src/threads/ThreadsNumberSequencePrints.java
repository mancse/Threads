package threads;

public class ThreadsNumberSequencePrints 
{
	private static volatile Integer count = 1;
    private static volatile Integer threadIdToRun = 1;
    private static String monitor = "monitor";
    
    public static void main(String[] args) 
   	{
   		Thread t1 = new Thread(new PrintNumbers(1));
   		Thread t2 = new Thread(new PrintNumbers(2));
   		Thread t3 = new Thread(new PrintNumbers(3));
   		
   		t1.start();
   		t2.start();
   		t3.start();
   	}
    private static class PrintNumbers implements Runnable
	{
		int threadId;
        public PrintNumbers(int id)
        {
        	this.threadId = id;
        }
		
        @Override
		public void run() 
		{
			try
			{
				while(count <= 100)
				{
					synchronized(monitor)
					{
						if (threadId != threadIdToRun)
						{
							monitor.wait();
						}
						else
						{
							System.out.println("Thread " + threadId + " printed " + count);
							count++;
							
							switch(threadId)
							{
								case 1: 
									threadIdToRun = 2;
								    break;
								case 2: 
									threadIdToRun = 3;
									break;
								case 3:
									threadIdToRun = 1;
									break;
							}
							
							monitor.notifyAll();
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
