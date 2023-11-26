package com.haunt.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Make a Peanut Butter and Jelly Sandwich in the Haunted House Before Time Runs Out");
		config.setWindowIcon("assets/environment/bg/vase.png");
		config.setWindowedMode(1024, 768);
		new Lwjgl3Application(new HauntGame(), config);
	}
}
