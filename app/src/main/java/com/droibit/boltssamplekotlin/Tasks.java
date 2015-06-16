package com.droibit.boltssamplekotlin;

import bolts.Task;

/**
 * Resolve the error that type of {@link Task.TaskCompletionSource}
 * that you created in {@link Task#create()} is unknown. <br>
 *
 * Created by kumagai on 2015/06/16.
 */
public final class Tasks {
  
    private Tasks() { }

    /**
     * Interface to delegate the implementation of the task.
     *
     * @param <T> The type of the result of the task.
     */
    public interface Delegate<T> {
        void create(TaskCompletionSource<T> tcs);
    }

    /**
     * Wrapper class of {@link Task.TaskCompletionSource}.
     * Create the Java side for "Type Mismatch" when generating the A in Kotlin.
     */
    public static class TaskCompletionSource<T> {

        private Task<T>.TaskCompletionSource mSource;

        private TaskCompletionSource(Task<T>.TaskCompletionSource tcs) {
            mSource = tcs;
        }

        public boolean trySetCancelled() {
            return mSource.trySetCancelled();
        }

        public boolean trySetResult(T result) {
            return mSource.trySetResult(result);
        }

        public boolean trySetError(Exception error) {
            return mSource.trySetError(error);
        }

        public void setCancelled() {
            mSource.setCancelled();
        }

        public void setResult(T result) {
            mSource.setResult(result);
        }

        public void setError(Exception error) {
            mSource.setError(error);
        }
    }

    /**
     * Create a new Task, including asynchronous processing.
     *
     * @param delegate
     * @return
     */
    public static <T> Task<T> createTask(Delegate<T> delegate) {
        final Task<T>.TaskCompletionSource tcs = Task.create();
        delegate.create(new TaskCompletionSource<>(tcs));
        return tcs.getTask();
    }
}