/**
 * @Description: TODO
 * @author: Zhou Haitao(zhouhaitao@yazuo.com)
 * @create: 2012-7-9
 */

package net.iaf.framework.exception;

/**
 * 没有网络异常
 *
 * @author Bob
 */
public class NoNetworkException extends NetworkException {
    private static final long serialVersionUID = 1L;

    /**
     * 默认构造
     */
    public NoNetworkException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param detailMessage
     */
    public NoNetworkException(String detailMessage) {
        super(detailMessage);
    }
}
