/**
 *
 */
package com.sunshireshuttle.driver.dao;

import android.text.TextUtils;

import net.iaf.framework.Config;
import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.NoNetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.Loger;
import net.iaf.framework.util.PhoneStateUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

/**
 * @param <T>
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: DataAccessRemote
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016年8月15日 下午11:31:33
 */

public class DataAccessRemote<T extends BaseJsonEntity<T>> extends DataAccess<T> {

    private int timeout;

    /**
     * Constructor for DataAccessRemote
     *
     * @param cacheType
     */
    public DataAccessRemote(BaseJsonEntity<T> jsonEntity) {
        super(jsonEntity);
        this.timeout = Config.TIMEOUT;
    }

    public DataAccessRemote(BaseJsonEntity<T> jsonEntity, int timeout) {
        super(jsonEntity);
        this.timeout = timeout;
    }

    @Override
    public T getData(HashMap<String, String> paramsMap, boolean cacheInvalidate)
            throws TimeoutException, NetworkException, DBException {
        buildUrlWhole(paramsMap);
        String jsonStr = getJsonStrRequest(this.urlWhole, null, "POST");
        T data = this.jsonEntity.parseJson2Entity(jsonStr);
        return data;
    }

    protected void buildUrlWhole(final HashMap<String, String> paramsMap) {
        this.urlWithParam = this.urlWithoutParam + paramMap2paramStr(paramsMap);
        this.urlWhole = this.urlWithParam + getDefaultParams(paramsMap);
    }

    protected String getJsonStrRequest(String strURL, final Map<String, String> headers, String requestMethod)
            throws TimeoutException, NetworkException {
        Loger.d("strURL:" + strURL);

        if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
            throw new NoNetworkException();
        }

        String urlstr = strURL.substring(0, strURL.indexOf("?"));
        String param = strURL.substring(strURL.indexOf("?") + 1);
        StringBuilder json = new StringBuilder();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlstr);
            BufferedReader br = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(this.timeout);// TODO 需要修改为生产环境的值
            conn.setReadTimeout(this.timeout);// TODO
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            //add header
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> header : headers.entrySet()) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            conn.connect();
            OutputStream outs = conn.getOutputStream();
            outs.write(param.getBytes());

            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            boolean isGzip = false;

            // 判别gzip方法一
            // isGzip = "gzip".equals(conn.getContentEncoding());

            // 判别gzip方法二
            bis.mark(2);// 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            bis.reset();// reset输入流到开始位置
            // isGzip = (result!=-1 && getShort(header)==0x8b1f);
            isGzip = (result != -1 && getShort(header) == 0x1f8b);

            // 如果是gzip的压缩流 进行解压缩处理
            if (isGzip) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                json = json.append(temp);
            }
            br.close();
            is.close();
            outs.close();
        } catch (SocketTimeoutException e) {
            throw new TimeoutException();
        } catch (IOException e) {
            throw new NetworkException();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        String jsonStr = json.toString().replace("\\u000d", "");

        Loger.d("jsonStr:" + jsonStr);

        return jsonStr;
    }

    /**
     * 上传图片
     *
     * @param paramsMap 参数(不包括图片)
     * @param imgKey    图片key
     * @param imgPath   图片路径
     * @return
     * @throws NetworkException
     * @throws TimeoutException
     */
    public T uploadPic(HashMap<String, String> paramsMap, String imgKey, String imgPath)
            throws NetworkException, TimeoutException {
        String jsonStr = getJsonStrUploadImg(
                this.urlWithoutParam, paramsMap, imgKey, imgPath);

        T data = this.jsonEntity.parseJson2Entity(jsonStr);

        return data;
    }

    private String getJsonStrUploadImg(String actionUrl,
                                       HashMap<String, String> params, String imgKey, String imgPath)
            throws NetworkException, TimeoutException {

        Loger.d("actionUrl:" + actionUrl);

        if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
            throw new NoNetworkException();
        }

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        StringBuilder sb2 = new StringBuilder();

        HttpURLConnection conn = null;

        try {
            URL uri = new URL(actionUrl);
            conn = (HttpURLConnection) uri.openConnection();
            // 缓存的最长时间
            conn.setReadTimeout(60 * 1000);
            // 允许输入
            conn.setDoInput(true);
            // 允许输出
            conn.setDoOutput(true);
            // 不允许使用缓存
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {

                Loger.d("key:" + entry.getKey());
                Loger.d("value:" + entry.getValue());

                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET
                        + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }

            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            // Log.d("aa", sb.toString());
            outStream.write(sb.toString().getBytes());
            // 发送文件数据
            if (!TextUtils.isEmpty(imgPath)) {

                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"" + imgKey + "\"; filename=\""
                        + "temp.jpg" + "\"" + LINEND);
                // sb1.append("Content-Type: application/octet-stream; charset="+CHARSET+LINEND);
                sb1.append("Content-Type: image/png; charset=" + CHARSET
                        + LINEND);
                sb1.append("Content-Transfer-Encoding: binary" + LINEND);
                sb1.append(LINEND);

                // Log.d("aa", "sb1-->" + sb1.toString());
                outStream.write(sb1.toString().getBytes());

                try {
                    InputStream is = new FileInputStream(new File(imgPath));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        // Log.d("aa", "sb1-->" + len);
                    }

                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outStream.write(LINEND.getBytes());
            }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();

            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            String msg = conn.getResponseMessage();
            Loger.i("res:" + res);
            Loger.i("msg:" + msg);
            // System.out.println("getResponseCode-->" + res);
            // StringBuilder sb2 = new StringBuilder();
            if (res == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                String tmp = "";
                while ((tmp = br.readLine()) != null) {
                    sb2.append(tmp);
                }
            }
            outStream.close();
        } catch (SocketTimeoutException e) {
            throw new TimeoutException();
        } catch (IOException e) {
            throw new NetworkException();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        String jsonStr = sb2.toString();
        Loger.d("jsonStr:" + jsonStr);

        return jsonStr;
    }

    private int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }
}
