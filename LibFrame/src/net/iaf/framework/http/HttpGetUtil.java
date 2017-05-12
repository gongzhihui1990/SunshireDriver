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

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HttpGet工具方法
 *
 * @author Bob
 */
public class HttpGetUtil extends BaseHttp {

    /**
     * 传入get格式url，返回HttpResult的方法
     *
     * @param httpGetUrl
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public HttpResult executeRequest(String httpGetUrl)
            throws TimeoutException, NetworkException {
        Loger.d("HttpGetURL = " + httpGetUrl);
        return sendRequest(new HttpGet(httpGetUrl));
    }

    /**
     * 传入url，参数集合，返回HttpResult的方法
     *
     * @param url
     * @param hmParams
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public HttpResult executeRequest(String url,
                                     Map<String, String> hmParams) throws TimeoutException,
            NetworkException {
        StringBuffer sb = new StringBuffer();
        sb.append(url).append("?");
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
        return executeRequest(sb.toString());
    }

    /**
     * 超时时间构造DefaultHttpClient对象的方法
     */
    @Override
    protected DefaultHttpClient buildClient(int connectionPoolTimeout,
                                            int connectionTimeout, int socketTimeout) {
        return HttpClientHelper.buildHttpClient(connectionPoolTimeout,
                connectionTimeout, socketTimeout);
    }

}
