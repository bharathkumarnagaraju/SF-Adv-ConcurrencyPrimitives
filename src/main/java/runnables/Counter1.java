package runnables;

public class Counter1 {
  static /*volatile*/ long counter = 0;
  public static void main(String[] args) throws InterruptedException {
    Runnable task = ()->{
      for (var i = 1_000_000_000L; i > 0; i--) {
        counter++;  // read-modify-write?!
      }
    };
    Thread t1 = new Thread(task);
    t1.start();
    Thread t2 = new Thread(task);
    t2.start();
//    Thread.sleep(2000);
//    t1.isAlive();
    t1.join(); // "notices" the thread is dead, but also waits!!!
    t2.join();
    System.out.println("counter is " + counter);
  }
}
