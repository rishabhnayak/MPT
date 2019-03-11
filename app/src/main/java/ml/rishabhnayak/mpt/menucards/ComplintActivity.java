package ml.rishabhnayak.mpt.menucards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ml.rishabhnayak.mpt.POJO.Success;
import ml.rishabhnayak.mpt.QrScanner.QrScannerActivity;
import ml.rishabhnayak.mpt.R;

public class ComplintActivity extends AppCompatActivity {
    EditText nameE, emailE, subjectE, messageE,mobileE,d_idE;
    String name, email, subject, message,d_id;
    SweetAlertDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String cid,mobile;


    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setId();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        cid=pref.getString("cid",null);
        mobile=pref.getString("mobile",null);
        name=pref.getString("name",null);
        nameE.setText(name);
        mobileE.setText(mobile);


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
               // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                if(subject.length()==0){
                    subjectE.setError("This Field must not be empty");
                }else if(message.length()==0){
                    messageE.setError("This Field must not be empty");
                }else if(d_id.length()==0){
                    d_idE.setError("This Field must not be empty");
                }
                else {
                    volley("http://139.59.66.55/mobiapp/complaint");
                    loading("yes");
                    getValue();
                }

            }
        });
        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), QrScannerActivity.class));
                Intent i = new Intent(getApplicationContext(),QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
            }
        });
    }
    public void setId() {
        nameE = findViewById(R.id.name);
        emailE = findViewById(R.id.email);
        subjectE = findViewById(R.id.subject);
        messageE = findViewById(R.id.message);
        mobileE=findViewById(R.id.mobile_no);
        d_idE=findViewById(R.id.d_id);
    }
    public void getValue() {
        mobile=mobileE.getText().toString();
        name = nameE.getText().toString();
        email = emailE.getText().toString();
        subject=subjectE.getText().toString();
        message = messageE.getText().toString();
        d_id=d_idE.getText().toString();
    }
    public void volley(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("yhi hai response....." + response);
                        loading("no");
                        Gson gson=new Gson();
                        Success successObject=gson.fromJson(response,Success.class);
                        String success=successObject.getSuccess();
                        switch (success){
                            case "success":
                                successAlert();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Server Error Please Retry", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        loading("no");
                        Toast.makeText(getApplicationContext(), "Server Problem please retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("cid",cid);
                map.put("subject",subject);
                map.put("message",message);
                map.put("d_id",d_id);
            //    Toast.makeText(ComplintActivity.this, d_id, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        d_idE.setText(d_id);
        super.onResume();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
            Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                Toast.makeText(this, "No QRCode Found", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            System.out.println("ihi haray...."+result);
            d_id=result;
          //  Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
