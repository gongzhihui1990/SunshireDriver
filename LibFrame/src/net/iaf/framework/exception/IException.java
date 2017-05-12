package net.iaf.framework.exception;

/**
 * 自定义异常的基类
 *
 * @author Bob
 */
public class IException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造
     */
    public IException() {
        super();
    }

    /**
     * 构造方法
     *
     * @param msg
     */
    public IException(String msg) {
        super(msg);
    }

    /**
     * 构造方法
     *
     * @param msg
     * @param exception
     */
    public IException(String msg, Exception exception) {
        super(msg, exception);
    }

    /**
     * 构造方法
     *
     * @param exception
     */
    public IException(Exception exception) {
        super(exception);
    }

}
