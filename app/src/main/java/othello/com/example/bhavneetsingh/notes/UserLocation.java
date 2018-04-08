package othello.com.example.bhavneetsingh.notes;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class UserLocation {
    private double lattitude,longitude;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;
    private String user_id,name,profile_picture;


    public UserLocation(double lattitude, double longitude, String user_id) {
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public LatLng getPosition()
    {
        return new LatLng(lattitude,longitude);
    }
}
