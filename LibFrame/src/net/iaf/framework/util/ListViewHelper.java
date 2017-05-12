package net.iaf.framework.util;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.iaf.framework.app.BaseApplication;

/**
 * ListView相关帮助类
 *
 * @author jiangsy
 */
public class ListViewHelper {

//	private Context mContext = null;

//	public ListViewHelper(Context context) {
//		mContext = context;
//	}

    /**
     * 计算ListView的高度，主要用于ListView与ScrollView嵌套的时候，需要计算ListView的高度
     *
     * @param listview ListView实例
     * @param space    ListView两侧与屏幕的间距(单位dip)
     */
    public static void setListViewHeight(ListView listview, int space) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listview.getAdapter();

        if (listAdapter == null || 0 == listAdapter.getCount()) {
            return;
        }

        int totalHeight = 0;

        int screenWidth = BaseApplication.getContext()
                .getResources().getDisplayMetrics().widthPixels;
        float density = BaseApplication.getContext()
                .getResources().getDisplayMetrics().density;

        // listAdapter.getCount()返回数据项的数目
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listview);

            int childWidthSpec = MeasureSpec.makeMeasureSpec(
                    (int) (screenWidth - (space * density)),
                    MeasureSpec.EXACTLY);
            // 计算子项View 的宽高
            listItem.measure(childWidthSpec, View.MeasureSpec.UNSPECIFIED);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listview.getLayoutParams();

        params.height = totalHeight
                + (listview.getDividerHeight() * (listAdapter.getCount() - 1))
                + listview.getPaddingTop() + listview.getPaddingBottom();

        Loger.d("BaseApplication.ScreenWidth:" + BaseApplication.getApplication().ScreenWidth);
        Loger.d("BaseApplication.density:" + BaseApplication.getApplication().density);
        Loger.d("listview.height:" + params.height);

        // params.height最后得到整个ListView完整显示需要的高度
        listview.setLayoutParams(params);
    }
}
