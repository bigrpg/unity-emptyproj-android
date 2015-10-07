package com.zulong.sdk.CInterface;


public class  PayParams
{
	public String 	orderId;	//������
	public int 	price;				//�۸��Է�Ϊ��λ
	public int 	serverId;			//��Ϸ������id��ÿһ��������id��UniSDK��̨��Ӧһ��֧��֪ͨ��ַ��Ĭ��Ϊ0��int
	public int	exchangeRate;
	public String		productId;
	public int			productCount;
	public String 	productName;
	public String 	productDesc;
	public String		balance;				//�û���Ϸ�������û���˻�������0;String
	public String		vip;						//vip�ȼ������û�У�����0��String
	public String 	level;					//��ɫ�ȼ������û�У�����0��String
	public String 	partyName;			
	public String 	roleName;
	public String 	roleId;					//��ɫid��String
	public String		serverName;	
	public String		company;
	public String 	currencyName;		//�������ƣ�String
	public String		ext;						//�����ֶΣ����ڸ����ֶ��е�ֵ��UniSDK�������˻᲻���κ��޸�ͨ��������֪ͨ͸������Ϸ��������String
	
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