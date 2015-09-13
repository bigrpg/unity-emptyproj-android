package com.zulong.sdk.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

public class SDKFactory
{
  private static final String tag = SDKFactory.class.getName();		//a
  private String commonFullFilename;  //b    //full filename of common.jar
  private String commonFullDir;				//c
  private Activity activity;	//d
  private DexClassLoader loader;	//e
  private DexFile dexfile;  			//f
  private static volatile SDKFactory INSTANCE;

  public static SDKFactory instance()
  {
    if (INSTANCE == null)
      synchronized (SDKFactory.class)
      {
        if (INSTANCE == null)
          INSTANCE = new SDKFactory();
      }
    return INSTANCE;
  }	
	
	
  public void init(Activity paramActivity)
  {
    this.activity = paramActivity;
    this.commonFullFilename = (this.activity.getFilesDir().getAbsolutePath() + "/common.jar");
    this.commonFullDir = this.activity.getFilesDir().getAbsolutePath();
    copyCommonJar();
    this.loader = new DexClassLoader(this.commonFullFilename, this.commonFullDir, null, this.activity.getClassLoader());
    try
    {
      this.dexfile = DexFile.loadDex(this.commonFullFilename, this.commonFullDir + "/classes.dex", 0);
      return;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
	private void copyCommonJar()
  {
    InputStream localInputStream = null;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localInputStream = this.activity.getAssets().open("common.jar");
      File localFile = new File(this.commonFullFilename);
      localFileOutputStream = new FileOutputStream(localFile);
      byte[] arrayOfByte = new byte[1024];
      int i;
      while ((i = localInputStream.read(arrayOfByte)) != -1)
        localFileOutputStream.write(arrayOfByte, 0, i);
      localFileOutputStream.flush();
      try
      {
        localFileOutputStream.close();
        if (localInputStream != null)
          localInputStream.close();
        return;
      }
      catch (IOException localIOException1)
      {
        localIOException1.printStackTrace();
        return;
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      throw new RuntimeException("common.jar not found");
    }
    catch (IOException localIOException2)
    {
      try
      {
        if (localFileOutputStream != null)
          localFileOutputStream.close();
        if (localInputStream != null)
          localInputStream.close();
        return;
      }
      catch (IOException localIOException3)
      {
        localIOException3.printStackTrace();
        return;
      }
    }
    finally
    {
      try
      {
        if (localFileOutputStream != null)
          localFileOutputStream.close();
        if (localInputStream != null)
          localInputStream.close();
      }
      catch (IOException localIOException4)
      {
        localIOException4.printStackTrace();
      }
    }
  }
  
 @SuppressLint({"NewApi"})
  public <T> T create(Class<T> paramClass)
  {
    Enumeration localEnumeration = this.dexfile.entries();
    while (localEnumeration.hasMoreElements())
      try
      {
        Object localObject = (String)localEnumeration.nextElement();
        localObject = this.loader.loadClass((String)localObject);
        LogUtil.d(tag, ((Class)localObject).getName() + " >>will be cast to>> " + paramClass.getName());
        Constructor<T>  contruct = ((Class)localObject).asSubclass(paramClass).getConstructor();
        return contruct.newInstance();
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        localIllegalArgumentException.printStackTrace();
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
      }
      catch (InstantiationException localInstantiationException)
      {
        localInstantiationException.printStackTrace();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        localIllegalAccessException.printStackTrace();
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        localInvocationTargetException.printStackTrace();
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        localNoSuchMethodException.printStackTrace();
      }
      catch (ClassCastException localClassCastException)
      {
      }
    throw new RuntimeException("can't instantiation");
  }

}