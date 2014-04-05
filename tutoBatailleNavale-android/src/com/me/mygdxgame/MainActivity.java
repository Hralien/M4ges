package com.me.mygdxgame;

import m4ges.controllers.MyGame;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    MulticastLock multicastLock = null;
    WifiManager wifi = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		multicastLock = wifi.createMulticastLock("multicastLock");
		multicastLock.setReferenceCounted(true);
		multicastLock.acquire();
		
//		Thread thread = new Thread(new Runnable(){
//			@Override
//			public void run() {
//				AndroidHelp ah = new AndroidHelp(getBaseContext());
//				try {
//					ah.test();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		thread.start();
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		initialize(new MyGame(new AndroidHelp(this)), cfg);

	}
	@Override
    public void onDestroy()
    {
        super.onDestroy();
        multicastLock.release();
    }
}