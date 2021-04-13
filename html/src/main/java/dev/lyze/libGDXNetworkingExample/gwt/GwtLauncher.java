package dev.lyze.libGDXNetworkingExample.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.websocket.GwtWebSockets;
import dev.lyze.libGDXNetworkingExample.Main;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration configuration = new GwtApplicationConfiguration();
		return configuration;
	}

	@Override
	public ApplicationListener createApplicationListener() {
		// Initiating GWT web sockets module:
		GwtWebSockets.initiate();
		return new Main();
	}
}