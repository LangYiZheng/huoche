package view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell004.myapplication.R;
import app.BaseApplication;
import app.GlobalConstants;
import bean.UserBean;
import okhttp3.Request;
import okhttp3.Response;
import utils.NetWorkCallback;
import utils.OkHttpUtils;
import view.wiget.CustomDialog;

public class UserInfoActivity extends AppCompatActivity {

    BaseApplication baseApp;
    UserBean userBean;
    String userNumber,userId,userTrueName,userPost;
    TextView tvName,tvType,tvNumber,tvLoginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        init();
    }

    private void init() {
        baseApp = BaseApplication.getInstance();
        baseApp.addActivity(this);
        userBean = baseApp.getUser();
        userTrueName = userBean.getData().getTrue_name();
        userNumber = userBean.getData().getNumber();
        userPost = userBean.getData().getPost(userNumber);
        userId = userBean.getData().getId();
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvName = findViewById(R.id.tv_userinfo_name);
        tvType = findViewById(R.id.tv_userinfo_type);
        tvNumber = findViewById(R.id.tv_userinfo_number);
        tvLoginTime = findViewById(R.id.tv_userinfo_logintime);
        tvName.setText(userTrueName);
        tvType.setText(userPost);
        tvNumber.setText(userNumber);
        tvLoginTime.setText(BaseApplication.loginTime);
        findViewById(R.id.btn_info_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog() {
        final CustomDialog dialog = new CustomDialog(UserInfoActivity.this);
        dialog.setTitle("退出登录");
        dialog.setMessage("确定退出登录？");
        dialog.setOnSureClickListener("确定", new CustomDialog.OnSureClickListener() {
            @Override
            public void onCliclk() {
                submmitLogout();
                dialog.dismiss();
            }
        });
        dialog.setOnCancelClickListener("取消", new CustomDialog.OnCancelClickListener() {
            @Override
            public void onCliclk() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void submmitLogout() {
        if(baseApp.isNetworkAvailable()){
            asyncGetLogout();
        }else {
            Toast.makeText(this,"网络异常",Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void asyncGetLogout(){
        String url = GlobalConstants.LOGOUT_URL+"?id="+ userId +"&number="+userNumber;
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<Object>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, Object result) {
                baseApp.setUser(null);
                baseApp.setLogin(false);
                baseApp.exitAllActivity();
                startActivity(new Intent(baseApp,LoginActivity.class));
            }

            @Override
            public void onError(Response response) {
                Toast.makeText(UserInfoActivity.this,"退出登录失败！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Toast.makeText(UserInfoActivity.this,"网络异常，退出登录失败！",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clearnuserinfo() {
        SharedPreferences sp = getSharedPreferences("saveUserNamePwd",MODE_PRIVATE);
        sp.edit().clear().commit();
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
