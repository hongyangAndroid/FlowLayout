package com.zhy.flowlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description：GravityFragment
 * Created by：CaMnter
 * Time：2015-12-25 16:11
 */
public class GravityFragment extends Fragment {

    public static GravityFragment ourInstance;

    public static GravityFragment getOurInstance() {
        if (ourInstance == null) ourInstance = new GravityFragment();
        return ourInstance;
    }

    private View self;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.self == null) {
            this.self = inflater.inflate(R.layout.fragment_gravity_flow_layout, null);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
        return this.self;
    }
}
