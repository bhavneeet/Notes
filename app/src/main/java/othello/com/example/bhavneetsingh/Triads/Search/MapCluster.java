package othello.com.example.bhavneetsingh.Triads.Search;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import othello.com.example.bhavneetsingh.Triads.User.User;

/**
 * Created by bhavneet singh on 09-Apr-18.
 */

public class MapCluster implements ClusterItem {
    User user;

    public MapCluster(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public LatLng getPosition() {
        return user.getPosition();
    }

    @Override
    public String getTitle() {
        return user.getName();
    }

    @Override
    public String getSnippet() {
        return user.getStatus_text();
    }
}
