package com.sunshireshuttle.driver.dao;

import net.iaf.framework.db.DBHandler;
import net.iaf.framework.util.Version;

import java.util.ArrayList;
import java.util.List;

import com.sunshireshuttle.driver.database.provider.Provider.LocationRecordColumns;

public class DBConstants {

    public static final String DATABASE_NAME = "cachedb.db";
    public static final int DATABASE_VERSION = Version.getVersionCode();

    public static String TABLE_CACHE = "tb_cache";

    public static List<String> DB_CREATE_SQL;
    public static DBHandler dbHandler;

    static {
        DB_CREATE_SQL = new ArrayList<String>();

        /**
         * 缓存内容表
         */
        DB_CREATE_SQL.add("DROP TABLE IF EXISTS " + TABLE_CACHE + ";");
        DB_CREATE_SQL.add("CREATE TABLE IF NOT EXISTS " + TABLE_CACHE + " ( " +
                "_id INTEGER PRIMARY KEY autoincrement, " +
                "key TEXT NOT NULL," +
                "create_time INTEGER NOT NULL," +
                "value BLOB NOT NULL" +
                ");");
        /**
         * 记录导航表
         */
        DB_CREATE_SQL.add("DROP TABLE IF EXISTS " + LocationRecordColumns.TABLE_NAME + ";");
        DB_CREATE_SQL.add("CREATE TABLE IF NOT EXISTS " + LocationRecordColumns.TABLE_NAME + " ( " +
                "_id INTEGER PRIMARY KEY autoincrement, " +
                LocationRecordColumns.TRIPID + " TEXT NOT NULL," +
                LocationRecordColumns.RECORDTIME + " INTEGER NOT NULL," +
                LocationRecordColumns.LONGITUDE + " TEXT NOT NULL," +
                LocationRecordColumns.LATITUDE + " TEXT NOT NULL," +
                LocationRecordColumns.ACTION + " TEXT NOT NULL," +
                LocationRecordColumns.STATE + " INTEGER NOT NULL" +
                ");");

    }

}
