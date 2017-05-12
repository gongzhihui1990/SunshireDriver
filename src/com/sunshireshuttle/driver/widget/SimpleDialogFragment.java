package com.sunshireshuttle.driver.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.sunshireshuttle.driver.R;

public class SimpleDialogFragment extends android.support.v4.app.DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return ininMobile(inflater, container, savedInstanceState);
    }

    private View ininMobile(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Actions4SimpleDlg actions = (Actions4SimpleDlg) getArguments().getSerializable("actions");
        if (actions.layoutView != null) {
            return actions.layoutView;
        } else {
            setCancelable(actions.cancelble);
            View view = inflater.inflate(R.layout.layout_common_dialog, container, false);
            View verticial_divider1 = view.findViewById(R.id.verticial_divider1);
            View verticial_divider2 = view.findViewById(R.id.verticial_divider2);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvContent.setText(actions.message);
            tvContent.setTextColor(getResources().getColor(actions.textColorRes));
            String title = actions.title;
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            }
            final Button btn_cancel = (Button) view.findViewById(R.id.btn_1);
            final Button btn_ok = (Button) view.findViewById(R.id.btn_2);
            final Button btn_3 = (Button) view.findViewById(R.id.btn_3);
            if (!TextUtils.isEmpty(actions.btn1)) {
                btn_cancel.setText(actions.btn1);
            }
            if (!TextUtils.isEmpty(actions.btn2)) {
                btn_ok.setText(actions.btn2);
            }
            if (!TextUtils.isEmpty(actions.btn3)) {
                btn_3.setText(actions.btn3);
            }
            if (actions.otherListener != null) {
                verticial_divider2.setVisibility(View.VISIBLE);
                btn_3.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                        actions.otherListener.onClick(v);
                    }
                });
            } else {
                btn_3.setVisibility(View.GONE);
                verticial_divider2.setVisibility(View.GONE);
            }
            if (actions.cancelble) {
                verticial_divider1.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                getDialog().getWindow().getDecorView().setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            } else {
                btn_cancel.setVisibility(View.VISIBLE);
            }
            if (actions.cancleListener != null) {
                btn_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                        actions.cancleListener.onClick(btn_cancel);
                    }
                });
            } else {
                btn_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (actions.confirmListener != null) {
                        actions.confirmListener.onClick(btn_ok);
                    }
                }
            });
            return view;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView() {
        try {
            final IBinder token = getActivity().getCurrentFocus().getWindowToken();
            final InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        imm.hideSoftInputFromWindow(token, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 50);
        } catch (Exception e) {
        }
        super.onDestroyView();
    }
}