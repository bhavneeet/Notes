package othello.com.example.bhavneetsingh.notes;
import java.util.ArrayList;

public interface InternetActivity {
    ArrayList<Posts> doInBackground(String... strings);
    void onPostExecute(ArrayList<Posts> posts);
    void onPreExecute(ArrayList<Posts>posts);
}

