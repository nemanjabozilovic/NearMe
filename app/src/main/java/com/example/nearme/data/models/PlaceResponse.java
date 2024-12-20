package com.example.nearme.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlaceResponse {

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    @SerializedName("results")
    private List<Place> results;

    @SerializedName("status")
    private String status;

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Place {

        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("icon")
        private String icon;

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("opening_hours")
        private OpeningHours openingHours;

        @SerializedName("photos")
        private List<Photo> photos;

        @SerializedName("place_id")
        private String placeId;

        @SerializedName("reference")
        private String reference;

        @SerializedName("scope")
        private String scope;

        @SerializedName("types")
        private List<String> types;

        @SerializedName("vicinity")
        private String vicinity;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public static class Geometry {

            @SerializedName("location")
            private Location location;

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            public static class Location {

                @SerializedName("lat")
                private Double lat;

                @SerializedName("lng")
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }
            }
        }

        public static class OpeningHours {

            @SerializedName("open_now")
            private Boolean openNow;

            public Boolean getOpenNow() {
                return openNow;
            }

            public void setOpenNow(Boolean openNow) {
                this.openNow = openNow;
            }
        }

        public static class Photo {

            @SerializedName("height")
            private Integer height;

            @SerializedName("html_attributions")
            private List<String> htmlAttributions;

            @SerializedName("photo_reference")
            private String photoReference;

            @SerializedName("width")
            private Integer width;

            public Integer getHeight() {
                return height;
            }

            public void setHeight(Integer height) {
                this.height = height;
            }

            public List<String> getHtmlAttributions() {
                return htmlAttributions;
            }

            public void setHtmlAttributions(List<String> htmlAttributions) {
                this.htmlAttributions = htmlAttributions;
            }

            public String getPhotoReference() {
                return photoReference;
            }

            public void setPhotoReference(String photoReference) {
                this.photoReference = photoReference;
            }

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }
        }
    }
}