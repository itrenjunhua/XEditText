package com.renj.xedittext.edittext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-08   16:02
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class XEditText extends android.support.v7.widget.AppCompatEditText {
    private static final char DEFAULT_SPLIT = ' ';
    private char mSplitChar = DEFAULT_SPLIT;
    private int[] mSplitPosition;
    private int mPreLength;
    private int mCurrentLen;
    private int maxLength;
    private MyTextWatcher mTextWatcher;

    private OnTextChangeListener mOnTextChangeListener;

    public XEditText(Context context) {
        this(context, null);
    }

    public XEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextWatcher = new MyTextWatcher();
        addTextChangedListener(mTextWatcher);

        setTemplet(new int[]{4, 4, 4, 4, 3});
        setSplitChar(' ');
    }

    public XEditText setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.mOnTextChangeListener = onTextChangeListener;
        return this;
    }

    public XEditText setSplitChar(@NonNull char mSplitChar) {
        if (TextUtils.isEmpty(mSplitChar + "")) return this;
        this.mSplitChar = mSplitChar;
        return this;
    }

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
        maxLength = temp;
        return this;
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mPreLength = s.length();
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null == mSplitPosition || mSplitPosition.length == 0) {
                if (null != mOnTextChangeListener)
                    mOnTextChangeListener.onTextChanged(s, start, before, count);
                return;
            }
            if (TextUtils.isEmpty(mSplitChar + "")) {
                if (null != mOnTextChangeListener)
                    mOnTextChangeListener.onTextChanged(s, start, before, count);
                return;
            }
            mCurrentLen = s.toString().length();
            if (mCurrentLen > 0) {
                if (maxLength > 0 && mCurrentLen > maxLength) {
                    getText().delete(mCurrentLen - 1, mCurrentLen);
                    return;
                }
                for (int i = 0; i < mSplitPosition.length; i++) {
                    if (mCurrentLen == mSplitPosition[i]) {
                        if (mCurrentLen > mPreLength) {
                            removeTextChangedListener(mTextWatcher);
                            mTextWatcher = null;
                            getText().insert(mCurrentLen, mSplitChar + "");
                        } else {
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
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.afterTextChanged(s);
        }
    }

    public interface OnTextChangeListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }
}
