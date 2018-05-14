import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池工具类
 *
 * @author yinyiyun
 * @date 2018/5/3 9:18
 */
public class WorkThreadPool {

    /**
     * 线程池
     */
    private ExecutorService pool;

    /**
     * 任务返回值集合
     */
    private List<Future> listTask = new ArrayList<>();

    /**
     * 创建默认线程池
     *
     * @param threadName  线程名称
     * @param size        循环的size
     * @param maximumSize 最大线程数
     */
    public WorkThreadPool(String threadName, Integer size, Integer maximumSize) {
        int coorPoolSize = Math.min(size, maximumSize);
        this.pool = getPool(threadName, coorPoolSize, maximumSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    /**
     * 根据参数创建线程池
     *
     * @param threadName    线程名称
     * @param size          循环的size
     * @param maximumSize   最大线程数
     * @param keepAliveTime 当前线程池线程总数达到核心线程数时，终止多余的空闲线程的时间
     * @param timeUnit      keepAliveTime参数的时间单位
     * @param deque         任务队列 如果当前线程池达到核心线程数量corePoolSize后，且当前所有线程都处于活动状态时，则将新加入的任务放到此队列中
     */
    public WorkThreadPool(String threadName, Integer size, Integer maximumSize, Long keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> deque) {
        int coorPoolSize = Math.min(size, maximumSize);
        this.pool = getPool(threadName, coorPoolSize, maximumSize, keepAliveTime, timeUnit, deque);
    }

    /**
     * 创建线程池
     *
     * @param threadName    线程名称
     * @param coorPoolSize  线程数
     * @param maximumSize   最大线程数
     * @param keepAliveTime 当前线程池线程总数达到核心线程数时，终止多余的空闲线程的时间
     * @param timeUnit      keepAliveTime参数的时间单位
     * @param deque         任务队列 如果当前线程池达到核心线程数量corePoolSize后，且当前所有线程都处于活动状态时，则将新加入的任务放到此队列中
     * @return
     */
    private ExecutorService getPool(String threadName, Integer coorPoolSize, Integer maximumSize, Long keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> deque) {
        // 线程工厂 - 定义线程名称
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d-" + threadName).build();
        return new ThreadPoolExecutor(coorPoolSize, maximumSize, keepAliveTime, timeUnit, deque, namedThreadFactory);
    }

    /**
     * 执行任务
     *
     * @param worker
     */
    public void submit(Callable worker) {
        // 执行任务并获取Future对象
        Future f1 = pool.submit(worker);
        // 将任务结果加入集合
        listTask.add(f1);
    }

    /**
     * 线程池终止(在终止前允许执行以前提交的任务)
     */
    public void shutdown() {
        pool.shutdown();
    }

    /**
     * 获取线程执行结果并终止线程
     */
    public void getAndShutdown() {
        // 获取所有并发任务的运行结果
        for (Future f : listTask) {
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.shutdown();
    }

    public static void main(String[] args) {
        WorkThreadPool pool = new WorkThreadPool("test", 4, 8);
        int [] arr = new int[]{1,2,3,4,5,6,7,8,9,10};
        for (int i : arr) {
            pool.submit(() -> {
                System.out.println(i);
                return null;
            });
        }
        pool.getAndShutdown();
    }

}
