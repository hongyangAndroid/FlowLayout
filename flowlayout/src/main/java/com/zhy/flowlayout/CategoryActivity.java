package com.zhy.flowlayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity
{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private String[] mTabTitles = new String[]
            {"Muli Selected", "Limit 3",
                    "Event Test", "ScrollView Test", "Single Choose", "Gravity", "ListView Sample","Limit Enable"};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int i)
            {
                switch (i)
                {
                    case 0:
                        return new SimpleFragment();
                    case 1:
                        return new LimitSelectedFragment();
                    case 2:
                        return new EventTestFragment();
                    case 3:
                        return new ScrollViewTestFragment();
                    case 4:
                        return new SingleChooseFragment();
                    case 5:
                        return new GravityFragment();
                    case 6:
                        return new ListViewTestFragment();
                    case 7:
                        return new LimitEnableFragment();
                    default:
                        return new EventTestFragment();

                }
            }

            @Override
            public CharSequence getPageTitle(int position)
            {

                return mTabTitles[position];
            }

            @Override
            public int getCount()
            {
                return mTabTitles.length;
            }
        });


        mTabLayout.setupWithViewPager(mViewPager);
    }


}
