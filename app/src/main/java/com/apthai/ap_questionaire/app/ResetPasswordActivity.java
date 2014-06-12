package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.core.CoreEngine;


public class ResetPasswordActivity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 5;
    ImageButton btn_back , btn_send;
    EditText txtEmail;
    LinearLayout linearLayout, content_view;
    CoreEngine service;

    questionniare_delegate delegate;
    TextView title,question,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setObject();
    }
    private void setObject(){
        delegate = (questionniare_delegate)getApplicationContext();

        service = new CoreEngine(this);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btn_back = (ImageButton) findViewById(R.id.btnBack);
        btn_send = (ImageButton) findViewById(R.id.btnSend);

        btn_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        title = (TextView) findViewById(R.id.project_name);
        title.setTextSize(30);
        title.setTypeface(delegate.font_type);
        title.setGravity(Gravity.CENTER);

        question = (TextView) findViewById(R.id.question);
        question.setTextSize(30);
        question.setTypeface(delegate.font_type);
        question.setGravity(Gravity.CENTER);

        description = (TextView) findViewById(R.id.description);
        description.setTextSize(25);
        description.setTypeface(delegate.font_type);
        description.setGravity(Gravity.CENTER);

        txtEmail.setTextSize(25);
        txtEmail.setTypeface(delegate.font_type);
        txtEmail.setGravity(Gravity.CENTER);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reset_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        if(v.getId() == R.id.btnSend){
            send();
        } else if(v.getId() == R.id.btnBack) {
            finish();
        }
    }

    //email = test@example.com // true
    public void send(){
        if(service.ForgotPassword(txtEmail.getText().toString())){
            Toast.makeText(this, "YES", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(this, ResetPasswordComplate.class),0);
        } else {
            Toast.makeText(this, service.getForgotMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.setResult(resultCode);
        finish();
    }

}
