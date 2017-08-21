# XEditText
自动格式化输入内容的EditText，同时可以指定格式化的模板

## 效果图
![](https://github.com/itrenjunhua/XEditText/raw/master/image1.gif)

![](https://github.com/itrenjunhua/XEditText/raw/master/image2.gif)

![](https://github.com/itrenjunhua/XEditText/raw/master/image3.gif)

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
