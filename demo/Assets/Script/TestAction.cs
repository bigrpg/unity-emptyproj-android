using UnityEngine;
using System.Collections;

public class TestAction : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}



    public void onClickLogin()
    {
        Debug.Log("onClickLogin");
        var activity = new AndroidJavaClass("com.unity3d.player.UnityPlayer").GetStatic<AndroidJavaObject>("currentActivity");
        activity.Call("login");

    }

    public void onClickLogout()
    {
        Debug.Log("onClickLogout");
        var activity = new AndroidJavaClass("com.unity3d.player.UnityPlayer").GetStatic<AndroidJavaObject>("currentActivity");
        activity.Call("logout");
    }

    public void onClickPay()
    {
        Debug.Log("onClickPay");
        var activity = new AndroidJavaClass("com.unity3d.player.UnityPlayer").GetStatic<AndroidJavaObject>("currentActivity");
        activity.Call("pay");
    }
}
