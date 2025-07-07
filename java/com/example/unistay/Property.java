package com.example.unistay;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Property implements Serializable {

    private String name;
    private String location;
    private String price;
    private String rating;
    private String imageUrl;
    private String availability;
    private String description;
    private List<String> amenities;
    private List<RoomOption> roomOptions;
    private boolean isFavorite;

    // Inner class for Room Options
    public static class RoomOption implements Serializable {
        private String type;
        private String price;
        private String availability;
        private boolean isSelected;

        public RoomOption(String type, String price, String availability, boolean isSelected) {
            this.type = type;
            this.price = price;
            this.availability = availability;
            this.isSelected = isSelected;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAvailability() {
            return availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    // Constructor
    public Property(String name, String location, String price, String rating, String imageUrl,
                    String availability, String description, List<String> amenities,
                    List<RoomOption> roomOptions) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.availability = availability;
        this.description = description;
        this.amenities = amenities;
        this.roomOptions = roomOptions;
        this.isFavorite = false;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<RoomOption> getRoomOptions() {
        return roomOptions;
    }

    public void setRoomOptions(List<RoomOption> roomOptions) {
        this.roomOptions = roomOptions;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // equals & hashCode for comparison (wishlist support)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Property property = (Property) obj;
        return name.equals(property.name) && location.equals(property.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}