package com.example.nftstore.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nftstore.CollectionViewPage;
import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.R;
import com.example.nftstore.models.Collection;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    Context context;
    List<Collection> collList;
    private CollectionViewHolder holder;

    public CollectionAdapter(Context context, List<Collection> collList) {
        this.context = context;
        this.collList = collList;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collection_item, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection collection = collList.get(position);

        Picasso.get().load(collection.getCollection_image()).into(holder.collection_image);
        holder.coll_name.setText(collection.getName());
        holder.all_time_volume.setText(collection.getAll_time_volume() + "$");
        holder.floor_price.setText(collection.getFloor_price() + "$");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CollectionViewPage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                        new Pair<View, String>(holder.collection_image, "URLImage"));
                DBQuery.g_selected_URL_collection_image = collection.getCollection_image();
                intent.putExtra("coll_name", collection.getName());
                intent.putExtra("all_time_volume", collection.getAll_time_volume());
                intent.putExtra("col_id", collection.getCol_id());
                intent.putExtra("creator_name", collection.getCreator_name());
                intent.putExtra("floor_price", collection.getFloor_price());
                intent.putExtra("amount_of_nft", collection.getAmount_of_nft());

                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return collList.size();
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder{
        ImageView collection_image;
        TextView coll_name, all_time_volume, floor_price;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);

            collection_image = itemView.findViewById(R.id.coll_image);
            coll_name = itemView.findViewById(R.id.coll_name_field);
            all_time_volume = itemView.findViewById(R.id.all_time_volume_field);
            floor_price = itemView.findViewById(R.id.floor_price_field);

        }
    }
}
