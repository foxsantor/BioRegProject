package com.example.bioregproject.ui.others;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.bioregproject.MainActivity;
import com.example.bioregproject.R;
import com.example.bioregproject.Utils.StaticUse;
import com.google.android.material.textfield.TextInputLayout;

public class FormPopUp extends Fragment {

    private FormPopUpViewModel mViewModel;
    private TextView name;
    private ImageView profile;
    private Button login;
    private Bundle bundle;
    private ImageButton backi;
    private TextInputLayout password;
    private String checker,fullName;
    private byte[] imageBytes;
    private long id;


    public static FormPopUp newInstance() {
        return new FormPopUp();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

           View view = inflater.inflate(R.layout.form_pop_up_fragment, container, false);
           return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FormPopUpViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);



        name = view.findViewById(R.id.filtrage1);
        profile = view.findViewById(R.id.profile);
        login = view.findViewById(R.id.login);
        password = view.findViewById(R.id.argax);
        bundle = getArguments();
        backi = view.findViewById(R.id.backi);
        backi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_formPopUp_to_listAccountPopUp);
            }
        });




        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.admin_user)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
        if(bundle !=null)
        {
            fullName = StaticUse.capitalize(bundle.getString("FirstName",""))
                    +" "+StaticUse.capitalize(bundle.getString("LastName",""));
              name.setText(fullName);
            imageBytes = bundle.getByteArray("image");
            Glide.with(getActivity()).asBitmap().load(imageBytes).apply(options).into(profile);
            checker = bundle.getString("password","");
            id = bundle.getLong("id",0);

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StaticUse.validateEmpty(password,"Password")){return;}
                else {
                    if(!checker.isEmpty())
                    {
                     if(password.getEditText().getText().toString().equals(checker))
                     {
                         password.setError(null);
                        StaticUse.clearShared(StaticUse.SHARED_NAME_USER_LOG,getActivity());
                        StaticUse.saveSession(getActivity(),checker,fullName,id);
                        //ConstraintLayout layout = getActivity().findViewById(R.id.connetcd);
                         TextView names = getActivity().findViewById(R.id.namePopup);
                         ImageView imagex = getActivity().findViewById(R.id.image);
                         names.setText(fullName);
                         Glide.with(getActivity()).asBitmap().load(imageBytes).into(imagex);
                         MainActivity.dismissMessage();
                         Navigation.findNavController(v).navigate(R.id.action_formPopUp_to_listAccountPopUp);
                         Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.mainMenu);

                     }else
                     {
                         password.setError("Password is wrong");
                     }
                    }
                }
            }
        });


    }
}
