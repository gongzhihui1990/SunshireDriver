/**
 * @Description: TODO
 * @author: Zhou Haitao(zhouhaitao@yazuo.com)
 * @create: 2012-9-12
 */

package net.iaf.framework.http;

import net.iaf.framework.Config;
import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.NoNetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.Loger;
import net.iaf.framework.util.PhoneStateUtil;

import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 网络连接工具方法
 *
 * @author Bob
 */
public class URLConnectionHelper {
    private static final String ENCODING = Config.ENCODING;
    private static final int CONNECTION_TIMEOUT = Config.TIMEOUT;

    /**
     * 拼接请求参数字符串的方法
     *
     * @param hmParams
     * @return
     * @throws NetworkException
     */
    private static String buildHmParams(HashMap<String, String> hmParams)
            throws NetworkException {
        StringBuffer sb = new StringBuffer();
        Iterator<Entry<String, String>> iter = hmParams.entrySet().iterator();
        boolean isFirstParam = true;
        while (iter.hasNext()) {
            if (!isFirstParam) {
                sb.append("&");
            } else {
                isFirstParam = false;
            }
            Entry<String, String> entry = iter.next();
            try {
                sb.append(URLEncoder.encode(entry.getKey(), ENCODING));
                sb.append("=");
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                    Loger.e("Key - value is null ------" + entry.getKey());
                }
                sb.append(URLEncoder.encode(value, ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new NetworkException(e);
            }
        }
        return sb.toString();
    }

    /**
     * get方式请求，返回HttpResult
     *
     * @param httpUrl
     * @param hmParams
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public static HttpResult executeGetRequest(String httpUrl,
                                               HashMap<String, String> hmParams) throws TimeoutException,
            NetworkException {
        if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
            throw new NoNetworkException();
        }
        if (hmParams != null && hmParams.size() != 0) {
            httpUrl = httpUrl + "?" + buildHmParams(hmParams);
        }
        StringBuffer rsltSb = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection urlConn;
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection urlsConn = (HttpsURLConnection) url
                        .openConnection();
                urlConn = urlsConn;
                urlsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
            } else {
                urlConn = (HttpURLConnection) url.openConnection();
            }
            urlConn.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(false);
            urlConn.setReadTimeout(CONNECTION_TIMEOUT);
            int statusCode = urlConn.getResponseCode();
            // 获取数据
            BufferedInputStream bis;
            if (statusCode == HttpStatus.SC_OK) {
                // 获取数据
                bis = new BufferedInputStream(urlConn.getInputStream());
            } else {
                // 获取失败时的数据
                bis = new BufferedInputStream(urlConn.getErrorStream());
            }
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                rsltSb.append(new String(buffer, 0, length));
            }
            bis.close();
            urlConn.disconnect();
            return new HttpResult(String.valueOf(statusCode), rsltSb.toString());
        } catch (MalformedURLException e) {
            throw new NetworkException(e);
        } catch (ConnectTimeoutException e) {
            throw new TimeoutException(e.getMessage(), e);
        } catch (SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage());
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * post方式请求，返回HttpResult
     *
     * @param httpUrl
     * @param hmParams
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public static HttpResult executePostRequest(String httpUrl,
                                                HashMap<String, String> hmParams) throws TimeoutException,
            NetworkException {
        //TODO check hmParams is not null
        if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
            throw new NoNetworkException();
        }
        StringBuffer rsltSb = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection urlConn;
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection urlsConn = (HttpsURLConnection) url
                        .openConnection();
                urlConn = urlsConn;
                urlsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
            } else {
                urlConn = (HttpURLConnection) url.openConnection();
            }
            urlConn.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConn.setReadTimeout(CONNECTION_TIMEOUT);
            // 因为这个是post请求,设立需要设置为true
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            // 设置以POST方式
            urlConn.setRequestMethod("POST");
            // Post 请求不能使用缓存
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            urlConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            urlConn.connect();
            // DataOutputStream流
            DataOutputStream out = new DataOutputStream(
                    urlConn.getOutputStream());
            // 要上传的参数
            String content = buildHmParams(hmParams);
            // 将要上传的内容写入流中
            out.writeBytes(content);
            // 刷新、关闭
            out.flush();
            out.close();
            int statusCode = urlConn.getResponseCode();
            // 获取数据
            BufferedInputStream bis;
            if (statusCode == HttpStatus.SC_OK) {
                // 获取数据
                bis = new BufferedInputStream(urlConn.getInputStream());
            } else {
                // 获取失败时的数据
                bis = new BufferedInputStream(urlConn.getErrorStream());
            }
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                rsltSb.append(new String(buffer, 0, length));
            }
            bis.close();
            urlConn.disconnect();
            return new HttpResult(String.valueOf(statusCode), rsltSb.toString());
        } catch (MalformedURLException e) {
            throw new NetworkException(e);
        } catch (ConnectTimeoutException e) {
            throw new TimeoutException(e.getMessage(), e);
        } catch (SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage());
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * SSL处理相关方法
     */
    private static void trustAllHosts() {

        X509TrustManager easyTrustManager = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {

            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
