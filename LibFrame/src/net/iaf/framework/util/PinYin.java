package net.iaf.framework.util;

import net.iaf.framework.util.HanziToPinyin.Token;

import java.util.ArrayList;

/**
 * 中文转拼音的工具类
 *
 * @author Bob
 */
public class PinYin {
    /**
     * 传入中文转拼音
     *
     * @param input
     * @return
     */
    public static String getPinYin(String input) {
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (Token token : tokens) {
                if (Token.PINYIN == token.type) {
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    sb.append(token.target);
                    sb.append(' ');
                    sb.append(token.source);
                } else {
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toLowerCase();
    }
}
