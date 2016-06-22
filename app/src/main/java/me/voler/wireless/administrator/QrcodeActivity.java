package me.voler.wireless.administrator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class QrcodeActivity extends AppCompatActivity {

    private EditText urlText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        urlText = (EditText) findViewById(R.id.download_url);
        textView = (TextView) findViewById(R.id.download_text);
    }

    public void downloadClickHandler(View view) {
        // Gets the URL from the UI's text field.
        String stringUrl = urlText.getText().toString();
        // Before attempting to fetch the URL, makes sure that there is a network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageText().execute(stringUrl);
        } else {
            textView.setText("当前网络连接不可用...");
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadHtml(urls[0]);
            } catch (IOException e) {
                return "获取网页失败...";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }

    private String downloadHtml(String stringUrl) throws IOException {
        Document document = Jsoup.connect(stringUrl).timeout(15000)
                .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/50.0.2661.102" +
                        " Safari/537.36").get();
        return document.html();
    }

}
