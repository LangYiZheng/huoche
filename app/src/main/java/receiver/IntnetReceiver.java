package receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import app.BaseApplication;

/**
 * Created by dell003 on 2018/5/18.
 */

public class IntnetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!BaseApplication.getInstance().isNetworkAvailable()){
            showDialog(context);
        }

    }

    private void showDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage("网络中断")
                .setCancelable(false)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                    }
                }).setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
