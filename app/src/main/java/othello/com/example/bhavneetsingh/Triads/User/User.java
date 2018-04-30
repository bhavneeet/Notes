package othello.com.example.bhavneetsingh.Triads.User;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;

/**
 * Created by bhavneet singh on 30-Apr-18.
 */
public class User {
    public static final String NAME = "NAME", PASSWORD = "PASSWORD", USER_ID = "USER_ID", TABLE = "USER", PROFILE_PICTURE = "imgurl", CATEGORY = "category", LONGITUDE = "longitude", LATITUDE = "latitude";
    private String name;
    private String password;
    private String user_id;
    private String category;
    private String status_text;
    private String status_cover;
    private double latitude;
    private double longitude;
    private boolean liked;

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getStatus_cover() {
        return status_cover;
    }

    public void setStatus_cover(String status_cover) {
        this.status_cover = status_cover;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String mobile_no;
    private String city;
    private URL profile_picture = null;
    private Bitmap profilePicture = null;
    private boolean currentFollowing;

    public boolean isCurrentFollowing() {
        return currentFollowing;
    }

    public void setCurrentFollowing(boolean currentFollowing) {
        this.currentFollowing = currentFollowing;
    }

    public User() {

    }

    public String toString() {
        return name;
    }

    public User(String name, String password, String user_id, String mobile_no, String city, URL profile_picture) {
        this.name = name;
        this.password = password;
        this.user_id = user_id;
        this.mobile_no = mobile_no;
        this.city = city;
        this.profile_picture = profile_picture;
    }

    public User(String user_id, String name, String password) {
        this.name = name;
        this.password = password;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureUrl() {
        String url = "null";
        if (profile_picture != null)
            url = profile_picture.toString();
        return url;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        try {

            this.profile_picture = new URL(profilePictureUrl);
        } catch (Exception e) {
            this.profile_picture = null;
        }
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
