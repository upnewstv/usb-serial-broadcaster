
package tv.onsign.rc.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class CaptureExceptionsScheduledExecutor extends ScheduledThreadPoolExecutor {
    static private Logging sLogging = new Logging(CaptureExceptionsScheduledExecutor.class);

    private static class NamedThreadFactory implements ThreadFactory {
        private final String mName;

        public NamedThreadFactory(String name) {
            mName = name;
        }

        @Override
        public Thread newThread(Runnable r) {
          return new Thread(r, mName);

        }
    }

    public CaptureExceptionsScheduledExecutor(int corePoolSize, String name) {
        super(corePoolSize, new NamedThreadFactory(name));
    }

    @Override
    public void execute(Runnable command) {
        try {
            super.execute(detectExceptionsIn(command));
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        try {
            return super.schedule(detectExceptionsIn(command), delay, unit);
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        try {
            return super.submit(detectExceptionsIn(task));
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,
            long delay, TimeUnit unit) {
        try {
            return super.scheduleWithFixedDelay(detectExceptionsIn(command), initialDelay, delay, unit);
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period,
            TimeUnit unit) {
        try {
            return super.scheduleAtFixedRate(detectExceptionsIn(command), initialDelay, period, unit);
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        try {
            return super.schedule(detectExceptionsIn(callable), delay, unit);
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        try {
            return super.submit(detectExceptionsIn(task));
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    @Override
    public <T> java.util.concurrent.Future<T> submit(Runnable task, T result) {
        try {
            return super.submit(detectExceptionsIn(task), result);
        } catch (RejectedExecutionException e) {
            sLogging.warning("Job rejected, executor is shutdown.");
        }
        return null;
    }

    private static Runnable detectExceptionsIn(final Runnable command) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } catch (Exception e) {
                    onException(e);
                }
            }
        };
    }

    private static <V> Callable<V> detectExceptionsIn(final Callable<V> callable) {
        return new Callable<V>() {
            @Override
            public V call() throws Exception {
                try {
                    return callable.call();
                } catch (Exception e) {
                    onException(e);
                }

                return null;
            }
        };
    }

    private static void onException(Exception e) {
        UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        if (handler != null) handler.uncaughtException(Thread.currentThread(), e);
    }
}
