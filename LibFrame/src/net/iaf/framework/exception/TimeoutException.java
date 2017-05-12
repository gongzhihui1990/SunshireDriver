package net.iaf.framework.exception;

/**
 * 超时异常
 *
 * @author Bob
 */
public class TimeoutException extends NetworkException {
    private static final long serialVersionUID = 1L;

    /**
     * 默认构造
     */
    public TimeoutException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param detailMessage
     */
    public TimeoutException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 构造方法
     *
     * @param detailMessage
     * @param exception
     */
    public TimeoutException(String detailMessage, Exception exception) {
        super(detailMessage, exception);
    }
}
