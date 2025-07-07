package com.example.unistay;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Calendar;

public class PropertyDetailsActivity extends AppCompatActivity {

    private Property property;
    private ImageView ivFavorite, ivPropertyImage;
    private TextView tvPropertyTitle, tvPropertyName, tvRating, tvPrice, tvDescription;
    private Button btnBookVisit;
    private Calendar selectedDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propertydetailsactivity);

        // Sample data since no intent data is assumed
        property = new Property(
                "Zolo Stays Prime",
                "Potheri Main Road, Kattankulathur, Chennai 603203",
                "₹7,500",
                "4.2",
                "@drawable/property", // Local drawable resource
                "Available",
                "A premium co-living space offering comfortable stays for students. Located conveniently near SRM Main Gate with all modern amenities.",
                Arrays.asList("Electricity", "High-Speed Wi-Fi", "Meals Included", "24/7 Security", "AC Rooms", "Single/Shared Rooms", "Laundry Service", "Housekeeping"),
                Arrays.asList(
                        new Property.RoomOption("Single Occupancy", "₹12,000", "1 Bed Available", false),
                        new Property.RoomOption("Two Sharing (AC)", "₹8,500", "1 Bed Available", true),
                        new Property.RoomOption("Three Sharing (Non-AC)", "₹7,500", "1 Bed Available", false)
                )
        );

        initViews();
        if (property != null) {
            setupData();
            setupClickListeners();
        } else {
            Toast.makeText(this, "No property data found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        ivPropertyImage = findViewById(R.id.iv_property_image);
        ivFavorite = findViewById(R.id.iv_favorite);
        tvPropertyTitle = findViewById(R.id.tv_property_title);
        tvPropertyName = findViewById(R.id.tv_property_name);
        tvRating = findViewById(R.id.tv_rating);
        tvPrice = findViewById(R.id.tv_price);
        tvDescription = findViewById(R.id.tv_description);
        btnBookVisit = findViewById(R.id.btn_book_visit);
    }

    private void setupData() {
        tvPropertyTitle.setText(property.getName());
        tvPropertyName.setText(property.getName());
        tvRating.setText(property.getRating());
        tvPrice.setText(property.getPrice());
        tvDescription.setText(
                property.getDescription() != null ? property.getDescription() : "No description available"
        );
        ivPropertyImage.setImageResource(getResources().getIdentifier(property.getImageUrl().replace("@drawable/", ""), "drawable", getPackageName()));
        updateFavoriteIcon();
    }

    private void setupClickListeners() {
        ivFavorite.setOnClickListener(v -> {
            property.setFavorite(!property.isFavorite());
            updateFavoriteIcon();

            if (property.isFavorite()) {
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
        });

        btnBookVisit.setOnClickListener(v -> {
            if (property.getLocation() != null && "Available".equalsIgnoreCase(property.getAvailability())) {
                showDatePickerDialog();
            } else {
                Toast.makeText(this, "No slots available or location missing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelected, monthSelected, dayOfMonth) -> {
                    selectedDateTime.set(yearSelected, monthSelected, dayOfMonth);
                    showTimePickerDialog();
                }, year, month, day);

        // Set minimum date to today (July 08, 2025)
        calendar.add(Calendar.DAY_OF_MONTH, 1); // Minimum 1 day ahead
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minuteOfHour);

                    String visitDetails = "Visit booked for " + property.getName() + "\n" +
                            "Date: " + selectedDateTime.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()) + " " +
                            selectedDateTime.get(Calendar.DAY_OF_MONTH) + ", " + selectedDateTime.get(Calendar.YEAR) + "\n" +
                            "Time: " + String.format("%02d:%02d %s", (hourOfDay % 12 == 0 ? 12 : hourOfDay % 12), minuteOfHour, hourOfDay >= 12 ? "PM" : "AM") + "\n" +
                            "Location: " + property.getLocation() + "\n" +
                            "Fee: ₹49\n" +
                            "Payment Confirmed";

                    Intent intent = new Intent(this, VisitConfirmationActivity.class);
                    intent.putExtra("confirmationDetails", visitDetails);
                    startActivity(intent);
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void updateFavoriteIcon() {
        ivFavorite.setImageResource(
                property.isFavorite() ? R.drawable.redheart : R.drawable.heart
        );
    }
}