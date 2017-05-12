package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

public class MessageListResponse extends BaseResponseBean {
    private static final long serialVersionUID = 1L;
    private ArrayList<MessageItem> message_list;

    public ArrayList<MessageItem> getMessage_list() {
        return message_list;
    }

    public void setMessage_list(ArrayList<MessageItem> message_list) {
        this.message_list = message_list;
    }
}
