package com.linxiao.spinnertest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 练习使用spinner和windowManager
 * <p>
 * 小结：
 * <p>
 * 1.通过xml可以自定义spinner
 * 2.windowmanager在使用的时候，注意不同sdk版本下的的type，以及当sdk<23的时候，权限的申请。同时，可以
 * 设置不同的flag产生各种效果。
 */
public class MainActivity extends AppCompatActivity {


  private static final int REQUEST_OVERLAY_CODE = 100;
  private WindowManager mWindowManager;
  private View mSpinnerInflate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void text(View view) {
    //在windowManager中加载Spinner,对于一些type类型，在targerApi>22并且sdk>6.0的时候，需要申请SYSTEM_ALERT_WINDOW权限.
    //这个权限比较特殊，动态申请一般会直接拒绝，需要用户手动开启
//    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
//      ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_OVERLAY_CODE);
//      return;
//    }
    //用这个方法也可以判断
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!Settings.canDrawOverlays(this)) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_OVERLAY_CODE);
        return;
      }
    }

    mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    WindowManager.LayoutParams params = initWindowManagerParams();

    mSpinnerInflate = View.inflate(this, R.layout.view_spinner, null);
    Spinner mSpinnerView = mSpinnerInflate.findViewById(R.id.sp_camera);
    //数据
    ArrayList<String> list = new ArrayList<>();
    list.add("北京");
    list.add("上海");
    list.add("广州");

    //适配器
    ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, list);
    //设置样式
    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
    //加载适配器
    mSpinnerView.setAdapter(adapter);

    //设置选中
    mSpinnerView.setSelection(1);

    //点击事件
    mSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //do something
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    mWindowManager.addView(mSpinnerInflate, params);
  }

  @NonNull
  private WindowManager.LayoutParams initWindowManagerParams() {
    int LAYOUT_FLAG;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      //8.0需要设置这个type，否则会崩溃
      LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    } else {
      LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
    }
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        LAYOUT_FLAG,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.RGBA_8888);
    params.gravity = Gravity.CENTER;
    params.x = 0;
    params.y = 0;
    return params;
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mWindowManager != null) {
      mWindowManager.removeView(mSpinnerInflate);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_OVERLAY_CODE) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
          //拒绝并且不需要提示了，显示手动开启弹框
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //这个intent只在sdk>=23的时候才有
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_CODE);
          } else {
            //sdk<23的时候，SYSTEM_ALERT_WINDOW会自动获取，所以一般执行不到这里
            Toast.makeText(this, "权限申请失败", Toast.LENGTH_LONG).show();
          }
        }
      }
    }
  }

}
