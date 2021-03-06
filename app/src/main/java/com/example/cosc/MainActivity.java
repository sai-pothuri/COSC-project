package com.example.cosc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    static String postUrl = "http://cbit-qp-api.herokuapp.com/get-subjects";//url for sending branch details go here//
    public String text1, text2, text3, text4;
    public static String Text1, Text2, Text3, Text4;
    public Spinner spinner1, spinner2, spinner3, spinner4;
    public static String text;
    private RequestQueue mQueue;
    public static ArrayList<String> subjects = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1 = findViewById(R.id.branchsp);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setPrompt("select branch");
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                spinner1.setSelection(p, false);
                text1 = adapterView.getItemAtPosition(p).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner2 = (Spinner) findViewById(R.id.semestersp);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.semester, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setPrompt("select semester");
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                spinner2.setSelection(p, false);
                text2 = adapterView.getItemAtPosition(p).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner3 = (Spinner) findViewById(R.id.examtypesp);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.examtype, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setPrompt("select exam-type");
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                spinner3.setSelection(p, false);
                text3 = adapterView.getItemAtPosition(p).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        spinner4 = (Spinner) findViewById(R.id.subtypesp);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.subtype, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setPrompt("select sub-type");
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                spinner4.setSelection(p, false);
                text4 = adapterView.getItemAtPosition(p).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button button1 = findViewById(R.id.viewbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Text1 = text1;
                Text2 = text2;
                Text3 = text3;
                Text4 = text4;
                jsonparse();
            }
        });
    }
    public void jsonparse(){
        text = "red";
        JsonArrayRequest request= new JsonArrayRequest(Request.Method.GET, modified_url(postUrl), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject JO = response.getJSONObject(i);
                                String s = JO.getString("subject_name");
                                subjects.add(s);
                                openActivity2();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );

        mQueue = Volley.newRequestQueue(MainActivity.this);
        mQueue.add(request);
    }
    public static String modified_url(String url){
        if(!url.endsWith("?"))
            url += "?";
        List<NameValuePair> params = new LinkedList<NameValuePair>();


        if (Text1 != null && Text2 != null){
            params.add(new BasicNameValuePair("branch_name", Text1));
            params.add(new BasicNameValuePair("sem_no", Text2));
            params.add(new BasicNameValuePair("exam_type", Text3));
            params.add(new BasicNameValuePair("subtype", Text4));
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        return url;
    }
    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}