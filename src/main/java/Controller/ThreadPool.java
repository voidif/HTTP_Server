package Controller;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Thread pool, this is a singleton class
 * This class using Java Executor class to provide a thread pool
 */
class ThreadPool {
    private static ThreadPool threadPool = null;

    public static synchronized ThreadPool getInstance(){
        if (threadPool == null){
            threadPool = new ThreadPool();
        }
        return threadPool;
    }

    private static final int NTHREADS = 20;
    private static final Executor exec = null;

    public void init() {

    }

    public void executeTask(Runnable task) {
        exec.execute(task);
    }
}