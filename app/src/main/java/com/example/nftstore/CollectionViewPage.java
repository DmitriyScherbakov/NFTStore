package com.example.nftstore;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.adapter.NftAdapter;
import com.example.nftstore.models.NFT;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CollectionViewPage extends AppCompatActivity {

    private ImageView creator_avatar, coll_image;
    private TextView coll_name, creator_name, all_time_volume, floor_price, items_amount;
    private String URL, URL_avatar;
    private List<NFT> collNftList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_view_page);

        creator_avatar = findViewById(R.id.avatar_image);
        coll_image = findViewById(R.id.collection_image);
        coll_name = findViewById(R.id.coll_name_field);
        coll_name.setPaintFlags(coll_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        creator_name = findViewById(R.id.creator_name_field);
        all_time_volume = findViewById(R.id.volume_field);
        floor_price = findViewById(R.id.floor_price_field);
        items_amount = findViewById(R.id.amount_items_field);

        URL = DBQuery.g_selected_URL_collection_image;

        Picasso.get().load(URL).into(coll_image);
        coll_name.setText(getIntent().getStringExtra("coll_name"));
        creator_name.setText(getIntent().getStringExtra("creator_name"));
        all_time_volume.setText(getIntent().getStringExtra("all_time_volume") + "$");
        floor_price.setText(getIntent().getStringExtra("floor_price") + "$");
        items_amount.setText(getIntent().getStringExtra("amount_of_nft"));

        RecyclerView recyclerView = findViewById(R.id.nft_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionViewPage.this));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        for (int i = 0; i<DBQuery.g_nftList.size(); i++){
            if (Objects.equals(DBQuery.g_nftList.get(i).getCollection().trim().toUpperCase(), coll_name.getText().toString().trim().toUpperCase())){
                collNftList.add(DBQuery.g_nftList.get(i));
            }
        }

        NftAdapter nftAdapter = new NftAdapter(CollectionViewPage.this, collNftList);
        recyclerView.setAdapter(nftAdapter);
    }
}