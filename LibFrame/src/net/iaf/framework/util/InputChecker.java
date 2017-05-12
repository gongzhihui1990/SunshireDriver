package net.iaf.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入内容检查
 *
 * @version 1.0.0
 */
public class InputChecker {

    /**
     * 判断字符串格式是否整型
     *
     * @param msg
     * @return
     */
    public static boolean isNum(String msg) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(msg);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * str是否全由字母组成（A-Z a-z）
     */
    public static boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("[A-Za-z]*");
        Matcher isLetter = pattern.matcher(str);
        if (!isLetter.matches()) {
            return false;
        }
        return true;
    }

    /**
     * validation numeric
     *
     * @param str
     * @return
     * @Title: isNumeric
     */
    public static boolean isNumeric(String str) {
        return isMatcher("[0-9]*", str);
    }

    /**
     * validation mobile
     *
     * @param mobile
     * @return
     * @Title: checkMobile
     */
    public static boolean isMobile(String str) {
        // String regex = "^1(3[0-9]|5[012356789]|8[0789])\\d{8}$";
        return isMatcher("^((13[0-9])|(147)|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$",
                str);
    }

    /**
     * check msg address
     *
     * @param str
     * @return
     */
    public static boolean isMsgAddress(String str) {
        return isMatcher("(05328)\\d{7}", str);
    }

    /**
     * get six verification code
     *
     * @param str
     * @return
     */
    public static String getSixVerificationCode(String str) {
        return matcherString("\\d{6}", str);
    }

    /**
     * validation email
     *
     * @param mail
     * @return
     * @Title: checkEmail
     */
    public static boolean isEmail(String str) {
        String reg = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return isMatcher(reg, str);
    }

    /**
     * validation is null
     *
     * @param str
     * @return ture is null
     * @Title: isNull
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 正则匹配的方法
     *
     * @param regex
     * @param str
     * @return
     */
    private static boolean isMatcher(String regex, String str) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
//		return m.find();
        return m.matches();
    }

    public static Pattern getWebPattern() {
        Pattern webpattern = Pattern
                .compile("http[s]*://[[[^/:]&&[a-zA-Z_0-9]]\\.]+(:\\d+)?(/[a-zA-Z_0-9]+)*(/[a-zA-Z_0-9]*([a-zA-Z_0-9]+\\.[a-zA-Z_0-9]+)*)?(\\?(&?[a-zA-Z_0-9]+=[%[a-zA-Z_0-9]-]*)*)*(#[[a-zA-Z_0-9]|-]+)?");
        return webpattern;
    }

    public static Pattern getPhonePattern() {
        Pattern phonepattern = Pattern
                .compile("((13[0-9])|(147)|(15[^4,\\D])|(170)|(18[0-9]))\\d{8}");
        return phonepattern;
    }

    /**
     * 返回匹配的字符序列
     *
     * @param regex 要编译的表达式
     * @param str   要匹配的字符序列
     * @return
     */
    private static String matcherString(String regex, String str) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        String result = "";
        while (m.find()) {
            result = m.group();
        }
        return result;
    }

    /**
     * 返回是否是车牌号
     *
     * @param carno
     * @return true 为是正确的车牌号
     */
    public static boolean isCarNo(String carno) {

        String regex = "[\u4eac\u6caa\u6d59\u82cf\u7ca4\u9c81\u664b\u5180\u8c6b\u5ddd\u6e1d\u8fbd\u5409\u9ed1\u7696\u9102\u6e58\u8d63\u95f5\u9655\u7518\u5b81\u8499\u6d25\u8d35\u4e91\u6842\u743c\u9752\u65b0\u85cf][a-zA-Z][0-9a-zA-Z]{5}";
//		String regex = "[\u4E00-\u9FFF][a-zA-Z][0-9a-zA-Z]{5}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(carno);
        return matcher.matches();
    }
}
