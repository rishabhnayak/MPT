package ml.rishabhnayak.mpt;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    EditText name,mobile,otp;
    String userName,userMobile,userOtp;
    Button register,verify;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile_no);
        otp=findViewById(R.id.otp);
        register=findViewById(R.id.register);
        verify=findViewById(R.id.verify_otp);
        getSupportActionBar().hide();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=name.getText().toString();
                userMobile=mobile.getText().toString();
                if (userName.length()==0){
                    name.setError("Please enter user name");
                }
                else if (mobile.getText()==null||userMobile.length()!=10){
                    mobile.setError("Please enter valid mobile no.");
                }
                else{
                    Toast.makeText(RegisterActivity.this, userName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, userMobile, Toast.LENGTH_SHORT).show();
                    volley("https://httpbin.org/get");
                    loading("yes");
                }

            }
        });


    }
    public void volley(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        loading("no");

                        name.setEnabled(false);
                        mobile.setEnabled(false);

                        register.setVisibility(View.GONE);
                        findViewById(R.id.otp_layout).setVisibility(View.VISIBLE);
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(RegisterActivity.this, "working", Toast.LENGTH_SHORT).show();
                                userOtp=otp.getText().toString();
                                if (userOtp.length()!=0){
                                    loading("yes");
                                    volleyOTP("https://httpbin.org/get");
                                }else {
                                  otp.setError("Please Enter OTP.");

                                }
                            }
                        });
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

    public void volleyOTP(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        loading("no");
                        startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();

                return map;
            }
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
}
