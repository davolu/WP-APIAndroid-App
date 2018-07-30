package davolusoft.davidoluyale.wordpressapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by David Oluyale on 6/24/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {

            return new PostFeedFragment();

        }
        else if(position == 1)
        {

            return new CategoryFragment();

        }

        else
            {
                 return new PostFeedFragment();
            }


    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position)
    {

        // Generate title based on item position
        switch (position) {
            case 0:
                return "Recent Posts";
            case 1:
                return "CATegory";
             default:
                return null;
        }
    }

}