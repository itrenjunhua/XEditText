# XEditText
自动格式化输入内容的EditText，同时可以指定格式化的模板

## 效果图
![](https://github.com/itrenjunhua/XEditText/raw/master/image1.gif)

![](https://github.com/itrenjunhua/XEditText/raw/master/image2.gif)

![](https://github.com/itrenjunhua/XEditText/raw/master/image3.gif)

## 基本使用

<com.renj.xedittext.edittext.XEditText
        android:id="@+id/xedit_text3"
        style="@style/style_xedittext"
        android:hint="分隔符+，模板1,2,3,4，一直显示清除图片按钮"
        renj:custom_templet="1,2,3,4"
        renj:del_show_time="always_show"
        renj:splitChar="+" />
