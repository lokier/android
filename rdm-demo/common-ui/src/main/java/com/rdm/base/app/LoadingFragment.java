package com.rdm.base.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rdm.base.app.view.TipView;

public class LoadingFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TipView tipView = new TipView(getActivity());

        tipView.showLoadingTip("正在加载");

        return tipView;
    }

}
