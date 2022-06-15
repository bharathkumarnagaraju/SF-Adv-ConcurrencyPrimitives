package prodcons;

public class BadQueue<E> {
  private Object rendezvous = new Object();
  private E[] data = (E[]) new Object[10];
  private int count = 0;

  public void put(E e) throws InterruptedException {
    synchronized (rendezvous) {
      while (count == 10) { // MUST BE A LOOP (can waken for wrong reasons)
        // returns the key!!!
        // must be transactionally stable!!
        // reclaims key before continuing
        rendezvous.wait();
      }

      data[count++] = e;
//      rendezvous.notify();
      // functionall correct with multiple producer/consumer
      // but horribly inefficient.
      // use ReentrantLock instead
      rendezvous.notifyAll();
    }
  }

  public E take() throws InterruptedException {
    synchronized (rendezvous) {
      while (count == 0) {
        rendezvous.wait();
      }
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      // FAILS if we have multiple producers/consumers
      // no guarantee or wake up order!!!
//      rendezvous.notify();
      rendezvous.notifyAll();
      return rv;
    }
  }

  public static void main(String[] args) throws Throwable {
    BadQueue<int[]> bq = new BadQueue<>();
    Thread prod = new Thread(() -> {
      try {
        for (int i = 0; i < 1_000; i++) {
          int[] data = {-1, i}; // transactionally "unsound"
          if (i < 100) Thread.sleep(1); // test "empty" queue
          data[0] = i;

          if (i == 500) data[0] = -1;

          bq.put(data);
          data = null;
        }
      } catch (InterruptedException ie) {
        System.out.println("Unexpected!");
      }
    });
    Thread cons = new Thread(() -> {
      try {
        for (int i = 0; i < 1_000; i++) {
          if (i > 900) Thread.sleep(1);
          int[] data = bq.take();

          if (data[0] != data[1] || data[0] != i) {
            System.out.println("***** ERROR AT " + i);
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("Unexpected!");
      }
    });
    prod.start();
    cons.start();
    prod.join();
    cons.join();
    System.out.println("all done");


//    BadQueue<Integer> bq = new BadQueue<>();
//    bq.put(1);
//    bq.put(2);
//    System.out.println(bq.take());
//    System.out.println(bq.take());
//    bq.put(3);
//    System.out.println(bq.take());
  }
}
