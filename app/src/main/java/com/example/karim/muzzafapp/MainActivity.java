package com.example.karim.muzzafapp;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.karim.muzzafapp.Activites.Durations;
import com.example.karim.muzzafapp.Activites.ImagesAct;
import com.example.karim.muzzafapp.Activites.newoffer;
import com.example.karim.muzzafapp.Activites.timerList;
import com.example.karim.muzzafapp.Fragment.AddOrder;
import com.example.karim.muzzafapp.Fragment.MapFragment;
import com.example.karim.muzzafapp.Fragment.OrderList;
import com.example.karim.muzzafapp.Model.PlaceData;
import com.example.karim.muzzafapp.Model.TimerData;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {





    public void  openImage(View view){
        startActivity(new Intent(this,ImagesAct.class));
    }
    ViewPager vp;
    private void __init__() {
//        tab=findViewById(R.id.tb);
//        vp=findViewById(R.id.vp);
//        vp.setAdapter(new PagerAdapter(getSupportFragmentManager()));
//        tab.setupWithViewPager(vp);
//        tab.getTabAt(0).setText("Order List");
//        tab.getTabAt(1).setText("New Order");
//        tab.getTabAt(2).setText("Map");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w=getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
            __init__();



    }


//    public class  PagerAdapter extends FragmentPagerAdapter {
//
//        public PagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position){
//                case 2:
//                    return new MapFragment();
//                case 1:
//                    return new AddOrder();
//                case 0:
//                    default:
//                    return new OrderList();
//            }
//        }
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }


}
