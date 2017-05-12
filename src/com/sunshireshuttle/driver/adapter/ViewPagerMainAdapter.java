package com.sunshireshuttle.driver.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.sunshireshuttle.driver.BaseFragmentArgs;

import net.iaf.framework.util.Loger;

import java.util.ArrayList;

public class ViewPagerMainAdapter extends FragmentPagerAdapter {
    /**
     * the  {@link #fragments} in adapter
     */
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    /**
     * the {@link #fragmentsUpdateFlag} to judge the pager(fragment) item should be update or not
     */
    private boolean[] fragmentsUpdateFlag;
    /**
     * the  {@link #fm} to manage fragments
     */
    private FragmentManager fm;

    public ViewPagerMainAdapter(FragmentManager fm, ArrayList<BaseFragmentArgs> fragmentArgs) {
        super(fm);
        this.fm = fm;
        fragmentsUpdateFlag = new boolean[fragmentArgs.size()];//default update=false
        for (BaseFragmentArgs args : fragmentArgs) {
            try {
                Fragment fragment = args.getFragment().newInstance();
                Bundle arg = new Bundle();
                arg.putSerializable(BaseFragmentArgs.class.getName(), args);
                fragment.setArguments(arg);
                fragments.add(fragment);
                Loger.d("add " + fragment.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Loger.d(e.getMessage());
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        String fragmentTag = fragment.getTag();
        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {//if update=true
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            fragment = fragments.get(position % fragments.size());
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();
            fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
            Loger.d("instantiateItem");
            container.addView(fragment.getView());
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int index) {
        return fragments.get(index);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
//		Loger.d("destroyItem");
    }
}
