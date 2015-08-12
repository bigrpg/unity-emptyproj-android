package com.zulong.sdk.core.open;

import android.app.Activity;
import java.util.HashMap;
import java.util.Timer;
import android.text.TextUtils;
import android.content.Context;
import org.json.JSONException;

import com.zulong.sdk.core.open.SDKInterface;
import com.zulong.sdk.core.util.LogUtil;
import com.zulong.sdk.core.util.Toast;
import com.zulong.sdk.core.config.ConfigReader;
import com.zulong.sdk.core.ui.floatview.FloatViewItem;

public abstract class SDKBase extends SDKImpl
{
	//for test only
	public static SDKBase  sdk = null;
  private static final String TAG = SDKBase.class.getName();
  private static final String DEFAULT_CONFIG_FILE_NAME = "UniSDK.config";
  private static final long INTERVAL = 2000L;
  private static Activity mActivity;
  private int mAppId;
  private String mAppKey;
  private int mChannelId;
  private String mChannelName;
  private volatile boolean mHasLogin = false;
  private volatile InitState mChannelInitState = InitState.process;
  private volatile InitState mOnesdkInitState = InitState.process;
  private volatile String mInitMsg;
  protected SDKInterface.InitCallBack mInitCallBack;
  protected SDKInterface.LoginCallBack mLoginCallBack;
  protected SDKInterface.PayCallBack mPayCallBack;
  protected SDKInterface.LogoutCallBack mLogoutCallBack;
  private ConfigReader mConfigReader;
  private String mConfigFilePath;
  private static volatile SDKBase INSTANCE;
  private String commonOrderMsg;
  private static final String FLASH_PIC_PORTRAIT = "common_flash_portrait.png";
  private static final String FLASH_PIC_LANDSCAPE = "common_flash_landscape.png";
  private static final int FLASH_TIMER_TIME_MILLISECEND = 3000;
  private HashMap<IntervalType, Long> lastTimeHashMap = new HashMap();

  //Status  
	private static enum InitState
  {
    success, process, fail;
  }

  private static enum IntervalType
  {
    INIT, LOGIN, PAY, LOGOUT;
  }

  public static enum UserInfoType
  {
    CREATE_ROLE, LOGIN, ROLE_LEVEL_CHANGE;
  }

  public static SDKBase getInstance(Activity activity)
  {
    mActivity = activity;
		return sdk;
  }

  public void init(int appId, String appKey, SDKInterface.InitCallBack initCallBack)
  {
    init(appId, appKey, DEFAULT_CONFIG_FILE_NAME, initCallBack);
  }

