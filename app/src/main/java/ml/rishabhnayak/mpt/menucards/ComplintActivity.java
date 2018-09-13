package ml.rishabhnayak.mpt.menucards;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ml.rishabhnayak.mpt.R;

public class ComplintActivity extends AppCompatActivity {
    EditText nameE, emailE, subjectE, messageE;
    String name, email, subject, message;
    Button submit;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setId();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                volley("https://httpbin.org/get");
                loading("yes");
                getValue();
            }
        });
    }
    public void setId() {
        nameE = findViewById(R.id.name);
        emailE = findViewById(R.id.email);
        subjectE = findViewById(R.id.subject);
        messageE = findViewById(R.id.message);
        submit = findViewById(R.id.submit);

    }
    public void getValue() {
        name = nameE.getText().toString();
        email = emailE.getText().toString();
        subject=subjectE.getText().toString();
        message = messageE.getText().toString();
    }
    public void volley(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        loading("no");
                        successAlert();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        loading("no");
                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> map=new HashMap<>();
//
//                return map;
//            }
        };

        queue.add(postRequest);
    }
    public void loading(String show) {
        if (show == "yes") {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#0ab184"));
            pDialog.setTitleText("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        } else {
            pDialog.cancel();
        }
    }
   public void successAlert(){
     final SweetAlertDialog sweetAlertDialog=  new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
              sweetAlertDialog.setTitleText("Sent Successfully");
              sweetAlertDialog.show();
       Handler handler=new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               sweetAlertDialog.cancel();
               finish();
           }
       },1500);
   }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
