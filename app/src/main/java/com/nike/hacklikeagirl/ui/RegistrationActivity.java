package com.nike.hacklikeagirl.ui;

import com.nike.hacklikeagirl.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private RadioGroup mDevTypeRadioGroup;
    private RadioButton frontEndDev;
    private RadioButton backEndDev;
    private RadioButton mobileDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mDevTypeRadioGroup = (RadioGroup) findViewById(R.id.dev_type);
        frontEndDev = (RadioButton) findViewById(R.id.front_end_dev);
        frontEndDev.setOnClickListener(createButtonListener(frontEndDev));

        backEndDev = (RadioButton) findViewById(R.id.back_end_dev);
        backEndDev.setOnClickListener(createButtonListener(backEndDev));

        mobileDev = (RadioButton) findViewById(R.id.mobile_dev);
        mobileDev.setOnClickListener(createButtonListener(mobileDev));
    }


    private View.OnClickListener createButtonListener(final RadioButton button) {
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivity.this, "Clicked on " + button.getText(),
                        Toast
                        .LENGTH_LONG).show();
            }
        };
        return listener;
    }
}
