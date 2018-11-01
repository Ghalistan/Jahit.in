package protel.jahitin.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import protel.jahitin.R;

public class PembelianPagerAdapater extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    private Context context;

    public PembelianPagerAdapater(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.pakaian_custom_title);
            case 1:
                return context.getString(R.string.pakaian_jadi_title);
        }
        return null;
    }

    public void addFrag(Fragment fragment){
        fragmentList.add(fragment);
    }
}
