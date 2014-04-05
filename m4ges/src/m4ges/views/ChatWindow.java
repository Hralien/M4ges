package m4ges.views;


import java.io.IOException;
import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public  class ChatWindow{
	private Window window;
	private Skin skin;
	private List messageList;
	private List nameList;
	private TextButton envoyer;
	private TextField tfMessage;

	public ChatWindow(final MyGame mygame){
		String[] tabUsers = {};
		String[] tabMessage = {};

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		tfMessage = new TextField("", skin);
		tfMessage.setMessageText("Saisir votre message");
		envoyer = new TextButton("Envoyer",skin);
		envoyer.pad(5);
		//ajout d'un listener lors du clic
		envoyer.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (getSendText().length() == 0) return;
				try {
					mygame.mc.envoieMessage(getSendText());
				} catch (IOException e) {
					e.printStackTrace();
				}
				tfMessage.setText("");
			}
		});
		
		messageList = new List(tabUsers, skin);
		nameList = new List(tabMessage, skin);

		ScrollPane scrollPaneMessage = new ScrollPane(messageList,skin);
		scrollPaneMessage.setFlickScroll(true);

		ScrollPane scrollPaneUser = new ScrollPane(nameList, skin);

		scrollPaneUser.setFlickScroll(true);
		SplitPane splitPane = new SplitPane(scrollPaneMessage, scrollPaneUser, false, skin, "default-horizontal");

		float width = Constants.VIEWPORT_GUI_WIDTH;
		float height = Constants.VIEWPORT_GUI_HEIGHT;

		window = new Window("Chat", skin);
		window.setPosition(width*5, 100);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.row();
		window.add(splitPane).minWidth((float) (width*.5)).expandX().fillX().minHeight((float) (height*.3));
		window.row();
		window.add(tfMessage).minWidth((float) (width*.5)).expandX().fillX().colspan(10);
		window.row();
		window.add(envoyer);
		window.row();
		window.pack();



	}

	public Window getWindow(){
		return window;
	}

	public void setSendListener (final Runnable listener) {
		envoyer.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (getSendText().length() == 0) return;
				listener.run();
				tfMessage.setText("");
			}
		});
	}


	public String getSendText () {
		return tfMessage.getText().trim();
	}

	public void setNames (final String[] names) {
		// This listener is run on the client's update thread, which was started by client.start().
		// We must be careful to only interact with Swing components on the Swing event thread.
		//		EventQueue.invokeLater(new Runnable() {
		//			public void run () {
		nameList.setItems(names);
		//			}
		//		});
	}

	public void addMessage (final String message) {
		ArrayList<String> listMessage= new ArrayList<String>();
		for(String it: messageList.getItems()){
			listMessage.add(it);
		}
		listMessage.add(message);
		messageList.setItems(listMessage.toArray());
	}

	public void addName(String name) {
		String[] tmp = this.nameList.getItems();
		String[] names = new String[tmp.length+1];
		for(int i = 0; i < tmp.length; i ++)
			names[i] = tmp[i];
		names[tmp.length]=name;
		this.setNames(names);
	}

	public void removeName(String name) {
		String[] tmp = this.nameList.getItems();
		String[] names = new String[tmp.length - 1];
		int i = 0;
		int j = 0;
		boolean trouve = false;
		while(j < names.length){
			if(tmp[i].equals(name)){
				i++;
				trouve = true;
			}
			else{
				names[j] = tmp[i];
				i++;
				j++;
			}
		}
		
		if(trouve)
			this.setNames(names);
	}
}