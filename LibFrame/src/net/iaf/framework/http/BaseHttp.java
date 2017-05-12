/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see
 */

package net.iaf.framework.http;

import net.iaf.framework.Config;
import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.NoNetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.Loger;
import net.iaf.framework.util.PhoneStateUtil;

import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Http（Https）请求基类
 *
 * @author Bob
 */
abstract class BaseHttp {

    protected static final String ENCODING = Config.ENCODING;

    /**
     * 从ConnectionManager管理的连接池中取出连接的超时时间，此处设为8秒，
     * 超时时抛ConnectionPoolTimeoutException(是ConnectTimeoutException的子类)
     */
    private static final int CONNECTION_POOL_TIMEOUT = Config.TIMEOUT;

    /**
     * 定义了与服务器建立连接的超时时间，此处设为8秒，超时时抛ConnectTimeoutException
     */
    private static final int CONNECTION_TIMEOUT = Config.TIMEOUT;

    /**
     * 定义了从网络读取数据的超时时间，此处设为8秒，超时时抛SocketTimeoutException
     */
    private static final int SOCKET_TIMEOUT = Config.TIMEOUT;

    /**
     * 共用基础方法：传入HttpUriRequest，发送请求返回HttpResult
     *
     * @param request
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     * @throws NoNetworkException
     */
    protected HttpResult sendRequest(HttpUriRequest request)
            throws TimeoutException, NetworkException, NoNetworkException {
        if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
            throw new NoNetworkException("No Network");
        }
        DefaultHttpClient client = null;
        try {
            if (client == null) {
                client = buildClient(CONNECTION_POOL_TIMEOUT,
                        CONNECTION_TIMEOUT, SOCKET_TIMEOUT);
            }
            HttpResponse response = client.execute(request);
            Loger.d("result-len " + response.getEntity().getContentLength());
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            Loger.d("result-str " + result);
            final int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResult(String.valueOf(statusCode), result);
        } catch (ConnectTimeoutException e) {
            throw new TimeoutException(e.getMessage(), e);
        } catch (SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage());
        } catch (IOException e) {
            throw new NetworkException(e);
        } catch (ParseException e) {
            throw new NetworkException(e);
        } finally {
            // 退出应用时再关闭client
            if (client != null) {
                HttpClientHelper.closeClient(client);
            }
        }
    }

    /**
     * 构建DefaultHttpClient的方法，子类各自实现
     *
     * @param connectionPoolTimeout
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     */
    protected abstract DefaultHttpClient buildClient(int connectionPoolTimeout,
                                                     int connectionTimeout, int socketTimeout);

}
