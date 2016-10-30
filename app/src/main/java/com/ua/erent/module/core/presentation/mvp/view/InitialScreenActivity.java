package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.InitialScreenComponent;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IInitialScreenPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IInitialScreenView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Максим on 10/28/2016.
 */

public final class InitialScreenActivity extends InjectableActivity<InitialScreenActivity, IInitialScreenPresenter>
        implements IInitialScreenView, IInitialScreenView.NavigationListener {

    @BindView(R.id.app_bar)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;
    @BindView(R.id.category_tabs)
    protected TabLayout tabLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private ViewPagerAdapter pagerAdapter;

    /**
     * Custom fragment adapter
     */
    private static final class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragmentList;

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            fragmentList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    public InitialScreenActivity() {
        super(R.layout.activity_initial, InitialScreenComponent.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout.setExpanded(false);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        tabLayout.setupWithViewPager(viewPager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                hideKeyboard();

                if (tab.getPosition() != 0) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                } else {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowHomeEnabled(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {

        if (tabLayout.getSelectedTabPosition() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCreateAccount() {
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onRestorePassword() {

    }

    @Override
    public void onLogin() {
        viewPager.setCurrentItem(0, true);
    }

    private void hideKeyboard() {

        final View view = getCurrentFocus();

        if (view != null) {

            final InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}