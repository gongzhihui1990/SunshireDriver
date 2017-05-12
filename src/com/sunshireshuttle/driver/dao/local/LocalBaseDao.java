package com.sunshireshuttle.driver.dao.local;

import net.iaf.framework.db.DBHandler;
import net.iaf.framework.db.DBHelper;
import net.iaf.framework.exception.DBException;

import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.dao.DBConstants;
import com.sunshireshuttle.driver.dao.DBHelperImpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public class LocalBaseDao {
    /**
     * The database of the basic operation DBHandler object
     */
    public DBHandler dbHandler;

    /**
     * Every operation before judge whether database database open, if not open,
     * opened first database, to return to DBHandler object
     *
     * @return DBHandler
     * @throws SQLException
     * @see DBHandler#openDatabase()
     */
    public DBHandler init() throws DBException {
        if (DBConstants.dbHandler == null) {
            DBHelper dbHelper = new DBHelperImpl();
            DBConstants.dbHandler = new DBHandler(dbHelper);
        }
        this.dbHandler = DBConstants.dbHandler;
        if (!dbHandler.isOpen()) {
            try {
                dbHandler.openDatabase();
            } catch (Exception e) {
                throw new DBException("Database init error!", e);
            }
        }
        return this.dbHandler;
    }

    public void closeDatebase() {
        if (dbHandler != null && dbHandler.isOpen()) {
            dbHandler.closeDatabase();
        }
    }

    /**
     * To database insert data
     *
     * @param tableName
     * @param columns
     * @param contents
     * @param startIndex
     * @throws DBException
     */
    public void insert(String tableName, String columns[], String[] contents, int startIndex) throws DBException {
        ContentValues contentValues = buildContentValues(columns, startIndex, contents);
        long temp = dbHandler.insert(tableName, contentValues);
        if (-1 == temp) {
            throw new DBException(getClass().getSimpleName() + ": insert error ");
        }
    }

    /**
     * To update the data in the data, updated returning true, otherwise returns
     * false
     *
     * @param userIdIndex :要查询的条件的列名编号
     * @param tableName   ：表名
     * @param columns     ：要查询的条件的列的集合
     * @param startIndex  ：要修改的列编号
     * @param contents    ：需要修改的值
     * @return boolean
     * @throws DBException
     */
    public boolean update(int userIdIndex, String tableName, String columns[], int startIndex, String[] contents,
                          String userid) throws DBException {
        ContentValues contentValues = buildContentValues(columns, startIndex, contents);
        String whereClause = columns[userIdIndex] + "=?";
        String[] whereArgs = {userid};
        int temp = dbHandler.update(tableName, contentValues, whereClause, whereArgs);
        return temp >= 1;
    }

    /**
     * Delete this UserId record
     *
     * @param tableName
     * @param columnNameUserId
     * @throws DBException
     */
    public void delete(String tableName, String columnNameUserId, String userid) throws DBException {
        String whereClause = columnNameUserId + "=?";
        String whereArgs[] = {userid};
        dbHandler.delete(tableName, whereClause, whereArgs);
    }

    /**
     * Delete table
     *
     * @param tableName
     */
    public void delete(String tableName) throws DBException {
        dbHandler.delete(tableName, null, null);
    }

    /**
     * Lookup < code > tableName < / code > in < code > userId < / code > record
     *
     * @param tableName
     * @param columnNameUserId
     * @return
     */
    // public Cursor query(String tableName, String columnNameUserId, String
    // userid) {
    // String querySQL = "select * from " + tableName + " where " +
    // columnNameUserId + " = ?" ;
    // String[] selectionArgs = { userid };
    // Cursor cursor = dbHandler.query(querySQL, selectionArgs);
    // return cursor;
    // }

    /**
     * Lookup < code > tableName < / code > in < code > userId < / code > record
     *
     * @param tableName
     * @param columnNameUserId
     * @param orderBy          "ORDER BY order ASC"
     * @return cursor
     */
    public Cursor query(String tableName, String columnNameUserId, String orderBy, String userid) {
        String querySQL = "select * from " + tableName + " where " + columnNameUserId + " = ? " + " order by "
                + orderBy;
        String[] selectionArgs = {userid};
        Cursor cursor = dbHandler.query(querySQL, selectionArgs);
        return cursor;
    }

    public Cursor query(String tableName, String selectionArgs, String[] selectionValue, String orderBy,
                        String firstResult, String maxResult) {
        String querySQL = "select * from " + tableName + " where " + selectionArgs + " order by " + orderBy + " limit "
                + Integer.parseInt(firstResult) + "," + Integer.parseInt(maxResult);
        Cursor cursor = dbHandler.query(querySQL, selectionValue);
        return cursor;
    }

    public Cursor query(String querySQL, String[] selectionValue) {
        Cursor cursor = dbHandler.query(querySQL, selectionValue);
        return cursor;
    }

    /**
     * Lookup < code > tableName < / code > in < code > userId < / code > record
     *
     * @param tableName
     * @param columnNameUserId
     * @param orderBy          "ORDER BY order ASC"
     * @return cursor
     */
    public Cursor query(String tableName) {
        String querySQL = "select * from " + tableName;
        Cursor cursor = dbHandler.query(querySQL);
        return cursor;
    }

    /**
     * Lookup < code > tableName < / code > in < code > userId < / code > record
     *
     * @param tableName
     * @param columnNameUserId
     * @param orderBy          "ORDER BY order ASC"
     * @return cursor
     */
    public Cursor queryOrderBy(String tableName, String oderBy) {
        String querySQL = "select * from " + tableName + " " + oderBy;
        Cursor cursor = dbHandler.query(querySQL);
        return cursor;
    }

    /**
     * Lookup < code > tableName < / code > of record
     *
     * @param tableName
     * @param selection     where "uid = ?"
     * @param selectionArgs
     * @param orderBy       "ORDER BY order ASC"
     * @return cursor
     */
    public Cursor query(String tableName, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = dbHandler.query(tableName, null, selection, selectionArgs, orderBy);
        return cursor;
    }

    /**
     * Generate insert to update the database of the required contentValues
     * (key-value pairs.) array columns and contents length should be consistent
     *
     * @param columns
     * @param startIndex
     * @param contents
     * @return contentValues
     */
    public ContentValues buildContentValues(String columns[], int startIndex, String[] contents) {
        if (columns == null || contents == null) {
            throw new IllegalArgumentException("array is null");
        }
        final int N = columns.length;
        if (startIndex < 0 || startIndex >= N || N != contents.length) {
            throw new IllegalArgumentException("array index error.");
        }
        ContentValues contentValues = new ContentValues();
        for (int i = startIndex; i < N; i++) {
            contentValues.put(columns[i], contents[i]);
        }
        return contentValues;
    }

    public ContentValues buildContentValues(String columns[], int startIndex[], String[] contents) {
        if (columns == null || contents == null) {
            throw new IllegalArgumentException("array is null");
        }
        final int N = columns.length;
        if (startIndex.length < 0 || startIndex.length >= N) {
            throw new IllegalArgumentException("array index error.");
        }
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < startIndex.length; i++) {
            contentValues.put(columns[startIndex[i]], contents[i]);
        }
        return contentValues;
    }

    /**
     * Whether in the table with the current uesr_id and other_id exist at a
     * record
     */
    public boolean haveTheRecord(String table, String id, String value, String columnNameUserId, String userid) {
        Cursor cursor = queryRecord(table, id, value, columnNameUserId, userid);
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        return isExist;
    }

    /**
     * Lookup < code > tableName < / code > in < code > userId < / code > record
     *
     * @param tableName
     * @param merchant_id
     * @return cursor
     */
    public Cursor queryRecord(String tableName, String id, String value, String columnNameUserId, String userid) {
        String querySQL = "select * from " + tableName + " where " + id + " = ?  and " + columnNameUserId + " = ? ";
        String[] selectionArgs = {value, userid};
        Cursor cursor = dbHandler.query(querySQL, selectionArgs);
        return cursor;
    }

    /**
     * To update the data in the data, updated returning true, otherwise returns
     * false
     *
     * @param userIdIndex
     * @param tableName
     * @param columns
     * @param startIndex
     * @param contents
     * @return boolean
     * @throws DBException
     */
    public boolean update(int userIdIndex, int otherIdIndex, String tableName, String columns[], int startIndex,
                          String[] contents, String otherid, String userid) throws DBException {
        ContentValues contentValues = buildContentValues(columns, startIndex, contents);
        String whereClause = columns[userIdIndex] + "=? and " + columns[otherIdIndex] + "=?";
        String[] whereArgs = {userid, otherid};
        int temp = dbHandler.update(tableName, contentValues, whereClause, whereArgs);
        return temp >= 1;
    }
}
