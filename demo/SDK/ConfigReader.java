package com.zulong.sdk.core.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Base64;
import com.zulong.sdk.core.param.Param;
import com.zulong.sdk.core.util.LogUtil;

import java.io.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ConfigReader
{
  private static final String TAG = ConfigReader.class.getName();
  private static final String META = "meta";
  private static final String INIT = "init";
  private static final String LOGIN = "login";
  private static final String PAY = "pay";
  private static final String COMMON = "common";
  private static final String CHANNEL = "channel";
  private static final String VERSION = "version";
  private static final String PLATFORM = "platform";
  private static final String CHANNEL_ID = "channelId";
  private static final String PACKAGE_NAME = "packageName";
  private static final String CUSTOM_SERVICE = "customService";
  private static final String SHARE_WEIXIN_ID = "shareWeixinId";
  private HashMap<String, Param> mInitConfig;
  private HashMap<String, Param> mLoginConfig;
  private HashMap<String, Param> mPayConfig;
  private HashMap<String, Param> mMeta;
  private Context mContext;
  private int mAppId;
  private String mAppKey;
  private String mVersion;

  public ConfigReader(Context context)
  {
    this.mContext = context;
  }

  public HashMap<String, Param> getInitConfig()
  {
    return this.mInitConfig;
  }

  public HashMap<String, Param> getLoginConfig()
  {
    return this.mLoginConfig;
  }

  public HashMap<String, Param> getPayConfig()
  {
    return this.mPayConfig;
  }

  protected HashMap<String, Param> readChannelInitParams(JSONObject initJson)
    throws JSONException
  {
    return new HashMap();
  }

  protected HashMap<String, Param> readChannelLoginParams(JSONObject loginJson)
    throws JSONException
  {
    return new HashMap();
  }

  protected HashMap<String, Param> readChannelPayParams(JSONObject payJson)
    throws JSONException
  {
    return new HashMap();
  }

  public void readConfigFileName(String configFileName)
    throws JSONException
  {
    readConfig(readFile(configFileName));
  }

  public void readConfigFilePath(String configFilePath)
    throws JSONException
  {
    readConfig(readFilePath(configFilePath));
  }

  private void readConfig(String configStr)
    throws JSONException
  {
    if (TextUtils.isEmpty(configStr))
    {
      LogUtil.e(TAG, "ConfigFile's content is null or \"\"");
      return;
    }
    String preDecode = decodeFromString(configStr);
    LogUtil.d(TAG, "File raw: " + preDecode);
    HashMap<String, String>  metamap = getMetaString(preDecode);
    this.mMeta = getMeta((String)metamap.get("meta"));
    this.mInitConfig = readInitParams((String)metamap.get("init"));
    this.mLoginConfig = readLoginParams((String)metamap.get("login"));
    this.mPayConfig = readPayParams((String)metamap.get("pay"));
    validatePackageName();
    validateVersion();
    validateParams();
  }

  private void validateParams()
  {
    validate(this.mMeta);
    validate(this.mInitConfig);
    validate(this.mLoginConfig);
    validate(this.mPayConfig);
  }

  private void validate(HashMap<String, Param> paramMap)
  {
    Iterator  it = paramMap.values().iterator();
    while (it.hasNext())
    {
      Param localParam;
      if (((localParam = (Param)it.next()) != null) && (localParam.isNotNull()) && (localParam.getValue() == null))
        throw new ConfigFileException("param: " + localParam.getName() + " is empty");
    }
  }

  private void validatePackageName()
  {
    if (!this.mContext.getPackageName().startsWith((String)((Param)this.mMeta.get("packageName")).getValue()))
      throw new ConfigFileException("package error, packageName in config is: " + ((Param)this.mMeta.get("packageName")).getValue() + " while packageName in game is: " + this.mContext.getPackageName());
  }

  private void validateVersion()
  {
    if (!((Param)this.mMeta.get("version")).getValue().equals(this.mVersion))
      throw new ConfigFileException("version error, config version is: " + ((Param)this.mMeta.get("version")).getValue() + " while the sdk's version is" + this.mVersion);
  }

  private HashMap<String, Param> readInitParams(String initStr)
    throws JSONException
  {
  	JSONObject json;
    Object localObject = (json = new JSONObject(initStr)).getJSONObject("channel");
    localObject = readChannelInitParams((JSONObject)localObject);
    json.getJSONObject("common");
    return (HashMap<String, Param>)localObject;
  }

  private HashMap<String, Param> readLoginParams(String loginStr)
    throws JSONException
  {
    JSONObject json = new JSONObject(loginStr);
    return readChannelLoginParams(json.getJSONObject("channel"));
  }

  private HashMap<String, Param> readPayParams(String payStr)
    throws JSONException
  {
    JSONObject json = new JSONObject(payStr);
    return readChannelPayParams(json.getJSONObject("channel"));
  }

  private HashMap<String, Param> getMeta(String meta)
    throws JSONException
  {
  	LogUtil.d(TAG,"meta string:"+meta);
    HashMap localHashMap = new HashMap();
    JSONObject json = new JSONObject(meta);
    localHashMap.put("version", new Param("version", json.getString("version"), true));
    localHashMap.put("platform", new Param("platform", String.valueOf(json.getInt("platform")), true));
    localHashMap.put("channelId", new Param("channelId", String.valueOf(json.getInt("channelId")), true));
    localHashMap.put("packageName", new Param("packageName", json.getString("packageName"), true));
    return localHashMap;
  }

  private HashMap<String, String> getMetaString(String raw)
    throws JSONException
  {
    HashMap localHashMap = new HashMap(4);
    JSONObject json = new JSONObject(raw);
    localHashMap.put("meta", json.getJSONObject("meta").toString());
    localHashMap.put("init", json.getJSONObject("init").toString());
    localHashMap.put("login", json.getJSONObject("login").toString());
    localHashMap.put("pay", json.getJSONObject("pay").toString());
    return localHashMap;
  }
  
  
  private String readContent(InputStream in)
  {  
  	try {
  		ByteArrayOutputStream out = new ByteArrayOutputStream();
  		byte[] buffer = new byte[4096];
  		int count = 0;
  		while((count = in.read(buffer)) > 0) {
  			out.write(buffer,0,count);
  		}
  		in.close();
  		LogUtil.d(TAG,"config:"+ out.toString());
  		return out.toString();
  	}
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  	catch(Exception e) {
  		LogUtil.e(TAG, "failed to read config file");
  	} 
  	return null;
  }

  private String readFile(String configFileName)
  {
    try
    {
      return readContent(this.mContext.getAssets().open(configFileName));
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }

  private String readFilePath(String filePath)
  {
    try
    {
      File localFile;
      if (!(localFile = new File(filePath)).exists())
        throw new FileNotFoundException("file not fond: " + filePath);
      return readContent(new FileInputStream(localFile));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }

  private String decodeFromString(String body)
  {
  	
		return body;
  }

  public void setBasic(int appId, String appKey, String version)
  {
    this.mAppId = appId;
    this.mAppKey = appKey;
    this.mVersion = version;
  }
}