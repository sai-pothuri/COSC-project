package com.example.cosc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Response;

import static okhttp3.RequestBody.create;


public class MainActivity extends AppCompatActivity {
    static String postUrl = "http://cbit-qp-api.herokuapp.com/get-subjects";//url for sending branch details go here//
    public static String text1, text2, text3, text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner1 = findViewById(R.id.branchsp);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setPrompt("select branch");
        text1 = spinner1.getSelectedItem().toString();


        Spinner spinner2 = (Spinner) findViewById(R.id.semestersp);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.semester, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setPrompt("select semester");
        try {
            text2 = spinner2.getSelectedItem().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Spinner spinner3 = (Spinner) findViewById(R.id.examtypesp);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.examtype, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setPrompt("select exam-type");
        text3 = spinner3.getSelectedItem().toString();

        Spinner spinner4 = (Spinner) findViewById(R.id.subtypesp);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.subtype, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setPrompt("select sub-type");
        text4 = spinner4.getSelectedItem().toString();
        try {
            if (text1.length() == 0 || text2.length() == 0 || text3.length() == 0 || text4.length() == 0) {
                Toast.makeText(getApplicationContext(), "Something is wrong. Please check your inputs.", Toast.LENGTH_LONG).show();
            } else {
                JSONObject details = new JSONObject();
                try {
                    details.put("branch_name", text1);
                    details.put("sem_no", text2);
                    details.put("exam_type", text3);
                    details.put("subtype", text4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = create(MediaType.parse("application/json; charset=utf-8"), details.toString());
                postRequest(postUrl, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button button1 = (Button) findViewById(R.id.viewbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
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
            public void onFailure( Call call, IOException e) {
                call.cancel();
                Log.d("FAIL", Objects.requireNonNull(e.getMessage()));
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
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            if (responseString.equals("success")) {
                                responseTextRegister.setText("");
                                finish();
                            }
                            else {
                                responseTextRegister.setText(responseString);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);

    }
}