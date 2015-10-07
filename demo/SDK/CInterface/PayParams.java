package com.zulong.sdk.CInterface;


public class  PayParams
{
	public String 	orderId;	//订单号
	public int 	price;				//价格，以分为单位
	public int 	serverId;			//游戏服务器id，每一个服务器id在UniSDK后台对应一个支付通知地址，默认为0；int
	public int	exchangeRate;
	public String		productId;
	public int			productCount;
	public String 	productName;
	public String 	productDesc;
	public String		balance;				//用户游戏币余额，如果没有账户余额，请填0;String
	public String		vip;						//vip等级，如果没有，请填0；String
	public String 	level;					//角色等级，如果没有，请填0；String
	public String 	partyName;			
	public String 	roleName;
	public String 	roleId;					//角色id；String
	public String		serverName;	
	public String		company;
	public String 	currencyName;		//货币名称；String
	public String		ext;						//附加字段；放在附加字段中的值，UniSDK服务器端会不作任何修改通过服务器通知透传给游戏服务器；String
	
	PayParams() {
		
		orderId = "";
		price = 0;
		serverId = 0;
		exchangeRate = 100;
		productId = "";
		productCount = 1;
		productName = "";
		productDesc = "";
		balance = "0";
		vip = "0";
		level = "0";
		partyName = "";
		roleName = "";
		roleId = "";
		serverName = "";
		company = "";
		currencyName = "";
		ext = "";
		
		
	}
}