package ml.rishabhnayak.mpt.menucards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ml.rishabhnayak.mpt.R;

public class ComplaintHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView1;
    LinearLayoutManager manager1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        manager1=new LinearLayoutManager(this);
        recyclerView1=findViewById(R.id.userlist);
        recyclerView1.setLayoutManager(manager1);
        searchlistvolley();
    }
    public void searchlistvolley(){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        String  url = "http://online-exam.dx.am/get_detail.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ihi haray....." + response);
                        try
                        {
//                            DataPojo[] dataPOJO = gson.fromJson(response, DataPojo[].class);
//                            recyclerView1.setAdapter(new AdapterList(getApplicationContext(),dataPOJO));
                        }catch (Exception e){
                            System.out.println("empty");
                        }
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error"+error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                //agar post karna hai to isme key and value bas dal dena isko abhi comment bna rha hu

                // map.put("key","value");

                return map;
            }
        };

        queue.add(postRequest);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
