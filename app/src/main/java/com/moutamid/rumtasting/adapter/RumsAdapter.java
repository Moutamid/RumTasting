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

public class RumsAdapter extends RecyclerView.Adapter<RumsAdapter.RumsVH> implements Filterable {
    Context context;
    ArrayList<RumModel> list;
    ArrayList<RumModel> listAll;

    public RumsAdapter(Context context, ArrayList<RumModel> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public RumsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RumsVH(LayoutInflater.from(context).inflate(R.layout.rum_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RumsVH holder, int position) {
        RumModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.name);
        holder.description.setText(model.description);
        Glide.with(context).load(model.image).placeholder(R.color.background).into(holder.profile);

        if (model.rating != null) {
            double rate = model.rating.star1 + model.rating.star2 + model.rating.star3 + model.rating.star4 + model.rating.star5;
            rate = rate / 5;
            holder.rating.setText(String.format("%.2f", rate));
        }

        ArrayList<RumModel> favorite = Stash.getArrayList(Constants.FAVORITES, RumModel.class);
        boolean check = favorite.stream().anyMatch(favoriteModel -> Objects.equals(favoriteModel.id, model.id));

        if (check) {
            holder.favoriteImage.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
        } else {
            holder.favoriteImage.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
        }

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, DetailActivity.class).putExtra(Constants.RUMS, model.id));
        });

        holder.favorite.setOnClickListener(v -> {
            ArrayList<RumModel> favor = Stash.getArrayList(Constants.FAVORITES, RumModel.class);
            boolean cc = favor.stream().anyMatch(favoriteModel -> Objects.equals(favoriteModel.id, model.id));
            if (!cc) {
                favor.add(model);
                holder.favoriteImage.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                int index = favor.stream()
                        .filter(favoriteModel -> Objects.equals(favoriteModel.id, model.id))
                        .findFirst()
                        .map(favor::indexOf)
                        .orElse(-1);
                if (index != -1) {
                    favor.remove(index);
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
                holder.favoriteImage.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
            }
            Stash.put(Constants.FAVORITES, favor);
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
        TextView name, description, rating;
        ImageView profile, favoriteImage;
        MaterialCardView favorite;

        public RumsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            rating = itemView.findViewById(R.id.rating);
            profile = itemView.findViewById(R.id.profile);
            favorite = itemView.findViewById(R.id.favorite);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
        }
    }

}
