package com.example.indexsity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editindex;
    private Button buttonsearch;
    private TextView cityname, longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editindex = findViewById(R.id.editindex);
        buttonsearch = findViewById(R.id.buttonsearch);
        cityname = findViewById(R.id.cityname);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);

        buttonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editindex.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this, R.string.noinput, Toast.LENGTH_LONG).show();
                else {
                    String index = editindex.getText().toString();
                    String url = "https://api.zippopotam.us/ru/" + index;

                    new GetData().execute(url);
                }
            }
        });
    }

    private class GetData extends AsyncTask<String, String, String> {

        protected void onProExecute() {
            super.onPreExecute();
            cityname.setText("Название города...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();

                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                cityname.setText("Долгота " + jsonObject.getJSONObject("places").getDouble("longitude"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cityname.setText(result);
        }
    }
}