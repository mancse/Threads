package threads;
class B
{
	int i=0;
	public synchronized void test()
	{
		for (int j=0; j<10; j++)
		{
			System.out.println("Thread: "+Thread.currentThread()+"Value of i is: "+i++);
		}
			
	}
}
2
class C extends Thread
{
	B b = null;
	public C(B b)
	{
		this.b=b;
	}
    public void run()
    {
    	b.test();
    }
}

class D extends Thread
{
	B b = null;
	public D(B b)
	{
		this.b=b;
	}
	
	public void run()
	{
		b.test();
	}
}

public class ThreadPractice 
{
    public static void main(String args[])
    {
    	/*B b = new B();
    	Thread tb = new D(b);
    	Thread tc = new C(b);
    	tb.start();
    	tc.start();*/
    	
    	Thread tb1 = new D(new B());
    	Thread tc1 = new C(new B());
    	
    	tb1.start();
    	tc1.start();
    	
    	
    }
}


