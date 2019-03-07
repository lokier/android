package com.rdm.base.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.rdm.common.ui.R;

/**
 * Created by Rao on 2015/1/25.
 */
public abstract class SingleFragmentActivity extends BaseActivity {


    protected abstract Fragment onCreateFragment(Bundle savedInstanceState);


    @Override
    protected void onCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_single_fragment);


        Fragment fragment = onCreateFragment(savedInstanceState);
        if(fragment == null){
            fragment = new LoadingFragment();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tran =  fm.beginTransaction();
        tran.add(R.id.main_content,fragment);
        tran.commit();

    }





}
