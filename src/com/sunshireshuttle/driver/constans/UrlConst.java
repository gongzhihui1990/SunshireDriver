package com.sunshireshuttle.driver.constans;

public interface UrlConst {
    public static final String API_ConnectionKey = "sunshireshuttle";
    public static final String API_HOST = "http://sunshireshuttle.com/";
    public static final String APIS_HOST = "https://sunshireshuttle.com/";
    public static final String ApiURL = API_HOST + "app/mirror_api.php";
    public static final String SearchFlightURL = API_HOST + "chrisyao4700/search_flight.php";
    public static final String SynchroContactHistoryURL = APIS_HOST + "chrisyao4700/synchro_contact_history.php";
    public static final String SynchroTripHistoryURL = APIS_HOST + "chrisyao4700/synchro_trip_history.php";
    public static final String SearchCustomerLocationURL = API_HOST + "app/search_customer_location.php";
    public static final String DriverInfoURL = API_HOST + "chrisyao4700/getDriverInfo.php";
    public static final String HistoryOrderURL = API_HOST + "chrisyao4700/get_history_order.php";
    public static final String CurrentOrderURL = API_HOST + "chrisyao4700/getCurrentOrder.php";
    public static final String UpdateLocationURL = API_HOST + "chrisyao4700/updateLocation.php";
    public static final String OrderDetailURL = API_HOST + "app/api.php";
    /**
     * 不缓存
     */
    public static final int CACHE_TIME_NO_CACHE = 0;

    public static final int CACHE_TIME_ONE_MINUTE = 1;

    public static final int CACHE_TIME_FIVE_MINUTE = 5;

    public static final int CACHE_TIME_ONE_HOUR = 60;

    public static final int CACHE_TIME_ONE_DAY = 24 * 60;

    public static final int CACHE_TIME_ONE_WEEK = 7 * 24 * 60;

    public static final int CACHE_TIME_FOREVER = Integer.MAX_VALUE;
}
