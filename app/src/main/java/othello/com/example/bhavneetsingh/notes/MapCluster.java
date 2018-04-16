package othello.com.example.bhavneetsingh.notes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by bhavneet singh on 09-Apr-18.
 */

public class MapCluster implements ClusterItem {
    MyDatabase.User user;

    public MapCluster(MyDatabase.User user) {
        this.user = user;
    }

    public MyDatabase.User getUser() {
        return user;
    }

    public void setUser(MyDatabase.User user) {
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
