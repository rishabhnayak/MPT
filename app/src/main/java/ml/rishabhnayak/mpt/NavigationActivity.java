package ml.rishabhnayak.mpt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;


import ml.rishabhnayak.mpt.activities.ActivityGps;
import ml.rishabhnayak.mpt.menucards.ComplaintHistoryActivity;
import ml.rishabhnayak.mpt.menucards.ComplintActivity;
import ml.rishabhnayak.mpt.menucards.ReviewActivity;
import ml.rishabhnayak.mpt.menucards.TrackComplaintActivity;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        ((RelativeLayout)findViewById(R.id.review_card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ReviewActivity.class));
            }
        });

        ((RelativeLayout)findViewById(R.id.complaint_card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ComplintActivity.class));
            }
        });

        ((RelativeLayout)findViewById(R.id.trackcomplaint_card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), TrackComplaintActivity.class));
                Toast.makeText(NavigationActivity.this, "Coming Soon......", Toast.LENGTH_SHORT).show();
            }
        });

        ((RelativeLayout)findViewById(R.id.complainthistory_card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ComplaintHistoryActivity.class));
                Toast.makeText(NavigationActivity.this, "Coming Soon......", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            editor.clear();
            editor.commit();
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            return true;
        }
        else if(id==R.id.overSpeed){
            startActivity(new Intent(getApplicationContext(),ActivityGps.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.review) {
            startActivity(new Intent(getApplicationContext(), ReviewActivity.class));
        } else if (id == R.id.complaint) {
            startActivity(new Intent(getApplicationContext(), ComplintActivity.class));
        } else if (id == R.id.track_complaint) {
//            startActivity(new Intent(getApplicationContext(), TrackComplaintActivity.class));
            Toast.makeText(this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.complaint_history) {
//            startActivity(new Intent(getApplicationContext(), ComplaintHistoryActivity.class));
            Toast.makeText(this, "Coming Soon.....", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_overspeed) {
            startActivity(new Intent(getApplicationContext(), ActivityGps.class));
        }
        else if (id == R.id.nav_logout) {
          editor.clear();
          editor.commit();
          finish();
          startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
