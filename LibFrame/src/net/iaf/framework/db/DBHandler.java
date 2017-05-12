package net.iaf.framework.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import net.iaf.framework.exception.DBException;


/**
 * Database
 */
public class DBHandler {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    /**
     * Use the default context structure, database operation before judge
     * database is on
     *
     * @see {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()}
     */
    public DBHandler(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * Context of the context DBHandler object structure
     *
     * @param context
     * @see {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()}
     */
    public DBHandler(DBHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * close database
     */
    public void closeDatabase() {
        dbHelper.close();
    }

    /**
     * @see {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()}
     */
    public SQLiteDatabase getWriteableDatabase() throws DBException {
        try {
            db = dbHelper.getWritableDatabase();
            return db;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Open Database
     *
     * @param
     * @return void
     * @throws DBException
     * @Title: openDatabase
     * @Description:
     * @see {@link android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()}
     */
    public void openDatabase() throws DBException {
        this.getWriteableDatabase();
    }

    /**
     * <p>
     * is open database
     * </p>
     *
     * @return .
     */
    public boolean isOpen() {
        if (null == db) {
            return false;
        }
        return db.isOpen();
    }

    public void beginTransaction() {
        db.beginTransaction();
    }

    public void commitTransaction() {
        db.setTransactionSuccessful();
    }

    public void endTransaction() {
        db.endTransaction();
    }

    /**
     * query
     *
     * @param sql           the SQL query. The SQL string must not be terminated.
     * @param selectionArgs You may include ?s in where clause in the query, which will be
     *                      replaced by the values from selectionArgs. The values will be
     *                      bound as Strings.
     * @return object, which is positioned before the first entry. Note that
     * Cursors are not synchronized, see the documentation for more
     * details.
     * @see {@link android.database.sqlite.SQLiteDatabase#rawQuery(String, String[])}
     */
    public Cursor query(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    /**
     * query
     *
     * @param sql
     * @param selectionArgs
     * @return object, which is positioned before the first entry. Note that
     * Cursors are not synchronized, see the documentation for more
     * details.
     */
    public Cursor query(String sql) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     * query
     *
     * @param distinct
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     * @see Cursor
     * @see {@link SQLiteDatabase#query(boolean, String, String[], String, String[], String, String, String, String)}
     */
    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        return cursor;
    }

    /**
     * query
     *
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @return
     * @see Cursor
     * @see SQLiteDatabase#query(String, String[], String, String[], String,
     * String, String, String)
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, orderBy, null);
        return cursor;
    }

    /**
     * execute
     *
     * @param sql
     * @throws SQLException
     * @see SQLiteDatabase#execSQL(String)
     */
    public void execute(String sql) throws SQLException {
        db.execSQL(sql);
    }

    /**
     * insert
     *
     * @param table
     * @param values
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     * @see SQLiteDatabase#insert(String, String, ContentValues)
     */
    public long insert(String table, ContentValues contentValues) {
        long result = db.insert(table, null, contentValues);
        return result;
    }

    /**
     * update
     *
     * @param tableName
     * @param contentValues
     * @param whereClause
     * @param whereArgs
     * @return <code>int</code>
     * @see SQLiteDatabase#update(String, ContentValues, String, String[])
     */
    public int update(String table, ContentValues contentValues, String whereClause, String[] whereArgs) {
        int result = db.update(table, contentValues, whereClause, whereArgs);
        return result;
    }

    /**
     * delete
     *
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return the number of rows affected if a whereClause is passed in, 0
     * otherwise. To remove all rows and get a count pass "1" as the
     * whereClause.
     * @see SQLiteDatabase#delete(String, String, String[])
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        int result = 0;
        if (tabIsExist(table)) {
            result = db.delete(table, whereClause, whereArgs);
        }
        return result;
    }


    /**
     * 判断某张表是否存在
     *
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
}
