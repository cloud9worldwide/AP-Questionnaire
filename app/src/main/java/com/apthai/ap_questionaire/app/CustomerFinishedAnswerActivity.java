package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.QuestionTypeData;


public class CustomerFinishedAnswerActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btn_staff,btn_menu, btn_back_home ;
    TextView customerName,thanks1, thanks2, project_name, lblgreen;
    questionniare_delegate delegate;
    static PopupWindow popup;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_finished_answer);

        ctx = this;

        setObject();
    }

    private void setObject() {
        delegate = (questionniare_delegate)getApplicationContext();

        popup = new PopupWindow(this);

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btn_staff = (ImageButton) findViewById(R.id.btnStaff);
        btn_back_home = (ImageButton) findViewById(R.id.btnBackHome);
        lblgreen = (TextView) findViewById(R.id.question);
        lblgreen.setTypeface(delegate.font_type);
        lblgreen.setTextSize(30);

        customerName = (TextView) findViewById(R.id.txtcustomer_name);
        customerName.setTypeface(delegate.font_type);
        customerName.setTextSize(35);

        thanks1 = (TextView) findViewById(R.id.thanks1);
        thanks1.setTypeface(delegate.font_type);
        thanks1.setTextSize(25);

        thanks2 = (TextView) findViewById(R.id.thanks2);
        thanks2.setTypeface(delegate.font_type);
        thanks2.setTextSize(25);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        btn_menu.setOnClickListener(this);
        btn_staff.setOnClickListener(this);
        btn_back_home.setOnClickListener(this);

        if(!delegate.QM.isStaffQustion()){

            thanks1.setText("โครงการ "+ delegate.project.getName() +" ขอขอบคุณ");
            customerName.setText(delegate.customer_selected.getFname()+ " " + delegate.customer_selected.getLname());
            btn_back_home.setEnabled(false);
            btn_back_home.setVisibility(View.GONE);
            btn_staff.setImageResource(R.drawable.btn_staff);
        } else {
            btn_back_home.setImageResource(R.drawable.btn_projects);
            btn_staff.setImageResource(R.drawable.btn_questionniare);
            btn_back_home.setVisibility(View.VISIBLE);
            thanks1.setText("");
            thanks2.setText("");
            if (delegate.service.isOnline()) {
                customerName.setText("บันทึกข้อมูลเรียบร้อยแล้ว");
                customerName.setTextColor(getResources().getColor(R.color.GREEN));
            } else {
                customerName.setText("ขณะนี้เป็นระบบ offline หาก online แล้ว ระบบ จะทำการ sync ให้ อัตโนมัติ");
                customerName.setTextColor(getResources().getColor(R.color.ORANGE));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_finished_answer, menu);
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
        if (v.getId() == R.id.btnStaff) {
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                Log.e("among question",""+delegate.QM.CheckQuestionNotAns().size());
                if(delegate.QM.CheckQuestionNotAns().size()!=0){
                    Intent newPage = new Intent(this, DoNotAnswerListActivity.class);
                    delegate.nextQuestionPage(newPage);
                    startActivityForResult(newPage,0);
                } else {
                    if (!delegate.QM.isStaffQustion()) {
                        delegate.initQuestionsStaff();
                        nextPage();
                    } else {
                        delegate.sendAnswer();
                        if (delegate.service.isOnline()) {
                            final ProgressDialog progress = new ProgressDialog(this);
                            progress.setTitle("Please wait");
                            progress.setMessage("Sync local data to server.");
                            progress.setCancelable(false);
                            progress.show();

                            final Handler uiHandler = new Handler();
                            final Runnable onUi = new Runnable() {
                                @Override
                                public void run() {
                                    // this will run on the main UI thread
                                    progress.dismiss();
                                    Intent i = new Intent(CustomerFinishedAnswerActivity.this, QuestionniareActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                            };
                            Runnable background = new Runnable() {
                                @Override
                                public void run() {
                                    // This is the delay
                                    delegate.service.sync_save_questionnaire(progress);
                                    uiHandler.post(onUi);
                                }
                            };
                            new Thread(background).start();

                        } else {
                            Intent i = new Intent(this, QuestionniareActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                }

            }
        } else if(v.getId() == R.id.btnBackHome){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                delegate.sendAnswer();
                Intent i = new Intent(this,ProjectsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        } else if(v.getId() == R.id.btnMenu){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                showPopup(this);
            }

        }
    }

    public void nextPage(){
        QuestionTypeData data = delegate.getQuestions();
        if(data == null){
            startActivityForResult(new Intent(this, CustomerFinishedAnswerActivity.class),0);
        } else {
            Intent intent = delegate.getCurentQuestionIntent();
            delegate.nextQuestionPage(intent);
        }
    }

    public void onBackPressed() {
        this.setResult(3);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0|| resultCode == 1){
            this.setResult(resultCode);
            finish();
        }
    }

    public void showPopup(final Activity context) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_menu, viewGroup);
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(delegate.pxToDp(180));
        popup.setHeight(delegate.pxToDp(118));
        popup.setBackgroundDrawable(null);


        ImageButton v = (ImageButton)findViewById(R.id.btnMenu);
        popup.showAtLocation(layout, Gravity.NO_GRAVITY,0,(int)v.getY()+delegate.dpToPx(50));

        View view_instance = (View)layout.findViewById(R.id.popup);
        final RelativeLayout home = (RelativeLayout) layout.findViewById(R.id.menu_home);
        final RelativeLayout settings = (RelativeLayout) layout.findViewById(R.id.menu_settings);
        final RelativeLayout logout = (RelativeLayout) layout.findViewById(R.id.menu_logout);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
                if (popup.isShowing()) {
                    popup.dismiss();
                }
                Intent i = new Intent(CustomerFinishedAnswerActivity.this,ProjectsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                settings.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                if (popup.isShowing()) {
                    popup.dismiss();
                }
                delegate.service.Logout();
                Intent i = new Intent(CustomerFinishedAnswerActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

}
