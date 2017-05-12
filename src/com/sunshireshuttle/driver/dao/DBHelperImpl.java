package com.sunshireshuttle.driver.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sunshireshuttle.driver.SunDriverApplication;

import net.iaf.framework.db.DBHelper;
import net.iaf.framework.exception.DBException;
import net.iaf.framework.util.Loger;
import net.iaf.framework.util.ObjectConverter;
import net.iaf.framework.util.Version;

import java.util.Date;

public class DBHelperImpl extends DBHelper {

    public DBHelperImpl() {
        super(SunDriverApplication.getContext(), DBConstants.DATABASE_NAME, DBConstants.DATABASE_VERSION, DBConstants.DB_CREATE_SQL);
    }

    /**
     * 该处要区别升级的不同原始版本,做不同的数据库更新处理
     */
    @Override
    protected void UpgradeDB(SQLiteDatabase db, int oldVersion, int newVersion) {
        Loger.d("================oldVersion" + oldVersion + ",newVersion" + newVersion);
        if (db != null) {
            try {
                db.beginTransaction();

                if (oldVersion < Version.getVersionCode()) {

                    // ==当前版本针对之前所有版本的处理开始=================================================
                    // 处理结构改变，但需要保持原数据的表。用ALERT方式ADD COLUMN来更新表结构，需要考虑从不同版本升级情况！
                    // 处理结构改变，但不需要保存原数据的表。可以删除不用的表，或简单通过DROP和CREATE方式更新表结构。
//					if(oldVersion < 20000){
//						//2.0之前版本升级到当前版本需要重新创建数据表
//						onCreate(db);
//					}else if(oldVersion < 20100){
//						//2.0.X 升级到当前版本 添加搜索历史记录表
//						ArrayList<String> sqls = new ArrayList<String>();
//						sqls.add("DROP TABLE IF EXISTS tb_position_point_his;");
//						sqls.add("CREATE TABLE tb_position_point_his ("
//								+ "_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
//								+ "lat  TEXT(15)," //
//								+ "lon  TEXT(15)," //
//								+ "address_short  TEXT(255)," //
//								+ "address_long  TEXT(255),"  //
//								+ "creattime  TEXT(20) NOT NULL" //创建日期
//								+ ");");
//						for (String sql : sqls) {
//							db.execSQL(sql);
//						}
//					}
                    if (oldVersion < 20200) {
                        onCreate(db);
                    }
                    // ==当前版本针对之前所有版本的处理结束=================================================
                }

                db.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        } else {
            throw new IllegalArgumentException(getClass().getSimpleName() + "\tonUpgrade()");
        }

    }

    @Override
    protected void InitDB(SQLiteDatabase db) {

    }

    public void setDataToLocal(Object data, String cacheKey) throws DBException {
        byte[] bytes = null;
        try {
            bytes = ObjectConverter.ObjectToByte(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException();
        }
        if (bytes != null) {
            beginTransaction();
            Cursor cursor = null;
            try {
                cursor = query("SELECT value FROM tb_cache WHERE key=?", new String[]{cacheKey});
                if (cursor.moveToFirst()) {
                    execute(
                            "UPDATE tb_cache SET create_time=?, value=? WHERE key=?;",
                            new Object[]{new Date().getTime(), bytes, cacheKey});

                } else {
                    execute(
                            "INSERT INTO tb_cache (key ,create_time, value) VALUES (?, ?, ?);",
                            new Object[]{cacheKey, new Date().getTime(), bytes});
                }
                setTransactionSuccessful();
            } catch (DBException e) {
                throw new DBException();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
                endTransaction();
            }

        }
    }

    public Object getDataFromLocal(String cacheKey) throws DBException {
        byte[] bytes = null;
        Cursor cursor = query("SELECT value FROM tb_cache WHERE key=?", new String[]{cacheKey});
        if (cursor.moveToFirst()) {
            bytes = cursor.getBlob(0);
        }
        cursor.close();
        if (bytes == null) { //没有数据返回null
            return null;
        }
        try {
            Object t = ObjectConverter.ByteToObject(bytes);
            return t;
        } catch (Exception e) {
            throw new DBException();
        }
    }
    // /**
    // * 清空数据库缓存，仅保留城市列表
    // */
    // public void clearCache(){
    // this.delete("tb_cache", "key!=?", new String[]{APITool.URL_DOMAIN +
    // "GetCityList?"});
    // }
}
