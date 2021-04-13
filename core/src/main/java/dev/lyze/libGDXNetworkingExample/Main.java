package dev.lyze.libGDXNetworkingExample;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketException;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

public class Main extends Game
{
	private Stage stage;
	private WebSocket webSocket;

	private VisTextArea outputArea;
	private VisTextField inputNameField;
	private VisTextField inputMessageField;

	private VisTextButton sendButton;

	@Override
	public void create() {
		// Loading VisUI skin assets:
		VisUI.load();

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		setupUi();
		addListeners();

		connect();
	}

	private void setupUi()
	{
	    Table root = new Table();
	    root.defaults().pad(3);
	    root.setFillParent(true);

	    outputArea = new VisTextArea();
	    outputArea.setDisabled(true);
	    root.add(outputArea).colspan(3).grow().row();

		inputNameField = new VisTextField();
		root.add(inputNameField);

		inputMessageField = new VisTextField();
	    root.add(inputMessageField).growX();

	    sendButton = new VisTextButton("Send");
	    root.add(sendButton);

	    stage.addActor(root);
	}

	private void addListeners() {
	    sendButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
			    sendMessage();
			}
		});
	}

	public void connect() {
		String host = "localhost";
		int port = 9999;
		// webSocket = WebSockets.newSocket(WebSockets.toSecureWebSocketUrl(host, port));
		webSocket = WebSockets.newSocket(WebSockets.toWebSocketUrl(host, port));
		webSocket.addListener(getWebSocketListener());

		try {
			outputArea.appendText("Connecting.\n");
			webSocket.connect();
		} catch (WebSocketException exception) {
		    outputArea.appendText("Couldn't connect.\n");
		    outputArea.appendText(exception.getMessage() + "\n");
			Gdx.app.error("WebSocket", "Cannot connect.", exception);

			connect();
		}
	}

	private WebSocketListener getWebSocketListener() {
		return new WebSocketAdapter() {
			@Override
			public boolean onOpen(WebSocket webSocket) {
				outputArea.appendText("Connected.\n");
				return FULLY_HANDLED;
			}

			@Override
			public boolean onMessage(WebSocket webSocket, String packet) {
				outputArea.appendText(packet + "\n");
				return FULLY_HANDLED;
			}

			@Override
			public boolean onClose(WebSocket webSocket, int closeCode, String reason)
			{
			    outputArea.appendText("Disconnected: " + reason + "\n");
				return FULLY_HANDLED;
			}
		};
	}

	public void sendMessage() {
		String username = inputNameField.getText();
		String message = inputMessageField.getText();

		if (webSocket != null && webSocket.isOpen()) {
			webSocket.send(username + ": " + message);
			inputMessageField.clearText();
		}
	}

	public void disconnect() {
		if (webSocket != null) {
			try {
				webSocket.close();
			} catch (WebSocketException exception) {
				Gdx.app.log("WebSocket", "Unable to close web socket.", exception);
			} finally {
				webSocket = null;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		VisUI.dispose();
		stage.dispose();
		disconnect();
	}
}
