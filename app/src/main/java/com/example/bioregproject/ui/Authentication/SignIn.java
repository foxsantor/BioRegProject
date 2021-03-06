package com.example.bioregproject.ui.Authentication;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bioregproject.MainActivity;
import com.example.bioregproject.R;
import com.example.bioregproject.Utils.StaticUse;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignIn extends Fragment {

    private SignInViewModel mViewModel;
        private Button login,forgetPassword;
    private ConstraintLayout loading_1,mother;
    private TextView error;
    private ImageButton close;
    private ImageView icon,logo;
    private TextInputLayout email,password;
    private RequestQueue requestQueue;
    private String emailS;
    private static boolean exit;
    private ImageButton notifcation;
    private CardView errors,closer;
    private final String URL = StaticUse.SKELETON+"login";
    private final String URLRESET ="http://localhost:4200/forgotPasswordApp" ;


    public static SignIn newInstance() {
        return new SignIn();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
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
        ((MainActivity) requireActivity()).getSupportActionBar().hide();
        notifcation = getActivity().findViewById(R.id.notification);
        notifcation.setVisibility(View.GONE);
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.start();
        emailS = StaticUse.loadEmail(getActivity());
        if(!emailS.isEmpty())
        {
            if(!StaticUse.loggedInInternal(getActivity()))
            Navigation.findNavController(view).navigate(R.id.accountBindiner);
            else
            Navigation.findNavController(view).navigate(R.id.mainMenu);
        }

        //Section fo binding views
        login = view.findViewById(R.id.login);
        logo = view.findViewById(R.id.logo);
        mother =  view.findViewById(R.id.mother);
        StaticUse.backgroundAnimator(mother);
        loading_1 = view.findViewById(R.id.loading_1);
        close = view.findViewById(R.id.close);
        forgetPassword = view.findViewById(R.id.forget);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        error = view.findViewById(R.id.error);
        errors = view.findViewById(R.id.errors);
        icon = view.findViewById(R.id.icon);
        Glide.with(getActivity()).load(R.drawable.logo_transparent).into(logo);

        errors.setVisibility(View.GONE);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StaticUse.validateEmpty(email,"Email")| !StaticUse.validateEmail(email) | !StaticUse.validateEmpty(password,"Password"))
                {return;}
                else{ login(v);}
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            errors.setVisibility(View.GONE);
            Animation animFadeOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fade_out);
            errors.startAnimation(animFadeOut);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        // TODO: Use the ViewModel
    }

    private void  login(final View view)
    {
        loading_1.setVisibility(View.VISIBLE);
        icon.setImageResource(R.drawable.ic_error_outline_red_24dp);
        HashMap< String, String > params = new HashMap<String, String>();
        String containerEmail ,containerPassword ;
        containerEmail = email.getEditText().getText().toString();
        containerPassword = password.getEditText().getText().toString();
        params.put("email",containerEmail );
        params.put("password",containerPassword );
        // the entered data as the JSON body.
        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.POST,
                URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("email"))
                            {

                            StaticUse.saveEmail(getActivity(),response.getString("email"));
                            loading_1.setVisibility(View.GONE);
                            Navigation.findNavController(view).navigate(R.id.action_signIn_to_accountBindiner);
                            }else
                            {
                                errors.setVisibility(View.VISIBLE);
                                Animation animFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fade_in);
                                errors.startAnimation(animFadeIn);
                                error.setText(response.getString("message"));
                                loading_1.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errorx) {
                loading_1.setVisibility(View.GONE);
                if(!haveNetworkConnection()){
                    errors.setVisibility(View.VISIBLE);
                Animation animFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fade_in);
                errors.startAnimation(animFadeIn);
                error.setText("Can't connect to the internet");
                icon.setImageResource(R.drawable.ic_cloud_off_red_24dp);
                }
                else{
                Toast.makeText(getActivity(), errorx.getMessage(), Toast.LENGTH_SHORT).show();}
            }
        });
        requestQueue.add(jsObjRequest);
    }
    private  boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile || MainActivity.getConnection();
    }

}
