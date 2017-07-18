package com.renj.xedittext.edittext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-18   10:30
 * <p>
 * 描述：EditText过滤工具类，在对EditText进行输入的时候过滤掉不需要的内容，同时也可以弹出自定义Toast尽心提示。<br/>
 * <b>注意：</b><br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * <b>使用正则表达式过滤时，输入内容和正则表达式匹配成功的不过滤，匹配失败的将被过滤</b>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class EditTextFilter {
    /**
     * 最大长度
     */
    private int mMaxLength;
    /**
     * 超过最大长度时提示的消息
     */
    private String mMaxMsg;
    /**
     * 是否过滤回车键
     */
    private boolean isFilterEnter;
    /**
     * 是否过滤删除键
     */
    private boolean isFilterDel;
    /**
     * 过滤正正则表达式和相对应的提示信息
     */
    private Map<String, String> mRegMsgs;
    /**
     * EditText过滤器对象
     */
    private InputFilter mFilter;
    /**
     * 上下文
     */
    private Context mContext;

    private EditTextFilter(int maxLength, String maxMsg, boolean isFilterEnter,
                           boolean isFilterDel, Map<String, String> regMsgs) {
        this.mMaxLength = maxLength;
        this.mMaxMsg = maxMsg;
        this.isFilterEnter = isFilterEnter;
        this.isFilterDel = isFilterDel;
        this.mRegMsgs = regMsgs;
        this.mFilter = createInputFilter();
    }

    /**
     * 对 {@link EditText} 开启过滤模式
     *
     * @param editText 需要开启过滤的 {@link EditText}
     */
    public void startFilter(EditText editText) {
        if (null == editText)
            throw new NullPointerException("EditText 不能为 null");
        this.mContext = editText.getContext();
        editText.setFilters(new InputFilter[]{mFilter});
    }

    /**
     * 创建一个 {@link InputFilter} 对象
     *
     * @return {@link InputFilter} 对象
     */
    @NonNull
    private InputFilter createInputFilter() {
        return new InputFilter() {
            // 返回null时表示不管，让系统处理，返回 "" 表示过滤掉了
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int length = source.length();
                int length2 = dest.length();
                // 表示过滤删除
                if (isFilterDel && length == 0) {
                    return "";
                }
                // 表示过滤回车
                if (isFilterEnter && length == 1 && source.charAt(0) == 10) {
                    return "";
                }
                // 控制最大长度
                if ((mMaxLength != -1) && (length + length2 > mMaxLength)) {
                    if (!TextUtils.isEmpty(mMaxMsg))
                        Toast.makeText(mContext, mMaxMsg, Toast.LENGTH_SHORT).show();
                    return "";
                }

                // 过滤指定的正则表达式
                if (mRegMsgs.size() > 0) {
                    Set<String> strings = mRegMsgs.keySet();
                    for (String string : strings) {
                        Pattern p = Pattern.compile(string);
                        Matcher m = p.matcher(source);
                        if (m.matches()) {
                            return null;
                        } else {
                            String str = mRegMsgs.get(string);
                            if (!TextUtils.isEmpty(str))
                                Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            }
        };
    }

    public static class Builder {
        private int maxLength = -1;
        private String maxMsg = null;
        private boolean isFilterEnter = false;
        private boolean isFilterDel = false;
        private Map<String, String> regMsgs = new HashMap<>();

        /**
         * 设置最大长度
         *
         * @param maxLength 最大长度
         * @param maxMsg    超过长度时的提示信息，不需要提示时可以传 "" 或 {@code null}
         * @return
         */
        public Builder maxLength(int maxLength, String maxMsg) {
            this.maxLength = maxLength;
            this.maxMsg = maxMsg;
            return this;
        }

        /**
         * 是否过滤回车键
         *
         * @param isFilterEnter true：过滤  false：不过滤
         * @return
         */
        public Builder isFilterEnter(@NonNull boolean isFilterEnter) {
            this.isFilterEnter = isFilterEnter;
            return this;
        }

        /**
         * 是否过滤删除键
         *
         * @param isFilterDel true：过滤  false：不过滤
         * @return
         */
        public Builder isFilterDel(@NonNull boolean isFilterDel) {
            this.isFilterDel = isFilterDel;
            return this;
        }

        /**
         * 添加单个的过滤正则表达式。<br/>
         * <b>注意：</b><br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         * <b>使用正则表达式过滤时，输入内容和正则表达式匹配成功的不过滤，匹配失败的将被过滤</b>
         *
         * @param reg    正则表达式
         * @param regMsg 当输入内容和正则表达式不匹配时的提示信息，不需要提示时可以传 "" 或 {@code null}
         * @return
         */
        public Builder putReg(@NonNull String reg, String regMsg) {
            regMsgs.put(reg, regMsg);
            return this;
        }

        /**
         * 添加过滤正则表达式集合。<br/>
         * <b>注意：</b><br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         * <b>使用正则表达式过滤时，输入内容和正则表达式匹配成功的不过滤，匹配失败的将被过滤</b>
         *
         * @param regMsgs 一组过滤正则表达式，map的键用正则表达式表示，值表示 不匹配时的提示信息，不需要提示时可以传 "" 或 {@code null}
         * @return
         */
        public Builder regMsgs(@NonNull Map<String, String> regMsgs) {
            this.regMsgs.putAll(regMsgs);
            return this;
        }

        /**
         * 根据正则表达式(map集合的键)移除单个的过滤正则表达式
         *
         * @param reg
         */
        public void removeReg(@NonNull String reg) {
            this.regMsgs.remove(regMsgs);
        }

        /**
         * 清除所有的正则表达式过滤
         */
        public void clearReg() {
            this.regMsgs.clear();
        }

        /**
         * 构建一个 {@link EditTextFilter} 对象
         *
         * @return {@link EditTextFilter} 对象
         */
        public EditTextFilter build() {
            return new EditTextFilter(maxLength, maxMsg, isFilterEnter, isFilterDel, regMsgs);
        }
    }
}
