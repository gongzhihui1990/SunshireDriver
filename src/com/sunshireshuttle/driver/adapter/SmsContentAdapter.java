package com.sunshireshuttle.driver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.model.SmsContent;

import java.util.ArrayList;

public class SmsContentAdapter extends MyBaseAdapter {

    public static final int ModeList = 0;
    public static final int ModeGrid = 1;
    private ArrayList<SmsContent> smsContentList = new ArrayList<>();
    private BaseActivity context;
    private int green;
    private int gray;
    private int adaptMode = ModeList;

    public SmsContentAdapter(final ArrayList<SmsContent> smsContent, BaseActivity context) {
        for (SmsContent sms : smsContent) {
            this.smsContentList.add(sms);
        }
        this.context = context;
        green = context.getResources().getColor(R.color.green);
        gray = context.getResources().getColor(R.color.gray);
    }

    public void setList(final ArrayList<SmsContent> smsContent, final int mode) {
        switch (mode) {
            case ModeList:
            case ModeGrid:
                this.adaptMode = mode;
                break;
            default:
                break;
        }
        this.smsContentList.clear();
        for (SmsContent sms : smsContent) {
            this.smsContentList.add(sms);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return smsContentList.size();
    }

    @Override
    public SmsContent getItem(int position) {
        return smsContentList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            switch (adaptMode) {
                case ModeList:
                    convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_sms_content_list, parent, false);
                    break;
                case ModeGrid:
                    convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_sms_content_gird, parent, false);
                    break;
                default:
                    convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_sms_content_list, parent, false);
                    break;
            }
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }
        final SmsContent smsContent = getItem(position);
        holder = (Holder) convertView.getTag();
        holder.tv_title.setText(smsContent.getTitle());
        holder.tv_content.setText(smsContent.getContent());
        if (smsContent.isSelected()) {
            holder.tv_content.setTextColor(green);
            if (adaptMode == ModeList) {
                holder.tv_content.setSingleLine(false);
            }
        } else {
            if (adaptMode == ModeList) {
                holder.tv_content.setSingleLine(true);
            }
            holder.tv_content.setTextColor(gray);
        }
        holder.tv_title.setSelected(smsContent.isSelected());
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smsContent.isSelected()) {
                    return;
                }
                for (SmsContent item : smsContentList) {
                    item.setSelected(false);
                }
                smsContent.setSelected(true);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public String getSelectText() {
        for (SmsContent item : smsContentList) {
            if (item.isSelected()) {
                return item.getContent();
            }
        }
        return null;
    }

    class Holder {
        TextView tv_title;
        TextView tv_content;
    }

}
