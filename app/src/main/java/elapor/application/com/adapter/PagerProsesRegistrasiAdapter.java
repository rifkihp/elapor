package elapor.application.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import elapor.application.com.fragment.RegistrasiFormFragment;
import elapor.application.com.fragment.RegistrasiSuccessFragment;

public class PagerProsesRegistrasiAdapter extends FragmentStatePagerAdapter {

    public PagerProsesRegistrasiAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RegistrasiFormFragment registrasiFormFragment = new RegistrasiFormFragment();
                return registrasiFormFragment;

            case 1:
                RegistrasiSuccessFragment registrasiSuccessFragment = new RegistrasiSuccessFragment();
                return registrasiSuccessFragment;

            default:
                return null;

        }


    }

    @Override
    public int getCount() {
        return 2;
    }
}