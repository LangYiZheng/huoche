package view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.dell004.myapplication.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import app.BaseApplication;
import app.GlobalConstants;
import bean.UserBean;
import okhttp3.Request;
import okhttp3.Response;
import utils.NetWorkCallback;
import utils.OkHttpUtils;

public class LoginActivity extends Activity {

    public static final String TAG = "LoginActivity";
    EditText etName,etPwd;
    Button btnSubmmit;
    ProgressBar pbLogin;
    String userName ;
    String password ;
    boolean isDebug = false;

    String fileName = "saveUserNamePwd";
    SharedPreferences sp = null;
    UserBean userBean;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        BaseApplication.getInstance().addActivity(this);
        sp = getSharedPreferences(fileName,MODE_PRIVATE);
        initView();
    }

    private void initView() {
        etName = findViewById(R.id.et_name_login);
        etPwd = findViewById(R.id.et_pwd_login);

        initData();

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = etName.getText().toString().trim();
                Log.e(TAG,"userName="+userName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    String str = etPwd.getText().toString();
                    if(str != null || !"".equals(str)){
                        etPwd.requestFocus();
                        etPwd.setSelection(str.length());
                    }
                }
            }
        });

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = etPwd.getText().toString().trim();
                Log.e(TAG,"password="+password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pbLogin = findViewById(R.id.pb_login);
        btnSubmmit = findViewById(R.id.btn_submmit_login);
        btnSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(userName)){
                    Toast.makeText(LoginActivity.this,"输入用户名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                if("".equals(password)){
                    Toast.makeText(LoginActivity.this,"输入密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                if(isDebug){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else {
                    login();
                }

            }
        });

    }

    private void initData() {
        userName = sp.getString("name","");
        password = sp.getString("pwd","");

        etName.setText(userName);
        etPwd.setText(password);
    }


    private void login() {
        if(BaseApplication.getInstance().isNetworkAvailable()){
            pbLogin.setVisibility(View.VISIBLE);
            asynGetLogin();
        }else{
            Toast.makeText(this,"网络连接错误!",Toast.LENGTH_LONG).show();
        }

    }

    private void asynGetLogin() {
        String url = GlobalConstants.LOGIN_URL+"?number="+userName+"&password="+password;
        OkHttpUtils.getinstance().get(url, new NetWorkCallback<UserBean>() {
            @Override
            public void onLoadingBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, UserBean result) {
                pbLogin.setVisibility(View.GONE);
                if(!BaseApplication.getInstance().isLogin()){
                    calendar = Calendar.getInstance();
                    BaseApplication.loginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(calendar.getTime());
                    BaseApplication.getInstance().setLogin(true);
                    userBean = result;
                }

                ((BaseApplication)getApplication()).setUser(result);
                LoginActivity.this.startActivity(new Intent(LoginActivity.this
                        ,MainActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(Response response) {
                pbLogin.setVisibility(View.GONE);
                BaseApplication.getInstance().setLogin(false);
                Toast.makeText(LoginActivity.this,"用户名或密码错误，登录失败！"
                        ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                pbLogin.setVisibility(View.GONE);
                BaseApplication.getInstance().setLogin(false);
                Toast.makeText(LoginActivity.this,"网络异常，登录失败！"
                        ,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(null != sp && BaseApplication.getInstance().isLogin()){
            sp.edit().putString("name",userName).putString("pwd",password)
                    .putString("loginTime",BaseApplication.loginTime).commit();
        }

        BaseApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
