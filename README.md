# unity-emptyproj-android

UniSDK�Ĵ�����demo/SDKĿ¼��

�����²�������Լ���sdk

.	��UniSDK.config�������滻���Լ������ݣ���ʽ�μӴ��ļ�ԭʼ����
	��"channel"Ϊkey��value������ͨ��SDKBase.getXXXConfig��ȡ
	
. ��SDKBase�����Լ������࣬ȡ��CommonXXX  ��XXX�Լ����壩

. ����initImpl��������Ҫ�����sdk����������getInitConfig���ȡ

. ����doLoginImpl�������ڽ����sdk����token��userid����Ҫ���ã�
				  	getAccount().setToken(token);
  					getAccount().setUserId(userid);
  ���Լ�ʵ��loginTask�ӿڣ��������ͨ��msg����
  
. ����doLogoutImpl����

. ����doPayImpl����

. UniSDK��ʹ�÷�����ZLPlayerActivity.java


����Ϊ����sdk�����������඼��Common��ͷ��������ӵ�build_common.bat�С��������ɵ�common.jar�滻Assets/Plugins/Android�µ�common.jar ��
�������Դ�������ޱ�Ҫ���벻Ҫ�޸ġ