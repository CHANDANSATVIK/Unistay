package com.example.unistay;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class Bookings extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Bookings() {
        // Required empty public constructor
    }

    public static Bookings newInstance(String param1, String param2) {
        Bookings fragment = new Bookings();
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

        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        // 🔹 Visit Reminder Card Click Setup
        CardView cardVisitReminder = view.findViewById(R.id.card_visit_reminder);
        if (cardVisitReminder != null) {
            cardVisitReminder.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), VisitConfirmationActivity.class);
                intent.putExtra("confirmationDetails",
                        "Zolo Stays Prime\n" +
                                "Date: July 9, 2025\n" +
                                "Time: 3:00 PM\n" +
                                "Location: Potheri Main Road, Chennai\n" +
                                "Fee: ₹49\nPayment Confirmed");
                startActivity(intent);
            });
        } else {
            Toast.makeText(getContext(), "Reminder card not found in layout", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
