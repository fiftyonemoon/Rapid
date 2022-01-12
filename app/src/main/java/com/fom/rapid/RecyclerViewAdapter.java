package com.fom.rapid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fom.rapid.app.Resize;
import com.fom.rapid.assistant.HeyMoon;
import com.fom.rapid.databinding.AdapterRecyclerViewBinding;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public Context context;
    private OnItemClickListener mListener;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public AdapterRecyclerViewBinding binding;

        public MyViewHolder(AdapterRecyclerViewBinding binding, final OnItemClickListener listener) {
            super(binding.getRoot());

            this.binding = binding;

            HeyMoon.resize().view(binding.ivMax).measureWith(Resize.Attrs.width).now(context);
            HeyMoon.resize().view(binding.ivMin).measureWith(Resize.Attrs.width).now(context);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterRecyclerViewBinding binding = AdapterRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.ivMax.setImageResource(R.drawable.max_button);
        holder.binding.ivMin.setImageResource(R.drawable.button_1);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
