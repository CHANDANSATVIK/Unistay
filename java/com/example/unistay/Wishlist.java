package com.example.unistay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Wishlist extends Fragment implements PropertyAdapter.OnPropertyClickListener {

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;

    public Wishlist() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        recyclerView = view.findViewById(R.id.rv_wishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PropertyAdapter(getContext(), WishlistManager.getFavorites());
        adapter.setOnPropertyClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onPropertyClick(Property property) {
        Intent intent = new Intent(getActivity(), PropertyDetailsActivity.class);
        intent.putExtra("property", property); // sending full object
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Property property) {
        property.setFavorite(false);
        WishlistManager.remove(property);
        adapter.notifyDataSetChanged();
    }
}
