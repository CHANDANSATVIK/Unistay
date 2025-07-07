package com.example.unistay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private Context context;
    private List<Property> propertyList;
    private OnPropertyClickListener listener;

    public interface OnPropertyClickListener {
        void onPropertyClick(Property property);
        void onFavoriteClick(Property property);
    }

    public PropertyAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    public void setOnPropertyClickListener(OnPropertyClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.tvPropertyName.setText(property.getName());
        holder.tvPrice.setText(property.getPrice());
        holder.tvRating.setText(property.getRating());
        holder.tvAvailability.setText(property.getAvailability());

        if (property.isFavorite()) {
            holder.ivFavorite.setImageResource(R.drawable.redheart);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.heart);
        }

        if (property.getAvailability().toLowerCase().contains("only")) {
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.orange));
        } else {
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.ivPropertyImage.setImageResource(R.drawable.property); // default static image

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPropertyClick(property);
        });

        holder.ivFavorite.setOnClickListener(v -> {
            property.setFavorite(!property.isFavorite());
            notifyItemChanged(position);
            if (listener != null) listener.onFavoriteClick(property);
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPropertyImage, ivFavorite;
        TextView tvPropertyName, tvPrice, tvRating, tvAvailability;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPropertyImage = itemView.findViewById(R.id.iv_property_image);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
            tvPropertyName = itemView.findViewById(R.id.tv_property_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvAvailability = itemView.findViewById(R.id.tv_availability);
        }
    }
}
