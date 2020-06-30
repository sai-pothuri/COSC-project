package com.example.cosc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity2 extends AppCompatActivity {
    Button subjectbtn;
    User sub;
    static String postUrl1 = "http://cbit-qp-api.herokuapp.com/get-yearwise";//url for sending subject details goes here//
    public TextView errortext;
    private RequestQueue mQueue;
    public static String text_sub;
    public static ArrayList<String> dates;
    public static ArrayList<String> request_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dates = new ArrayList<>();
        request_no = new ArrayList<>();
        subjectbtn = findViewById(R.id.subjectbtn);
        errortext = findViewById(R.id.errortext);
        subjectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonparse();
                openActivity3();
            }
        });
        //spinner//
        Spinner spinner_sub = (Spinner) findViewById(R.id.subjectsp);
        List<User> subject_list = new ArrayList<>();
        //add subjects array ka data into this spinner bro//
        try {
            for (int i = 0; i < MainActivity.subjects.size(); i++) {
                sub = new User(MainActivity.subjects.get(i));
                subject_list.add(sub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<User> adapter_sub = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, subject_list);
        adapter_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter_sub);
        spinner_sub.setPrompt("select subject");
        try {
            spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                    text_sub = adapterView.getItemAtPosition(p).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jsonparse(){
        JsonArrayRequest request= new JsonArrayRequest(Request.Method.GET, modified_url(postUrl1), null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length() && i<4; i++) {
                                JSONObject JO = response.getJSONObject(i);
                                dates.add(JO.getString("date"));
                                request_no.add(JO.getString("request_no"));

                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(request);
    }
    public static String modified_url(String url){
        if(!url.endsWith("?"))
            url += "?";
        List<NameValuePair> params = new LinkedList<NameValuePair>();

        if (text_sub != null){
            params.add(new BasicNameValuePair("branch_name", MainActivity.text1));
            params.add(new BasicNameValuePair("sem_no", MainActivity.text2));
            params.add(new BasicNameValuePair("exam_type", MainActivity.text3));
            params.add(new BasicNameValuePair("subtype", MainActivity.text4));
            params.add(new BasicNameValuePair("subject_name", text_sub));
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        return url;
    }

    public void openActivity3() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);

    }
}