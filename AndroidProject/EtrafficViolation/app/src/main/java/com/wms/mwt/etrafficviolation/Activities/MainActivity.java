package com.wms.mwt.etrafficviolation.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Fragments.DriverViolationsLogFragment;
import com.wms.mwt.etrafficviolation.Fragments.MainFragment;
import com.wms.mwt.etrafficviolation.Fragments.VehiclesLogFragment;
import com.wms.mwt.etrafficviolation.Fragments.ViolationsFragment;
import com.wms.mwt.etrafficviolation.Fragments.ViolationsLogFragment;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static FloatingActionButton fabSearch,fabAddViolationType,fabAddViolationLog;
    private Toolbar toolbar;
    boolean close=false;
    private ViewPager viewPager;
    SessionManager sessionManager;
    private TabLayout tabLayout;
    private int[]tabIcons=new int[]{
            R.drawable.ic_violations,
            R.drawable.ic_violations_log,
            R.drawable.ic_vehicles};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        sessionManager=SessionManager.getInstance(getApplicationContext());
        sessionManager.checkLogout();



       viewPager=findViewById(R.id.viewpager);
       setupViewPager(viewPager);

        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        fabAddViolationLog=findViewById(R.id.fabAddViolationLog);
        fabAddViolationType=findViewById(R.id.fabAddViolationType);
        fabSearch=findViewById(R.id.fabSearch);

        if(!sessionManager.isAdmin()){
            fabSearch.setVisibility(View.GONE);
            fabAddViolationLog.setVisibility(View.GONE);
            fabAddViolationType.setVisibility(View.GONE);
        }

        setupFabs();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setupFabs(){

            fabAddViolationLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddViolationLogActivity.class);
                    startActivity(intent);
                }
            });

            fabAddViolationType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddViolationActivity.class);
                    startActivity(intent);
                }
            });

            fabSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            });



    }

    public static void customizeFab(int state){
        switch (state){
            case ViolationsFragment.VIOLATIONS_CASE:
            {
                fabAddViolationType.setVisibility(View.VISIBLE);
                fabAddViolationLog.setVisibility(View.GONE);
                break;
            }
            case ViolationsLogFragment.VIOLATIONS_LOG_CASE:
            {
                fabAddViolationType.setVisibility(View.GONE);
                fabAddViolationLog.setVisibility(View.VISIBLE);
                break;
            }
            case VehiclesLogFragment.VEHICLES_CASE:
            {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(close) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }else{
            close=true;
            Toast.makeText(this,"press again to exit",Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPgerAdapter adapter=new ViewPgerAdapter(getSupportFragmentManager());
        if(sessionManager.isAdmin()) {
            adapter.addFragment(new ViolationsFragment(), "Violations");


            adapter.addFragment(new ViolationsLogFragment(), "Violations Log");

            adapter.addFragment(new VehiclesLogFragment(), "Vehicles Log");
        }else{
            adapter.addFragment(new DriverViolationsLogFragment(), "Violations Log");
        }

        viewPager.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemLogout:sessionManager.logout();

        }
        return true;

    }


    private void setupTabIcons(){
        if(sessionManager.isAdmin()) {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);

            //  }

            tabLayout.getTabAt(1).setIcon(tabIcons[1]);

            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }else{
            tabLayout.getTabAt(0).setIcon(tabIcons[1]);
        }

    }

    class ViewPgerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragmentList=new ArrayList<Fragment>();
        private final List<String>fragmentTitleList=new ArrayList<String>();

        public ViewPgerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
