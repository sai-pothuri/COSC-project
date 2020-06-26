package com.example.cosc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {
    User date;
    Button date_button;
    String Request_no;
    String postUrl2 = "";//url for sending request_no goes here//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        fetchData process = new fetchData();
        process.execute();
        date_button = findViewById(R.id.date_btn);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });
        //spinner//
        Spinner spinner_date = (Spinner) findViewById(R.id.spinner_date);
        List<User> date_list = new ArrayList<>();
        for (int i = 0; i < fetchData2.dates.length; i++) {
            date = new User(fetchData2.dates[i]);
            date_list.add(date);
        }
        ArrayAdapter<User> adapter_date = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, date_list);
        adapter_date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_date.setAdapter(adapter_date);
        spinner_date.setPrompt("select date");
        String text_date = spinner_date.getSelectedItem().toString();
        for (int j = 0; j < fetchData2.dates.length; j++) {
            if (fetchData2.dates[j].equals(text_date)) {
                Request_no = fetchData2.request_no[j];
            }
        }
        //sending request no from here//
        if (text_date.length() == 0) {
            Toast.makeText(getApplicationContext(), "Something is wrong. Please check your inputs.", Toast.LENGTH_LONG).show();
        } else {
            JSONObject details = new JSONObject();
            try {
                details.put("request_no", Request_no);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), details.toString());
            postRequest(postUrl2, body);

        }
    }
    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("FAIL", e.getMessage());
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.errortext);
                        responseText.setText("Failed to Connect to Server. Please Try Again.");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final TextView responseTextRegister = findViewById(R.id.errortext);
                try {
                    final String responseString = response.body().string().trim();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseString.equals("success")) {
                                responseTextRegister.setText("");
                                finish();
                            } else {
                                responseTextRegister.setText("Something went wrong. Please try again later.");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void openActivity4() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);

    }
}


