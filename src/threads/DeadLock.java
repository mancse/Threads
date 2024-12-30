package threads;

public class DeadLock {
	   public static String lock1 = "Lock1";
	   public static String lock2 = "Lock2";
	   public static void main(String args[]){
			Thread t1 = new Thread(new Task(lock1,lock2));
			Thread t2 = new Thread(new Task(lock2,lock1));
			t1.start();
			t2.start();
	   }
	   
	   private static class Task implements Runnable{
		   String lock1;
		   String lock2;
		   public Task(String lock1, String lock2) {
			   this.lock1 = lock1;
			   this.lock2 = lock2;
		   }
		   public void run() {
			   synchronized(lock1) {
				   System.out.println("Thread : "+Thread.currentThread().getId()+" acquired lock: "+lock1 +" and waiting for lock: "+lock2);
				   synchronized(lock2) {
					   System.out.println("Thread : "+Thread.currentThread().getId()+" acquired second lock: "+lock2);
				   }
			   }
		   }
	   }
	}


	
