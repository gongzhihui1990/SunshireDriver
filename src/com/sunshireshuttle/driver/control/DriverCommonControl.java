package com.sunshireshuttle.driver.control;

import android.annotation.SuppressLint;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.dao.DriverRemoteDao;
import com.sunshireshuttle.driver.dao.local.LocationRecordDao;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.model.CurrentOrderResponse;
import com.sunshireshuttle.driver.model.DirverToken;
import com.sunshireshuttle.driver.model.FlightInfoResponse;
import com.sunshireshuttle.driver.model.GetCustomerLocationResponse;
import com.sunshireshuttle.driver.model.IDQueryResponse;
import com.sunshireshuttle.driver.model.LocationRecordBean;
import com.sunshireshuttle.driver.model.LoginResponse;
import com.sunshireshuttle.driver.model.MessageListResponse;
import com.sunshireshuttle.driver.model.RecentOrderResponse;
import com.sunshireshuttle.driver.model.SunshineTripResponse;
import com.sunshireshuttle.driver.model.TripLogsQueryResponse;
import com.sunshireshuttle.driver.model.TripPrivateQueryResponse;
import com.sunshireshuttle.driver.model.TripRecordListResponseBean;

import net.iaf.framework.exception.IException;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.ServerException;
import net.iaf.framework.http.HttpResult;
import net.iaf.framework.util.Loger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class DriverCommonControl extends DriverBaseControl {

    private int KeyLogin = 0;
    private int KeyGetDirverInfo = 1;
    private int KeyGetHistoryOrder = 2;
    private int KeyGetCurrentOrder = 3;
    private int KeyUpdateLocationAction = 4;
    private int KeySendETA = 5;
    private int KeySendCOB = 6;
    private int KeySendCAD = 7;
    private int KeySendArrail = 8;
    private int KeySendCADWarn = 9;
    private int KeySendSearchRequestWithFlight = 10;
    private int KeyRequestCustomerLocationWithOrderID = 11;
    private int KeySendLink = 12;
    private int KeyRequestForSearchTripHistoryWithConditions = 13;
    private int KeyRequestForSearchTripHistoryLogWithTripID = 14;
    private int KeyUpdateGasPoint = 15;
    private int KeyQueryPrivate = 16;
    private int KeyUpdateSpecificLocationWithAction = 17;

    public void login(BaseViewCallback<LoginResponse> updateViewAsyncCallback, final String username,
                      final String driver_code) {
        DoAsyncTaskCallback<String, LoginResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, LoginResponse>() {

            @Override
            public LoginResponse doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                // ?username=%@&driver_code=%@&type=%@&apikey=%@"
                hmParams.put("username", username);
                hmParams.put("driver_code", driver_code);
                hmParams.put("type", "logon");
                hmParams.put("apikey", "sunshireshuttle");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(ApiURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    LoginResponse resp = new Gson().fromJson(response, new TypeToken<LoginResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyLogin, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void getDirverInfo(BaseViewCallback<IDQueryResponse> updateViewAsyncCallback, final String id) {
        DoAsyncTaskCallback<String, IDQueryResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, IDQueryResponse>() {

            @Override
            public IDQueryResponse doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("id", id);
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(DriverInfoURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    IDQueryResponse resp = new Gson().fromJson(response, new TypeToken<IDQueryResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyGetDirverInfo, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void getHistoryOrder(BaseViewCallback<RecentOrderResponse> updateViewAsyncCallback, final String id) {
        DoAsyncTaskCallback<String, RecentOrderResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, RecentOrderResponse>() {

            @Override
            public RecentOrderResponse doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("id", id);
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(HistoryOrderURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    RecentOrderResponse resp = new Gson().fromJson(response, new TypeToken<RecentOrderResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyGetHistoryOrder, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void getCurrentOrderList(BaseViewCallback<CurrentOrderResponse> updateViewAsyncCallback, final String id) {
        DoAsyncTaskCallback<String, CurrentOrderResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, CurrentOrderResponse>() {

            @Override
            public CurrentOrderResponse doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("id", id);
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(CurrentOrderURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new IException(resp.getMessage());
                    }
                    ArrayList<CurrentOrderItem> resultList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String resultF = object.optString("result");
                    CurrentOrderResponse orderResponse = new CurrentOrderResponse();
                    orderResponse.setMessage(resp.getMessage());
                    orderResponse.setStatus(resp.getStatus());
                    switch (resp.getMessage()) {
                        case "solo":
                            CurrentOrderItem resultListItem = new Gson().fromJson(resultF,
                                    new TypeToken<CurrentOrderItem>() {
                                    }.getType());
                            resultList.add(resultListItem);
                            break;
                        case "array":
                            resultList = new Gson().fromJson(resultF, new TypeToken<ArrayList<CurrentOrderItem>>() {
                            }.getType());
                            break;
                    }
                    orderResponse.setResult(resultList);
                    return orderResponse;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyGetCurrentOrder, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    @SuppressLint("DefaultLocale")
    private String toDecimal(double f) {
        return String.format("%.6f", f);
    }

    @SuppressLint("SimpleDateFormat")
    public void updateLocationAction(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String action) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

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


            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                hmParams.put("action", action);
                if (dirverToken.location != null) {
                    hmParams.put("longitude", toDecimal(dirverToken.location.getLongitude()));
                    hmParams.put("latitude", toDecimal(dirverToken.location.getLatitude()));
                }
                if (action.equals("1")) {
                    //new trip_id
                    dirverToken.setTripId(configTripId() + dirverToken.getId());
                }
                hmParams.put("trip_id", dirverToken.getTripId());
                Loger.d("action" + action);
                if (action.equals("3") || action.equals("1") || action.equals("9")) {
                    long recordTime = new Date().getTime();
                    LocationRecordBean bean = new LocationRecordBean(dirverToken.location, dirverToken.getTripId(), recordTime, action);
                    LocationRecordDao dao = new LocationRecordDao();
                    LocationRecordBean beanlast = dao.getLastReocrdByTripId(bean.getTripId());
                    dao.insert(bean);
                }
                if (action.equals("9")) {
                    //clear trip_id
                    dirverToken.setTripId("");
                }

                hmParams.put("driver_code", dirverToken.getId());
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(UpdateLocationURL, hmParams);
                    String response = result.getResponse();
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    Loger.d("response-" + response.toString());
                    if (!resp.isResponseOK()) {
                        throw new IException(resp.getMessage());
                    }
                    ArrayList<CurrentOrderItem> resultList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String resultF = object.optString("result");
                    CurrentOrderResponse orderResponse = new CurrentOrderResponse();
                    orderResponse.setMessage(resp.getMessage());
                    orderResponse.setStatus(resp.getStatus());
                    switch (resp.getMessage()) {
                        case "solo":
                            CurrentOrderItem resultListItem = new Gson().fromJson(resultF,
                                    new TypeToken<CurrentOrderItem>() {
                                    }.getType());
                            resultList.add(resultListItem);
                            break;
                        case "array":
                            resultList = new Gson().fromJson(resultF, new TypeToken<ArrayList<CurrentOrderItem>>() {
                            }.getType());
                            break;
                        default:
                            break;
                    }
                    orderResponse.setResult(resultList);
                    return orderResponse;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyUpdateLocationAction, updateViewAsyncCallback, doAsyncTaskCallback);
    }

//    NSDictionary * updatePack = @{
//        @"receive" : @"synchro_trip_history",
//        @"type" : @"update_specific_location",
//        @"driver_id" : driverToken.driver_id,
//        @"trip_id" : driverToken.trip_id,
//        @"latitude" :[NSString stringWithFormat:@"%f", location.latitude.doubleValue],
//        @"longitude" :[NSString stringWithFormat:@"%f", location.longitude.doubleValue],
//        @"action" : @"5"
//        };
//
//NSError *jsonError;
//NSString * logonURLStr =@"https://sunshireshuttle.com/chrisyao4700/synchro_trip_history.php";

    private BaseResponseBean updateGasPrice(String price, JSONObject hmParams) throws JSONException, NetworkException, TimeoutException, UnsupportedEncodingException {
        Loger.d("---1" + price);
        hmParams.put("type", "add_trip_log");
        JSONObject log_package = new JSONObject();
        DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
        log_package.put("employee_id", dirverToken.getId());
        log_package.put("employee_type", "DRIVER");
        log_package.put("message_type", "GAS_POINT");
        log_package.put("message_text", String.format("%s added $ %s gas", dirverToken.getName(), price));
        hmParams.put("log_package", log_package);
        HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, hmParams.toString());
        Loger.d("---1" + hmParams.toString());
        String response = result.getResponse();
        Loger.d("---1esponse" + response);
        response = URLDecoder.decode(response, "UTF-8");
        BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
        }.getType());
        return resp;
//        N-(void) updateGasLogPrice:(NSString *) price{
//        NSDictionary * log_pack = @{
//            @"employee_id": driverToken.driver_id,
//            @"employee_type": @"DRIVER",
//            @"message_type": @"GAS_POINT",
//            @"message_text": [NSString stringWithFormat:@"[%@] added $ %@ gas", driverToken.driver_name, price]
//            };
//NSDictionary * updatePack = @{
//              @"receive" : @"synchro_trip_history",
//              @"type" : @"add_trip_log",
//              @"trip_id": driverToken.trip_id,
//              @"log_package": log_pack
//              };
//
//NSError *jsonError;
//NSString * logonURLStr =@"https://sunshireshuttle.com/chrisyao4700/synchro_trip_history.php";
//NSURL *postURL = [NSURL URLWithString:logonURLStr];

    }

    public void updateGasPoint(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String price) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {


            @SuppressLint("DefaultLocale")
            private String toDecimal(double f) {
                return String.format("%.6f", f);
            }

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                JSONObject hmParams = new JSONObject();
                DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                try {
                    hmParams.put("action", "5");

                    hmParams.put("type", "update_specific_location");
                    hmParams.put("receive", "synchro_trip_history");
                    if (dirverToken.location != null) {
                        hmParams.put("longitude", toDecimal(dirverToken.location.getLongitude()));
                        hmParams.put("latitude", toDecimal(dirverToken.location.getLatitude()));
                    }
                    hmParams.put("trip_id", dirverToken.getTripId());
                    hmParams.put("driver_id", dirverToken.getId());
                    Loger.e(hmParams.toString());
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, hmParams.toString());
                    String response = result.getResponse();
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    Loger.d("response-" + response.toString());
                    if (!resp.isResponseOK()) {
                        throw new IException(resp.getMessage());
                    }
                    return updateGasPrice(price, hmParams);
                } catch (TimeoutException | JSONException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyUpdateGasPoint, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sentETA(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId, final String time) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("mins", time);
                hmParams.put("type", "ETA");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendETA, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sentCOB(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("type", "COB");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendCOB, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sentCOBExtension(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("type", "COBEXT");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendCOB, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sendArrival(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("type", "ARRIVAL");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendArrail, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sendLink(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("type", "customer_trip_link");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendLink, updateViewAsyncCallback, doAsyncTaskCallback);
    }


    public void sendCAD(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("type", "CAD");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendCAD, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sentCOBWARN(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("id", orderId);
                hmParams.put("type", "COBWARN");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(OrderDetailURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendCADWarn, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sendSearchRequestWithFlight(UpdateViewAsyncCallback<FlightInfoResponse> updateViewAsyncCallback, final String flightInfoId) {
        DoAsyncTaskCallback<String, FlightInfoResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, FlightInfoResponse>() {

            @Override
            public FlightInfoResponse doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
//				hmParams.put("apikey", API_ConnectionKey);
                hmParams.put("flight", flightInfoId);
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(SearchFlightURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    FlightInfoResponse resp = new Gson().fromJson(response, new TypeToken<FlightInfoResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeySendSearchRequestWithFlight, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void requestCustomerLocationWithOrderID(UpdateViewAsyncCallback<GetCustomerLocationResponse> updateViewAsyncCallback, final String orderId) {
        DoAsyncTaskCallback<String, GetCustomerLocationResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, GetCustomerLocationResponse>() {

            @Override
            public GetCustomerLocationResponse doAsyncTask(String... arg0) throws IException {
                HashMap<String, String> hmParams = new HashMap<String, String>();
                hmParams.put("id", orderId);
                hmParams.put("type", "order");
                hmParams.put("apikey", "sunshireshuttle");
                try {
                    HttpResult result = new DriverRemoteDao().httpGet(SearchCustomerLocationURL, hmParams);
                    String response = result.getResponse();
                    Loger.e("response:" + response);
                    response = URLDecoder.decode(response, "UTF-8");
                    GetCustomerLocationResponse resp = new Gson().fromJson(response, new TypeToken<GetCustomerLocationResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (TimeoutException | UnsupportedEncodingException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyRequestCustomerLocationWithOrderID, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void requestMessageWithTripID(UpdateViewAsyncCallback<MessageListResponse> updateViewAsyncCallback, final String product_id) {

        DoAsyncTaskCallback<String, MessageListResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, MessageListResponse>() {

            @Override
            public MessageListResponse doAsyncTask(String... arg0) throws IException {
//                HashMap<String, String> hmParams = new HashMap<String, String>();
//                hmParams.put("product_id", product_id);
//                hmParams.put("type", "find_trip");
//                hmParams.put("receive", "synchro_contact_history");
//                hmParams.put("apikey", "sunshireshuttle");
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("product_id", product_id);
                    jsonObject.put("type", "find_trip");
                    jsonObject.put("receive", "synchro_contact_history");
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroContactHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    MessageListResponse resp = new Gson().fromJson(response, new TypeToken<MessageListResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyRequestCustomerLocationWithOrderID, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sendMessageWithTripID(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback,
                                      final String product_id, final String sms_message) {

        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
//                @"product_id": trip_id,
//                @"type": @"send",
//                @"receive": @"synchro_contact_history",
//                @"message": sms_message

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", sms_message);
                    jsonObject.put("product_id", product_id);
                    jsonObject.put("type", "send");
                    jsonObject.put("receive", "synchro_contact_history");
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroContactHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyRequestCustomerLocationWithOrderID, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sendRequestForSearchTripHistoryWithConditions(UpdateViewAsyncCallback<TripRecordListResponseBean> updateViewAsyncCallback,
                                                              final String start_date, final String end_date, final String driver_id) {

        DoAsyncTaskCallback<String, TripRecordListResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, TripRecordListResponseBean>() {

            @Override
            public TripRecordListResponseBean doAsyncTask(String... arg0) throws IException {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("start_date", start_date);
                    jsonObject.put("end_date", end_date);
                    jsonObject.put("driver_id", driver_id);
                    jsonObject.put("type", "search_condition");
                    jsonObject.put("receive", "synchro_trip_history");
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    TripRecordListResponseBean resp = new Gson().fromJson(response, new TypeToken<TripRecordListResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyRequestForSearchTripHistoryWithConditions, updateViewAsyncCallback, doAsyncTaskCallback);
    }

//    -(void) sendRequestForSearchCompleteTripWithID:(NSString *) tripID{
//        NSDictionary * updatePack = @{
//            @"trip_id": tripID,
//            @"type": @"search_complete_trip",
//            @"receive": @"synchro_trip_history"
//        };
//        NSLog(@"Dict: %@" ,  updatePack.description);
//        NSError *jsonError;
//        NSString * tripHistoryURLStr =@"https://sunshireshuttle.com/chrisyao4700/synchro_trip_history.php";
//        NSURL *postURL = [NSURL URLWithString:tripHistoryURLStr];
//
//
//    }


    public void sendRequestForSearchCompleteTripWithID(UpdateViewAsyncCallback<SunshineTripResponse> updateViewAsyncCallback, final String tripId) {

        DoAsyncTaskCallback<String, SunshineTripResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, SunshineTripResponse>() {

            @Override
            public SunshineTripResponse doAsyncTask(String... arg0) throws IException {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("trip_id", tripId);
                    jsonObject.put("type", "search_complete_trip");
                    jsonObject.put("receive", "synchro_trip_history");
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    SunshineTripResponse resp = new Gson().fromJson(response, new TypeToken<SunshineTripResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyRequestForSearchTripHistoryWithConditions, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void sendRequestForSearchTripHistoryLogWithTripID(UpdateViewAsyncCallback<TripLogsQueryResponse> updateViewAsyncCallback, final String tripId) {

        DoAsyncTaskCallback<String, TripLogsQueryResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, TripLogsQueryResponse>() {

            @Override
            public TripLogsQueryResponse doAsyncTask(String... arg0) throws IException {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("trip_id", tripId);
                    jsonObject.put("type", "search_trip_history_log");
                    jsonObject.put("receive", "synchro_trip_history");
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    TripLogsQueryResponse resp = new Gson().fromJson(response, new TypeToken<TripLogsQueryResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyRequestForSearchTripHistoryLogWithTripID, updateViewAsyncCallback, doAsyncTaskCallback);
    }


    public void queryPrivate(UpdateViewAsyncCallback<TripPrivateQueryResponse> updateViewAsyncCallback) {

        DoAsyncTaskCallback<String, TripPrivateQueryResponse> doAsyncTaskCallback = new DoAsyncTaskCallback<String, TripPrivateQueryResponse>() {
//            NSDictionary * updatePack = @{
//                @"receive" : @"synchro_trip_history",
//                @"type" : @"check_private_use",
//                @"driver_id" : driverToken.driver_id,
//                @"trip_id" : driverToken.trip_id,
//            };
//
//            NSError *jsonError;
//            NSString * logonURLStr =@"https://sunshireshuttle.com/chrisyao4700/synchro_trip_history.php";
//            NSURL *postURL = [NSURL URLWithString:logonURLStr];


            @Override
            public TripPrivateQueryResponse doAsyncTask(String... arg0) throws IException {
                try {
                    JSONObject jsonObject = new JSONObject();
                    DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                    jsonObject.put("driver_id", dirverToken.getId());
                    jsonObject.put("trip_id", dirverToken.getTripId());
                    jsonObject.put("type", "check_private_use");
                    jsonObject.put("receive", "synchro_trip_history");
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    TripPrivateQueryResponse resp = new Gson().fromJson(response, new TypeToken<TripPrivateQueryResponse>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyQueryPrivate, updateViewAsyncCallback, doAsyncTaskCallback);
    }

    public void updateSpecificLocationWithAction(UpdateViewAsyncCallback<BaseResponseBean> updateViewAsyncCallback, final String actionCode) {

        DoAsyncTaskCallback<String, BaseResponseBean> doAsyncTaskCallback = new DoAsyncTaskCallback<String, BaseResponseBean>() {

//            -(void) updateSpecificLocationWithAction:(NSString *) action_code{
//                NSDictionary * updatePack = @{
//                    @"receive" : @"synchro_trip_history",
//                    @"type" : @"update_specific_location",
//                    @"driver_id" : driverToken.driver_id,
//                    @"trip_id" : driverToken.trip_id,
//                    @"latitude" :[NSString stringWithFormat:@"%f", location.latitude.doubleValue],
//                    @"longitude" :[NSString stringWithFormat:@"%f", location.longitude.doubleValue],
//                    @"action" : action_code
//                };

            @Override
            public BaseResponseBean doAsyncTask(String... arg0) throws IException {
                try {
                    JSONObject jsonObject = new JSONObject();
                    DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                    jsonObject.put("driver_id", dirverToken.getId());
                    jsonObject.put("trip_id", dirverToken.getTripId());
                    jsonObject.put("type", "update_specific_location");
                    jsonObject.put("receive", "synchro_trip_history");
                    jsonObject.put("action", actionCode);
                    if (dirverToken.location != null) {
                        jsonObject.put("longitude", toDecimal(dirverToken.location.getLongitude()));
                        jsonObject.put("latitude", toDecimal(dirverToken.location.getLatitude()));
                    }
                    HttpResult result = new DriverRemoteDao().httpPost(SynchroTripHistoryURL, jsonObject.toString());
                    Loger.e("result StatusCode: " + result.getStatusCode());

                    String response = result.getResponse();
                    Loger.e("response:" + response);
//                    response = URLDecoder.decode(response, "UTF-8");
                    BaseResponseBean resp = new Gson().fromJson(response, new TypeToken<BaseResponseBean>() {
                    }.getType());
                    if (!resp.isResponseOK()) {
                        throw new ServerException("1", resp.getMessage());
                    }
                    return resp;
                } catch (JSONException | TimeoutException | RuntimeException e) {
                    e.printStackTrace();
                    throw new IException(e.getMessage());
                }
            }
        };
        doAsyncTask(KeyUpdateSpecificLocationWithAction, updateViewAsyncCallback, doAsyncTaskCallback);
    }


}
//
//
//
//    -(void) sendRequestForSearchTripHistoryLogWithTripID:(NSString *) tripID{
//        NSDictionary * updatePack = @{
//            @"trip_id": tripID,
//            @"type": @"search_trip_history_log",
//            @"receive": @"synchro_trip_history"
//        };
//        NSLog(@"Dict: %@" ,  updatePack.description);
//        NSError *jsonError;
//        NSString * tripHistoryURLStr =@"https://sunshireshuttle.com/chrisyao4700/synchro_trip_history.php";
//        NSURL *postURL = [NSURL URLWithString:tripHistoryURLStr];
//
//        NSData* jsonData = [NSJSONSerialization dataWithJSONObject:updatePack options:0 error:&jsonError];
//        NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:postURL];
//        [request setValue:@"application/json" forHTTPHeaderField:@"content-type"];
//        [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
//        [request setValue:[NSString stringWithFormat:@"%lu", (unsigned long)[jsonData length]] forHTTPHeaderField:@"Content-Length"];
//        request.HTTPBody = jsonData;
//        request.HTTPMethod = @"POST";
//
//        NSLog(@"%@", request.description);
//
//        if (self.delegate) {
//            [self.delegate sunshireTripHistoryWillStartRequestWithTag:1];
//        }
//        [[[NSURLSession sharedSession] dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
//            // TODO: Handle success and failure
//            if (self.delegate) {
//                [self.delegate sunshireTripHistoryDidGetResponseWithTag:1];
//            }
//            if (data) {
//                //NSString * resultStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
//                //NSLog(@"%@", resultStr);
//                NSError * errorDecode;
//                NSDictionary * resultDict= [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&errorDecode];
//
//                NSNumber* status = (NSNumber *)[resultDict objectForKey:@"status"];
//                NSString * message = [resultDict objectForKey:@"message"];
//
//                if (status.boolValue == YES) {
//                    if (self.delegate) {
//                        [self.delegate sunshireTripHistoryFindTripHistoryLog:[resultDict objectForKey:@"log_list"]];
//                    }
//                }
//                if (status.boolValue == NO) {
//                    if (self.delegate) {
//                        [self.delegate sunshireTripHistoryErrorWithTag:1 forMessage:message];
//                    }
//                }
//
//            }else{
//                if (self.delegate) {
//                    [self.delegate sunshireTripHistoryReceivedEmptyDataWithTag:1];
//                }
//            }
//
//        }] resume];


//    }


//}


//        -(void) requestMessageWithTripID:(NSString *) trip_id{
//            NSDictionary * updatePack = @{
//                @"product_id": trip_id,
//                @"type": @"find_trip",
//                @"receive": @"synchro_contact_history"
//            };
//            NSError *jsonError;
//            NSString * logonURLStr =@"https://sunshireshuttle.com/chrisyao4700/synchro_contact_history.php";
//            NSURL *postURL = [NSURL URLWithString:logonURLStr];
//
//            NSData* jsonData = [NSJSONSerialization dataWithJSONObject:updatePack options:0 error:&jsonError];
//            NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:postURL];
//            [request setValue:@"application/json" forHTTPHeaderField:@"content-type"];
//            [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
//            [request setValue:[NSString stringWithFormat:@"%lu", (unsigned long)[jsonData length]] forHTTPHeaderField:@"Content-Length"];
//            request.HTTPBody = jsonData;
//            request.HTTPMethod = @"POST";
//
//            if (self.delegate) {
//                [self.delegate sunshireMessengerWillStartRequestWithTag:1];
//            }
//            [[[NSURLSession sharedSession] dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
//                if (self.delegate) {
//                    [self.delegate sunshireMessengerDidGetResponseWithTag:1];
//                }
//                if (data) {
//                    NSString * resultStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
//                    NSLog(@"%@", resultStr);
//                    NSError * errorDecode;
//                    NSDictionary * resultDict= [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&errorDecode];
//                    NSLog(@"%@", [resultDict objectForKey:@"query"]);
//
//                    NSNumber* status = (NSNumber *)[resultDict objectForKey:@"status"];
//                    NSString * message = [resultDict objectForKey:@"message"];
//
//                    if (status.boolValue == YES) {
//                        if (self.delegate) {
//                            [self.delegate sunshireMessengerDidFindMessageList:[resultDict objectForKey:@"message_list"]];
//                        }
//                    }
//                    if (status.boolValue == NO) {
//                        if (self.delegate) {
//                            [self.delegate sunshireMessengerErrorWithTag:1 forMessage:message];
//                        }
//                    }
//
//                }else{
//                    if (self.delegate) {
//                        [self.delegate sunshireMessengerReceivedEmptyDataWithTag:1];
//                    }
//                }
//
//            }] resume];


//        }

//    }
//}
