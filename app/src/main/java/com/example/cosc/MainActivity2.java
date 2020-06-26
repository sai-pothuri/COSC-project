package com.example.cosc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity2 extends AppCompatActivity {
    Button subjectbtn;
    User sub;
    static String postUrl1 = "";//url for sending subject details goes here//
    public TextView errortext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fetchData process = new fetchData();
        process.execute();
        subjectbtn = findViewById(R.id.subjectbtn);
        subjectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
        //spinner//
        Spinner spinner_sub = (Spinner) findViewById(R.id.subjectsp);
        List<User> subject_list = new ArrayList<>();
        //add subjects array ka data into this spinner bro//
        for (int i = 0; i < fetchData.subjects.length; i++) {
            sub = new User(fetchData.subjects[i]);
            subject_list.add(sub);
        }
        ArrayAdapter<User> adapter_sub = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, subject_list);
        adapter_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter_sub);
        spinner_sub.setPrompt("select subject");
        String text_sub = spinner_sub.getSelectedItem().toString();

        //from here is the thing//
        if (text_sub.length() == 0) {
            Toast.makeText(getApplicationContext(), "Something is wrong. Please check your inputs.", Toast.LENGTH_LONG).show();
        } else {
            JSONObject details = new JSONObject();
            try {
                details.put("branch_name", MainActivity.text1);
                details.put("sem_no", MainActivity.text2);
                details.put("exam_type", MainActivity.text3);
                details.put("subtype", MainActivity.text4);
                details.put("subject_name", text_sub);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), details.toString());
            postRequest(postUrl1, body);
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

    public void openActivity3() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);

    }
}