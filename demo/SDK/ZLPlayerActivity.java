package com.zulong.test;

import com.unity3d.player.UnityPlayerActivity;

import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;
import android.content.Intent;

import com.zulong.unisdk.UCSDK;
import com.zulong.sdk.core.open.SDKBase;
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
		SDKBase.INSTANCE = new UCSDK();
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
			// 支付代码
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
		/** 游戏传入的外部订单号，每笔订单请保持订单号唯一；String */
		/**
		 * appication's order serial number,please be unique for each request to
		 * doPay() method;String
		 */
		orderParams.setOrderNum(orderNumber);
		/**
		 * 金额，以分为单位；建议传入100分的整数倍，因为有些渠道(例如百度多酷、酷狗)以元为单位，若传入金额不是100分的整数倍，
		 * 则这些渠道包无法支付；int
		 */
		/**
		 * price,unit is RMB Fen. some channel's paying unit is RMB Yuan,but
		 * OneSDK's is RMB Fen,so you'd better set price to An integer multiple
		 * of 100;int
		 */
		orderParams.setPrice(price);
		/** 游戏服务器id，每一个服务器id在Onesdk后台对应一个支付通知地址，默认为0；int */
		/**
		 * game server id,in sdk developer center, you can config a payment
		 * notify url for each server id(1 notify url,1 server id);int
		 */
		orderParams.setServerId(1);
		/** 游戏币与人民币兑换比率,例如100为1元人民币兑换100游戏币；int */
		/**
		 * exchange rate between game currency and RMB Yuan,for example:100
		 * means 1 RMB Yuan could rate 100 game currency;int
		 */
		orderParams.setExchangeRate(100);
		/** 商品id；String */
		/**
		 * product Id;String
		 */
		orderParams.setProductId("1");
		/** 商品名称；String */
		/**
		 * 由于oppo、益玩（益玩横屏和益玩竖屏）渠道商品名称显示的特殊性，因此增加渠道判断，如果渠道号为8（oppo）、50（益玩横屏）、51（
		 * 益玩竖屏）则特殊处理商品名称
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
			orderParams.setProductName("元宝礼包");
		} else {
			orderParams.setProductName("1000元宝礼包");
		}
		/** 商品描述；String */
		/** product description;String */
		orderParams.setProductDesc("1000元宝礼包的商品描述");
		/** 附加字段；放在附加字段中的值，OneSDK服务器端会不作任何修改通过服务器通知透传给游戏服务器；String */
		/**
		 * extra data,sdk server will pass 'extra data' via sdk server notify
		 * with modify nothing;String
		 */
		orderParams.setExt("附加字段");
		/** 用户游戏币余额，如果没有账户余额，请填0;String */
		/**
		 * user game currency balance,if your game doesn't have this params set
		 * 0(zero) please;String
		 */
		orderParams.setBalance("1000");
		/** vip等级，如果没有，请填0；String */
		/**
		 * user vip level,if your game doesn't have this params set 0(zero)
		 * please;String
		 */
		orderParams.setVip("vip0");
		/** 角色等级，如果没有，请填0；String */
		/**
		 * user role level,if your game doesn't have this params set 0(zero)
		 * please;String
		 */
		orderParams.setLv("20");
		/** 工会、帮派名称，如果没有，请填0；String */
		/**
		 * party(a faction of users) name,if your game doesn't have this params
		 * set 0(zero) please;String
		 */
		orderParams.setPartyName("鏖战群雄帮会");
		/** 角色名称；String */
		/**
		 * role name;String
		 */
		orderParams.setRoleName("meteor");
		/** 角色id；String */
		/**
		 * role id;String
		 */
		orderParams.setRoleId("123456");
		/** 所在服务器名称；String */
		/**
		 * server name which the role in;String
		 */
		orderParams.setServerName("1区服务器");
		/** 公司名称；String */
		/**
		 * company name;String
		 */
		orderParams.setCompany("pwrd");
		/** 货币名称；String */
		/**
		 * game currency name;String
		 */
		orderParams.setCurrencyName("元宝");

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
					/** 在此进行游戏的资源释放、退出游戏等操作 */
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