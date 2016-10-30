package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.component.TestComponent;
import com.ua.erent.module.core.presentation.mvp.core.IBaseView;
import com.ua.erent.module.core.presentation.mvp.core.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ITestPresenter;
import com.ua.erent.module.core.presentation.mvp.view.util.ViewPagerAdapter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public final class MainActivity extends InjectableActivity<MainActivity, ITestPresenter>
        implements IBaseView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;
    @BindView(R.id.category_tabs)
    protected TabLayout tabLayout;

    private ViewPagerAdapter pagerAdapter;

    public MainActivity() {
        super(R.layout.activity_main, TestComponent.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Recent items");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        tabLayout.setupWithViewPager(viewPager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new RecentGoodsFragment());
        viewPager.setAdapter(pagerAdapter);
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

}
