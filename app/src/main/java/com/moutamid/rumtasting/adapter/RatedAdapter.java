package com.moutamid.rumtasting.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.activities.DetailActivity;
import com.moutamid.rumtasting.models.RumModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RatedAdapter extends RecyclerView.Adapter<RatedAdapter.RumsVH> implements Filterable {
    Context context;
    ArrayList<RumModel> list;
    ArrayList<RumModel> listAll;

    public RatedAdapter(Context context, ArrayList<RumModel> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public RumsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RumsVH(LayoutInflater.from(context).inflate(R.layout.rated_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RumsVH holder, int position) {
        RumModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.name);
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

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, DetailActivity.class).putExtra(Constants.RUMS, model.id));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RumModel> filterList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filterList.addAll(listAll);
            } else {
                for (RumModel listModel : listAll) {
                    if (listModel.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends RumModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class RumsVH extends RecyclerView.ViewHolder {
        TextView name, rating;
        public RumsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
        }
    }

}
