package protel.jahitin.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import protel.jahitin.R;

public class onBoarding extends AppCompatActivity implements View.OnClickListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    int page;
    ImageView indicator0, indicator1, indicator2;
    Button skip, previous, next, finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);

                switch (position) {
                    case 0:
                        skip.setVisibility(View.VISIBLE);
                        previous.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                        finish.setVisibility(View.GONE);
                        break;
                    case 1:
                        skip.setVisibility(View.GONE);
                        previous.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        finish.setVisibility(View.GONE);
                        break;
                    case 2:
                        skip.setVisibility(View.GONE);
                        previous.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);

        indicator0 = findViewById(R.id.intro_indicator_0);
        indicator1 = findViewById(R.id.intro_indicator_1);
        indicator2 = findViewById(R.id.intro_indicator_2);

        skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(this);
        previous = findViewById(R.id.previous_button);
        previous.setOnClickListener(this);
        next = findViewById(R.id.button_next);
        next.setOnClickListener(this);
        finish = findViewById(R.id.button_finish);
        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(onBoarding.this, Beranda.class);
        switch (v.getId()) {
            case R.id.skip_button:
                startActivity(intent);
                finish();
                break;
            case R.id.previous_button:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                break;
            case R.id.button_next:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                break;
            case R.id.button_finish:
                startActivity(intent);
                finish();
                break;
        }
    }

    private void updateIndicators(int num) {
        switch (num) {
            case 0:
                indicator0.setBackgroundColor(Color.parseColor("#f44336"));
                indicator1.setBackgroundColor(Color.parseColor("#ffcbc5"));
                indicator2.setBackgroundColor(Color.parseColor("#ffcbc5"));
                break;
            case 1:
                indicator0.setBackgroundColor(Color.parseColor("#ffcbc5"));
                indicator1.setBackgroundColor(Color.parseColor("#f44336"));
                indicator2.setBackgroundColor(Color.parseColor("#ffcbc5"));
                break;
            case 2:
                indicator0.setBackgroundColor(Color.parseColor("#ffcbc5"));
                indicator1.setBackgroundColor(Color.parseColor("#ffcbc5"));
                indicator2.setBackgroundColor(Color.parseColor("#f44336"));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_on_boarding, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        TextView title, deskripsi;
        ImageView gambar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_on_boarding, container, false);
            title = rootView.findViewById(R.id.title);
            deskripsi = rootView.findViewById(R.id.deskripsi);
            gambar = rootView.findViewById(R.id.gambar);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    title.setText("Langsung Diantar ke Rumah");
                    deskripsi.setText("Barang yang dipesan akan \nlangsung diantarkan pada \nalamat yang dituju");
                    gambar.setImageResource(R.drawable.icon_1);
                    break;
                case 2:
                    title.setText("Validitas Data Dari Toko");
                    deskripsi.setText("Terdapat informasi, rating \ndan ulasan mengenai \ntoko yang dipilih");
                    gambar.setImageResource(R.drawable.icon_2);
                    break;
                case 3:
                    title.setText("Custom Pakaian yang Diinginkan");
                    deskripsi.setText("Lakukan custom dari pakaian \nyang diinginkan dan lihat \nperkiraan harga nya");
                    gambar.setImageResource(R.drawable.icon_3);
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
