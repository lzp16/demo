package com.example.demo.producercustomer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Created by LiZhanPing on 2019/7/24.
 */
public class ProducerConsumer4 {
    private int maxValue = 3;

    //放置产品的空位
    final Semaphore emptySemaphore = new Semaphore(maxValue);
    //是否已放置产品
    final Semaphore nothingSemaphore = new Semaphore(0);
    final Semaphore mutexSemaphore = new Semaphore(1);

    class Producer implements Runnable {
        private Queue<Integer> queue;
        private String name;
        private int i;

        public Producer(Queue<Integer> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    emptySemaphore.acquire();
                    mutexSemaphore.acquire();
                    queue.offer(++i);
                    System.out.println("生产者" + name + "生产了" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutexSemaphore.release();
                    nothingSemaphore.release();
                }
//                try {
//                    Thread.sleep(new Random().nextInt(1000));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    class Consumer implements Runnable {
        private Queue<Integer> queue;
        private String name;
        private int i;

        public Consumer(Queue<Integer> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    nothingSemaphore.acquire();
                    mutexSemaphore.acquire();
                    i = queue.poll();
                    System.out.println("消费者" + name + "消费了" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutexSemaphore.release();
                    emptySemaphore.release();
                }
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumer4 producerConsumer = new ProducerConsumer4();
        Queue<Integer> queue = new LinkedList<>();
        Thread producer1 = new Thread(producerConsumer.new Producer(queue, "producer1"));
        Thread producer2 = new Thread(producerConsumer.new Producer(queue, "producer2"));
        Thread producer3 = new Thread(producerConsumer.new Producer(queue, "producer3"));
        Thread producer4 = new Thread(producerConsumer.new Producer(queue, "producer4"));
        Thread producer5 = new Thread(producerConsumer.new Producer(queue, "producer5"));
        Thread consumer1 = new Thread(producerConsumer.new Consumer(queue, "consumer1"));
        Thread consumer2 = new Thread(producerConsumer.new Consumer(queue, "consumer2"));
//        Thread consumer3 = new Thread(producerConsumer.new Consumer(queue, "consumer3"));
        producer1.start();
        producer2.start();
        producer3.start();
        producer4.start();
        producer5.start();
        consumer1.start();
        consumer2.start();
//        consumer3.start();
    }
}
