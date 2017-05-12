package net.iaf.framework.exception;

/**
 * 网络异常
 *
 * @author Bob
 */
public class NetworkException extends IException {

    private static final long serialVersionUID = 1L;

    protected String errCode = null;

    /**
     * 默认构造方法
     */
    public NetworkException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param exception
     */
    public NetworkException(Exception exception) {
        super(exception);
    }

    /**
     * 构造方法
     *
     * @param errorCode
     */
    public NetworkException(String errMsg) {
        super(errMsg);
    }

    /**
     * 构造方法
     *
     * @param errorCode
     * @param errMsg
     */
    public NetworkException(String errorCode, String errMsg) {
        super(errMsg);
        this.errCode = errorCode;
    }

    /**
     * 构造方法
     *
     * @param errorCode
     * @param exception
     */
    public NetworkException(String errorCode, Exception exception) {
        super(errorCode, exception);
    }

    /**
     * 发生网络异常时返回errorCode，如果异常不带code值，返回-1
     *
     * @return error code值
     * @Title: getErrorCode
     */
    public String getErrorCode() {
        return errCode;
    }
}
