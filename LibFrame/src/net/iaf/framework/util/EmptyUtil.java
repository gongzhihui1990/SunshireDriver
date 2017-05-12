package net.iaf.framework.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 提供字符串，集合，数组，map等常见对象判空处理
 *
 * @author heisenberg.gong
 */
public class EmptyUtil {

    /**
     * 判断字符串，集合，数组，map等常见对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {

        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            String str = (String) obj;
            return "".equals(str.trim());
        }

        if (obj instanceof CharSequence) {
            CharSequence str = (CharSequence) obj;
            return str.length() == 0;
        }

        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            return col.isEmpty();
        }

        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.isEmpty();
        }

        if (obj.getClass().getSimpleName().endsWith("[]")) {
            List<Object> list = Arrays.asList(obj);
            Object[] objs = (Object[]) list.get(0);
            return objs.length == 0;
        }

        return false;
    }

    /**
     * 判断多个对象中是否存在空
     *
     * @param objs
     * @return
     */
    public static boolean existEmpty(Object... objs) {
        for (Object obj : objs) {
            if (isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }
}
