package runnables;

class MyTask implements Runnable {
//  private String [] args;
//
//  public MyTask(String[] args) {
//    this.args = args;
//  }

  private int i = 0;

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName()
        + " running MyTask");
    for (/*int i = 0*/; i < 1000; i++) {
      System.out.println(Thread.currentThread().getName()
          + " i = " + i);
    }
    System.out.println(Thread.currentThread().getName()
        + " MyTask completed");
  }
}
public class SimpleRunnable {
  public static void main(String[] args) {

    System.out.println(Thread.currentThread().getName()
        + " running the main method");
//    MyTask mt = new MyTask(args);
    MyTask mt = new MyTask();

//    mt.run();
    Thread t1 = new Thread(mt);
//    t1.setDaemon(true);
    t1.start();
    Thread t2 = new Thread(mt);
    t2.start();

    System.out.println(Thread.currentThread().getName()
        + " returned from starting MyTask");
  }
}
