package Learning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Tester {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool();
        pool.init();
        for(int i = 0; i < 50; i ++) {
            Task task = new MyTask(String.valueOf(i));
            pool.addTask(task);
        }
    }
}


public class ThreadPool {
    BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    ArrayList<Worker> pool = new ArrayList<>();
    int MAX_THREAD = 10;

    public void init() {
        for(int i = 0; i < MAX_THREAD; i ++) {
            Worker newWorker = new Worker(taskQueue);
            pool.add(newWorker);
            newWorker.start();
        }
    }

//    public boolean executeTask(Task newTask) {
//        for(Worker worker : pool) {
//            if(!worker.working) {
//                worker.setTask(newTask);
//                worker.execute();
//                return true;
//            }
//        }
//        return false;
//    }

    public void addTask(Task newTask) {
        taskQueue.add(newTask);
    }

}

class Worker extends Thread {
    BlockingQueue<Task> taskQueue;
    private Task getTask() throws InterruptedException {
        Task newTask = taskQueue.take();
        return newTask;
    }
    private Task task = null;
    public boolean working = false;
    public boolean shutdown = false;


//    public Worker(Task newTask) {
//        this.task = newTask;
//    }
    public Worker(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {
            while(this.task != null || (this.task = getTask()) != null) {
                working = true;
                task.newTask();
                task = null;
                working = false;
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted! quiting...");
        }
        System.out.println("Thread " + this.hashCode() + " quit");
    }

    public void execute() {
        synchronized(this) {
            this.notify();
        }
    }
}


interface Task{
    void newTask();
}

class MyTask implements Task {
    private String message;

    public MyTask(String message) {
        this.message = message;
    }

    @Override
    public void newTask() {
        System.out.println(message);
    }
}