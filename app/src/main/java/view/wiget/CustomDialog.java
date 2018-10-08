package view.wiget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.dell004.myapplication.R;

/**
 * Created by dell003 on 2018/5/25.
 */

public class CustomDialog extends Dialog {

    private TextView titleTV, messageTv;
    private TextView sureBtn, cancelBtn;
    private String title,message;
    private String sureText ,cancelText ;

    private OnSureClickListener sureClickListener ;
    private OnCancelClickListener cancelClickListener;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    public void setOnSureClickListener(String s, OnSureClickListener sureClickListener){
        if(s != null){
            sureText = s;
        }
        this.sureClickListener = sureClickListener;
    }

    public void setOnCancelClickListener(String s, OnCancelClickListener cancelClickListener){
        if(s != null){
            cancelText = s;
        }
        this.cancelClickListener = cancelClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        setCanceledOnTouchOutside(false);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        sureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sureClickListener != null){
                    sureClickListener.onCliclk();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(cancelClickListener != null){
                    cancelClickListener.onCliclk();
                }
            }
        });
    }

    private void initData() {
        if(title != null){
            titleTV.setText(title);
        }

        if(message != null){
            messageTv.setText(message);
        }

        if(sureText != null){
            sureBtn.setText(sureText);
            sureBtn.setVisibility(View.VISIBLE);
        }

        if(cancelText != null){
            cancelBtn.setText(cancelText);
            cancelBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        titleTV = findViewById(R.id.dialog_title);
        messageTv = findViewById(R.id.dialog_message);
        sureBtn = findViewById(R.id.dialog_sure);
        sureBtn.setVisibility(View.GONE);
        cancelBtn = findViewById(R.id.dialog_cancel);
        cancelBtn.setVisibility(View.GONE);
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public interface OnSureClickListener{
        public void onCliclk();
    }

    public interface OnCancelClickListener{
        public void onCliclk();
    }
}
