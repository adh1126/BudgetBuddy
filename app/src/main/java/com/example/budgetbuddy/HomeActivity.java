package com.example.budgetbuddy;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.HomeBottomNavBar);
        frameLayout = findViewById(R.id.HomeFrame);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navAdd){
                    loadFragment(new AddFragment(),false);
                } else if (itemId == R.id.navAnalysis) {
                    loadFragment(new AnalysisFragment(),false);
                } else{
                    loadFragment(new ProfileFragment(),false);
                }
                return true;
            }
        });
        loadFragment(new AnalysisFragment(),true);
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized)
            fragmentTransaction.add(R.id.HomeFrame,fragment);
        else
            fragmentTransaction.replace(R.id.HomeFrame,fragment);
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();// Go back to the previous fragment
        } else {
            super.onBackPressed(); // Exit the app if there's no fragment to go back to
        }
    }

}