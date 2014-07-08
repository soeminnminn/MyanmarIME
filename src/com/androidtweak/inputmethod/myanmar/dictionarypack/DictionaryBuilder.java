package com.androidtweak.inputmethod.myanmar.dictionarypack;

import android.content.Context;

public interface DictionaryBuilder {
	public static final String RECEIVER_INTERFACE = "com.androidtweak.inputmethod.myanmar.DICTIONARY";
    public static final String RECEIVER_META_DATA = "com.androidtweak.inputmethod.myanmar.dictionaries";

    public abstract String getDescription();

    public abstract String getDictionaryName();

    public abstract int getDictionaryResourceId();

    public abstract String getId();

    public abstract String getLanguage();

    public abstract Context getPackageContext();
}
