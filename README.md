# XEditText
自动格式化输入内容的EditText，同时可以指定格式化的模板

## 效果图
![](https://github.com/itrenjunhua/XEditText/raw/master/image1.gif)![](https://github.com/itrenjunhua/XEditText/raw/master/image2.gif)![](https://github.com/itrenjunhua/XEditText/raw/master/image3.gif)

## 属性定义预览
``` xml
<declare-styleable name="XEditText">
        <!--使用已经定义好的模板，如果既设置了原来的模板，又设置了自动以模板，以自定模板为主-->
        <attr name="my_templet" format="enum">
            <enum name="phone" value="1" />
            <enum name="bank_card" value="2" />
            <enum name="id_card" value="3" />
        </attr>
        <!--定义最大长度，如果定义了模板，就不需要定义最大的长度了-->
        <attr name="maxLength" format="integer" />
        <!--定义分隔符，注意：分隔符的最大长度为1，
        如果为null，使用默认分隔符 ' '，如果长度超过1，那么取第一个位置的字符作为分割符。
        注意：设置EditText的输入类型的时候(设置 inputType 或 digits 属性)需要能输入分割符,
        否则分割符不能输入到EditText中，那么不会对内容进行分割-->
        <attr name="splitChar" format="string" />
        <!-- 定义模板，格式固定(否则无效)："第一部分长度(正整数),第二部分长度(正整数),...,第n部分长度(正整数),..."。
         比如："3,4,4" 那么EditText显示的为 三个字符 + 分隔符 + 四个字符 + 分割符 + 四个字符-->
        <attr name="custom_templet" format="string" />
        <!--指定右边清除图片-->
        <attr name="del_icon" format="reference" />
        <!--右边图片显示的时间-->
        <attr name="del_show_time" format="enum">
            <!--总是显示右边删除图片(一直显示)-->
            <enum name="always_show" value="0" />
            <!--总是隐藏右边图片(不显示)-->
            <enum name="always_hide" value="1" />
            <!--有内容时显示(不管是否有焦点)-->
            <enum name="has_content_show" value="2" />
            <!--有内容并且有焦点时显示-->
            <enum name="has_content_focus_show" value="3" />
        </attr>
</declare-styleable>
```

## XEditText控件 使用
### 1.使用代码设置模板和分割符
<b>在xml文件中定义控件</b>
``` xml
<com.renj.xedittext.edittext.XEditText
        android:id="@+id/xedit_text3"
        style="@style/style_xedittext"
        android:hint="分隔符+，模板1,2,3,4，一直显示清除图片按钮"
        renj:custom_templet="1,2,3,4"
        renj:del_show_time="always_show"
        renj:splitChar="+" />
```

<b>在Java代码中使用</b>
``` java
// 在Activity中找到控件：
xEditText1 = (XEditText) findViewById(R.id.xedit_text1);

// 1.代码设置为手机号码格式，使用预定义模板形式
xEditText1.setSplitChar(' ');
//xEditText2.setSplitChar(' ');//不设置分割符，使用默认分隔符
xEditText1.setMyTemplet(XEditText.MyTemplet.PHONE);

// 2.代码设置为身份证号码格式，使用模板的形式设置
xEditText1.setTemplet(new int[]{4, 4, 4, 4, 2});

 // 3.直接将银行卡号设置到输入框1，指定模板
xEditText1.setToTextEdit("1234567890123456789", new int[]{4, 4, 4, 4, 3}, ' ');

 // 4.获取最后一个输入框去除分隔符之后的值
String splitCharText = xEditText1.getNoSplitCharText();
```

### 2.直接在layout.xml文件中指定模板和分隔符
``` xml
<com.renj.xedittext.edittext.XEditText
        android:id="@+id/xedit_text4"
        style="@style/style_xedittext"
        android:hint="使用预定义的手机号模板，改变分隔符为-"
        renj:my_templet="phone"
        renj:splitChar="-" />


<com.renj.xedittext.edittext.XEditText
        android:id="@+id/xedit_text3"
        style="@style/style_xedittext"
        android:hint="分隔符+，模板1,2,3,4，一直显示清除图片按钮"
        renj:custom_templet="1,2,3,4"
        renj:del_show_time="always_show"
        renj:splitChar="+" />
```

## EditText控件过滤器使用
<b>在xml文件中定义普通的EditText控件</b>
``` xml
<EditText
        android:id="@+id/edittext1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="12dp"
        android:hint="填写身份证号码，输入数字和Xx"
        android:digits="1234567890Xx"
        android:maxLines="1"
        android:textColor="#333666"
        android:textColorHint="#bbbbbb"
        android:textSize="14sp" />
```
<b>在Java代码中使用</b>
``` java
// 找到控件
edittext1 = (EditText) findViewById(R.id.edittext1);

// 过滤身份证号码
edittext1.setKeyListener(DigitsKeyListener.getInstance(digists));
// 使用过滤器
new EditTextFilter.Builder()
        .maxLength(18, "已经达到最大长度")
        .putReg("[0123456789xX]", "请输入正确的字符")
        .build()
        .startFilter(edittext);
        
// 过滤EditText,必须是中文，最大长度为4
new EditTextFilter.Builder()
        .maxLength(4, "已经达到最大长度")
        .putReg("[\\u4e00-\\u9fa5]+", "只能输入中文")
        .build()
        .startFilter(edittext);
        
// 使用过滤器设置最大长度，提示信息
new EditTextFilter.Builder()
        .maxLength(13, "超过大陆手机号码的长度")
        .build()
        .startFilter(xedittext);
```

## XEditText和过滤器联合使用
<b>在layout.xml文件中定义</b>
``` xml
<com.renj.xedittext.edittext.XEditText
        android:id="@+id/xedittext"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:hint="使用XEditText控件，输入手机号码并且过滤"
        android:inputType="phone"
        android:maxLines="1"
        android:textColor="#333666"
        android:textColorHint="#bbbbbb"
        android:textSize="14sp" />
```

<b>在Java文件中的代码</b>
``` java
// 找到控件
xedittext = (XEditText) findViewById(R.id.xedittext);

// 使用过滤器
// 过滤EditText，使用XEditText控件，手机号码格式，注意现在设置最大长度时必须加上分隔符的个数
xedittext.setMyTemplet(XEditText.MyTemplet.PHONE); // 设置模板
// 使用过滤器
new EditTextFilter.Builder()
        .maxLength(13, "超过大陆手机号码的长度")
        .build()
        .startFilter(xedittext);
```
