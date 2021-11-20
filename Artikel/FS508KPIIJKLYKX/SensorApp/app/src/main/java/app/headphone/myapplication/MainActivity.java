package app.headphone.myapplication;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    TextView temp,celcius,percent,hum;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = (TextView)findViewById(R.id.temp);
        celcius=(TextView)findViewById(R.id.celcius);
        percent = (TextView)findViewById(R.id.percent);
        hum=(TextView)findViewById(R.id.hum);
        button= (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTextTask()
                        .execute(
                                "http://www.serverconnect.site88.net/sensor.html");
            }
        });
        Typeface font = Typeface.createFromAsset(getAssets(),"PrintClearly.otf");
        temp.setTypeface(font);
        celcius.setTypeface(font);
        hum.setTypeface(font);
        percent.setTypeface(font);
    }


    public static InputStream OpenHttpGETConnection(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return inputStream;
    }

    private String DownloadText(String URL) {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpGETConnection(URL);
        } catch (Exception e) {
            Log.d("DownloadText", e.getLocalizedMessage());
            return "";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
// ---convert the chars to a String---
                String readString = String
                        .copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("DownloadText", e.getLocalizedMessage());
            return "";
        }
        return str;
    }int length;
    String temperature = "";
    String humidity= "";

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("DownloadTextTask", result);
            //length = result.length();

            // length = result.indexOf("<!-- H");
            temperature = result.substring(16,19);
            humidity= result.substring(46, 48);
            temp.setText(temperature);
            hum.setText(humidity);


        }
    }
}



