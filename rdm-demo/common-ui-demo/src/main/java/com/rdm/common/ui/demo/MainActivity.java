package com.rdm.common.ui.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rdm.common.Debug;
import com.rdm.common.ILog;
import com.rdm.common.util.SdcardUtils;

import java.io.File;
import java.util.Map;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
  //  private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static Map gDemoData;
    private static String gName;

    public static  void launch(Context context,String name,Map demoData){

        gDemoData = demoData;
        gName = name;
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        DemoActivityListAdpter adpter = new DemoActivityListAdpter(this);

        Map demoData = gDemoData;
        String name = gName;
        gDemoData = null;
        if(demoData == null) {
            name = "Main Demo";
            demoData = DemoData.getDemoData();
        }
        adpter.setDemoData(demoData);
        listView.setAdapter(adpter);

        setTitle(name);


        int memoryClass = getMemoryClass(getApplicationContext());
        long maxMemory = Runtime.getRuntime().maxMemory();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getApplicationContext());
        builder.denyCacheImageMultipleSizesInMemory();
        int maxSize = memoryClass * 1024 * 1024 / 10;
        builder.memoryCache(new LargestLimitedMemoryCache(maxSize));

        File imageCacheDir = new File(SdcardUtils.getSDFile(),"wssdfw");

        builder.discCache(new UnlimitedDiskCache(imageCacheDir));
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
     //   ILog.i(tag, String.format("memory:%d,max:%d,img_load_max:%d", memoryClass, maxMemory, maxSize / (1024 * 1024)));

        DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
        optionsBuilder.cacheInMemory(true);
        optionsBuilder.cacheOnDisc(true);

        BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
        decodingOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        optionsBuilder.decodingOptions(decodingOptions);
        builder.defaultDisplayImageOptions(optionsBuilder.build());
        if (Debug.isDebug()) {
            builder.writeDebugLogs();
        }
        ImageLoader.getInstance().init(builder.build());

    }

    private static int getMemoryClass(Context context) {
        return ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
