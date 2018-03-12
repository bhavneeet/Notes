package othello.com.example.bhavneetsingh.notes;
import java.util.ArrayList;

public interface InternetActivity<T> {
    T doInBackground(String... strings);
    void onPostExecute(T result);
    void onPreExecute(T result);
}

