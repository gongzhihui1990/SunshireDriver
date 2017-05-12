/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see
 */

package net.iaf.framework.http;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * HttpsGet工具方法
 *
 * @author Bob
 */
public class HttpsGetUtil extends HttpGetUtil {

    /**
     * 构造DefaultHttpClient的方法
     */
    @Override
    protected DefaultHttpClient buildClient(int connectionPoolTimeout,
                                            int connectionTimeout, int socketTimeout) {
        return HttpClientHelper.buildHttpsClient(connectionPoolTimeout,
                connectionTimeout, socketTimeout);
    }

}
