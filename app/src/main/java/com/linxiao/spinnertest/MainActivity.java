package com.linxiao.spinnertest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


  private WindowManager mWindowManager;
  private View mSpinnerInflate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void text(View view) {
    //在windowManager中加载Spinner,在targerApi>22并且sdk>6.0的时候，需要动态申请SYSTEM_ALERT_WINDOW权限
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
}
