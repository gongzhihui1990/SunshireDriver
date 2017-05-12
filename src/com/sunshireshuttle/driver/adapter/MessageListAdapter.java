package com.sunshireshuttle.driver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.model.MessageItem;
import com.sunshireshuttle.driver.model.SmsContent;

import net.iaf.framework.util.Loger;

import java.util.ArrayList;

public class MessageListAdapter extends MyBaseAdapter {

    private ArrayList<MessageItem> messageItems = new ArrayList<>();
    private BaseActivity context;
    private int green;
    private int gray;

    public MessageListAdapter(final ArrayList<MessageItem> smsContent, BaseActivity context) {
        for (MessageItem sms : smsContent) {
            this.messageItems.add(sms);
        }
        this.context = context;
        green = context.getResources().getColor(R.color.green);
        gray = context.getResources().getColor(R.color.gray);
    }

    public void setList(final ArrayList<MessageItem> msgs) {
        this.messageItems.clear();
        for (MessageItem msg : msgs) {
            this.messageItems.add(msg);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public MessageItem getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_message_content_list, parent, false);
            holder.viewIn = convertView.findViewById(R.id.ll_in);
            holder.viewOut = convertView.findViewById(R.id.ll_out);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_in);
            holder.tvInTime = (TextView) convertView.findViewById(R.id.tv_in_time);
            holder.ivIn = (ImageView) convertView.findViewById(R.id.icon_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_out);
            holder.tvOutTime = (TextView) convertView.findViewById(R.id.tv_out_time);
            holder.ivOut = (ImageView) convertView.findViewById(R.id.icon_out);
            convertView.setTag(holder);
        }
        final MessageItem messageItem = getItem(position);
        holder = (Holder) convertView.getTag();
        if (!messageItem.isIn()) {
            holder.viewOut.setVisibility(View.VISIBLE);
            holder.viewIn.setVisibility(View.GONE);
            holder.tvOut.setText(messageItem.getMessage_content());
            holder.tvOutTime.setText(messageItem.getReg_date());
            String staff_type = messageItem.getStaff_type();
            Loger.d("staff_type" + staff_type);
            if ("DRIVER".equals(staff_type)) {
                holder.ivOut.setImageResource(R.drawable.icon_driver);
            } else if ("DISPATCH".equals(staff_type)) {
                holder.ivOut.setImageResource(R.drawable.icon_dispatch);
            } else {
                holder.ivOut.setImageResource(R.drawable.icon_manager);
            }

        } else {
            holder.viewOut.setVisibility(View.GONE);
            holder.viewIn.setVisibility(View.VISIBLE);
            holder.tvIn.setText(messageItem.getMessage_content());
            holder.tvInTime.setText(messageItem.getReg_date());
            holder.ivIn.setImageResource(R.drawable.icon_customer);
        }
        return convertView;
    }


    class Holder {
        View viewIn;
        View viewOut;
        TextView tvIn;
        TextView tvInTime;
        TextView tvOut;
        TextView tvOutTime;
        ImageView ivIn;
        ImageView ivOut;
    }

}
