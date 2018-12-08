import java.util.ArrayList;

public class ThreadPool {
    ArrayList<pooledThread> pool = null;
    /**
     * Default initial capacity.
     */
    private int DEFAULT_CAPACITY = 10;
    /**
     * Default maximum capacity.
     */
    private int MAX_CAPACITY = 50;

    /**
     * Initialize thread pool, including create pool with default capacity,
     * create thread.
     */
    public void init(){
        if(pool == null) {
            pool = new ArrayList<>();
            for(int i = 0; i < DEFAULT_CAPACITY; i ++) {
                pool.add(new pooledThread());
            }
        }
    }

    /**
     * Get an idle thread
     * @return An idle thread, if none, return null
     */
    public Thread getThread() {
        for ()
    }

    /**
     * Release a thread to pool, set it to idle
     */
    public void releaseThread() {

    }
}

/**
 * A class contains thread and manager it
 */
class pooledThread{
    private Thread thread = null;
    private boolean busy = false;

    public pooledThread() {
        thread = new MyThread();
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    /**
     * Check if inside thread is still valid
     * @return if the thread is valid
     */
    public boolean isValid() {
        return thread.isAlive();
    }

    /**
     * Refresh inside thread, create a new thread
     * @return If this function completed successful.
     */
    public boolean refreshThread() {
        thread = new MyThread();
        return true;
    }
}

class MyThread extends Thread{
    private Run task = null;
    public void setTask(Run task){
        this.task = task;
    }

    @Override
    public void run(){
        //infinite running
        while(true) {
            if(task != null){
                task.run();
            } else {

            }
            
            wait();
        }
    }
}
