package com.penguinlabs.rko.seatsale_alerts;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    String url = "https://www.cebupacificair.com/Pages/seat-sale-promo.aspx";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Locate the Buttons in activity_main.xml
        Button scrapebutton = (Button) findViewById(R.id.scrapebutton);

        // Capture button click
        scrapebutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Execute WebScraper AsyncTask
                new WebScraper().execute();
            }
        });

    }

    private class WebScraper extends AsyncTask<Void, Void, Void> {
        String seatsaleinfoall = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("SCRAPING DATA..");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                Elements seatsaleinfo = document.select("div.seatsale");

                for(Element info : seatsaleinfo) {
                    seatsaleinfoall = seatsaleinfoall + info.text() + "\n";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            TextView seatsaletext = (TextView) findViewById(R.id.seatsaleinfo);
            seatsaletext.setText(seatsaleinfoall);
            seatsaletext.setMovementMethod(new ScrollingMovementMethod());
            mProgressDialog.dismiss();
        }
    }
}
