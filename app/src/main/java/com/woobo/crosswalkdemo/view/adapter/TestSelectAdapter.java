package com.woobo.crosswalkdemo.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woobo.crosswalkdemo.R;
import com.woobo.crosswalkdemo.common.app.MyApplication;

/**
 * Created by sanji on 2018/3/21.
 */
public class TestSelectAdapter extends RecyclerView.Adapter<TestSelectAdapter.MyViewHolder> {

    private final MyApplication context;
    private final String[] datas;
    private OnItemClickListener onItemClickListener;

    public TestSelectAdapter(String[] datas) {
        this.datas = datas;
        context = MyApplication.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_test_select, null);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datas.length;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.content.setText(datas[position]);
        holder.position = position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView content;
        private int position;
        public MyViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onItemClickListener) onItemClickListener.onItemClick(position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
