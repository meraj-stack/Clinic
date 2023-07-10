package com.example.clinic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageView hamburger;
    NavigationView nav_view;
    private CircularImageView profileimage, profile;
    private TextView txt_firstname, txt_lastname;
    private ExpandableListView elv;
    private AdminAdapter adapter;
    private List<adnavitems> itemList;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout2);
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

        View headerview = LayoutInflater.from(this).inflate(R.layout.nav_header2, elv, false);
        elv.addHeaderView(headerview);
        itemList = generateNavItemList();
        adapter = new AdminAdapter(this, itemList);
        elv.setAdapter(adapter);
        elv.setGroupIndicator(null);
        String image = getIntent().getStringExtra("image");
        String firstname = getIntent().getStringExtra("firstname");
        String lastname = getIntent().getStringExtra("lastname");
        profileimage = headerview.findViewById(R.id.profileimage);
        txt_firstname = findViewById(R.id.txt_firstname);
        txt_lastname = findViewById(R.id.txt_lastname);
        txt_firstname.setText(firstname);
        txt_lastname.setText(lastname);
        if(image!=null && !image.isEmpty()){
            Glide.with(this).load(image).into(profileimage);
            Glide.with(this).load(image).into(profile);
        }


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(MainActivity2.this, v);
                menu.getMenuInflater().inflate(R.menu.profile_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                showConfirmDialog();
                                return true;
                            case R.id.settings:
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
                switch (groupPosition) {
                    case 2:
                        fragment = new AddDrugFragment();
                        break;
                    case 3:
                        fragment = new AddTestFragment();
                        break;
                    case 4:
                        fragment = new ReportFragment();
                        break;
                }
                if (fragment != null) {
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
                                fragment = new ReportFragment();
                                break;
                            case 1:
                                fragment = new AllDoctorFragment();
                                break;
                            case 2:
                                fragment = new DoctorsProfileFragment();
                                break;
                        }
                        break;
                    case 2:
                        fragment = new AddDrugFragment();
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

            private List<adnavitems> generateNavItemList() {
                List<adnavitems> navitems = new ArrayList<>();
                navitems.add(new adnavitems("DashBoard", R.drawable.dashboard1, null));

                List<adsubitems> doctors = new ArrayList<>();
                doctors.add(new adsubitems("Add Doctor"));
                doctors.add(new adsubitems("All Doctors"));
                doctors.add(new adsubitems("Doctor's Profile"));
                navitems.add(new adnavitems("Doctors", R.drawable.doctors, doctors));

                navitems.add(new adnavitems("Add Drugs", R.drawable.drugs1, null));


                navitems.add(new adnavitems("Add Test", R.drawable.tests, null));

                navitems.add(new adnavitems("Reports", R.drawable.reportss, null));

                navitems.add(new adnavitems("Settings", R.drawable.settings1, null));
                return navitems;


            }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want tyo logout");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity2.this, LoginActivity.class));
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


        }