package net.iaf.framework.exception;

/**
 * 数据库相关异常
 *
 * @author Bob
 */
public class DBException extends IException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造
     */
    public DBException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param msg
     */
    public DBException(String msg) {
        super(msg);
    }

    /**
     * 构造方法
     *
     * @param msg
     * @param exception
     */
    public DBException(String msg, Exception exception) {
        super(msg, exception);
    }

    /**
     * 构造方法
     *
     * @param exception
     */
    public DBException(Exception exception) {
        super(exception);
    }
}
