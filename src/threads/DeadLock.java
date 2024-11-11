package threads;

public class DeadLock {
	   public static String lock1 = "Lock1";
	   public static String lock2 = "Lock2";
	   public static void main(String args[]){
			Thread t1 = new Thread(new Task1());
			Thread t2 = new Thread(new Task2());
			t1.start();
			t2.start();
	   }
	   
	   private static class Task1 implements Runnable{
		   public void run(){
			   synchronized(lock1){
				   System.out.println("Hello from Task1... acquired lock1 and waiting for lock2");
				   synchronized(lock2){
					   System.out.println("Hello from Task1");
				   }
			   }
		   }
	   }
	   
	   private static class Task2 implements Runnable{
		   public void run(){
			   synchronized(lock2){
				   System.out.println("Hello from Task2... acquired lock2 and waiting for lock1");
				   synchronized(lock1){
					   System.out.println("Hello from Task2");
				   }
			   }
		   }
	   }
	}


	
