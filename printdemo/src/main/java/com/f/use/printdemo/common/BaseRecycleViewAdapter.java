package com.f.use.printdemo.common;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f_ on 2018/5/22.
 * RecyclerView的adapter基类
 */

public abstract class BaseRecycleViewAdapter<T extends RecyclerView.ViewHolder, D extends Object> extends RecyclerView.Adapter<T> {

    private OnItemClickListener onItemClickListener;

    protected static final int TYPE_HEADER = -1;
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_FOOTER = 1;

    protected static int headerSize = 1;
    protected static int footerSize = 1;

    protected boolean isLoadMore;
    protected boolean hasHeader;

    protected final List<D> mDataList = new ArrayList<D>();

    protected Context mContext;

    protected LayoutInflater mInflater;

    public BaseRecycleViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataList.clear();
    }


    @Override// 获得列表数量
    public int getItemCount() {
        int extraSize = 0;
        if (isLoadMore) {
            extraSize++;
        }
        if (hasHeader) {
            extraSize++;
        }

        return mDataList.size() == 0 ? 0 : mDataList.size() + extraSize;

    }

    // 设置数据
    public void setData(List<D> dataList) {
        mDataList.clear();

        if (dataList == null || dataList.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override//
    public int getItemViewType(int position) {
     //   Logger.e("getItemViewType() -- position is "+position);



        if (!hasHeader) {
            if (!isLoadMore) {
                return super.getItemViewType(position);
            } else {
                if (position + 1 == getItemCount()) {
                    return TYPE_FOOTER;
                } else {
                    return TYPE_ITEM;
                }
            }
        } else {
            if (!isLoadMore) {
                if (position == 0) {
                    return TYPE_HEADER;
                } else {
                    return TYPE_ITEM;
                }
            } else {
                if (position == 0) {
                    return TYPE_HEADER;
                } else if (position + 1 == getItemCount()) {
                    return TYPE_FOOTER;
                } else {
                    return TYPE_ITEM;
                }

            }
        }
    }

    // 添加数据
    public void addData(D data) {
        if (data == null) {
            return;
        }

        mDataList.add(data);
        // notifyDataSetChanged();
        notifyItemInserted(mDataList.size() - 1);

    }

    // 添加数据列表
    public void addData(List<D> dataList) {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    // 删除数据列表
    public void removeData(List<D> datas) {
        if (datas != null && datas.size() > 0) {
            mDataList.removeAll(datas);
        }
        notifyDataSetChanged();
    }

    // 删除数据
    public void removeData(D data) {
        if (data == null) {
            return;
        }

        int index = mDataList.indexOf(data);
        if (index == -1) {
            return;
        }

        mDataList.remove(data);
        // notifyDataSetChanged(mData);
        notifyItemRemoved(index);
    }

    public void removeData(int position) {
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void removeData2(int position) {
        D data = mDataList.get(position);
        int index = mDataList.indexOf(data);
        mDataList.remove(data);
        notifyItemRemoved(index);
    }

    public void removeData3(int position) {
        D data = mDataList.get(position);
        int index = mDataList.indexOf(data);
        mDataList.remove(data);
        notifyDataSetChanged();
    }

    public void updateData(D data, int position) {
        if (data == null) {
            return;
        }
        notifyItemChanged(position, data);
    }

    public List<D> getData() {
        return mDataList;
    }


    // 替换数据
    public void replaceData(D data) {
        if (data == null) {
            return;
        }
        mDataList.remove(data);
        mDataList.add(data);
        notifyDataSetChanged();
    }

    // 是否加载更多
    public boolean isLoadMore() {
        return isLoadMore;
    }

    // 设置加载更多
    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
        notifyDataSetChanged();
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    // 监听
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
