package com.sunshireshuttle.driver.database.provider;

import android.provider.BaseColumns;

/**
 * 保存数据库中的常量
 *
 * @author jacp
 */
public class Provider {

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.smartpos.content";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.smartpos.content";


    public static final class LocationRecordColumns implements BaseColumns {
        public static final String TABLE_NAME = "tb_location_record";
        //		private String tripId;
//		private long recordTime;
//		private String longitude;//经度
//		private String latitude;//纬度
        public static final String TRIPID = "tripId";
        public static final String RECORDTIME = "recordTime";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String ACTION = "action";
        public static final String STATE = "state";
        public static final String DEFAULT_SORT_ORDER = RECORDTIME + " desc";


    }

}
