package com.renj.xedittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.renj.xedittext.edittext.XEditText;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-16   20:32
 * <p>
 * 描述：测试XEditText控件的使用
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class XEditTextActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btPhone, btIdCard, btBankCard, btGetValue;
    private XEditText xEditText1, xEditText2, xEditText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xedittext);

        btPhone = (Button) findViewById(R.id.bt_phone);
        btIdCard = (Button) findViewById(R.id.bt_idcard);
        btBankCard = (Button) findViewById(R.id.bt_bankcard);
        btGetValue = (Button) findViewById(R.id.bt_getValue);
        xEditText1 = (XEditText) findViewById(R.id.xedit_text1);
        xEditText2 = (XEditText) findViewById(R.id.xedit_text2);
        xEditText4 = (XEditText) findViewById(R.id.xedit_text4);

        // 设置右边删除按钮显示的时间为有内容就显示(默认是有内容并且有焦点时显示，xEditText1没设置，使用默认)
        xEditText2.setDelIconShowTime(XEditText.HAS_CONTENT_SHOW);

        btPhone.setOnClickListener(this);
        btIdCard.setOnClickListener(this);
        btBankCard.setOnClickListener(this);
        btGetValue.setOnClickListener(this);
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
                xEditText2.setHint("手机号模板");
                xEditText2.setText("");
                break;
            case R.id.bt_idcard:
                // 代码设置为身份证号码格式，使用模板的形式设置
                xEditText1.setTemplet(new int[]{4, 4, 4, 4, 2});
                xEditText2.setMyTemplet(XEditText.MyTemplet.ID_CARD);

                // 直接将身份证号码设置到输入框1，并指定分隔符
                xEditText1.setToTextEdit("111111111111111111", '-');
                xEditText2.setHint("身份证号码模板");
                xEditText2.setText("");
                break;
            case R.id.bt_bankcard:
                // 代码设置为银行卡号格式，使用模板的形式设置
                xEditText2.setTemplet(new int[]{4, 4, 4, 4, 3});

                // 直接将银行卡号设置到输入框1，指定模板
                xEditText1.setToTextEdit("1111111111111111111", new int[]{4, 4, 4, 4, 3}, ' ');
                xEditText2.setHint("银行卡号模板");
                xEditText2.setText("");
                break;
            case R.id.bt_getValue: // 获取最后一个输入框去除分隔符之后的值
                String splitCharText = xEditText4.getNoSplitCharText();
                if (TextUtils.isEmpty(splitCharText))
                    Toast.makeText(XEditTextActivity.this, "没有输入内容", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(XEditTextActivity.this, splitCharText, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
