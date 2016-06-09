package com.example.karan.testapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvData;
    private Button btnHit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnHit = (Button) findViewById(R.id.btnHit);
        tvData = (TextView) findViewById(R.id.textView);


        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://indiavoice.tv/index.php?option=com_xmlrpc&view=service&task=getImageGallery&format=json");
            }
        });

    }


    public class JSONTask extends AsyncTask<String, String, String> {
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        Hashtable<String, String> ht = new Hashtable<String, String>();
        String json,line,id, title,alias,desc,date,filename;
        @Override
        protected String doInBackground(final String... params) {


            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream stream = conn.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                //  line = "";
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line);
                }

                json = buffer.toString();

                return json;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if ((conn != null)) {
                    conn.disconnect();
                }
                try {

                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            LinearLayout layout=(LinearLayout) findViewById(R.id.linear);

            try {
                JSONArray a1 = new JSONArray(s);

                for (int i=0; i<a1.length(); i++)
                {

                    JSONObject obj = a1.getJSONObject(i);

                    //now, get whatever value you need from the object:
                    id = obj.getString("id");
                    title=obj.getString("title");
                    //       desc=obj.getString("description");
                    //     date=obj.getString("date");
                    //   filename=obj.getString("filename");

                    ht.put(id, title);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<String> v = new ArrayList<String>(ht.keySet());
            Collections.sort(v);

            tvData.setText("Fetching");

            final TextView[] myTextViews = new TextView[6]; // create an empty array;
            int i=0;
            for (String str : v){
                // create a new textview
                final TextView rowTextView = new TextView(getApplicationContext());

                // set some properties of rowTextView or something
                rowTextView.setText(str + " " + (String) ht.get(str));
                rowTextView.setTextColor(Color.BLACK);

                // add the textview to the linearlayout
                layout.addView(rowTextView);

                // save a reference to the textview for later
                myTextViews[i] = rowTextView;
                i++;
            }

        }



    }

}


