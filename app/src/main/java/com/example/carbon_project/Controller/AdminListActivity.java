package com.example.carbon_project.Controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.carbon_project.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * The AdminListActivity class is an activity that displays a list of users, events, and facilities for an admin user.
 */
public class AdminListActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list_layout);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // Set up the adapter for ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Link TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Users");
                    break;
                case 1:
                    tab.setText("Events");
                    break;
                case 2:
                    tab.setText("Facilities");
                    break;
            }
        }).attach();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        /**
         * Constructs a new ViewPagerAdapter.
         * @param fragmentActivity
         */
        public ViewPagerAdapter(@NonNull AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
        }

        /**
         * Creates a new fragment based on the position.
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new AdminUserListFragment();
                case 1:
                    return new AdminEventListFragment();
                case 2:
                    return new AdminFacilityListFragment();
                default:
                    throw new IllegalStateException("Unexpected position: " + position);
            }
        }

        /**
         * Returns the number of tabs.
         * @return
         */
        @Override
        public int getItemCount() {
            return 3; // Number of tabs
        }
    }
}
