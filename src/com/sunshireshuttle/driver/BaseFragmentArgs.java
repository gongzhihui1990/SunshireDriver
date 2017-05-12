package com.sunshireshuttle.driver;

import com.sunshireshuttle.driver.fragment.DisableFragment;

import java.io.Serializable;


/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: BaseFragmentArgs
 * @Description: 跳转参数
 * @date 2016年8月5日 下午10:48:15
 */

public class BaseFragmentArgs implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * the short name for the fragment
     */
    public int name;
    public int drawable;
    private Class<? extends BaseFragment> fragment;

    public BaseFragmentArgs(Class<? extends BaseFragment> fragment, int nameSrc, int iconSrc) {
        if (fragment == null) {
            fragment = DisableFragment.class;
        }
        this.fragment = fragment;
        name = nameSrc;
        drawable = iconSrc;
    }

    public Class<? extends BaseFragment> getFragment() {
        return fragment;
    }

}