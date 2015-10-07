package com.zulong.test;

import com.unity3d.player.UnityPlayerActivity;

import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;
import android.content.Intent;

import com.zulong.unisdk.CommonSDK;
import com.zulong.sdk.core.open.SDKBase;
import com.zulong.sdk.core.open.SDKBase.UserInfoType;
import com.zulong.sdk.core.open.UserInfo;
import com.zulong.sdk.core.open.SDKInterface;
import com.zulong.sdk.core.util.LogUtil;
import com.zulong.sdk.core.param.OrderParams;


public class ZLPlayerActivity extends UnityPlayerActivity {
	
	private static final int UNISDK_APPID = 1001;
	private static final String UNISDK_APPKEY = "abcdefg";
	private Context mContext = ZLPlayerActivity.this;
	private static final String TAG = ZLPlayerActivity.class.getName();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LogUtil.setLOG(true);
		SDKBase.getInstance(this);
		
		LogUtil.d("ZLPlayerActivity","onCreate called!");
		
		SDKBase.getInstance(this).init(UNISDK_APPID, UNISDK_APPKEY, new SDKInterface.InitCallBack() {
			@Override
			public void initSucceed(String extraJson) {
				LogUtil.d(TAG, "initSucceed");
				Toast.makeText(mContext, "initSucceed, extraJson:" + extraJson, Toast.LENGTH_LONG).show();
			}

			@Override
			public void initFailed(String reason) {
				LogUtil.d(TAG, "initFailed");
				Toast.makeText(mContext, "initFailed, reason:" + reason, Toast.LENGTH_LONG).show();
			}
		});
		
