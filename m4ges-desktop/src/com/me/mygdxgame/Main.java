package com.me.mygdxgame;

import m4ges.controllers.MyGame;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;



public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "M4GES";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		cfg.addIcon("data/ic_m4ges.png", FileType.Internal);

		new LwjglApplication(new MyGame(new JavaHelp(cfg)), cfg);


	}
}
