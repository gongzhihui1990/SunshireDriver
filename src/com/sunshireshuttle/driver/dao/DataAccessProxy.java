package com.sunshireshuttle.driver.dao;

import android.database.Cursor;

import net.iaf.framework.db.DBHelper;
import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.ObjectConverter;

import java.util.Date;
import java.util.HashMap;

public class DataAccessProxy<T extends BaseJsonEntity<T>> extends DataAccess<T> {

    protected DataAccessRemote<T> dataAccessRemote;
    private DBHelper dbHelperImpl;

    /**
     * 子类构造器务必调用该方法
     */
    public DataAccessProxy(BaseJsonEntity<T> jsonEntity) {
        super(jsonEntity);
        initDB();
        dataAccessRemote = new DataAccessRemote<T>(jsonEntity);
    }

    @Override
    public T getData(HashMap<String, String> paramsMap, boolean cacheInvalidate) throws TimeoutException, NetworkException, DBException {
        buildUrl(paramsMap);
        T entity = null;
        if (isCacheTimeout()) {
//			Log.v("getData", "[Time Out!!]"+this.urlCacheKey);
            try {
                entity = dataAccessRemote.getData(paramsMap, cacheInvalidate);
            } catch (TimeoutException e) {
                e.printStackTrace();
                throw new TimeoutException();
            }
            if (entity != null) {
                try {
                    setDataToLocal(entity, cacheInvalidate);
                } catch (DBException e) {
                    //TODO
                    e.printStackTrace();
                }
            }
        } else {
//			Log.v("getData", "[Not Time Out!!]"+this.urlCacheKey);
            entity = getDataFromLocal(paramsMap);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public T getDataFromLocal(HashMap<String, String> paramsMap) throws DBException {
        byte[] bytes = null;
        T t = null;
        buildUrl(paramsMap);
        Cursor cursor = dbHelperImpl.query("SELECT value FROM tb_cache WHERE key=?", new String[]{this.urlCacheKey});
        if (cursor.moveToFirst()) {
            bytes = cursor.getBlob(0);
        }
        cursor.close();
        if (bytes == null) { //没有数据返回null
            return null;
        }
        try {
            t = (T) ObjectConverter.ByteToObject(bytes);
        } catch (Exception e) {
            throw new DBException();
        }
        return t;
    }


    /**
     * @param @param  data
     * @param @throws DBException
     * @return void
     * @throws DBException
     * @throws
     * @Title: setDataToLocal
     * @Description: 更新数据到缓存，该方法在 getDataFromRemote后调用，
     */
    public void setDataToLocal(T data, boolean cacheInvalidate) throws DBException {
        byte[] bytes = null;
        try {
            bytes = ObjectConverter.ObjectToByte(data);
        } catch (Exception e) {
            e.printStackTrace();
//			throw new DBException();
        }
        if (bytes != null) {
            dbHelperImpl.beginTransaction();
            if (cacheInvalidate) {
                cacheInvalidate();
            }
            Cursor cursor = null;
            try {
                cursor = dbHelperImpl.query("SELECT value FROM tb_cache WHERE key=?", new String[]{this.urlCacheKey});
                if (cursor.moveToFirst()) {
                    dbHelperImpl.execute(
                            "UPDATE tb_cache SET create_time=?, value=? WHERE key=?;",
                            new Object[]{new Date().getTime(), bytes, this.urlCacheKey});

                } else {
                    dbHelperImpl.execute(
                            "INSERT INTO tb_cache (key ,create_time, value) VALUES (?, ?, ?);",
                            new Object[]{this.urlCacheKey, new Date().getTime(), bytes});
                }
                dbHelperImpl.setTransactionSuccessful();
            } catch (DBException e) {
                throw new DBException();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
                dbHelperImpl.endTransaction();
            }

        }
    }

    /**
     * 将数据保存至本地缓存（主要用于需删除本地缓存数据的case）
     *
     * @param data
     * @param paramsMap
     * @throws DBException
     */
    public void setDataToLocal(T data, HashMap<String, String> paramsMap) throws DBException {
        byte[] bytes = null;
        try {
            bytes = ObjectConverter.ObjectToByte(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String cacheKey = this.urlWithoutParam + paramMap2paramStr(paramsMap);

        if (bytes != null) {
            dbHelperImpl.beginTransaction();
            Cursor cursor = null;
            try {
                cursor = dbHelperImpl.query("SELECT value FROM tb_cache WHERE key=?", new String[]{cacheKey});
                if (cursor.moveToFirst()) {
                    dbHelperImpl.execute(
                            "UPDATE tb_cache SET create_time=?, value=? WHERE key=?;",
                            new Object[]{new Date().getTime(), bytes, cacheKey});

                } else {
                    dbHelperImpl.execute(
                            "INSERT INTO tb_cache (key ,create_time, value) VALUES (?, ?, ?);",
                            new Object[]{cacheKey, new Date().getTime(), bytes});
                }
                dbHelperImpl.setTransactionSuccessful();
            } catch (DBException e) {
                throw new DBException();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
                dbHelperImpl.endTransaction();
            }

        }
    }

    /**
     * @param @throws DBException
     * @return void
     * @throws
     * @Title: cacheClear
     * @Description: 缓存清理  缓存删除，数据库可用where条件加URL字段包含方式删除
     */
    public void cacheInvalidate() throws DBException {
        dbHelperImpl.execute("DELETE FROM tb_cache WHERE key like '" + this.urlWithoutParam + "%';");
    }

    //--工具函数-------------------------------------------------------------

    protected long getLastRefreshTime() throws DBException {
        long lastRefreshTime = 0; //默认超时
        Cursor cursor = dbHelperImpl.query("SELECT create_time FROM tb_cache WHERE key=?", new String[]{this.urlCacheKey});
        if (cursor.moveToFirst()) {
            lastRefreshTime = cursor.getLong(0);
        }
        cursor.close();
        return lastRefreshTime;
    }

    private boolean isCacheTimeout() {
        long currentTime = System.currentTimeMillis();
        boolean timeout = false;
        try {
            long tmp = currentTime - getLastRefreshTime();
            timeout = (tmp > cacheTime || tmp < 0);
        } catch (DBException e) { //判断刷新时间的时候出现数据库异常，则返回超时，走取新数据流程
            timeout = true;
        }
        return timeout;
    }

    protected long getLastRefreshTime(String urlKey) throws DBException {
        long lastRefreshTime = 0; //默认超时
        Cursor cursor = dbHelperImpl.query("SELECT create_time FROM tb_cache WHERE key=?", new String[]{urlKey});
        if (cursor.moveToFirst()) {
            lastRefreshTime = cursor.getLong(0);
        }
        cursor.close();
        return lastRefreshTime;
    }

    public boolean isCacheTimeout(HashMap<String, String> paramsMap) {
        String urlKey = this.urlWithoutParam + paramMap2paramStr(paramsMap);
        long currentTime = System.currentTimeMillis();
        try {
            return (currentTime - getLastRefreshTime(urlKey) > cacheTime) ? true : false;
        } catch (DBException e) { //判断刷新时间的时候出现数据库异常，则返回超时，走取新数据流程
            return true;
        }
    }

    private void initDB() {
        this.dbHelperImpl = new DBHelperImpl();
    }

}
