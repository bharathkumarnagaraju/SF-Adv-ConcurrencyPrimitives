package runnables;

public class BadTiming {
  static volatile boolean stop = false;

  public static void main(String[] args) throws InterruptedException {
    new Thread(() -> {
      System.out.println("Worker " + Thread.currentThread().getName()
          + " started");
      while (!stop)
        ;
      System.out.println("Worker " + Thread.currentThread().getName()
          + " finished!!!!");
    }).start();

    System.out.println(Thread.currentThread().getName() +
        " worker started, pausing...");
    Thread.sleep(1_000);
    System.out.println(Thread.currentThread().getName() +
        " about to set stop flag...");
    stop = true;
    System.out.println(Thread.currentThread().getName() +
        " stop flag is " + stop + " main exiting");
  }
}
