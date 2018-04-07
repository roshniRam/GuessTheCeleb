package com.example.dell.downloadingwebcontent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            Log.i("Extracting","From eco web hosting");
            String result = "";
            URL url;
            HttpURLConnection urlConnection;

            try{

                url = new URL(urls[0]); // getting url
                urlConnection = (HttpURLConnection) url.openConnection(); //likw opening a browser window , started to attempt load

                //doing that load
                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//reading that data, reads one char at a time

                int data = inputStreamReader.read();

                while (data!=-1){
                    char cuurentChar = (char)data;
                    result+= cuurentChar;

                    data = inputStreamReader.read();
                }
            return result;
            }catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);

        final Button downloadImage = (Button) findViewById(R.id.download_image);
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://t1.gstatic.com/images?q=tbn:ANd9GcRVuAiR3jcjv3FxB2GMTpo1suVR40sFOt0wa2Q472d6Vex4clj3
                ImageDownloader imageDownloader = new ImageDownloader();
                Bitmap myImage;

                try {
                    myImage = imageDownloader.execute("http://t1.gstatic.com/images?q=tbn:ANd9GcRVuAiR3jcjv3FxB2GMTpo1suVR40sFOt0wa2Q472d6Vex4clj3").get();
                    imageView.setImageBitmap(myImage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        DownloadTask task = new DownloadTask();
        try {
            String result = task.execute("https://www.ecowebhosting.co.uk/").get();
            Log.i("Contents of Url",result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
