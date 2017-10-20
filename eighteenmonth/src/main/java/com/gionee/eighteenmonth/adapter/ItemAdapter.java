package com.gionee.eighteenmonth.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.bean.ItemBean;

import java.util.List;

public class ItemAdapter
        extends BaseRecyclerAdapter<ItemBean>
{
    private boolean[] flag;
    private Activity mContext;
    public ItemAdapter(Activity context, List<ItemBean> datas) {
        super(context, R.layout.item, datas);
        mContext = context;
        flag = new boolean[datas.size()];
    }

    @Override
    public void convert(BaseRecyclerHolder holder, ItemBean item, final int position) {
        ImageView img = holder.getView(R.id.home_item_img);
        img.setImageResource(item.getIcon());
        TextView details = holder.getView(R.id.home_item_details_tv);
        details.setText(item.getDetails());
        TextView title = holder.getView(R.id.home_item_title_tv);
        title.setText(item.getDescribe());
//        CheckBox cb = holder.getView(R.id.home_item_cb);
//        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                flag[position] = b;
//            }
//        });
//        cb.setChecked(flag[position]);
    }

}
