package com.zulong.sdk.CInterface;


public class  LoginRet
{
	public int flag;
	public String 	userId;
	public String 	token;
	public String 	password;
	public String 	msg;
	
	LoginRet() {
		flag = 0;
		userId = "";
		token = "";
		password = "";
		msg = "";
	}
	
	
}