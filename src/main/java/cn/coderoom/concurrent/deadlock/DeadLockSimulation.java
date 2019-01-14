package cn.coderoom.concurrent.deadlock;

/**
 * @package：cn.coderoom.concurrent.deadlock
 * @description: 死锁模拟
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/1/14
 */
public class DeadLockSimulation {

    /** A锁 */
    private static String A = "A";

    /** B锁 */
    private static String B = "B";

    public static void main(String[] args) {
        new DeadLockSimulation().deadLock();
    }

    public void deadLock() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //锁住了A
                synchronized (A) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //尝试对B进行加锁
                    synchronized (B) {
                        System.out.println("thread1...");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    synchronized (A) {
                        System.out.println("thread2...");
                    }
                }
            }
        });

        t1.start();
        t2.start();

    }

}
