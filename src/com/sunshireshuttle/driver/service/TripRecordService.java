package com.sunshireshuttle.driver.service;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.constans.UrlConst;
import com.sunshireshuttle.driver.dao.DriverRemoteDao;
import com.sunshireshuttle.driver.dao.local.LocationRecordDao;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.DirverToken;
import com.sunshireshuttle.driver.model.LocationRecordBean;
import com.sunshireshuttle.driver.utils.GpsCorrect;
import com.sunshireshuttle.driver.widget.SimpleToast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import net.iaf.framework.exception.IException;
import net.iaf.framework.http.HttpResult;
import net.iaf.framework.util.Loger;

/**
 * Created by caroline on 2016/9/27.
 */

public class TripRecordService extends Service {
    boolean running = false;
    private LocationListener googleLocListener = new DirverLocationListenerVGoogle();
    private BDLocationListener myListener = new DirverLocationListener();
    private static final int GoogleLocMode = 0;
    private static final int BaiduLocMode = 1;
    private int LocMode = GoogleLocMode;
    private LocationClient mLocationClient = null;
    private GoogleApiClient mGoogleApiClient;
    public static boolean onlocationPage = false;
    public static boolean onlocationPerperd = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        switch (LocMode) {
            case BaiduLocMode:
                initBaiduLocation();
                break;
            case GoogleLocMode:
                initGoogleLocation();
                break;
            default:
                initBaiduLocation();
                break;
        }
        Thread locationCheckThread = new Thread(new Runnable() {
            private long locationDuration = 1000;// 每隔一秒检验

            @Override
            public void run() {
                while (running) {
                    switch (LocMode) {
                        case BaiduLocMode:
                            doBaiduLocation();
                            break;
                        case GoogleLocMode:
                            doGoogleLocation();
                            break;
                        default:
                            doBaiduLocation();
                            break;
                    }
                    try {
                        Thread.sleep(locationDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        locationCheckThread.start();
        Thread locationupdateThread = new Thread(new Runnable() {
            private long checkDu = 5000;// 每隔一秒检验
            // updateLocation to server

            @SuppressLint("SimpleDateFormat")
            private String configTripId() {
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    Date date = new Date();
                    return dateFormat2.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";

            }

            @SuppressLint("DefaultLocale")
            private String toDecimal(double f) {
                return String.format("%.6f", f);
            }

            private BaseResponseBean updateLocation() throws IException {
                String action = "3";
                HashMap<String, String> hmParams = new HashMap<String, String>();
                DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                hmParams.put("action", action);
                if (dirverToken.location != null) {
                    hmParams.put("longitude", toDecimal(dirverToken.location.getLongitude()));
                    hmParams.put("latitude", toDecimal(dirverToken.location.getLatitude()));
                }
                if (action.equals("1")) {
                    dirverToken.setTripId(configTripId() + dirverToken.getId());
                }
                hmParams.put("trip_id", dirverToken.getTripId());
                Loger.d("action" + action);
                if (action.equals("3") || action.equals("1") || action.equals("9")) {
                    long recordTime = new Date().getTime();
                    LocationRecordBean bean = new LocationRecordBean(dirverToken.location, dirverToken.getTripId(), recordTime, action);
                    LocationRecordDao dao = new LocationRecordDao();
                    LocationRecordBean beanlast = dao.getLastReocrdByTripId(bean.getTripId());
                    float[] result = new float[0];
                    double startLa = Double.valueOf(bean.getLatitude());
                    double startLon = Double.valueOf(bean.getLongitude());
                    double endLa = Double.valueOf(bean.getLatitude());
                    double endLon = Double.valueOf(bean.getLongitude());
                    Location.distanceBetween(startLa, startLon, endLa, endLon, result);
                    dao.insert(bean);
                }
                if (action.equals("9")) {
                    dirverToken.setTripId("");
                }

                hmParams.put("driver_code", dirverToken.getId());
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(UrlConst.UpdateLocationURL, hmParams);
                    String response = result.getResponse();
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    Loger.d("response-" + response.toString());
                    if (!resp.isResponseOK()) {
                        throw new IException(resp.getMessage());
                    }
                    JSONObject object = new JSONObject(response);
                    String resultF = object.optString("result");
                    BaseResponseBean orderResponse = new BaseResponseBean();
                    orderResponse.setMessage(resp.getMessage());
                    orderResponse.setStatus(resp.getStatus());
                    return orderResponse;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }

            }

            @Override
            public void run() {
                while (true) {
                    if (SunDriverApplication.getInstance().dirverToken != null
                            && SunDriverApplication.getInstance().dirverToken.getState() == 1
                            && onlocationPerperd) {
                        Intent intent = new Intent();
                        intent.setAction("LocationState");
                        intent.setFlags(1);
                        intent.putExtra(Location.class.getName(), SunDriverApplication.getInstance().dirverToken.location);
                        sendBroadcast(intent);
                        try {
                            BaseResponseBean rsp = updateLocation();
                            Loger.d("rsp" + rsp.toString());
                        } catch (IException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(checkDu);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        locationupdateThread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        mLocationClient.unRegisterLocationListener(myListener);
    }

    private void doGoogleLocation() {
        if (!mGoogleApiClient.isConnected()) {
            Loger.e("not connect");
            return;
        }
        handler4GoogleLocation.sendEmptyMessage(1);
    }

    private void doBaiduLocation() {
        Loger.d("doBaiduLocation");
        if (!mLocationClient.isStarted()) {
            if (onlocationPage || (SunDriverApplication.getInstance().dirverToken != null && SunDriverApplication.getInstance().dirverToken.getState() == 1)) {
                mLocationClient.start();
            }
        } else if (!onlocationPage && !(SunDriverApplication.getInstance().dirverToken != null && SunDriverApplication.getInstance().dirverToken.getState() == 1)) {
            mLocationClient.stop();
            onlocationPerperd = false;
        }
    }

    //    private void initGoogleLocation() {
//        lmgr = (LocationManager) getSystemService(LOCATION_SERVICE);
//    }
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    Handler handler4GoogleLocation = new Handler() {
//      private boolean setRequest = false;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Loger.d("onlocationPage:" + onlocationPage);
                    if (onlocationPage || (SunDriverApplication.getInstance().dirverToken != null && SunDriverApplication.getInstance().dirverToken.getState() == 1)) {
//                      if (setRequest) {
//                          Loger.d("already requestLocationUpdates");
//                          return;
//                      }
                        try {
                            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                SimpleToast.ToastMessage("Application needs permission for location");
                                return;
                            }
                            FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createLocationRequest(), googleLocListener);
                            Loger.e("start requestLocationUpdates");
//                          setRequest = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (!onlocationPage && !(SunDriverApplication.getInstance().dirverToken != null && SunDriverApplication.getInstance().dirverToken.getState() == 1)) {
                        Loger.e("remove requestLocationUpdates");
                        FusedLocationApi.removeLocationUpdates(mGoogleApiClient, googleLocListener);
                        onlocationPerperd = false;
//                      setRequest = false;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * Creating location request object
     */
    private LocationRequest createLocationRequest() {
        Loger.d("createLocationRequest");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        return mLocationRequest;
    }

    protected synchronized void buildGoogleApiClient() {


        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            SimpleToast.ToastMessage("Application needs permission for location");
                            Loger.e("requestLocationUpdates the missing permissions");
                            return;
                        }
                        Location location = FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (location != null) {
                            Loger.d("last requestLocationUpdates is " + location.toString());
                        }
                        onlocationPerperd = true;
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        onlocationPerperd = false;
                    }

                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        if (connectionResult.hasResolution()) {
//                            try {
//                                // Start an Activity that tries to resolve the error
//                                connectionResult.startResolutionForResult(MainSearchActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//                            }catch( IntentSender.SendIntentException e ){
//                                e.printStackTrace();
//                            }
                        } else {
//                            Utils.logger("Location services connection failed with code " + connectionResult.getErrorCode(), Utils.LOG_DEBUG );
                        }
                    }
                })
                .addApi(API)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                SimpleToast.ToastMessage("GooglePlaySerivice ConnectionResult " + resultCode);

//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                SimpleToast.ToastMessage("This device is not supported GooglePlaySerivice");
//                Toast.makeText(getApplicationContext(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//                finish();
            }
            return false;
        }
        return true;
    }

    private class DirverLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) { // Receive Location
            Loger.d("onReceiveLocation" + location.getLatitude());
            if (location.getLatitude() == location.getLongitude() && location.getLatitude() == 0) {
                // 非洲咱不去
                return;
            }
            onlocationPerperd = true;
            if (SunDriverApplication.getInstance().dirverToken != null) {
                Location lc = new Location("BDLocation");
                lc.setLatitude(location.getLatitude());
                lc.setLongitude(location.getLongitude());
//                Loger.d("location.getTime()"+location.getTime());2016-09-17 23:01:27
                lc.setTime(new Date().getTime());
                lc.setSpeed(location.getSpeed());
                SunDriverApplication.getInstance().dirverToken.location = lc;
            }
        }

    }

    private class DirverLocationListenerVGoogle implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            { // Receive Location
                Loger.e("onReceiveLocation" + location.getLatitude());
                if (location.getLatitude() == location.getLongitude() && location.getLatitude() == 0) {
                    // 非洲咱不去
                    return;
                }
                location = GpsCorrect.transfrom(location);
                onlocationPerperd = true;
                if (SunDriverApplication.getInstance().dirverToken != null) {
                    SunDriverApplication.getInstance().dirverToken.location = location;
                }
            }
        }
    }


    private void initBaiduLocation() {
        mLocationClient = new LocationClient(getBaseContext()); // 声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(5 * LocationClientOption.MIN_SCAN_SPAN);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
    }

    private void initGoogleLocation() {
        if (!checkPlayServices()) {
            Loger.d("checkPlayServices unable");
            LocMode = BaiduLocMode;
            initBaiduLocation();
        } else {
            Loger.d("checkPlayServices success");
            buildGoogleApiClient();
        }

    }

}
