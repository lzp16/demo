package com.example.demo.producercustomer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by LiZhanPing on 2019/7/24.
 */
public class ProducerConsumer3 {

    static class Producer implements Runnable {
        private BlockingQueue blockingQueue;
        private String name;
        private int i;

        public Producer(BlockingQueue<Integer> blockingQueue, String name) {
            this.blockingQueue = blockingQueue;
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    blockingQueue.put(++i);
                    System.out.println("生产者" + name + "生产了" + i);
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private BlockingQueue<Integer> blockingQueue;
        private String name;
        private int i;

        public Consumer(BlockingQueue<Integer> blockingQueue, String name) {
            this.blockingQueue = blockingQueue;
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    i = blockingQueue.take();
                    System.out.println("消费者" + name + "消费了" + i);
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) {
            final int maxValue = 5;
            BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(maxValue);
            Thread producer1 = new Thread(new Producer(queue, "producer1"));
            Thread producer2 = new Thread(new Producer(queue, "producer2"));
            Thread consumer1 = new Thread(new Consumer(queue, "consumer1"));
            Thread consumer2 = new Thread(new Consumer(queue, "consumer2"));
            Thread consumer3 = new Thread(new Consumer(queue, "consumer3"));
            producer1.start();
            producer2.start();
            consumer1.start();
            consumer2.start();
            consumer3.start();
        }
    }
}
