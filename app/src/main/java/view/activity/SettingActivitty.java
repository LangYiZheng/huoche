package view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.dell004.myapplication.R;

import app.BaseApplication;

public class SettingActivitty extends AppCompatActivity {

    BaseApplication baseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_activitty);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        baseApp = BaseApplication.getInstance();
        baseApp.addActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        baseApp.removeActivity(this);
        super.onDestroy();
    }
}
