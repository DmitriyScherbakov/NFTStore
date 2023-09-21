package com.example.nftstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.TabHost;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.adapter.NftAdapter;
import com.example.nftstore.models.NFT;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Library extends AppCompatActivity {

    private RecyclerView recyclerViewOwn, recyclerViewOnMrkt;

    private NftAdapter nftAdapterOwn, nftAdapterOnMrkt;

    private List<NFT> ownNftList = new ArrayList<>();
    private List<NFT> onMarketNftList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("In possession");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("On market");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);



        recyclerViewOwn = findViewById(R.id.nft_recycler_own);
        recyclerViewOnMrkt = findViewById(R.id.nft_recycler_on_mrkt);

        recyclerViewOwn.setLayoutManager(new LinearLayoutManager(Library.this));
        recyclerViewOnMrkt.setLayoutManager(new LinearLayoutManager(Library.this));
        recyclerViewOwn.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewOnMrkt.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewOwn.setHasFixedSize(true);
        recyclerViewOnMrkt.setHasFixedSize(true);

        for (int i = 0; i<DBQuery.g_nftList.size(); i++){
            if (Objects.equals(DBQuery.g_nftList.get(i).getOwner().trim().toUpperCase(), DBQuery.myProfile.getNickname().trim().toUpperCase())){
                if (DBQuery.g_nftList.get(i).isOnSale()) {
                    onMarketNftList.add(DBQuery.g_nftList.get(i));
                } else {
                    ownNftList.add(DBQuery.g_nftList.get(i));
                }
            }
        }

        nftAdapterOwn = new NftAdapter(Library.this, ownNftList);
        recyclerViewOwn.setAdapter(nftAdapterOwn);

        nftAdapterOnMrkt = new NftAdapter(Library.this, onMarketNftList);
        recyclerViewOnMrkt.setAdapter(nftAdapterOnMrkt);
    }
}