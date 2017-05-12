/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see
 */

package net.iaf.framework.http;

import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.Loger;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HttpPost工具方法
 *
 * @author Bob
 */
public class HttpPostUtil extends BaseHttp {

    /**
     * 传入uri,参数集合，构造HttpUriRequest的方法
     *
     * @param uri
     * @param hmParams
     * @return
     */
    private HttpUriRequest buildHttpUriRequest(String uri,
                                               Map<String, String> hmParams) {
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        Iterator<Entry<String, String>> iter = hmParams.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return post;
    }

    /**
     * url,json构造HttpUriRequest的方法
     *
     * @param url
     * @param json
     * @return
     */
    private HttpUriRequest buildHttpUriRequest(String url, String json) {
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type", "application/json");
//        post.addHeader("Content-Type", "application/json;charset=utf-8");
//        post.addHeader("Content-Length",String.valueOf(json.length()));
        try {
            Loger.e("1111" + json);
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            Loger.e("2222" + stringEntity.toString());
            post.setEntity(stringEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }

        return post;
    }

    /**
     * 执行Request，HttpPost中参数默认编码方式为BaseHttp中的ENCODING
     */
    public HttpResult executeRequest(String url,
                                     Map<String, String> hmParams) throws TimeoutException,
            NetworkException {
        return sendRequest(buildHttpUriRequest(url, hmParams));
    }

    /**
     * 发起请求的方法
     *
     * @param url
     * @param json
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public HttpResult executeRequest(String url, String json)
            throws TimeoutException, NetworkException {
        return sendRequest(buildHttpUriRequest(url, json));
    }

    /**
     * 发起请求的方法
     *
     * @param post
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public HttpResult executeRequest(HttpPost post) throws TimeoutException,
            NetworkException {
        return sendRequest(post);
    }

    /**
     * 构造DefaultHttpClient的方法
     */
    @Override
    protected DefaultHttpClient buildClient(int connectionPoolTimeout,
                                            int connectionTimeout, int socketTimeout) {
        return HttpClientHelper.buildHttpClient(connectionPoolTimeout,
                connectionTimeout, socketTimeout);
    }

}
