package com.example.tulikacode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb;
    TextView total_t;
    ListView lv;
    int page=0,total_i;
    Button load;
    ArrayList<DataModel> arr_l=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load=findViewById(R.id.more);
        lv=findViewById(R.id.list_v);
        total_t=findViewById(R.id.total_count);
        pb=findViewById(R.id.pb);
        new JsonFetch().execute("https://hn.algolia.com/api/v1/search_by_date?tags=story&page="+page);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page=page+1;
                new JsonFetch().execute("https://hn.algolia.com/api/v1/search_by_date?tags=story&page="+page);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        total_t.setText("Total display items "+lv.getChildCount());
    }

    class JsonFetch extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            total_t.setVisibility(View.GONE);

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                HttpGet hp=new HttpGet(strings[0]);
                HttpClient hc=new DefaultHttpClient();
                HttpResponse res=hc.execute(hp);

                int status=res.getStatusLine().getStatusCode();
                if(status==200){
                    HttpEntity ent=res.getEntity();
                    String str_json= EntityUtils.toString(ent);
                    JSONObject jsonObject=new JSONObject(str_json);
                    JSONArray j_arr=jsonObject.getJSONArray("hits");
                    total_i=j_arr.length();
                    for(int i=0;i<j_arr.length();i++){
                        JSONObject job=j_arr.getJSONObject(i);
                        arr_l.add(new DataModel(job.getString("title"),job.getString("created_at")));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            pb.setVisibility(View.GONE);
            total_t.setVisibility(View.VISIBLE);


            CustomAdapter c_ada=new CustomAdapter(arr_l,MainActivity.this);
            lv.setAdapter(c_ada);

            super.onPostExecute(aBoolean);

        }
    }


}
