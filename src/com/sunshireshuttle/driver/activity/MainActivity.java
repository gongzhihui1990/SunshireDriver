package com.sunshireshuttle.driver.activity;

import java.io.IOException;
import java.util.ArrayList;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.BaseFragmentArgs;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.adapter.ViewPagerMainAdapter;
import com.sunshireshuttle.driver.fragment.CurrentOrdersFragment;
import com.sunshireshuttle.driver.fragment.EarnFragment;
import com.sunshireshuttle.driver.fragment.HistoryOrdersFragment;
import com.sunshireshuttle.driver.fragment.LocationFragment;
import com.sunshireshuttle.driver.fragment.ProfileFragment;
import com.sunshireshuttle.driver.service.TripRecordService;
import com.sunshireshuttle.driver.widget.MainViewPager;
import com.sunshireshuttle.driver.widget.SimpleToast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.iaf.framework.util.Loger;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: MainActivity
 * @Description: 主页
 * @date 2016年8月5日 下午10:31:42
 */

public class MainActivity extends BaseActivity {
    private long firstTime;
    private LinearLayout mainPagerLayout;
    private ViewPagerMainAdapter mainPagerAdapter;
    private MainViewPager viewPager;
    private final static int requuestPermission = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPagerLayout = (LinearLayout) findViewById(R.id.main_pager_layout);
        viewPager = (MainViewPager) findViewById(R.id.main_pager_container);
        ArrayList<BaseFragmentArgs> fragmentArgs = new ArrayList<BaseFragmentArgs>();
        fragmentArgs.add(new BaseFragmentArgs(CurrentOrdersFragment.class, R.string.tab_current, R.drawable.icon_tab_current));
        fragmentArgs.add(new BaseFragmentArgs(HistoryOrdersFragment.class, R.string.tab_history, R.drawable.icon_tab_history));
        fragmentArgs.add(new BaseFragmentArgs(EarnFragment.class, R.string.tab_earn, R.drawable.icon_tab_earn));
        fragmentArgs.add(new BaseFragmentArgs(LocationFragment.class, R.string.tab_location, R.drawable.icon_tab_location));
        fragmentArgs.add(new BaseFragmentArgs(ProfileFragment.class, R.string.tab_profile, R.drawable.icon_tab_profile));
        initPager(viewPager, fragmentArgs);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        int item = intent.getIntExtra("page_position", 4);
        viewPager.setCurrentItem(item, false);
        Fragment fragment = mainPagerAdapter.getItem(item);
        if (fragment instanceof CurrentOrdersFragment) {
            ((CurrentOrdersFragment) fragment).receiveData(intent);
        }
        super.onNewIntent(intent);
    }

    /**
     * 初始化 Viewpager
     *
     * @param viewPager
     * @param fragmentArgs
     */
    private void initPager(final MainViewPager viewPager, final ArrayList<BaseFragmentArgs> fragmentArgs) {
        // set pager
        mainPagerAdapter = new ViewPagerMainAdapter(getSupportFragmentManager(), fragmentArgs);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int postion) {
                // final Fragment selectedFragment =
                // mainPagerAdapter.getItem(postion);
                BaseFragmentArgs curpage = fragmentArgs.get(postion);
                Class curFragment = curpage.getFragment();
                if (curFragment.getName().equals(LocationFragment.class.getName())) {
                    TripRecordService.onlocationPage = true;
                } else {
                    TripRecordService.onlocationPage = false;
                }
                for (int i = 0; i < mainPagerLayout.getChildCount(); i++) {
                    LinearLayout itemBoom = (LinearLayout) mainPagerLayout.getChildAt(i);
                    if (i == postion) {
                        itemBoom.setSelected(true);
                    } else {
                        itemBoom.setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        for (int index = 0; index < fragmentArgs.size(); index++) {
            addItemArg(fragmentArgs, index, viewPager);
        }
        // initial
        final int initPosition = 4;
        viewPager.setCurrentItem(initPosition, false);
        for (int i = 0; i < mainPagerLayout.getChildCount(); i++) {
            LinearLayout itemBoom = (LinearLayout) mainPagerLayout.getChildAt(i);
            if (i == initPosition) {
                itemBoom.setSelected(true);
            } else {
                itemBoom.setSelected(false);
            }
        }
    }

    @SuppressLint("InflateParams")
    private void addItemArg(ArrayList<BaseFragmentArgs> fragmentArgs, final int index, final MainViewPager viewPager) {
        BaseFragmentArgs args = fragmentArgs.get(index);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_item_page,
                null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);
        // layout.setGravity(Gravity.CENTER);
        ImageView icon = (ImageView) layout.findViewById(R.id.page_icon);
        TextView name = (TextView) layout.findViewById(R.id.page_name);
        name.setSingleLine();
        icon.setImageResource(args.drawable);
        try {
            ColorStateList csl = MainActivity.this.getResources().getColorStateList(R.color.color_tab_text);
            //  ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
            name.setTextColor(csl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText(args.name);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(index, false);
            }
        });
        mainPagerLayout.addView(layout);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
                SimpleToast.ToastMessage(R.string.double_click_to_back);
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
