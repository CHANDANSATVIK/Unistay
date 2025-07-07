package com.example.unistay;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends Fragment implements PropertyAdapter.OnPropertyClickListener {

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private List<Property> propertyList;

    private View selectedFilterButton = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupRecyclerView();
        loadSampleData();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_properties);

        view.findViewById(R.id.ll_search_bar).setOnClickListener(v ->
                Toast.makeText(getContext(), "Search clicked", Toast.LENGTH_SHORT).show());

        Button btnFilters = view.findViewById(R.id.btn_filters);
        Button btnForBoys = view.findViewById(R.id.btn_for_boys);
        Button btnBelow8k = view.findViewById(R.id.btn_below_8k);

        View.OnClickListener filterClickListener = v -> {
            if (selectedFilterButton != null) {
                selectedFilterButton.setBackgroundResource(R.drawable.filter_button_unselected);
                ((Button) selectedFilterButton).setTextColor(getResources().getColor(R.color.gray_600));
            }

            v.setBackgroundResource(R.drawable.filter_button_selected);
            ((Button) v).setTextColor(getResources().getColor(android.R.color.white));
            selectedFilterButton = v;

            Toast.makeText(getContext(), ((Button) v).getText() + " selected", Toast.LENGTH_SHORT).show();
        };

        btnFilters.setOnClickListener(filterClickListener);
        btnForBoys.setOnClickListener(filterClickListener);
        btnBelow8k.setOnClickListener(filterClickListener);
    }

    private void setupRecyclerView() {
        propertyList = new ArrayList<>();
        adapter = new PropertyAdapter(getContext(), propertyList);
        adapter.setOnPropertyClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadSampleData() {
        propertyList.clear();

        List<Property.RoomOption> roomOptions = Arrays.asList(
                new Property.RoomOption("Single Occupancy", "₹12,000", "1 Bed Available", false),
                new Property.RoomOption("Two Sharing (AC)", "₹8,500", "1 Bed Available", true),
                new Property.RoomOption("Three Sharing (Non-AC)", "₹7,500", "1 Bed Available", false)
        );

        propertyList.add(new Property(
                "Zolo Stays Prime",
                "Potheri, Near SRM Main Gate",
                "₹7,500",
                "4.2",
                "",
                "5 Rooms Available",
                "A premium co-living space offering comfortable stays for students. Located conveniently near SRM Main Gate with all modern amenities. Experience a hassle-free and secure living environment.",
                Arrays.asList("Electricity", "High-Speed Wi-Fi", "Meals Included", "24/7 Security", "AC Rooms", "Single/Shared Rooms", "Laundry Service", "Housekeeping"),
                roomOptions
        ));

        propertyList.add(new Property(
                "Kumar's Mens Hostel",
                "Guindy, Near Metro Station",
                "₹6,000",
                "3.9",
                "",
                "Only 2 Rooms Left!",
                "Modern hostel accommodation in the heart of Guindy. Perfect for working professionals and students with easy access to metro station and IT corridors.",
                Arrays.asList("High-Speed Wi-Fi", "Single/Shared Rooms", "Laundry Service", "24/7 Security"),
                roomOptions
        ));

        propertyList.add(new Property(
                "Green Valley PG",
                "Adyar, Near Beach",
                "₹8,500",
                "4.5",
                "",
                "3 Rooms Available",
                "Premium PG accommodation near Adyar beach. Enjoy the serene environment while staying connected to the city's major landmarks and educational institutions.",
                Arrays.asList("Electricity", "High-Speed Wi-Fi", "Meals Included", "24/7 Security", "AC Rooms"),
                roomOptions
        ));

        propertyList.add(new Property(
                "Student's Haven",
                "Velachery, IT Corridor",
                "₹5,800",
                "4.1",
                "",
                "7 Rooms Available",
                "Comfortable accommodation with modern amenities. Experience quality living with excellent facilities and convenient location access.",
                Arrays.asList("High-Speed Wi-Fi", "Meals Included", "24/7 Security", "Parking"),
                roomOptions
        ));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPropertyClick(Property property) {
        Intent intent = new Intent(getContext(), PropertyDetailsActivity.class);
        intent.putExtra("property", property);  // Property must implement Serializable
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Property property) {
        if (property.isFavorite()) {
            WishlistManager.add(property);
        } else {
            WishlistManager.remove(property);
        }

        String message = property.isFavorite() ? "Added to favorites" : "Removed from favorites";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
