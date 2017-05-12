package com.sunshireshuttle.driver.model;

public class SmsContent extends BaseResponseBean {
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
    private boolean selected;

    public SmsContent(String title, String content) {
        this.title = title;
        this.content = content;
        this.selected = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
