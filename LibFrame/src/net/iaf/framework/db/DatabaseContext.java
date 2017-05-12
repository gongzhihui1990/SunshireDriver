package net.iaf.framework.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.io.File;

public class DatabaseContext extends ContextWrapper {
    public DatabaseContext(Context context) {
        super(context);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public File getDatabasePath(String name) {
        return super.getDatabasePath(name);
//	    //判断是否存在sd卡
//	   // boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
//	    //if(!sdExist){//如果不存在,
//	    //	Loger.e("不存在sd卡");
//	     // return null;
//	    //}else{//如果存在
//	      //获取sd卡路径
//	      String dbDir= PersistenceFileManagerDataFilesImpl.getInstance().getRootPath(this)+"/database";
//	      String dbPath = dbDir+"/"+name;//数据库路径
//	      //判断目录是否存在，不存在则创建该目录
//	      File dirFile = new File(dbDir);
//	      if(!dirFile.exists()){
//	       boolean mkSuccess= dirFile.mkdirs();
//	       Loger.d(dirFile.getPath()+" mkSuccess:"+mkSuccess);
//	      }
//	      //数据库文件是否创建成功
//	      boolean isFileCreateSuccess = false; 
//	      //判断文件是否存在，不存在则创建该文件
//	      File dbFile = new File(dbPath);
//	      if(!dbFile.exists()){
//	        try {		
//	        	  boolean mkSuccess= dbFile.mkdirs();
//	   	       Loger.d(dbFile.getPath()+ "mkSuccess:"+mkSuccess);
//	          isFileCreateSuccess = dbFile.createNewFile();//创建文件
//	        } catch (IOException e) {
//	          e.printStackTrace();
//	        }
//	      }else{
//	        isFileCreateSuccess = true;
//	      }
//	      //返回数据库文件对象
//	      if(isFileCreateSuccess){
//	        return dbFile;
//	      }else{
//	        return null;
//	      }
//	    //}
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}