		SDKBase.getInstance(this).setLogoutCallBack(new SDKInterface.LogoutCallBack() {

			@Override
			public void succeed() {
				Toast.makeText(getApplicationContext(), "logout succeed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void failed(String msg) {
				Toast.makeText(getApplicationContext(), "logout failed,msg: " + msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	protected void login() {
		SDKBase.getInstance(this).doLogin(new SDKInterface.LoginCallBack() {

			@Override
			public void succeed(String userId, String token, String password, String msg) {

				Toast.makeText(
						mContext,
						"userId: " + userId + "\n" + "token: " + token + "\n" + "password: " + password + "\n"
								+ "msg: " + msg, Toast.LENGTH_LONG).show();
								
								
				UserInfo userInfo = new UserInfo();
				userInfo.setRoleId("312345");
				userInfo.setRoleName("tom");
				userInfo.setLv("99");
				userInfo.setZoneId(1);
				userInfo.setZoneName("��Ϸһ��");
				SDKBase.getInstance(ZLPlayerActivity.this).submitUserInfo(UserInfoType.CREATE_ROLE, userInfo,
						new SDKInterface.CompleteCallBack() {
	
							@Override
							public void onComplete() {
								Toast.makeText(mContext, "submitCreateRoleInfo complete", Toast.LENGTH_LONG).show();
							}
						});
			}

			@Override
			public void failed(String msg) {
				Toast.makeText(mContext, "login failed, msg:" + msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void cancelled() {
				Toast.makeText(mContext, "login cancelled", Toast.LENGTH_LONG).show();
			}
		});		
		
		
	}
	
	protected void logout() {
		SDKBase.getInstance(this).doLogout();
	}
	
  protected void pay() {
			// ֧������
			String orderNumber = "" + System.currentTimeMillis();
			Log.v(TAG, "GAME ORDER ID :" + orderNumber);
			//EditText payEditText = (EditText) findViewById(R.id.payEditText);
			//int price = Integer.valueOf(payEditText.getText().toString());
			int price = 6;
			Log.v(TAG, "PRICE :" + price);

			OrderParams orderParams = new OrderParams();
			setParamValues(orderNumber, price, orderParams);

			SDKBase.getInstance(this).doPay(orderParams, new SDKInterface.PayCallBack() {

				@Override
				public void succeed(String orderId, String msg) {
					Toast.makeText(mContext, "pay succeed, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				}

				@Override
				public void failed(String orderId, String msg) {
					Toast.makeText(mContext, "pay failed, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				}

				@Override
				public void cancelled(String orderId, String msg) {
					Toast.makeText(mContext, "pay cancelled, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				}

				@Override
				public void ordered(String orderId, String msg) {
					Toast.makeText(mContext, "pay ordered, orderId:" + orderId + "\n" + "msg: " + msg, Toast.LENGTH_LONG).show();
				}
			});  	
  }
	
	private void setParamValues(String orderNumber, int price, OrderParams orderParams) {
		/** ��Ϸ������ⲿ�����ţ�ÿ�ʶ����뱣�ֶ�����Ψһ��String */
		/**
		 * appication's order serial number,please be unique for each request to
		 * doPay() method;String
		 */
		orderParams.setOrderNum(orderNumber);
		/**
		 * ���Է�Ϊ��λ�����鴫��100�ֵ�����������Ϊ��Щ����(����ٶȶ�ᡢ�ṷ)��ԪΪ��λ�����������100�ֵ���������
		 * ����Щ�������޷�֧����int
		 */
		/**
		 * price,unit is RMB Fen. some channel's paying unit is RMB Yuan,but
		 * UniSDK's is RMB Fen,so you'd better set price to An integer multiple
		 * of 100;int
		 */
		orderParams.setPrice(price);
		/** ��Ϸ������id��ÿһ��������id��UniSDK��̨��Ӧһ��֧��֪ͨ��ַ��Ĭ��Ϊ0��int */
		/**
		 * game server id,in sdk developer center, you can config a payment
		 * notify url for each server id(1 notify url,1 server id);int
		 */
		orderParams.setServerId(1);
		/** ��Ϸ��������Ҷһ�����,����100Ϊ1Ԫ����Ҷһ�100��Ϸ�ң�int */
		/**
		 * exchange rate between game currency and RMB Yuan,for example:100
		 * means 1 RMB Yuan could rate 100 game currency;int
		 */
		orderParams.setExchangeRate(100);
		/** ��Ʒid��String */
		/**
		 * product Id;String
		 */
		orderParams.setProductId("1");
		/** ��Ʒ���ƣ�String */
		/**
		 * ����oppo�����棨�������������������������Ʒ������ʾ�������ԣ�������������жϣ����������Ϊ8��oppo����50�������������51��
		 * ���������������⴦����Ʒ����
		 */
		/** product name;String */
		/**
		 * because of the channel "oppo" and "ewan" is different from the
		 * others,so if channelId is 8(oppo) or 50(ewanh:"ewan" horizontal
		 * version) or 51(ewanv:"ewan" vertical version),you need code specially
		 * like follows when set productName
		 */
		int channelId = SDKBase.getInstance(this).getChannelId();
		if (channelId == 8 || channelId == 50 || channelId == 51) {
			orderParams.setProductCount(1000);
			orderParams.setProductName("Ԫ�����");
		} else {
			orderParams.setProductName("1000Ԫ�����");
		}
		/** ��Ʒ������String */
		/** product description;String */
		orderParams.setProductDesc("1000Ԫ���������Ʒ����");
		/** �����ֶΣ����ڸ����ֶ��е�ֵ��UniSDK�������˻᲻���κ��޸�ͨ��������֪ͨ͸������Ϸ��������String */
		/**
		 * extra data,sdk server will pass 'extra data' via sdk server notify
		 * with modify nothing;String
		 */
		orderParams.setExt("�����ֶ�");
		/** �û���Ϸ�������û���˻�������0;String */
		/**
		 * user game currency balance,if your game doesn't have this params set
		 * 0(zero) please;String
		 */
		orderParams.setBalance("1000");
		/** vip�ȼ������û�У�����0��String */
		/**
		 * user vip level,if your game doesn't have this params set 0(zero)
		 * please;String
		 */
		orderParams.setVip("vip0");
		/** ��ɫ�ȼ������û�У�����0��String */
		/**
		 * user role level,if your game doesn't have this params set 0(zero)
		 * please;String
		 */
		orderParams.setLv("20");
		/** ���ᡢ�������ƣ����û�У�����0��String */
		/**
		 * party(a faction of users) name,if your game doesn't have this params
		 * set 0(zero) please;String
		 */
		orderParams.setPartyName("��սȺ�۰��");
		/** ��ɫ���ƣ�String */
		/**
		 * role name;String
		 */
		orderParams.setRoleName("meteor");
		/** ��ɫid��String */
		/**
		 * role id;String
		 */
		orderParams.setRoleId("123456");
		/** ���ڷ��������ƣ�String */
		/**
		 * server name which the role in;String
		 */
		orderParams.setServerName("1��������");
		/** ��˾���ƣ�String */
		/**
		 * company name;String
		 */
		orderParams.setCompany("pwrd");
		/** �������ƣ�String */
		/**
		 * game currency name;String
		 */
		orderParams.setCurrencyName("Ԫ��");

	}

	@Override
	protected void onStart() {
		super.onStart();
		SDKBase.getInstance(this).onStart(new SDKInterface.CompleteCallBack() {

			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		SDKBase.getInstance(this).onPause(new SDKInterface.CompleteCallBack() {
			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SDKBase.getInstance(this).onResume(new SDKInterface.CompleteCallBack() {
			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		SDKBase.getInstance(this).onRestart(new SDKInterface.CompleteCallBack() {

			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		SDKBase.getInstance(this).onStop(new SDKInterface.CompleteCallBack() {
			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SDKBase.getInstance(this).onDestroy(new SDKInterface.CompleteCallBack() {
			@Override
			public void onComplete() {
				/** VM exiting after onDestroy */
				System.exit(0);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		return SDKBase.getInstance(this).onKeyDown(keyCode, event, new SDKInterface.CompleteCallBack() {
			@Override
			public void onComplete() {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					/** �ڴ˽�����Ϸ����Դ�ͷš��˳���Ϸ�Ȳ��� */
					/**
					 * release your game's resource or exit game etc
					 */
					//finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SDKBase.getInstance(this).onActivityResult(requestCode, resultCode, data, new SDKInterface.CompleteCallBack() {

			@Override
			public void onComplete() {

			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SDKBase.getInstance(this).onNewIntent(intent, new SDKInterface.CompleteCallBack() {

			@Override
			public void onComplete() {

			}
		});
	}



	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			LogUtil.d(TAG,"on back button");
			if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0)
				Toast.makeText(mContext, "click me", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
} 