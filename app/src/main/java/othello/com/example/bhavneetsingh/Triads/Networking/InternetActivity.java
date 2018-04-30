package othello.com.example.bhavneetsingh.Triads.Networking;

public interface InternetActivity<V,T> {
    T doInBackground(V... args);
    void onPostExecute(T result);
    void onPreExecute(T result);
}

