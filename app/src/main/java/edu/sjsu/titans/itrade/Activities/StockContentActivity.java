package edu.sjsu.titans.itrade.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import edu.sjsu.titans.itrade.Fragments.HistoricalChartFragment;
import edu.sjsu.titans.itrade.Fragments.StockNewsFragment;
import edu.sjsu.titans.itrade.Fragments.StockDetailFragment;
import edu.sjsu.titans.itrade.R;

/**
 * Created by pavan on 5/5/2017.
 */

public class StockContentActivity extends AppCompatActivity {

    public String symbol;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MenuItem addToFavoriteMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_content);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String symbol = extra.getString(getString(R.string.symbol_key));
            this.symbol = symbol == null ? null : symbol.toUpperCase();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.stockDetailViewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.stockDetailTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stock_content, menu);
        this.addToFavoriteMenuItem = menu.findItem(R.id.action_add_to_favorite);
        setFavoriteMenuItemIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_favorite:
                favoriteMenuItemButtonTapped();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavoriteMenuItemIcon() {
        if (symbol != null && !symbol.equals("")) {
            TreeSet<String> favorites = new TreeSet<>(getSharedPreferences(getString(R.string.default_shared_preferences_key), MODE_PRIVATE)
                    .getStringSet(getString(R.string.favorites_key), new HashSet<String>()));
            if (favorites.contains(symbol)) {
                addToFavoriteMenuItem.setIcon(R.mipmap.ic_star);
            }
        }
    }

    private void favoriteMenuItemButtonTapped() {
        if (symbol != null && !symbol.equals("")) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.default_shared_preferences_key), MODE_PRIVATE);
            TreeSet<String> favorites = new TreeSet<>(sharedPreferences
                    .getStringSet(getString(R.string.favorites_key), new TreeSet<String>()));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (favorites.contains(symbol)) {
                favorites.remove(symbol);
                addToFavoriteMenuItem.setIcon(R.mipmap.ic_star_border);
            } else {
                favorites.add(symbol);
                addToFavoriteMenuItem.setIcon(R.mipmap.ic_star);
            }
            editor.putStringSet(getString(R.string.favorites_key), favorites);
            editor.apply();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StockDetailFragment(), "CURRENT");
        adapter.addFragment(new HistoricalChartFragment(), "HISTORICAL");
        adapter.addFragment(new StockNewsFragment(), "NEWS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
