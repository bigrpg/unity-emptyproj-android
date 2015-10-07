package com.zulong.sdk.CInterface;

import android.content.Context;
import android.app.Activity;
import android.widget.Toast;

import com.zulong.unisdk.CommonSDK;
import com.zulong.sdk.core.open.SDKBase;
import com.zulong.sdk.core.open.SDKBase.UserInfoType;
import com.zulong.sdk.core.open.UserInfo;
import com.zulong.sdk.core.open.SDKInterface;
import com.zulong.sdk.core.util.LogUtil;
import com.zulong.sdk.core.param.OrderParams;
import com.zulong.sdk.CInterface.LoginRet;
import com.zulong.sdk.CInterface.PayRet;
import com.zulong.sdk.CInterface.PayParams;


public class CInterface
{
		public static Activity mActivity;	
		public static CInterface currentCInterface;
		private static final String TAG = "unisdk";
		
		public native void onInit(int flag,String msg);
		public native void onLogout(int flag,String msg);
		public native void onLogin(LoginRet ret);
		public native void onPay(PayRet ret);
		
	
		public CInterface(Activity activity) {
    	mActivity = activity;	
    	currentCInterface = this;
    }
    
    public void SetDebug(Boolean debug) {
    	LogUtil.setLOG(debug);
    }
    
    public void init(int appId, String appKey) {
			SDKBase.getInstance(mActivity).init(appId, appKey, new SDKInterface.InitCallBack() {
				@Override
				public void initSucceed(String extraJson) {
					LogUtil.d(TAG, "initSucceed");
					Toast.makeText(mActivity, "initSucceed, extraJson:" + extraJson, Toast.LENGTH_LONG).show();
					onInit(0,extraJson);
				}
	
				@Override
				public void initFailed(String reason) {
					LogUtil.d(TAG, "initFailed");
					Toast.makeText(mActivity, "initFailed, reason:" + reason, Toast.LENGTH_LONG).show();
					onInit(-1,reason);
				}
			});  
		
			SDKBase.getInstance(mActivity).setLogoutCallBack(new SDKInterface.LogoutCallBack() {
	
				@Override
				public void succeed() {
					Toast.makeText(mActivity, "logout succeed", Toast.LENGTH_SHORT).show();
					onLogout(0,"");
				}
	
				@Override
				public void failed(String msg) {
					Toast.makeText(mActivity, "logout failed,msg: " + msg, Toast.LENGTH_SHORT).show();
					onLogout(-1,msg);
				}
			});
			
			LogUtil.d(TAG, "UniSDK  inited");
		  	
    }
    
    public void login() {
 			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					_login();
				}
			});	   	
    }
    
		public void _login() {
			SDKBase.getInstance(mActivity).doLogin(new SDKInterface.LoginCallBack() {
	
				@Override
				public void succeed(String userId, String token, String password, String msg) {
	
					Toast.makeText(
							mActivity,
							"userId: " + userId + "\n" + "token: " + token + "\n" + "password: " + password + "\n"
									+ "msg: " + msg, Toast.LENGTH_LONG).show();
									
					LoginRet  ret = new LoginRet();
					ret.flag = 0;
					ret.userId = userId;
					ret.token = token;
					ret.password = password;
					ret.msg = msg;
					onLogin(ret);				
				}
	
				@Override
				public void failed(String msg) {
					Toast.makeText(mActivity, "login failed, msg:" + msg, Toast.LENGTH_LONG).show();
					
					LoginRet  ret = new LoginRet();
					ret.flag = -1;
					ret.msg = msg;
					onLogin(ret);
				}
	
				@Override
				public void cancelled() {
					Toast.makeText(mActivity, "login cancelled", Toast.LENGTH_LONG).show();
					
					LoginRet  ret = new LoginRet();
					ret.flag = -2;
					onLogin(ret);
				}
		});		
	}    
	
	public void pay(final PayParams params) {
			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					_pay(params);
				}
			});			
		
	}
	
    public void logout() {
 			mActivity.runOnUiThread(new Runnable() {
				public void run() {
					SDKBase.getInstance(mActivity).doLogout();
				}
			});	   	
    }
	
	public void _pay(PayParams   params) {
		
		OrderParams orderParams = new OrderParams();
		
		orderParams.setOrderNum( params.orderId );
		orderParams.setPrice(params.price);
		orderParams.setServerId(params.serverId);
		orderParams.setExchangeRate(params.exchangeRate);
		orderParams.setProductId(params.productId);
		orderParams.setProductCount(params.productCount);
		orderParams.setProductName(params.productName);
		orderParams.setProductDesc(params.productDesc);
		orderParams.setBalance(params.balance);
		orderParams.setVip(params.vip);
		orderParams.setLv(params.level);
		orderParams.setPartyName(params.partyName);
		orderParams.setRoleName(params.roleName);
		orderParams.setRoleId(params.roleId);
		orderParams.setServerName(params.serverName);
		orderParams.setCompany(params.company);
		orderParams.setCurrencyName(params.currencyName);
		orderParams.setExt(params.ext);
		
		SDKBase.getInstance(mActivity).doPay(orderParams, new SDKInterface.PayCallBack() {

			PayRet ret = new PayRet();
			
			@Override
			public void succeed(String orderId, String msg) {
				Toast.makeText(mActivity, "pay succeed, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				ret.flag = 0;
				ret.orderId = orderId;
				ret.msg = msg;
				onPay(ret);
			}

			@Override
			public void failed(String orderId, String msg) {
				Toast.makeText(mActivity, "pay failed, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				ret.flag = -1;
				ret.orderId = orderId;
				ret.msg = msg;
				onPay(ret);
			}

			@Override
			public void cancelled(String orderId, String msg) {
				Toast.makeText(mActivity, "pay cancelled, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				ret.flag = -2;
				ret.orderId = orderId;
				ret.msg = msg;
				onPay(ret);
			}

			@Override
			public void ordered(String orderId, String msg) {
				Toast.makeText(mActivity, "pay ordered, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				ret.flag = -3;
				ret.orderId = orderId;
				ret.msg = msg;
				onPay(ret);
			}
		});  	

	}
	
}