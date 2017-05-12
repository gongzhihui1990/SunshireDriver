package net.iaf.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String的帮助类
 *
 * @author zgg
 */
public class StringHelper {
    /**
     * 判断是否是电话号码的验证
     *
     * @param mobiles 电话号码字符串
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // 还原11位手机号 包括去除“-”
    public static String GetNumber(String num2) {
        String num;
        if (num2 != null) {
            System.out.println(num2);
            num = num2.replaceAll("-", "");
            if (num.startsWith("+86")) {
                num = num.substring(3);
            } else if (num.startsWith("86")) {
                num = num.substring(2);
            } else if (num.startsWith("86")) {
                num = num.substring(2);
            }
        } else {
            num = "";
        }
        return num;
    }

    /**
     * 把汉字转换为unicode编码
     *
     * @param str
     * @return
     */
    public static String toUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        char[] ch = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (isChinese(ch[i])) {
                sb.append("\\u" + Integer.toHexString(str.charAt(i)));
            } else {
                sb.append(ch[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 判断字符串中是否有汉字的方法
     *
     * @param str
     * @return
     */
    public static boolean hasChinese(String str) {
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是汉字
     *
     * @param c
     * @return
     */
    private static final boolean isChinese(char c) {
        // GENERAL_PUNCTUATION 判断中文的“号
        // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
        // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

	/*
     * "<font color='red'>"+ token.source + "</font>"
	 */

    /**
     * 用html<font color=>表情包裹指定的source
     * e.g. wrapHtmlColors("source", "red")  return "<font color = 'color'>source</font>"
     */
    public static String wrapColorTag(String source, String color) {
        return "<font color='" + color + "'>" + source + "</font>";
    }

    public static String filterColorTag(String source, String color) {
        String temp = source;
        temp = temp.replace("<font color='" + color + "'>", "");
        temp = temp.replace("</font>", "");
        return temp;
    }

    /**
     * 截取最小的小时字符串
     *
     * @param msg 源字符串
     * @return String
     */
    public static String cutString_HourMin(String msg) {
        int index = msg.lastIndexOf(":");
        if (-1 == index) {
            return msg;
        }
        msg = msg.substring(0, index);
        return msg;
    }

    /**
     * 文字竖排的时候替换竖排的括号 ︵︶︷︸︿﹀︹︺︽︾﹁﹂﹃﹄︻︼
     *
     * @param horizontalString
     * @return
     */
    public static String replacePunctuation4Vertical(String horizontalString) {
        if (null == horizontalString || "".equals(horizontalString.trim())) {
            return "";
        }
        return horizontalString.replace("(", "︵").replace(")", "︶")
                .replace("[", "︹").replace("]", "︺").replace("{", "︷")
                .replace("}", "︸").replace("（", "︵").replace("）", "︶")
                .replace("【", "︻").replace("】", "︼").replace("『", "﹃")
                .replace("』", "﹄");
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}
