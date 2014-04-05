package com.me.mygdxgame;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import m4ges.controllers.UITrick;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Input.Keys;




public class JavaHelp implements UITrick{

	LwjglApplicationConfiguration cfg;

	public JavaHelp(LwjglApplicationConfiguration cfg) {
		this.cfg = cfg;
	}


	@Override
	public void showToast(CharSequence toastMessage, int toastDuration,Stage stage) {
		//		JOptionPane.showMessageDialog(new JFrame("Message"), toastMessage);
		new Dialog("Some Text", new Skin(Gdx.files.internal("data/uiskin.json")), "dialog") {
			protected void result (Object object) {
			}
		}.text(toastMessage.toString()).button("Continuer", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
	}


	@Override
	public void showAlertBox(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText, Stage stage) {
		if(stage==null){
			System.err.println(alertBoxMessage);
		}
		else
			new Dialog(alertBoxTitle, new Skin(Gdx.files.internal("data/uiskin.json")), "dialog") {
			protected void result (Object object) {
			}
		}.text(alertBoxMessage).button(alertBoxButtonText, true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
	}
	@Override
	public void openUri(String uri) {
		// TODO Auto-generated method stub

	}


	@Override
	public String getMacAddress() {
		InetAddress ip;
		String macAddress="";
		try {

			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			System.out.print("Current MAC address : ");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			macAddress = sb.toString();
			System.out.println(macAddress);

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (SocketException e){

			e.printStackTrace();

		}
		return macAddress;
	}


	@Override
	public int[] getScreenSize() {
		int[] size={cfg.width,cfg.height};
		return size;
	}





}
