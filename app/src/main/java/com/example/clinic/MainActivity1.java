package com.example.clinic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {
  Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageView hamburger;
    NavigationView nav_view;
    private CircularImageView profileimage, profile;
    private TextView txt_firstname, txt_lastname, txt_specialization;
    private ExpandableListView elv;
    private DocAdapter docAdapter;
    private List<docnavitems> itemlist;
    Fragment fragment = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.Drawerlayout1);
        nav_view = findViewById(R.id.nav_view);
        hamburger = findViewById(R.id.hamburger);
        elv = findViewById(R.id.expandableListView);
        profile=findViewById(R.id.profile);




        hamburger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout. openDrawer(GravityCompat.START);
            }
        });

        View headerview = LayoutInflater.from(this).inflate(R.layout.nav_header, elv, false);
        elv.addHeaderView(headerview);
        itemlist = generateNavItemList();
        docAdapter = new DocAdapter(this, itemlist);
        elv.setAdapter(docAdapter);
        elv.setGroupIndicator(null);
        String imageuri = getIntent().getStringExtra("imageuri");
        String firstname = getIntent().getStringExtra("first");
        String lastname = getIntent().getStringExtra("last");
        String specialization = getIntent().getStringExtra("special");
        Uri profileuri = Uri.parse(imageuri);
        profileimage = headerview.findViewById(R.id.profileimage);
        txt_firstname = findViewById(R.id.txt_firstname);
        txt_lastname = findViewById(R.id.txt_lastname);
        txt_specialization = findViewById(R.id.txt_specialization);
        txt_firstname.setText(firstname);
        txt_lastname.setText(lastname);
        txt_specialization.setText(specialization);
        if(imageuri!=null && !imageuri.isEmpty()){
            Glide.with(this).load(imageuri).into(profileimage);
            Glide.with(this).load(imageuri).into(profile);
        }




        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(MainActivity1.this, v);
                menu.getMenuInflater().inflate(R.menu.profile_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.logout:
                              showConfirmDialog();
                                return true;
                            case R.id.settings:
                                Fragment fragment1 = new DoctorSettingFragment();
                                if(fragment1!=null){
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

                                }

                                return true;
                            default:
                                return false;
                        }

                    }
                });
                MenuItem logout = menu.getMenu().findItem(R.id.logout);
                MenuItem settings = menu.getMenu().findItem(R.id.settings);
                logout.setIcon(R.drawable.logout);
                settings.setIcon(R.drawable.set);
                menu.show();

            }
        });

        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition){
                    case 2:
                        fragment = new AppointmentDoctorFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;
                }
                if (fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
                return false;
            }
        });
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition){
                    case 0:
                        fragment = new DashBoardFragment();
                        break;
                    case 1:
                        switch (childPosition){
                            case 0:
                                fragment = new NewPrescriptionFragment();
                                break;
                            case 1:
                                fragment = new AllPrescriptionFragment();
                                break;
                        }
                        break;
                }
                if (fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });


    }


    private List<docnavitems> generateNavItemList(){
        List<docnavitems> navitems = new ArrayList<>();
        navitems.add(new docnavitems("DashBoard", R.drawable.dashboard1, null));

        List<docsubitems> subitems = new ArrayList<>();
        subitems.add(new docsubitems("New Prescription"));
        subitems.add(new docsubitems("All Prescriptions"));
        navitems.add(new docnavitems("Prescription", R.drawable.pres, subitems));
        navitems.add(new docnavitems("Appointments", R.drawable.appointment6, null));
        navitems.add(new docnavitems("Patients", R.drawable.patient1, null));

        navitems.add(new docnavitems("Calender", R.drawable.calendar1, null));
        return navitems;

    }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity1.this, LoginActivity.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getDocFirstName(){
        return txt_firstname.getText().toString();
    }

    public String getDoclastName(){
        return txt_lastname.getText().toString();
    }

    public String getSpecialization(){
        return txt_specialization.getText().toString();
    }
}