  public void init(final int appId,final String appKey,final String configFileName,final SDKInterface.InitCallBack initCallBack)
  {
    this.mInitCallBack = initCallBack;
    this.mAppId = appId;
    this.mAppKey = appKey;
    this.mChannelId = getChannelId();
    this.mChannelName = getChannelName();
    LogUtil.d(TAG, "channelId : " + this.mChannelId + "\nchannelName : " + this.mChannelName);
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        if (SDKBase.this.mInitCallBack == null)
          throw new RuntimeException("initCallBack is null");
        if (!SDKBase.this.checkInterval(SDKBase.IntervalType.INIT))
          return;
        try
        {
          //SDKCoreFacade.getInstance().init(SDKBase.mActivity, SDKBase.this.mAppId, SDKBase.this.mAppKey, SDKBase.this.mChannelId);
          //SDKCoreFacade.getInstance().setLoginSchemeVersion(SDKBase.this.getLoginSchemeVersion());
          SDKBase.this.readConfig(appId, appKey, SDKBase.this.getVersion(), configFileName, SDKBase.this.getConfigReader());
          SDKBase.this.tryFlash();
          SDKBase.this.doInitImpl();
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          SDKBase.this.initFailed("");
        }
      }
    });
  } 
  
  private void tryFlash()
  {
  	onOnesdkInitSuccess();
  	notifyInitResult();
  }
  
  private void onOnesdkInitSuccess()
  {
    this.mOnesdkInitState = InitState.success;
    notifyInitResult();
  } 
  
  protected void readConfig(int appId, String appKey, String version, String configFileName, ConfigReader configReader)
    throws JSONException
  {
    this.mConfigReader = configReader;
    this.mConfigReader.setBasic(appId, appKey, version);
    if (!TextUtils.isEmpty(this.mConfigFilePath))
    {
      this.mConfigReader.readConfigFilePath(this.mConfigFilePath);
      return;
    }
    this.mConfigReader.readConfigFileName(configFileName);
  }
  
  private void doInitImpl()
  {
    this.mChannelInitState = InitState.process;
    initImpl();
  }
  
  public void setConfigFilePath(String configFilePath)
  {
    this.mConfigFilePath = configFilePath;
  }
  
  public static Activity getActivity()
  {
    return mActivity;
  }
  
  protected void initSucceed(String msg)
  {
    this.mChannelInitState = InitState.success;
    this.mInitMsg = msg;
    notifyInitResult();
  }

  protected void initFailed(String msg)
  {
    this.mChannelInitState = InitState.fail;
    this.mInitMsg = msg;
    notifyInitResult();
  }
  
  private synchronized void notifyInitResult()
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        if ((SDKBase.this.mOnesdkInitState == SDKBase.InitState.success) && (SDKBase.this.mChannelInitState == SDKBase.InitState.success))
        {
          SDKBase.this.mInitCallBack.initSucceed(SDKBase.this.mInitMsg);
          return;
        }
        if ((SDKBase.this.mOnesdkInitState == SDKBase.InitState.fail) || (SDKBase.this.mChannelInitState == SDKBase.InitState.fail))
          SDKBase.this.mInitCallBack.initFailed(SDKBase.this.mInitMsg);
      }
    });
  }  
  
  //login
  public void doLogin(final SDKInterface.LoginCallBack loginCallBack)
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        if (loginCallBack == null)
          throw new RuntimeException("loginCallBack is null");
        SDKBase.this.mLoginCallBack = loginCallBack;
        if ((SDKBase.this.checkInterval(SDKBase.IntervalType.LOGIN)) && (SDKBase.this.checkInit()))
          SDKBase.this.doLoginImpl();
      }
    });
  }
  
  
  protected void loginSucceed(final String msg)
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        //SDKBase.access$1102(SDKBase.this, true);
        //SDKBase.this.mLoginCallBack.succeed(SDKBase.this.getUserId(), SDKBase.this.getToken(), SDKBase.this.getPassword(), msg);
        //SDKBase.this.showFloatView(SDKBase.getActivity(), SDKBase.this.getFloatViewPlace());
      }
    });
  }

  protected void loginFailed(final String msg)
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        SDKBase.this.mLoginCallBack.failed(msg);
      }
    });
  }

  protected void loginCancelled()
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        SDKBase.this.mLoginCallBack.cancelled();
      }
    });
  }
  
  //logout
  public void doLogout()
  {
    doLogout(this.mLogoutCallBack);
  }
  
  public void doLogout(final SDKInterface.LogoutCallBack logoutCallBack)
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        if (logoutCallBack == null)
          throw new RuntimeException("logoutCallBack is null");
        SDKBase.this.mLogoutCallBack = logoutCallBack;
        if ((SDKBase.this.checkInterval(SDKBase.IntervalType.LOGOUT)) && (SDKBase.this.checkInit()) && (SDKBase.this.checkLogin()))
          SDKBase.this.doLogoutImpl();
      }
    });
  }
  
  public void setLogoutCallBack(SDKInterface.LogoutCallBack logoutCallBack)
  {
    this.mLogoutCallBack = logoutCallBack;
  }
  
  protected void logoutSucceed()
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        //SDKBase.access$1102(SDKBase.this, false);
        SDKBase.this.mLogoutCallBack.succeed();
        SDKBase.this.dismissFloatView(SDKBase.getActivity());
      }
    });
  }

  protected void logoutFailed(final String msg)
  {
    getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        SDKBase.this.mLogoutCallBack.failed(msg);
      }
    });
  }  
  
  //GUI
  private void showFloatView(Context con, int floatViewPlace)
  {
    FloatViewItem[] arrayOfFloatViewItem;
    if ((arrayOfFloatViewItem = getFloatViewItemImpl()) != null)
    {
      //c.a().a(con, floatViewPlace, arrayOfFloatViewItem);
      return;
    }
    showFloatViewImpl();
  }

  private void dismissFloatView(Context con)
  {
    if (getFloatViewItemImpl() != null)
    {
      //c.a().a(con);
      return;
    }
    dismissFloatViewImpl();
  }

  private void destroyFloatView(Context con)
  {
    if (getFloatViewItemImpl() != null)
    {
      //c.a().b(con);
      return;
    }
    destroyFloatViewImpl();
  }

  protected void showFloatViewImpl()
  {
  }

  protected void dismissFloatViewImpl()
  {
  }

  protected void destroyFloatViewImpl()
  {
  }

  protected FloatViewItem[] getFloatViewItemImpl()
  {
    return null;
  }

  protected int getFloatViewPlace()
  {
    return 1;
  }
  
  
  //check functions
  private boolean checkLogin()
  {
    if (!this.mHasLogin)
      Toast.makeToast(getActivity(), "δ��¼");
    return this.mHasLogin;
  }
  
  private boolean checkInit()
  {
    if ((this.mOnesdkInitState != InitState.success) || (this.mChannelInitState != InitState.success))
      throw new RuntimeException("δ��ʼ��");
    return true;
  }
  
  private boolean checkInterval(IntervalType intervalType)
  {
    long l1 = System.currentTimeMillis();
    long l2 = null == this.lastTimeHashMap.get(intervalType) ? 0L : ((Long)this.lastTimeHashMap.get(intervalType)).longValue();
    if (l1 - l2 > 2000L)
    {
      this.lastTimeHashMap.put(intervalType, Long.valueOf(l1));
      return true;
    }
    this.lastTimeHashMap.put(intervalType, Long.valueOf(l1));
    LogUtil.e(TAG, "less than 2000 milliseconds between two requests," + intervalType);
    return false;
  }
  
  //login
  protected boolean isHasLogin()
  {
    return this.mHasLogin;
  }

  protected void setHasLogin(boolean hasLogin)
  {
    this.mHasLogin = hasLogin;
  }
  
  //Event callback
  public void onDestroy(SDKInterface.CompleteCallBack completeCallBack)
  {
    destroyFloatView(getActivity());
    completeCallBack.onComplete();
  }
  
  //
  protected abstract ConfigReader getConfigReader();
  public abstract int getChannelId();
  public abstract String getChannelName();
  protected abstract String getVersion();

 
}