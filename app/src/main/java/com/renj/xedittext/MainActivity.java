package com.renj.xedittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.renj.xedittext.edittext.XEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btPhone, btIdCard, btBankCard;
    private XEditText xEditText1, xEditText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btPhone = (Button) findViewById(R.id.bt_phone);
        btIdCard = (Button) findViewById(R.id.bt_idcard);
        btBankCard = (Button) findViewById(R.id.bt_bankcard);
        xEditText1 = (XEditText) findViewById(R.id.xedit_text1);
        xEditText2 = (XEditText) findViewById(R.id.xedit_text2);

        // 设置右边删除按钮显示的时间为有内容就显示(默认是有内容并且有焦点时显示，xEditText1没设置，使用默认)
        xEditText2.setDelIconShowTime(XEditText.HAS_CONTENT_SHOW);

        btPhone.setOnClickListener(this);
        btIdCard.setOnClickListener(this);
        btBankCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_phone:
                // 代码设置为手机号码格式，使用预定义模板形式
                xEditText1.setSplitChar(' ');
                //xEditText2.setSplitChar(' ');//不设置分割符，使用默认分隔符
                xEditText1.setMyTemplet(XEditText.MyTemplet.PHONE);
                xEditText2.setMyTemplet(XEditText.MyTemplet.PHONE);

                // 直接将手机号码设置到输入框1
                xEditText1.setToTextEdit("13211111111");
                xEditText2.setText("");
                break;
            case R.id.bt_idcard:
                // 代码设置为身份证号码格式，使用模板的形式设置
                xEditText1.setTemplet(new int[]{4, 4, 4, 4, 2});
                xEditText2.setMyTemplet(XEditText.MyTemplet.ID_CARD);

                // 直接将身份证号码设置到输入框1，并指定分隔符
                xEditText1.setToTextEdit("111111111111111111", '-');
                xEditText2.setText("");
                break;
            case R.id.bt_bankcard:
                // 代码设置为银行卡号格式，使用模板的形式设置
                xEditText2.setTemplet(new int[]{4, 4, 4, 4, 3});

                // 直接将身份证号码设置到输入框1，指定模板
                xEditText1.setToTextEdit("1111111111111111111", new int[]{4, 4, 4, 4, 3},' ');
                xEditText2.setText("");
                break;
            default:
                break;
        }
    }
}
