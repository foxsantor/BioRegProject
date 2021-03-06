package com.example.bioregproject.ui.OilControl;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bioregproject.Adapters.PostAdapter;
import com.example.bioregproject.Adapters.SpinnerCatAdapter;
import com.example.bioregproject.MainActivityViewModel;
import com.example.bioregproject.R;
import com.example.bioregproject.Utils.StaticUse;
import com.example.bioregproject.entities.Oil;
import com.example.bioregproject.entities.Post;
import com.example.bioregproject.ui.History.DeviceHistoryViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class oilAjout extends Fragment  {
    private static OliControlViewModel mViewModel;
    private Spinner spinner;
    private TextInputLayout textInputLayout ,textInputLayout4;
    private Button save,cancel;
    private Button calender;
    private EditText date;
    private PostAdapter postAdapter;
    private MainActivityViewModel mainActivityViewModel;
    private static DeviceHistoryViewModel deviceHistoryViewModel;
    private RecyclerView post1;
    private String valeur;
    private String namePost;


    public static oilAjout newInstance() {
        return new oilAjout();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mViewModel = ViewModelProviders.of(this).get(OliControlViewModel.class);

        View view = inflater.inflate(R.layout.fragment_oil_ajout, container, false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_oilAjout_to_oliControl);


            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


        spinner = view.findViewById(R.id.spinner);
        textInputLayout=view.findViewById(R.id.textInputLayout);
        textInputLayout4 = view.findViewById(R.id.textInputLayout4);
        save = view.findViewById(R.id.save);
        cancel=view.findViewById(R.id.cancel);
        calender = view.findViewById(R.id.calender);
        post1 = view.findViewById(R.id.post);
        mainActivityViewModel  = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        deviceHistoryViewModel = ViewModelProviders.of(this).get(DeviceHistoryViewModel.class);







// affichage ajout



        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(calender);
            }
        });



        SpinnerLoader(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0)
                {
                    valeur = (String) parent.getItemAtPosition(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//afichage Post
        //get Post

        postAdapter = new PostAdapter(getActivity());
        post1.setLayoutManager(new GridLayoutManager(getContext(),8));
        post1.setHasFixedSize(true);
        post1.setAdapter(postAdapter);
        mViewModel.getAllPost().observe(getActivity(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                postAdapter.submitList(posts);
                postAdapter.notifyDataSetChanged();
            }
        });


        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickLisnter() {
            @Override
            public void onItemClick(Post Post) {
                mViewModel.getPostById(Post.getId()).observe(getActivity(), new Observer<List<Post>>() {
                    @Override
                    public void onChanged(List<Post> posts) {
                        namePost=posts.get(0).getName();

                    }
                });

            }
        });





        // ajout
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                    @Override
                    public void handleOnBackPressed() {
                        Navigation.findNavController(view).navigate(R.id.action_oilAjout_to_oliControl);

                    }
                };
                requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StaticUse.validateEmpty(textInputLayout4,"Filtrage")
                ){return;}else {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Oil oil = new Oil();
                    try {
                        oil.setDateUtilisation(simpleDateFormat.parse(calender.getText().toString()));
                        oil.setCreationDate(new Date());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    oil.setPost(namePost);
                    oil.setFiltrage(Float.parseFloat(textInputLayout4.getEditText().getText().toString()));
                    oil.setAction(valeur);


                    mViewModel.insert(oil);
                    Toast.makeText(getActivity(), "oil Action Added Successfully", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(view).navigate(R.id.action_oilAjout_to_oliControl);


                    StaticUse.SaveNotification(getActivity(),mainActivityViewModel,getActivity(),"Oil Control"
                            ,"has added a new "+valeur+" for "
                            ,namePost,null,null,R.drawable.ic_add_circle_blue_24dp);



                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.getOilById(0).observe(getActivity(), new Observer<List<Oil>>() {
                            @Override
                            public void onChanged(List<Oil> oils) {
                                StaticUse.SaveHistory(getActivity(),deviceHistoryViewModel,getActivity(),"Oil Control",
                                        "has added a new action for ",namePost,0,valeur);

                            }
                        });

                        handler.removeCallbacksAndMessages(null);
                    }
                }, 500);



            }
        });



        return view;
    }


    private void SpinnerLoader(final Spinner spinner)
    {
        final ArrayList<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Choose action *");
        spinnerArray.add("Changement");
        spinnerArray.add("Mise à niveau");
        spinnerArray.add("Filtrage");
        SpinnerCatAdapter adapter = new SpinnerCatAdapter(getActivity(),spinnerArray);
        spinner.setAdapter(adapter);

    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    private boolean validateSpinner(String spinner) {
        if ((spinner == null || spinner.equals("") || spinner.isEmpty())) {
            return false;
        } else {
            return true;

        }
    }


    private void showDateTimeDialog (Button date_time_in) {
        final Calendar calendar=Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Log.i("PEW PEW", "Double fire check");

                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}
