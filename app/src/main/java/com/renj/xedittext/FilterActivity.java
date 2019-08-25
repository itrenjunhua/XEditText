package com.renj.xedittext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.renj.xedittext.edittext.EditTextFilter;
import com.renj.xedittext.edittext.XEditText;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-19   15:42
 * <p>
 * 描述：测试EditText过滤器的页面
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class FilterActivity extends AppCompatActivity {
    private EditText editText1, editText2;
    private XEditText xedittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fliter);

        editText1 = (EditText) findViewById(R.id.edittext1);
        editText2 = (EditText) findViewById(R.id.edittext2);
        xedittext = (XEditText) findViewById(R.id.xedittext);

        // 设置过滤器
        setFilter();
    }

    /**
     * 设置过滤器
     */
    private void setFilter() {
        // 过滤EditText1，身份证号码
        // 下面的代码主要是作用是用于调起数字键盘但是还可以输入 X x
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        String digits = "0123456789xX";
        editText1.setKeyListener(DigitsKeyListener.getInstance(digits));
        // 使用过滤器
        new EditTextFilter.Builder()
                .maxLength(18, "已经达到最大长度")
                .putReg("[0123456789xX]", "请输入正确的字符")
                .build()
                .startFilter(editText1);

        // 过滤EditText2，必须是中文，最大长度为4
        new EditTextFilter.Builder()
                .maxLength(4, "已经达到最大长度")
                .putReg("[\\u4e00-\\u9fa5]+", "只能输入中文")
                .build()
                .startFilter(editText2);

        // 过滤EditText3，使用XEditText控件，手机号码格式，注意现在设置最大长度时必须加上分隔符的个数
        xedittext.setXTemplate(XEditText.XTemplate.PHONE); // 设置模板
        // 使用过滤器
        new EditTextFilter.Builder()
                .maxLength(13, "超过大陆手机号码的长度")
                .build()
                .startFilter(xedittext);
    }
}
