package othello.com.example.bhavneetsingh.notes;
import java.util.ArrayList;

public interface InternetActivity<V,T> {
    T doInBackground(V... args);
    void onPostExecute(T result);
    void onPreExecute(T result);
}

