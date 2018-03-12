package othello.com.example.bhavneetsingh.notes;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Networking<T> extends AsyncTask<String,Void,T> {

    private InternetActivity<T> internetActivity;
    private OnDownloadComplete<T> downloadComplete;
    public Networking(InternetActivity<T> internetActivity,OnDownloadComplete<T>downloadComplete) {
        this.internetActivity = internetActivity;
        this.downloadComplete=downloadComplete;
    }

    @Override
    protected T doInBackground(String... strings) {
        return internetActivity.doInBackground(strings);
    }

    protected void onPostExecute(T posts) {
        super.onPostExecute(posts);
        downloadComplete.onDownloadComplete(posts);
    }

    protected void onPreExecute(T posts) {
        internetActivity.onPreExecute(posts);
    }

    public static String connectionResult(String urlLink) {
        String result="";
        try {
            URL url = new URL(urlLink);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.next());
            }
            result= stringBuilder.toString();
            inputStream.close();
            scanner.close();
        }
        catch (Exception e) {
        }
        finally {
            return result;
        }
    }
}
interface OnDownloadComplete<T>{
    void onDownloadComplete(T result);
}