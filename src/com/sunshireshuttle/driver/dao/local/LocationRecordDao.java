package com.sunshireshuttle.driver.dao.local;

import java.util.ArrayList;

import com.sunshireshuttle.driver.database.provider.Provider.LocationRecordColumns;
import com.sunshireshuttle.driver.model.LocationRecordBean;
import com.sunshireshuttle.driver.utils.DateUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.iaf.framework.exception.DBException;
import net.iaf.framework.util.Loger;


public class LocationRecordDao extends LocalBaseDao {

    String table_name = LocationRecordColumns.TABLE_NAME;

    public LocationRecordDao() {
        try {
            init();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            delete(table_name);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    ;

    /**
     * 插入一条PaymentBean到数据库，遇到重复则更新
     *
     * @param bean
     * @throws DBException
     */
    public void insert(LocationRecordBean bean) throws DBException {
        SQLiteDatabase db = dbHandler.getDb();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(LocationRecordColumns.TRIPID, bean.getTripId());
            cv.put(LocationRecordColumns.RECORDTIME, bean.getRecordTime());
            cv.put(LocationRecordColumns.LONGITUDE, bean.getLongitude());
            cv.put(LocationRecordColumns.LATITUDE, bean.getLatitude());
            cv.put(LocationRecordColumns.STATE, bean.getState());
            cv.put(LocationRecordColumns.ACTION, bean.getAction());
            db.insertWithOnConflict(table_name, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void update(LocationRecordBean bean) throws DBException {
        SQLiteDatabase db = dbHandler.getDb();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(LocationRecordColumns.STATE, bean.getState());
            String whereClause = LocationRecordColumns.TRIPID + "=?," + LocationRecordColumns.RECORDTIME + "=?";
            String[] whereArgs = {String.valueOf(bean.getTripId()), String.valueOf(bean.getRecordTime())};
            db.updateWithOnConflict(table_name, cv, whereClause, whereArgs, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public LocationRecordBean getLastReocrdByTripId(String tripId) {
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append("select * from " + table_name + " where " + LocationRecordColumns.TRIPID + " = '" + tripId + "' " + "order by " + LocationRecordColumns.DEFAULT_SORT_ORDER + " limit 0,1");
        LocationRecordBean bean = null;
        Cursor cursor = querySQL(sqlSb);
        if (cursor.moveToNext()) {
            bean = cursorlizable(cursor);
        }
        if (bean != null) {
            Loger.e("sqlSb:getLastReocrdByTripId-" + bean.toString());
        } else {
            Loger.e("sqlSb:getLastReocrdByTripId- is null");
        }
        cursor.close();
        return bean;
    }

    public ArrayList<LocationRecordBean> getListByTripId(String tripId) {
        ArrayList<LocationRecordBean> beans = new ArrayList<LocationRecordBean>();
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append("select * from " + table_name + " where " + LocationRecordColumns.TRIPID + " = '" + tripId + "'");
        Cursor cursor = querySQL(sqlSb);
        Loger.e("sqlSb:" + cursor.getCount());
        while (cursor.moveToNext()) {
            LocationRecordBean bean = cursorlizable(cursor);
            Loger.e("sqlSb:" + bean.toString());
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }

    /**
     * @return
     */
    public ArrayList<String> getAllTripId() {
        ArrayList<String> strings = new ArrayList<>();
        StringBuffer sqlSb = new StringBuffer();
//		DISTINCT
        sqlSb.append("select DISTINCT " + LocationRecordColumns.TRIPID + " from " + table_name);
        Cursor cursor = querySQL(sqlSb);
        String item = "";
        while (cursor.moveToNext()) {
            item = cursor.getString(cursor.getColumnIndex(LocationRecordColumns.TRIPID));
            strings.add(item);
        }
        cursor.close();
        return strings;
    }


    private Cursor querySQL(StringBuffer sqlSb) {
        return querySQL(sqlSb.toString());
    }

    private Cursor querySQL(String sqlSb) {
        Loger.e("SQL:" + sqlSb);
        return dbHandler.query(sqlSb);
    }


    private static final LocationRecordBean cursorlizable(Cursor cursor) {
        LocationRecordBean bean = new LocationRecordBean();
        String tripId = cursor.getString(cursor.getColumnIndex(LocationRecordColumns.TRIPID));
        long recordTime = cursor.getLong(cursor.getColumnIndex(LocationRecordColumns.RECORDTIME));
        String longitude = cursor.getString(cursor.getColumnIndex(LocationRecordColumns.LONGITUDE));
        String latitude = cursor.getString(cursor.getColumnIndex(LocationRecordColumns.LATITUDE));
        int state = cursor.getInt(cursor.getColumnIndex(LocationRecordColumns.STATE));
        String action = cursor.getString(cursor.getColumnIndex(LocationRecordColumns.ACTION));
        bean.setTripId(tripId);
        bean.setRecordTime(recordTime);
        bean.setLongitude(longitude);
        bean.setLatitude(latitude);
        bean.setState(state);
        bean.setAction(action);
        return bean;
    }


}
