package com.example.nftstore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.adapter.NftAdapter;
import com.example.nftstore.models.NFT;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NftPageFragment extends Fragment{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final List<NFT> nftList = new ArrayList<>();
    private RecyclerView recyclerView;

    private NftAdapter nftAdapter;
    private ImageButton addNFTButton;

    public NftPageFragment(){
    }

    public static NftPageFragment newInstance(String param1, String param2) {
        NftPageFragment fragment = new NftPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nft_page, container, false);
        recyclerView = view.findViewById(R.id.nft_recycler);
        addNFTButton = view.findViewById(R.id.add_nft_button);

        nftList.clear();
        for (int i = 0; i<DBQuery.g_nftList.size(); i++){
            if (DBQuery.g_nftList.get(i).isOnSale()) {
                nftList.add(DBQuery.g_nftList.get(i));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        nftAdapter = new NftAdapter(getContext(), nftList);
        recyclerView.setAdapter(nftAdapter);

        addNFTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNFT.class);
                startActivity(intent);
            }
        });

        return view;
    }





//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//
//        nftList.clear();
//    }

//    private void dataInitialize() {
//
//        nftsArrayList.add(new NFT(1, "a0f554", "111", "example1", 120));
//        nftsArrayList.add(new NFT(2, "a172540", "111", "example2", 180));
//        nftsArrayList.add(new NFT(3, "b2db8b895c1a34ba063cc0c4b66458a06", "111", "example3", 200));
//        nftsArrayList.add(new NFT(4, "g172706", "111", "example4", 250));
//        nftsArrayList.add(new NFT(5, "c172503", "111", "example5", 400));
//        nftsArrayList.add(new NFT(6, "d172540", "111", "example6", 545));
//
//        nftAdapter.notifyDataSetChanged();
//    }

}