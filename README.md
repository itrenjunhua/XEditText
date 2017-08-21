# XEditText
自动格式化输入内容的EditText，同时可以指定格式化的模板

## 效果图
![](https://github.com/itrenjunhua/XEditText/raw/master/image1.gif)

![](https://github.com/itrenjunhua/XEditText/raw/master/image2.gif)

![](https://github.com/itrenjunhua/XEditText/raw/master/image3.gif)

## 基本使用
在xml文件中定义控件：
``` xml
<com.renj.xedittext.edittext.XEditText
        android:id="@+id/xedit_text3"
        style="@style/style_xedittext"
        android:hint="分隔符+，模板1,2,3,4，一直显示清除图片按钮"
        renj:custom_templet="1,2,3,4"
        renj:del_show_time="always_show"
        renj:splitChar="+" />
```

在Java代码中使用
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
