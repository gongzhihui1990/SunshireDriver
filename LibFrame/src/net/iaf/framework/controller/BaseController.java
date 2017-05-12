package net.iaf.framework.controller;

import net.iaf.framework.exception.IException;
import net.iaf.framework.imgload.AsyncTask;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller 基类
 * 提供基本的异步处理方法
 *
 * @author Bob
 */
public class BaseController {

    // ==异步任务（请求网络和读写数据库等）========================================================================================

    private ConcurrentHashMap<String, AsyncTask<?, ?, ?>> asyncTaskMap = new ConcurrentHashMap<String, AsyncTask<?, ?, ?>>();

    /**
     * 运行单个异步任务，一个ViewUpdateCallback界面回调，一个DoCallback执行回调
     *
     * @param taskKey
     * @param updateViewAsyncCallback
     * @param doAsyncTaskCallback
     * @param params
     * @throws IException
     */
    protected <Param, Progress, Result> void doAsyncTask(final int taskKey,
                                                         final UpdateViewAsyncCallback<Result> updateViewAsyncCallback,
                                                         final DoAsyncTaskCallback<Param, Result> doAsyncTaskCallback,
                                                         Param... param) {
        doAsyncTask(String.valueOf(taskKey), updateViewAsyncCallback,
                doAsyncTaskCallback, param);
    }

    /**
     * 运行单个异步任务，一个ViewUpdateCallback界面回调，一个DoCallback执行回调
     *
     * @param taskKey
     * @param updateViewAsyncCallback
     * @param doAsyncTaskCallback
     * @param params
     * @throws IException
     */
    protected <Param, Progress, Result> void doAsyncTask(final String taskKey,
                                                         final UpdateViewAsyncCallback<Result> updateViewAsyncCallback,
                                                         final DoAsyncTaskCallback<Param, Result> doAsyncTaskCallback,
                                                         Param... param) {
        // 如果没有需要处理的回调直接退出
        if (null == updateViewAsyncCallback || taskKey == null) {
            return;
        }

        AsyncTask<Param, Void, Result> asyncTask = new AsyncTask<Param, Void, Result>() {
            private IException ie = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                updateViewAsyncCallback.onPreExecute();
            }

            @Override
            protected Result doInBackground(Param... params) {
                Result result = null;
                try {
                    result = doAsyncTaskCallback.doAsyncTask(params);
                } catch (IException ie) {
                    this.ie = ie;
                }
                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);
                if (null == ie) {
                    updateViewAsyncCallback.onPostExecute(result);
                } else {
                    updateViewAsyncCallback.onException(ie);
                    ie = null;
                }
                asyncTaskMap.remove(taskKey);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                updateViewAsyncCallback.onCancelled();
            }

        };

        cancel(taskKey);
        asyncTaskMap.put(String.valueOf(taskKey), asyncTask);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
    }

    /**
     * 按队列的方式顺序运行异步任务
     *
     * @param taskKey
     * @param updateViewAsyncCallback
     * @param doAsyncTaskCallback
     * @param params
     * @throws IException
     */
    protected <Param, Progress, Result> void doAsyncTaskWithSerialExecutor(final String taskKey,
                                                                           final UpdateViewAsyncCallback<Result> updateViewAsyncCallback,
                                                                           final DoAsyncTaskCallback<Param, Result> doAsyncTaskCallback,
                                                                           Param... param) {
        // 如果没有需要处理的回调直接退出
        if (null == updateViewAsyncCallback || taskKey == null) {
            return;
        }

        AsyncTask<Param, Void, Result> asyncTask = new AsyncTask<Param, Void, Result>() {
            private IException ie = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                updateViewAsyncCallback.onPreExecute();
            }

            @Override
            protected Result doInBackground(Param... params) {
                Result result = null;
                try {
                    result = doAsyncTaskCallback.doAsyncTask(params);
                } catch (IException ie) {
                    this.ie = ie;
                }
                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);
                if (null == ie) {
                    updateViewAsyncCallback.onPostExecute(result);
                } else {
                    updateViewAsyncCallback.onException(ie);
                    ie = null;
                }
                asyncTaskMap.remove(taskKey);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                updateViewAsyncCallback.onCancelled();
            }

        };

        cancel(taskKey);
        asyncTaskMap.put(String.valueOf(taskKey), asyncTask);
        asyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, param);
    }

    /**
     * 撤销某个异步任务
     *
     * @param asyncTaskKey
     */
    public void cancel(int asyncTaskKey) {
        cancel(String.valueOf(asyncTaskKey));
    }

    /**
     * 撤销某个异步任务
     *
     * @param asyncTaskKey
     */
    public void cancel(String asyncTaskKey) {
        if (asyncTaskMap.containsKey(asyncTaskKey)) {
            asyncTaskMap.get(asyncTaskKey).cancel(true);
            asyncTaskMap.remove(asyncTaskKey);
        }
    }

    /**
     * 撤销所有任务
     */
    public void cancelAllTasks() {
        Set<Entry<String, AsyncTask<?, ?, ?>>> entrySet = asyncTaskMap.entrySet();
        Iterator<Entry<String, AsyncTask<?, ?, ?>>> it = entrySet.iterator();
        while (it.hasNext()) {
            Entry<String, AsyncTask<?, ?, ?>> entry = it.next();
            AsyncTask<?, ?, ?> task = entry.getValue();
            if (task != null) {
                task.cancel(true);
            }
            it.remove();
        }
    }

    /**
     * 需要controller层实现的线程执行事务的回调接口
     *
     * @param <Param>
     * @param <Result>
     * @author Bob
     */
    public interface DoAsyncTaskCallback<Param, Result> {
        public abstract Result doAsyncTask(Param... params) throws IException;
    }

    /**
     * 全功能线程处理View层Controller参数中实现该接口
     *
     * @param <Result>
     * @author Bob
     */
    public interface UpdateViewAsyncCallback<Result> {
        public abstract void onPreExecute();

        public abstract void onPostExecute(Result result);

        public abstract void onCancelled();

        public abstract void onException(IException ie);
    }

    /**
     * 常规线程处理时View层Controller参数中实现该抽象类 只需实现onPostExecute和onException
     *
     * @author Bob
     */
    public abstract static class CommonUpdateViewAsyncCallback<Result>
            implements UpdateViewAsyncCallback<Result> {
        @Override
        public void onPreExecute() {
        }

        ;

        @Override
        public void onCancelled() {
        }

        ;
    }
}
