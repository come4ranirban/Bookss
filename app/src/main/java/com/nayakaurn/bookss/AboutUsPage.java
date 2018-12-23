package com.nayakaurn.bookss;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsPage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.aboutus, container, false);
        TextView aboutus= (TextView)v.findViewById(R.id.aboutus);
        StaticVariableClass.toolbartxt.setText("About us");
        String aboutustext= "Created by <b>Bhavika Technosolutions</b> for 2nd PUC Karnataka Exam. This app contains 2nd PUC Karnataka Exam Solved papers which will help to prepare for Exam in a smart and efficient way. Access Model Papers and Study Material With Solutions.<br><br>"+
                "Bhavika  TechnoSolutions is a professional Website and App Designing and development Company located in Bangalore India. Bhavika TechnoSolutions dedicated to provide web based solutions to wide variety of clients. We provide customer-oriented web design services and more importantly, deliver them 100 % effectively.";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutus.setText(Html.fromHtml(aboutustext, Html.FROM_HTML_MODE_LEGACY)) ;
        } else {
            aboutus.setText(Html.fromHtml(StaticVariableClass.dques));
        }


        return v;
    }
}
