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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import ml.rishabhnayak.mpt.R;

public class ReviewActivity extends AppCompatActivity {

    Button submit;
    EditText d_idE;
    SweetAlertDialog pDialog;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText message;
    String messageString;
    String cid,review,d_id;
    int selectedId;


    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setId();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        cid=pref.getString("cid",null);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d_id=d_idE.getText().toString();
                // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
              if (d_id.length()==0){
                  d_idE.setError("Please Enter Valid ID");
              }else {
                  volley("http://139.59.66.55/mobiapp/rating");
                  loading("yes");
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

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
    }
    public void setId() {
        submit = findViewById(R.id.submit);
        d_idE=findViewById(R.id.d_id);

    }
    public void volley(String url) {
        message=findViewById(R.id.message);
        selectedId=radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton)findViewById(selectedId);
        messageString=message.getText().toString();
      //  Toast.makeText(getApplicationContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
        review=radioButton.getText().toString();
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
                                Toast.makeText(ReviewActivity.this, "Server Error Please Retry", Toast.LENGTH_SHORT).show();
                        }
                       // Toast.makeText(ReviewActivity.this, d_id, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volley error" + error);
                        loading("no");
                        Toast.makeText(getApplicationContext(), "Server Error Please Retry", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("cid",cid);
                map.put("review",review );
                map.put("message",messageString);
                map.put("d_id",d_id);
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
        //    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        d_idE.setText(d_id);
        super.onResume();
    }
}
