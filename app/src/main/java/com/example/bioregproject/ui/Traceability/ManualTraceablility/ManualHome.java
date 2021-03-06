package com.example.bioregproject.ui.Traceability.ManualTraceablility;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bioregproject.Adapters.ManualTracedAdapater;
import com.example.bioregproject.DataBases.BioRegDB;
import com.example.bioregproject.R;
import com.example.bioregproject.Utils.StaticUse;
import com.example.bioregproject.entities.Products;
import com.example.bioregproject.ui.Traceability.ImageFlow.ManageDataViewModel;

public class ManualHome extends Fragment {

    private ManualHomeViewModel mViewModel;
    private ConstraintLayout constraintLayout;
    private Button addNew;
    private TextView tracedObj;
    private RecyclerView manual;
    private ConstraintLayout loading,empty;

    public static ManualHome newInstance() {
        return new ManualHome();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manual_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ManualHomeViewModel.class);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.mainMenu);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        addNew =view.findViewById(R.id.addNew);
        tracedObj = view.findViewById(R.id.traced);
        manual = view.findViewById(R.id.manual);

        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        empty = view.findViewById(R.id.empty);

        StaticUse.backgroundAnimator(constraintLayout);

        mViewModel.initAllTeams(BioRegDB.getInstance(getActivity()).productDAO());
        final ManualTracedAdapater adapter = new ManualTracedAdapater(getActivity(),getActivity());
        manual.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        manual.setAdapter(adapter);
        mViewModel.teamAllList.observe(
                getActivity(), new Observer<PagedList<Products>>() {
                    @Override
                    public void onChanged(PagedList<Products> pagedList) {
                        try {
                            if(pagedList.isEmpty())
                            {
                                empty.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);

                            }else
                            {
                            //Toast.makeText(getActivity(), ""+pagedList.get(0).getId(), Toast.LENGTH_SHORT).show();
                            Log.e("Paging ", "PageAll" + pagedList.size());
                            //refresh current list
                            adapter.submitList(pagedList);
                            tracedObj.setText("Traced items "+pagedList.size());
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        empty.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);
                                        handler.removeCallbacksAndMessages(null);
                                    }
                                }, 1000);
                            //adapter.submitList(pagedList);
                                }
                        } catch (Exception e) {
                        }
                    }
                });
        mViewModel.filterTextAll.
                setValue("ManuelT");

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_manualHome_to_formManual);

            }
        });


    }

}
