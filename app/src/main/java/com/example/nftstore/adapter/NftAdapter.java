package com.example.nftstore.adapter;

import android.annotation.SuppressLint;
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

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.NftViewPage;
import com.example.nftstore.R;
import com.example.nftstore.models.NFT;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NftAdapter extends RecyclerView.Adapter<NftAdapter.NftViewHolder> {

    Context context;
    List<NFT> nftList;
    private NftViewHolder holder;

    public NftAdapter(Context context, List<NFT> nftList) {
        this.context = context;
        this.nftList = nftList;
    }

    @NonNull
    @Override
    public NftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nft_item, parent, false);
        return new NftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NftViewHolder holder, int position) {
        NFT nft = nftList.get(position);

        Picasso.get().load(nft.getURL()).into(holder.nftImage);
        holder.name.setText(nft.getName());
        holder.price.setText(nft.getPrice() + "$");
//        holder.collection.setText(nft.getCollection());
//        holder.owner.setText(nft.getOwner());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NftViewPage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                        new Pair<View, String>(holder.nftImage, "URLImage"));
                DBQuery.g_selected_URL = nft.getURL();
                intent.putExtra("URL", nft.getURL());
                intent.putExtra("coll_name", nft.getCollection());
                intent.putExtra("nft_name", nft.getName());
                intent.putExtra("owner_name", nft.getOwner());
                intent.putExtra("price", nft.getPrice());

                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return nftList.size();
    }

    public static class NftViewHolder extends RecyclerView.ViewHolder{
        ImageView nftImage;
        TextView name, price, owner, collection;

        public NftViewHolder(@NonNull View itemView) {
            super(itemView);

            nftImage = itemView.findViewById(R.id.nft_image);
            name = itemView.findViewById(R.id.name_of_nft);
            price = itemView.findViewById(R.id.price);
        }
    }
}
