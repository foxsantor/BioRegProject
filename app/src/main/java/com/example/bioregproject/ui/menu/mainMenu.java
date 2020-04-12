package com.example.bioregproject.ui.menu;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bioregproject.Adapters.MenuAdapter;
import com.example.bioregproject.R;
import com.example.bioregproject.Utils.StaticUse;
import com.example.bioregproject.entities.MenuItems;

import java.util.ArrayList;

public class mainMenu extends Fragment {

    private MainMenuViewModel mViewModel;
    private GridView gridView;
    private  boolean exit = false;
    private ArrayList<MenuItems>  categoryItems;
    private MenuAdapter categoryAdapter;

    public static mainMenu newInstance() {
        return new mainMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(exit)
                {
                    StaticUse.clearShared(StaticUse.SHARED_NAME_USER_LOG,getActivity());
                    getActivity().finish();
                    System.exit(0);
                }

                exit = true;
                Toast.makeText(getActivity(), "Click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        exit =false;
                    }
                }, 2000);

            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        gridView = view.findViewById(R.id.grid);
        categoryItems = new ArrayList<>();
        categoryItems = Populater();
        categoryAdapter = new MenuAdapter(getActivity(),categoryItems);
        gridView.setAdapter(categoryAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                MenuItems cat = categoryItems.get(position);
                Bundle data = new Bundle();
                data.putString("key",cat.getName());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(cat.getDestination(),data);
            }
        });

    }

    private ArrayList<MenuItems> Populater()
    {
        ArrayList<MenuItems> categoryItems = new ArrayList<>();
        categoryItems.add(new MenuItems("cleaning and disinfection",R.drawable.cleanning,R.id.cleaning));
        categoryItems.add(new MenuItems("Oil Control",R.drawable.oilcontrol,R.id.oliControl));
        categoryItems.add(new MenuItems("Product Traceability",R.drawable.trac,R.id.products));
        categoryItems.add(new MenuItems("Task Planification ",R.drawable.task,R.id.taskPlan));
        categoryItems.add(new MenuItems("Printing Labels",R.drawable.printing,R.id.labels));
        categoryItems.add(new MenuItems("Refrigerators temperature",R.drawable.ref,R.id.refrigerato_temperature));
        categoryItems.add(new MenuItems("Products and Services temperature",R.drawable.serv,R.id.refrigerato_temperature));
        categoryItems.add(new MenuItems("Storage Control",R.drawable.stor,R.id.storageControl));
        categoryItems.add(new MenuItems("History and logs",R.drawable.logs,R.id.history));
        categoryItems.add(new MenuItems("Documents and Sheets Management",R.drawable.docu,R.id.documents));
        return categoryItems;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        // TODO: Use the ViewModel
    }

}