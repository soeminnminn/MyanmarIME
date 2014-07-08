package com.androidtweak.inputmethod.myanmar.dictionarypack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.androidtweak.inputmethod.myanmar.MyanmarImeLogger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

public class DictionaryPackage {
	
	private static final String TAG = DictionaryPackage.class.getSimpleName(); 
	
	private static final String XML_ASSETS_ATTRIBUTE = "dictionaryAssertName"; 
	private static final String XML_DESCRIPTION_ATTRIBUTE = "description";
	private static final String XML_DICTIONARIES_TAG = "Dictionaries";
	private static final String XML_DICTIONARY_TAG = "Dictionary";
	private static final String XML_ID_ATTRIBUTE = "id";
	private static final String XML_LANGUAGE_ATTRIBUTE = "locale";
	private static final String XML_NAME_RES_ID_ATTRIBUTE = "nameResId";
	private static final String XML_RESOURCE_ATTRIBUTE = "dictionaryResourceId";
	
	private static ArrayList<DictionaryBuilder> ms_creators = null;
	
	/** @ deprecated */
	public static ArrayList<DictionaryBuilder> getAllBuilders(Context context) {
		try {
	      if (ms_creators == null) {
	        ms_creators.addAll(getAllExternalDictionaryBuilders(context));
	      }
	      return ms_creators;
	    } finally {
	    }
	  }
	
	private static ArrayList<DictionaryBuilder> getAllExternalDictionaryBuilders(Context context) {
		List<ResolveInfo> list = context.getPackageManager().queryBroadcastReceivers(new Intent(DictionaryBuilder.RECEIVER_INTERFACE), 128);
	    if (MyanmarImeLogger.sDBG)
	      Log.d(TAG, "Number of potential external dictionary packages found: " + list.size());
	    ArrayList<DictionaryBuilder> arrayList = new ArrayList<DictionaryBuilder>();
	    Iterator<ResolveInfo> iterator = list.iterator();
	    
	    while (iterator.hasNext()) {
	    	ResolveInfo info = (ResolveInfo)iterator.next();
	        if (info.activityInfo == null)
	          Log.e(TAG, "BroadcastReceiver has null ActivityInfo. Receiver's label is " + info.loadLabel(context.getPackageManager()));
	        else {
	          try {
	        	  arrayList.addAll(getDictionaryBuildersFromActivityInfo(context.createPackageContext(info.activityInfo.packageName, 128), info.activityInfo));
	          } catch (NameNotFoundException localNameNotFoundException) {
	            Log.e(TAG, "Did not find package: " + info.activityInfo.packageName);
	          }
	        }
	    }
	    
	    return arrayList;
	}
	
	private static ArrayList<DictionaryBuilder> getDictionaryBuildersFromActivityInfo(Context context, ActivityInfo activityInfo) { 
		return parseDictionaryBuildersFromXml(context, activityInfo.loadXmlMetaData(context.getPackageManager(), DictionaryBuilder.RECEIVER_META_DATA));
	}

	@SuppressWarnings("unused")
	private static ArrayList<DictionaryBuilder> getDictionaryBuildersFromResId(Context context, int id) {
	    return parseDictionaryBuildersFromXml(context, context.getResources().getXml(id));
	}
	
	private static ArrayList<DictionaryBuilder> parseDictionaryBuildersFromXml(Context context, XmlPullParser xmlPullParser) {
		ArrayList<DictionaryBuilder> arrayList = new ArrayList<DictionaryBuilder>();
		try {
			boolean start = false;
	        int tagType = xmlPullParser.next();
	        
	        while (tagType != XmlPullParser.END_DOCUMENT) {
	        	final String tagName = xmlPullParser.getName();
	        	
	        	switch(tagType) {
	        	case XmlPullParser.START_TAG: {
	        		if (tagName.equals(XML_DICTIONARIES_TAG)) {
	        			start = true;
	        		} else if (start && tagName.equals(XML_DICTIONARY_TAG)) {
	        			
	        			final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
	        			final String id = attributeSet.getAttributeValue(null, XML_ID_ATTRIBUTE);
	        			final String language = attributeSet.getAttributeValue(null, XML_LANGUAGE_ATTRIBUTE);
	        			final int nameResId = attributeSet.getAttributeResourceValue(null, XML_NAME_RES_ID_ATTRIBUTE, -1);
	        			final String assets = attributeSet.getAttributeValue(null, XML_ASSETS_ATTRIBUTE);
	        			final String description = attributeSet.getAttributeValue(null, XML_DESCRIPTION_ATTRIBUTE);
	        			final int resId = attributeSet.getAttributeResourceValue(null, XML_RESOURCE_ATTRIBUTE, -1);
	        			
	        			arrayList.add(new BinaryDictionaryBuilderImpl(context, id, language, nameResId, assets, resId, description));
	        		}
	        		break;
	        	}
	        		
	        	case XmlPullParser.END_TAG: {
	        		if (tagName.equals(XML_DICTIONARIES_TAG)) {
	        			start = false;
	        		}
	        		break;
	        	}
	        	
        		default:
        			break;
	        	}
	        	
	        	tagType = xmlPullParser.next();
	        } 
	        
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (XmlPullParserException xmlPullParserException) {
			xmlPullParserException.printStackTrace();
		}catch (NullPointerException nullPointerException) {
			nullPointerException.printStackTrace();
		}
		
		return arrayList;
	}
	
	/** @ deprecated */ 
	public static void resetBuildersCache() { 
		try {
	      ms_creators = null;
	    } finally {
	    }
	  }
}
