package ml.rishabhnayak.mpt.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ml.rishabhnayak.mpt.POJO.Success;
import ml.rishabhnayak.mpt.R;
import ml.rishabhnayak.mpt.utils.Speedometer;
import ml.rishabhnayak.mpt.utils.gpsutils.Constants;
import ml.rishabhnayak.mpt.utils.gpsutils.GPSCallback;
import ml.rishabhnayak.mpt.utils.gpsutils.GPSManager;
import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;



public class ActivityGps extends AppCompatActivity implements GPSCallback {
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;
    TextView tvSensor;
    Button btnSwitch;
    Speedometer speedometer;
    Context ctx;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String cid,message,velocity;
    SweetAlertDialog pDialog;

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    String result,d_id;
    EditText d_idE;
    @Override
    protected void onResume() {
        super.onResume();
        d_idE.setText(d_id);
        PermissionManager.with(ActivityGps.this)
                .permission(PermissionEnum.ACCESS_COARSE_LOCATION, PermissionEnum.ACCESS_FINE_LOCATION)
                .askagain(true)
                .askagainCallback(new AskagainCallback() {
                    @Override
                    public void showRequestPermission(UserResponse response) {
                        showDialog(response);
                    }
                })
                .callback(new FullCallback() {
                    @Override
                    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
                    }
                })
                .ask();
    }
    public void getValue() {
        d_id=d_idE.getText().toString();

    }
    public void setId() {
        d_idE=findViewById(R.id.d_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedometer);
        ctx=this;
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        cid=pref.getString("cid",null);
        initialiseElements();
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValue();
                if (d_id.length()==0){
                 d_idE.setError("Field must not empty.");
                }else {
                    loading("yes");
                    volley("http://139.59.66.55/mobiapp/speed");

                    message = ((EditText)findViewById(R.id.message)).getText().toString();
                    System.out.println("ihi haray................."+cid+velocity+message+d_id);
                }

            }
        });

      //  Toast.makeText(ctx, String.valueOf(speedKM), Toast.LENGTH_SHORT).show();
        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), QrScannerActivity.class));
                Intent i = new Intent(getApplicationContext(),QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
            }
        });
        setId();
    }

    @Override
    public void onGPSUpdate(Location location) {
        location.getLatitude();
        location.getLongitude();
        speed = location.getSpeed();

        Double speedKM=(roundDecimal(convertSpeed(speed), 2));
        velocity=String.valueOf(speedKM);
        speedometer.onSpeedChanged(speedKM.longValue());

        if (speedKM>50)
        {
            speedometer.setBackgroundColor(getResources().getColor(R.color.red));
//            tvSensor.setText("HIGH");
//            tvSensor.setTextColor(getResources().getColor(R.color.red));
        }
        else if (speedKM>25)
        {
//            tvSensor.setText("AVERAGE");
//            tvSensor.setTextColor(getResources().getColor(R.color.green));
            speedometer.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else
        {
//            tvSensor.setText("LOW");
//            tvSensor.setTextColor(getResources().getColor(R.color.blue));
            speedometer.setBackgroundColor(getResources().getColor(R.color.blue));
        }



    }


    private void initialiseElements()
    {
        gpsManager = new GPSManager(this);
        gpsManager.startListening(getApplicationContext());
        gpsManager.setGPSCallback(this);
        speedometer = (Speedometer) findViewById(R.id.speedometer);
     //   btnSwitch= (Button) findViewById(R.id.switch_activity);
    }
    @Override
    protected void onDestroy() {
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);

        gpsManager = null;

        super.onDestroy();
    }


    private double convertSpeed(double speed) {
        return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS);
    }

    private double roundDecimal(double value, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);

        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();

        return value;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    private void showDialog(final AskagainCallback.UserResponse response) {
        new AlertDialog.Builder(ActivityGps.this)
                .setTitle("Permission needed")
                .setMessage("This app realy need to use this permission, you wont to authorize it?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        response.result(true);
                    }
                })
                .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        response.result(false);
                    }
                })
                .show();
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

                        Toast.makeText(getApplicationContext(), "Not getting speed please retry ", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("cid",cid);
                map.put("speed",velocity);
                map.put("message",message);
                map.put("d_id",d_id);
                return map;
            }
        };

        queue.add(postRequest);
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
           // Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

}