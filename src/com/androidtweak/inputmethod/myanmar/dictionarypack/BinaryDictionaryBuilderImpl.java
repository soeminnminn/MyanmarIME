package com.androidtweak.inputmethod.myanmar.dictionarypack;

import com.androidtweak.inputmethod.myanmar.MyanmarImeLogger;

import android.content.Context;
import android.util.Log;

public class BinaryDictionaryBuilderImpl 
	implements DictionaryBuilder {
	
	private static final String TAG = BinaryDictionaryBuilderImpl.class.getSimpleName();
	
	private final String mAssetsFilename;
    private final String mDescription;
    private final int mDictionaryResourceId;
    private final String mId;
    private String mLanguage;
    private final int mNameId;
    private final Context mPackageContext;

    public BinaryDictionaryBuilderImpl(Context context, String id, String language, 
    		int nameId, String assetsFilename, int resId, String description) {
    	
      this.mId = ("dictionary_" + id);
      this.mLanguage = language;
      this.mNameId = nameId;
      this.mDescription = description;
      this.mAssetsFilename = assetsFilename;
      this.mPackageContext = context;
      this.mDictionaryResourceId = resId;
      if (MyanmarImeLogger.sDBG)
        Log.d(TAG, "Creator for " + this.mLanguage + " with id " + this.mId + " assets:" + this.mAssetsFilename + " package:" + this.mPackageContext.getPackageName());
    }

    public String getDescription() {
      return this.mDescription;
    }

    public String getDictionaryName() {
      return this.mPackageContext.getString(this.mNameId);
    }

    public int getDictionaryResourceId() {
      return this.mDictionaryResourceId;
    }

    public String getId() {
      return this.mId;
    }

    public String getLanguage() {
      return this.mLanguage;
    }

    public Context getPackageContext() {
      return this.mPackageContext;
    }
}
