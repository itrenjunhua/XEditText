package com.renj.xedittext;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-19   15:32
 * <p>
 * 描述：测试Demo主页面
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class MainActivity extends AppCompatActivity {
    private Button btTestXEdit, btTestFliter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btTestXEdit = (Button) findViewById(R.id.bt_test_xedit);
        btTestFliter = (Button) findViewById(R.id.bt_test_fliter);

        // 打开测试XEditText控件
        btTestXEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, XEditTextActivity.class));
            }
        });

        // 打开测试EditText输入过滤器
        btTestFliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FilterActivity.class));
            }
        });
    }
}
