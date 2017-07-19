package com.renj.xedittext.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.renj.xedittext.R;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-08   16:02
 * <p>
 * 描述：自动格式化的EditText控件<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 支持自定义分隔符、设置最大的长度，指定分割的模板等<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 可以使用方法 <code>setMyTemplet(@NonNull {@link MyTemplet} myTemplete)</code> 指定预定义模板(包括中国大陆手机、最多19位的银行卡、18位身份证号)<br/>
 * <b>注意：</b><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>1.如果在布局文件中指定属性，同时设置了使用预定义模板和自定义模板，那么，自定义模板生效</b><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>2.如果使用代码调用方法同时设置预定义模板和自定义模板，那么，最后设置的生效</b><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>3.如果同时在布局文件和代码中设置了相同的属性，那么，代码中设置的生效</b><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>4.在设置EditText的输入类型的时候(设置 {@code inputType} 或 {@code digits} 属性)需要能输入分割符，
 * 否则分割符不能输入到EditText中，那么不会对内容进行分割</b><br/>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class XEditText extends android.support.v7.widget.AppCompatEditText {
    /**
     * 总是显示右边删除图片(一直显示)
     */
    public static final int ALWAYS_SHOW = 0x0000;
    /**
     * 总是隐藏右边图片(不显示)
     */
    public static final int ALWAYS_HIND = 0x0001;
    /**
     * 有内容时显示(不管是否有焦点)
     */
    public static final int HAS_CONTENT_SHOW = 0x0002;
    /**
     * 有内容并且有焦点时显示
     */
    public static final int HAS_CONTENT_FOUCUS_SHOW = 0x0003;

    /**
     * 默认分隔符，{@code ' '} 表示
     */
    private static final char DEFAULT_SPLIT = ' ';
    /**
     * 实际分隔符
     */
    private char mSplitChar = DEFAULT_SPLIT;
    /**
     * 分隔符所在位置的数组
     */
    private int[] mSplitPosition;
    /**
     * 输入之前的长度
     */
    private int mPreLength;
    /**
     * 当前的长度
     */
    private int mCurrentLen;
    /**
     * EditText输入框的最大长度，当设置了模板的时候可以不用设置，因为会根据模板计算出最大的长度
     */
    private int mMaxLength;
    /**
     * 内容改变监听对象
     */
    private MyTextWatcher mTextWatcher;
    /**
     * 自定义的文字改变监听对象
     */
    private OnTextChangeListener mOnTextChangeListener;
    /**
     * 自定义焦点改变监听对象
     */
    private OnMyFocusChangeListener mOnMyFocusChangeListener;
    /**
     * 指定右边删除图片显示的时间，默认有内容并且有焦点时显示
     */
    private int mDelIconTime = HAS_CONTENT_FOUCUS_SHOW;
    /**
     * 右边删除按钮图片对象
     */
    private Drawable mClearDrawable;
    private Drawable[] mCompoundDrawables;

    /**
     * 提供默认的几个模板(包括中国大陆手机、最多19位的银行卡、18位身份证号)
     */
    public enum MyTemplet {
        /**
         * 大陆手机格式(11位 3,4,4)
         */
        PHONE,
        /**
         * 银行卡号(最多19位 4,4,4,4,3)
         */
        BANK_CARD,
        /**
         * 省份证号(18位 4,4,4,4,2)
         */
        ID_CARD
    }

    public XEditText(Context context) {
        this(context, null);
    }

    public XEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        init();
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.XEditText);
        mMaxLength = typedArray.getInteger(R.styleable.XEditText_maxLength, 0);
        int myTemplet = typedArray.getInteger(R.styleable.XEditText_my_templet, -1);
        String splitChar = typedArray.getString(R.styleable.XEditText_splitChar);
        String customTemplet = typedArray.getString(R.styleable.XEditText_custom_templet);
        mClearDrawable = typedArray.getDrawable(R.styleable.XEditText_del_icon);
        mDelIconTime = typedArray.getInt(R.styleable.XEditText_del_show_time, HAS_CONTENT_FOUCUS_SHOW);
        // 校验和设置自定义的属性
        setCustomAttrs(myTemplet, splitChar, customTemplet);
        typedArray.recycle();
    }

    /**
     * 检验的设置自定的属性
     */
    private void setCustomAttrs(int myTemplet, String splitChar, String customTemplet) {
        // 校检设置的预定义模板
        if (myTemplet > 0) {
            if (1 == myTemplet) setMyTemplet(MyTemplet.PHONE);
            else if (2 == myTemplet) setMyTemplet(MyTemplet.BANK_CARD);
            else if (3 == myTemplet) setMyTemplet(MyTemplet.ID_CARD);
        }
        // 右边清除图片
        if (null == mClearDrawable) { // 没有定义就使用默认的图片
            // mClearDrawable = getContext().getResources().getDrawable(R.mipmap.icon_delete,null);
            mClearDrawable = getContext().getResources().getDrawable(R.mipmap.icon_delete);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        mCompoundDrawables = getCompoundDrawables();
        mClearDrawable.setVisible(false, false); // 最开始默认隐藏
        setCompoundDrawables(mCompoundDrawables[0], mCompoundDrawables[1],
                mClearDrawable.isVisible() ? mClearDrawable : null,
                mCompoundDrawables[3]);
        // 校检分隔符
        if (!TextUtils.isEmpty(splitChar)) mSplitChar = splitChar.charAt(0);
        // 校验自定义模板格式
        if (!TextUtils.isEmpty(customTemplet) && customTemplet.contains(",")) {
            String[] splits = customTemplet.split(",");
            int length = splits.length;
            if (length > 1) {
                int[] arr = new int[length];
                try {
                    for (int i = 0; i < length; i++) {
                        arr[i] = Integer.parseInt(splits[i]);
                    }
                    setTemplet(arr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("XEditText", "自定义模板格式错误，请检查布局文件中的 custom_templet属性是否参照格式定义");
                }
            }
        }
    }

    /**
     * 初始化其他的相关内容
     */
    private void init() {
        // 如果设置 inputType="number" 的话是没法插入空格的，所以强行转为inputType="phone"
        if (getInputType() == InputType.TYPE_CLASS_NUMBER)
            setInputType(InputType.TYPE_CLASS_PHONE);

        // 初始化并设置监听
        mTextWatcher = new MyTextWatcher();
        addTextChangedListener(mTextWatcher);

        // 焦点改变监听
        focusChangeListener();
    }

    /**
     * 焦点改变监听
     */
    private void focusChangeListener() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeDelShowStatu(getText(), hasFocus);
                if (null != mOnMyFocusChangeListener)
                    mOnMyFocusChangeListener.onFocusChange(v, hasFocus);
            }
        });
    }

    /**
     * 设置右边删除图片
     *
     * @param delDrawable 右边的图片
     * @return
     */
    public XEditText setDelDrawable(Drawable delDrawable) {
        this.mClearDrawable = delDrawable;
        return this;
    }

    /**
     * 设置右边删除图片资源id
     *
     * @param delDrawableId 右边的图片资源id
     * @return
     */
    public XEditText setDelDrawable(int delDrawableId) {
        this.mClearDrawable = getResources().getDrawable(delDrawableId);
        return this;
    }

    /**
     * 设置右边删除图片显示的时间，默认有内容并且有焦点时显示
     *
     * @param delIconTime 设置右边删除图片显示的时间，取值<br/>
     *                    XEditText.ALWAYS_SHOW：总是显示(一直显示)<br/>
     *                    XEditText.ALWAYS_HIND：总是隐藏(从不显示)<br/>
     *                    XEditText.HAS_CONTENT_SHOW：有内容时显示(不管焦点)<br/>
     *                    XEditText.HAS_CONTENT_FOUCUS_SHOW：有内容并且有焦点时显示<br/>
     * @return
     */
    public XEditText setDelIconShowTime(int delIconTime) {
        this.mDelIconTime = delIconTime;
        return this;
    }

    /**
     * 设置EditText输入内容的最大长度<br/>
     * <b>注意：</b><br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * <b>1.如果调用了{@code setTemplet(@NonNull int[] templet)}方法设置了模板时可以不用在设置最大长度，因为设置了模板会根据模板计算出最大的长度</b><br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * <b>2.如果调用了{@code setTemplet(@NonNull int[] templet)}方法设置了模板的同时，还要设置最大长度，一定要加上分割符的长度(分隔符也占用EditText的长度)</b><br/>
     *
     * @param maxLength 最大长度
     * @return
     * @see #setTemplet(int[])
     */
    public XEditText setMaxLength(int maxLength) {
        if (maxLength <= 0)
            throw new IllegalArgumentException("maxLength 的值必须大于 0");
        this.mMaxLength = maxLength;
        return this;
    }

    /**
     * 设置已经定义好的模板 {@link MyTemplet}，分割符为 ' '<br/>
     * <b>如果既设置了定义好的模板，又设置了自动以的模板，那么以最后一个生效</b>
     *
     * @param myTemplete 定义好的模板 {@link MyTemplet}
     * @return
     */
    public XEditText setMyTemplet(@NonNull MyTemplet myTemplete) {
        if (null == myTemplete) return this;
        setSplitChar(mSplitChar);
        switch (myTemplete) {
            case PHONE: // 手机
                setTemplet(new int[]{3, 4, 4});
                break;
            case BANK_CARD: // 银行卡
                setTemplet(new int[]{4, 4, 4, 4, 3});
                break;
            case ID_CARD: // 身份证
                setTemplet(new int[]{4, 4, 4, 4, 2});
                break;
        }
        return this;
    }

    /**
     * 设置分割符，默认' '
     *
     * @param splitChar 需要的分隔符<br/>
     *                  <b>注意：设置EditText的输入类型的时候(设置 {@code inputType} 或 {@code digits} 属性)需要能输入分割符,
     *                  否则分割符不能输入到EditText中，那么不会对内容进行分割</b>
     * @return
     */
    public XEditText setSplitChar(@NonNull char splitChar) {
        this.mSplitChar = splitChar;
        return this;
    }

    /**
     * 设置EditText分割样式，如 {@code new int[]{3,4,4}} 表示大陆手机号码的样式，<br/>
     * <b>注意：如果设置了模板，表示已经设置的最大的长度，如果设置了模板没有设置分割符，使用默认{@code ' '}分隔符</b>
     *
     * @param templet 模板样式 如：{@code new int[]{3,4,4}} 显示：132 1234 5678
     * @return
     */
    public XEditText setTemplet(@NonNull int[] templet) {
        if (null == templet) return this;
        int length = templet.length;
        mSplitPosition = new int[length - 1];
        int temp = 0;
        for (int i = 0; i < length; i++) {
            temp += templet[i];
            if (i < length - 1) {
                mSplitPosition[i] = temp;
                temp += 1;
            }
        }
        if (this.mSplitChar == ' ') {
            this.mSplitChar = DEFAULT_SPLIT;
        }
        mMaxLength = temp;
        return this;
    }

    /**
     * 设置需要格式化的文字<br/>
     * 1.调用该方法前，请先调用<code>setTemplet(@NonNull int[] templet)</code>方法设置好模板或<br/>
     * 2.直接调用<code>setToTextEdit(@NonNull String text, @NonNull int[] templet)</code>方法
     *
     * @param text 需要格式化的内容
     * @return
     * @see #setTemplet(int[])
     * @see #setToTextEdit(String, int[])
     */
    public XEditText setToTextEdit(@NonNull String text) {
        if (TextUtils.isEmpty(text)) {
            setText("");
            return this;
        }
        if (null == mSplitPosition || mSplitPosition.length <= 0) {
            setText(text);
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            for (int position : mSplitPosition) {
                if (position == stringBuilder.toString().length()) stringBuilder.append(mSplitChar);
            }
            stringBuilder.append(text.charAt(i));
        }
        setText(stringBuilder.toString());
        return this;
    }

    /**
     * 指定模板设置格式化的文字，如果已经指定过模板了，可以调用<code>setToTextEdit(@NonNull String text)</code>方法<br/>
     * <b>注意：如果设置了模板没有设置分割符，使用默认{@code ' '}分隔符</b>
     *
     * @param text    需要格式化的内容
     * @param templet 格式化的模板，如：{@code setToTextEdit("13212345678",new int[]{3,4,4}}) 显示：132 1234 5678
     * @return
     * @see #setTemplet(int[])
     * @see #setToTextEdit(String)
     */
    public XEditText setToTextEdit(@NonNull String text, @NonNull int[] templet) {
        setTemplet(templet);
        return setToTextEdit(text);
    }

    /**
     * 指定分隔符和模板设置格式化的文字，如果已经指定过模板了，可以调用<code>setToTextEdit(@NonNull String text)</code>方法<br/>
     * <b>注意：如果设置了模板没有设置分割符，使用默认{@code ' '}分隔符</b>
     *
     * @param text      需要格式化的内容
     * @param templet   格式化的模板，如：{@code setToTextEdit("13212345678",new int[]{3,4,4}}) 显示：132 1234 5678
     * @param splitChar 分隔符
     * @return
     * @see #setSplitChar(char)
     * @see #setTemplet(int[])
     * @see #setToTextEdit(String)
     */
    public XEditText setToTextEdit(@NonNull String text, @NonNull int[] templet, @NonNull char splitChar) {
        setSplitChar(splitChar);
        setTemplet(templet);
        return setToTextEdit(text);
    }

    /**
     * 指定分隔符设置格式化的文字<br/>
     * 调用该方法前，请先调用<code>setTemplet(@NonNull int[] templet)</code>方法设置好模板<br/>
     * <b>注意：如果设置了模板没有设置分割符，使用默认{@code ' '}分隔符</b>
     *
     * @param text      需要格式化的内容
     * @param splitChar 分隔符
     * @return
     * @see #setSplitChar(char)
     * @see #setTemplet(int[])
     * @see #setToTextEdit(String)
     */
    public XEditText setToTextEdit(@NonNull String text, @NonNull char splitChar) {
        setSplitChar(splitChar);
        return setToTextEdit(text);
    }

    /**
     * 设置文字改变监听
     *
     * @param onTextChangeListener
     * @return
     */
    public XEditText setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.mOnTextChangeListener = onTextChangeListener;
        return this;
    }

    /**
     * 设置焦点改变监听
     *
     * @param onMyFocusChangeListener
     * @return
     */
    public XEditText setOnMyFocusChangeListener(OnMyFocusChangeListener onMyFocusChangeListener) {
        this.mOnMyFocusChangeListener = onMyFocusChangeListener;
        return this;
    }

    /**
     * 获得除去分割符的输入框内容
     */
    public String getNoSplitCharText() {
        if (TextUtils.isEmpty(mSplitChar + ""))
            return getText().toString();
        return getText().toString().replaceAll(mSplitChar + "", "");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        if (mClearDrawable.isVisible() && MotionEvent.ACTION_UP == event.getAction()
                && eventX > getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicHeight()) {
            setText("");
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据是否有内容和焦点以及设置的显示状态改变右边图片的显示状态
     *
     * @param text
     * @param hasFocus
     */
    private void changeDelShowStatu(Editable text, boolean hasFocus) {
        if (ALWAYS_SHOW == mDelIconTime) { // 总是显示
            mClearDrawable.setVisible(true, false);
        } else if (HAS_CONTENT_SHOW == mDelIconTime) { // 有内容就显示
            if (null != text && !TextUtils.isEmpty(text.toString()))
                mClearDrawable.setVisible(true, false);
            else mClearDrawable.setVisible(false, false);
        } else if (HAS_CONTENT_FOUCUS_SHOW == mDelIconTime) { // 有内容并且有焦点时显示
            if (null != text && !TextUtils.isEmpty(text.toString()) && hasFocus)
                mClearDrawable.setVisible(true, false);
            else mClearDrawable.setVisible(false, false);
        } else { // 总是隐藏
            mClearDrawable.setVisible(false, false);
        }
        setCompoundDrawables(mCompoundDrawables[0], mCompoundDrawables[1],
                mClearDrawable.isVisible() ? mClearDrawable : null,
                mCompoundDrawables[3]);
    }

    /**
     * 文字改变监听
     */
    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mPreLength = s.length();
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 当没有分割符位置的时候，表示就是普通的EditText
            if (null == mSplitPosition || mSplitPosition.length == 0) {
                if (null != mOnTextChangeListener)
                    mOnTextChangeListener.onTextChanged(s, start, before, count);
                return;
            }
            // 当没有分割符的时候，表示就是普通的EditText
            if (TextUtils.isEmpty(mSplitChar + "")) {
                if (null != mOnTextChangeListener)
                    mOnTextChangeListener.onTextChanged(s, start, before, count);
                return;
            }
            mCurrentLen = s.toString().length();
            if (mCurrentLen > 0) {
                // 设置了最大值，或者设置了模板，并且已经操过了最大值，输入的值无效
                if (mMaxLength > 0 && mCurrentLen > mMaxLength) {
                    getText().delete(mCurrentLen - 1, mCurrentLen);
                    return;
                }
                for (int i = 0; i < mSplitPosition.length; i++) {
                    if (mCurrentLen == mSplitPosition[i]) {
                        if (mCurrentLen > mPreLength) {
                            // 正在增加内容
                            removeTextChangedListener(mTextWatcher);
                            mTextWatcher = null;
                            getText().insert(mCurrentLen, mSplitChar + "");
                        } else {
                            // 正在删除内容
                            removeTextChangedListener(mTextWatcher);
                            mTextWatcher = null;
                            getText().delete(mCurrentLen - 1, mCurrentLen);
                        }
                    }

                    if (mTextWatcher == null) {
                        mTextWatcher = new MyTextWatcher();
                        addTextChangedListener(mTextWatcher);
                    }
                }
            }

            if (null != mOnTextChangeListener)
                mOnTextChangeListener.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            changeDelShowStatu(s, isFocused());
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.afterTextChanged(s);
        }
    }

    /**
     * 提供给开发者调用的文字改变监听
     */
    public interface OnTextChangeListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    /**
     * 提供给开发者调用的焦点改变监听
     */
    public interface OnMyFocusChangeListener {
        void onFocusChange(View view, boolean hasFocus);
    }
}
