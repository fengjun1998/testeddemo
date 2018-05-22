package com.f.use.printdemo.print;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.f.use.printdemo.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f_ on 2018/5/22.
 * adapter
 */

public class UnBondDeviceAdapter extends RecyclerView.Adapter<UnBondDeviceAdapter.PrintViewHolder> {

    private Context mContext;

    private LayoutInflater mInflater;

    private final List<BluetoothDevice> mDataList = new ArrayList<BluetoothDevice>();

    public UnBondDeviceAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataList.clear();
    }


    @Override
    public PrintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.unbond_device_list_item, null);
        PrintViewHolder viewHolder = new PrintViewHolder(view);
        return viewHolder;
    }

    public void setData(List<BluetoothDevice> dataList) {
        mDataList.clear();

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        mDataList.addAll(dataList);
        notifyDataSetChanged();

    }

    public void addData(BluetoothDevice data) {
        if (data == null) {
            return;
        }
        if (!mDataList.contains(data)) {

            mDataList.add(data);
            notifyDataSetChanged();
            //notifyItemInserted(mDataList.size() - 1);
        }

    }

    public void removeData(BluetoothDevice data) {
        if (data == null) {
            return;
        }
        mDataList.remove(data);
        notifyDataSetChanged();
        // notifyItemRemoved(mDataList.size());
    }


    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(PrintViewHolder holder, int position) {
        if (position >= mDataList.size()) {
            return;
        }
        final BluetoothDevice device = mDataList.get(position);
        holder.tvName.setText(device.getName());
        holder.tvAddress.setText(device.getAddress());
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BondEvent event = new BondEvent();
                event.device = device;
                EventBus.getDefault().post(event);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class PrintViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public TextView tvAddress;

        public TextView tvDelete;

        public RelativeLayout rlRoot;


        public PrintViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
        }
    }


}
