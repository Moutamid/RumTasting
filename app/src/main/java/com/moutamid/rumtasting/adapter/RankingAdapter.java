package com.moutamid.rumtasting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.models.RumModel;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingVH> {
    Context context;
    ArrayList<RumModel> list;

    public RankingAdapter(Context context, ArrayList<RumModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RankingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new RankingVH(LayoutInflater.from(context).inflate(R.layout.ranking_top_item, parent, false));
        }
        return new RankingVH(LayoutInflater.from(context).inflate(R.layout.ranking_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RankingVH holder, int position) {
        RumModel model = list.get(holder.getAdapterPosition());

        holder.name.setText(model.name);
        int counter = holder.getAdapterPosition() + 1;
        holder.counter.setText(String.valueOf(counter));
        Glide.with(context).load(model.image).placeholder(R.color.background).into(holder.profile);

        if (model.rating != null) {
            double star = 0;
            for (Float rate : model.rating) {
                star += rate;
            }
            star = star / model.rating.size();
            holder.rating.setText(String.format("%.2f", star));
        } else {
            holder.rating.setText(String.format("%.2f", 0.0));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public static class RankingVH extends RecyclerView.ViewHolder {
        TextView name, counter, rating;
        ImageView profile;
        public RankingVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            counter = itemView.findViewById(R.id.counter);
            rating = itemView.findViewById(R.id.rating);
            profile = itemView.findViewById(R.id.profile);
        }
    }

}
