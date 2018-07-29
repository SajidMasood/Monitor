package com.mr_abdali.monitor.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mr_abdali.monitor.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sadiq on 18/05/2018.
 */

public class FragmentCallogs extends Fragment {
    private RecyclerView recyclerView;
    private View v;
    public FragmentCallogs(){
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_callog,container, false) ;
        recyclerView= (RecyclerView) v.findViewById(R.id.rv_callog);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager=linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);
        return v;
    }

}
