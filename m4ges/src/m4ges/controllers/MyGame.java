package m4ges.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import m4ges.models.Personnage;
import m4ges.models.classes.Joueur;
import m4ges.util.GamePreferences;
import m4ges.views.BattleScreen;
import m4ges.views.ChatScreen;
import m4ges.views.CreateCharacterScreen;
import m4ges.views.EncyclopedieScreen;
import m4ges.views.FinalScreen;
import m4ges.views.LoadingScreen;
import m4ges.views.MenuPrincipalScreen;
import m4ges.views.ResultScreen;
import reseau.UnicastClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Ici on initialise tout les screen
 * Depart du jeu appelle dans les main
 * @author Florian
 *
 */
public class MyGame extends Game{


	private Hashtable<Integer,Screen> screenHashtable;

	public final static int LOADINGSCREEN = 0;
	public final static int MENUSCREEN = 1;
	public final static int NEWCHARACTERSCREEN = 2;
	public final static int CHATSCREEN = 3;
	public final static int BATTLESCREEN = 4;
	public final static int RESULTSCREEN = 5;
	public final static int DICOSCREEN = 6;
	public final static int FINALSCREEN = 7;


	/**
	 * le personnage du joueur initialise lors de la creation
	 */
	public Joueur player;
	/**
	 * interface qui permet l'appel a des methodes propres a android
	 */
	public UITrick androidUI;

	public ArrayList<String> listHost ;
	public ArrayList<Personnage> playersConnected;
	public UnicastClient mc;

	public int currentVagueIndex;

	public static AssetManager manager;

	public int currentScreen;

	public MyGame(UITrick actionResolver) {
		super();
		this.androidUI = actionResolver;
		listHost = new ArrayList<String>();
		playersConnected = new ArrayList<Personnage>();
		manager = new AssetManager();

	}


	@Override
	public void create() {
		screenHashtable = new Hashtable<Integer, Screen>();
		screenHashtable.put(LOADINGSCREEN, new LoadingScreen(this));
		screenHashtable.put(MENUSCREEN, new MenuPrincipalScreen(this));
		screenHashtable.put(NEWCHARACTERSCREEN,new CreateCharacterScreen(this));
		screenHashtable.put(CHATSCREEN,new ChatScreen(this));
		screenHashtable.put(BATTLESCREEN,new BattleScreen(this));
		screenHashtable.put(RESULTSCREEN,new ResultScreen(this));
		screenHashtable.put(DICOSCREEN,new EncyclopedieScreen(this));
		screenHashtable.put(FINALSCREEN,new FinalScreen(this));
		changeScreen(0);
	}

	/**
	 * methode permettant de changer d'ecran
	 * @param nextScreen la variable int public correspondant 
	 */
	public void changeScreen(int nextScreen){
		if(screenHashtable.containsKey(nextScreen)){
			setScreen(screenHashtable.get(nextScreen));
			currentScreen =(nextScreen);
		}
	}



	@Override
	public void dispose(){
		try {
			this.mc.deco();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GamePreferences.instance.load();
		GamePreferences prefs = GamePreferences.instance;
		prefs.timePlayed += TimeUtils.millis() - AbstractScreen.getTimePlayed();
		prefs.save();
		//System.err.println("fini:"+prefs.timePlayed/1000+"sec");

	}

	public void setMC(UnicastClient m){
		this.mc = m;
	}

	public UnicastClient getMC(){
		return this.mc;
	}


}
