package com.sunshireshuttle.driver.model;

import android.text.TextUtils;

import com.sunshireshuttle.driver.utils.UtilForTime;

import java.io.Serializable;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: RecentOrder
 * @Description: 消息
 * @date 2016年8月6日 下午9:15:05
 */

public class MessageItem implements Serializable, Comparable<MessageItem> {
    @Override
    public int compareTo(MessageItem another) {
        return 0;
    }

    public boolean isIn() {
        return "in".equals(direction);
    }

    private String direction;

    private String message_content;

    private String staff_type;

    private String reg_date;

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setStaff_type(String staff_type) {
        this.staff_type = staff_type;
    }

    public String getStaff_type() {
        return staff_type;
    }
}
