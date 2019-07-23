package com.example.demo.producercustomer;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by LiZhanPing on 2019/7/24.
 */
public class ProducerConsumer1 {

    static class Producer implements Runnable {
        private Queue<Integer> queue;
        private String name;
        private int maxValue;
        private int i;

        public Producer(Queue<Integer> queue, String name, int maxValue) {
            this.queue = queue;
            this.name = name;
            this.maxValue = maxValue;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == maxValue) {
                        System.out.println("队列是满的，生产者" + name + "进入等待");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                    queue.offer(i);
                    System.out.println("生产者" + name + "生产了" + i);
                    queue.notifyAll();
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Consumer implements Runnable {

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
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        System.out.println("队列是空的，消费者" + name + "进入等待");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    i = queue.poll();
                    System.out.println("消费者" + name + "消费了" + i);
                    queue.notifyAll();
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        final int maxValue = 1;
        Thread producer1 = new Thread(new Producer(queue, "producer1", maxValue));
        Thread producer2 = new Thread(new Producer(queue, "producer2", maxValue));
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
