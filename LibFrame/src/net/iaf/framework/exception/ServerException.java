package net.iaf.framework.exception;

/**
 * 服务端异常
 *
 * @author Bob
 */
public class ServerException extends NetworkException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造
     */
    public ServerException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param exception
     */
    public ServerException(Exception exception) {
        super(exception);
    }

    /**
     * 构造方法
     *
     * @param errorCode
     */
    public ServerException(String errorCode) {
        this.errCode = errorCode;
    }

    /**
     * 构造方法
     *
     * @param errCode
     * @param errMsg
     */
    public ServerException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }

    /**
     * 构造方法
     *
     * @param msg
     * @param exception
     */
    public ServerException(String msg, Exception exception) {
        super(msg, exception);
    }

}
