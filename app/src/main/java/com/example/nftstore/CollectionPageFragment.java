package com.example.nftstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.adapter.CollectionAdapter;
import com.example.nftstore.models.Collection;

import java.util.ArrayList;
import java.util.List;


public class CollectionPageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private CollectionAdapter collAdapter;


    public CollectionPageFragment() {
        // Required empty public constructor
    }

    public static CollectionPageFragment newInstance(String param1, String param2) {
        CollectionPageFragment fragment = new CollectionPageFragment();
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
        View view = inflater.inflate(R.layout.fragment_collection_page, container, false);
        recyclerView = view.findViewById(R.id.collection_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        collAdapter = new CollectionAdapter(getContext(), DBQuery.g_collectionList);
        recyclerView.setAdapter(collAdapter);
        return view;
    }
}