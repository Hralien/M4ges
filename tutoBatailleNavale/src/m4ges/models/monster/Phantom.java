package m4ges.models.monster;


import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Item;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Phantom extends Monstre {
	
	private final static String DESCRIPTION = "Le Phantom est ou n'est pas ...";
	protected static volatile Animation animation;

	public Phantom() {
		super();
		super.nom="Phantom";
		super.hp=50;
		super.intel=3;
		super.mana=5*super.intel;
		super.strength=3;
		super.speed=2;		

	}

	@Override
	public String getDesc() {
		return DESCRIPTION;
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public Animation animate() {
		//Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
		//d'éviter un appel coûteux à synchronized, 
		//une fois que l'instanciation est faite.
		if (Phantom.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Phantom.class) {
				if (Phantom.animation == null) {
			        TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
			        AtlasRegion sprite = atlas.findRegion("phantom");
			        TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 32, 50);
					regions[1] = new TextureRegion(sprite, 63, 0, 42, 50);
					regions[2] = new TextureRegion(sprite, 128, 0, 51, 50);
					regions[3] = new TextureRegion(sprite, 192, 0, 64, 50);
					regions[4] = new TextureRegion(sprite, 0, 50, 32, 50);
					regions[5] = new TextureRegion(sprite, 63, 50, 42, 50);
					regions[6] = new TextureRegion(sprite, 128, 50, 51, 50);
					regions[7] = new TextureRegion(sprite, 192, 50, 64, 50);
					
					animation = new Animation(0.1f, regions);              // #11


				}
			}
		}
		return Phantom.animation;
	}
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}

	@Override
	public ArrayList<Item> getDropPossible() {
		super.dropPossible.add(Item.selectItemFromItemID(7026));
		return super.dropPossible;
	}
}
