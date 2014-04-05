package m4ges.models.monster;

import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Item;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Bloborange extends Monstre{
	private final static String DESCRIPTION = "Le Blob est ou n'est pas ...";
	protected static volatile Animation animation;

	public Bloborange() {
		super();
		super.nom="Bloborange";
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
		if (Bloborange.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Bloborange.class) {
				if (Bloborange.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("bloborange");
					TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 60, 50);
					regions[1] = new TextureRegion(sprite, 60, 0, 60, 50);
					regions[2] = new TextureRegion(sprite, 120, 0, 80, 50);
					regions[3] = new TextureRegion(sprite, 201, 0, 100, 50);
					regions[4] = new TextureRegion(sprite, 0, 50, 60, 50);
					regions[5] = new TextureRegion(sprite, 60, 50, 60, 50);
					regions[6] = new TextureRegion(sprite, 120, 50, 80, 50);
					regions[7] = new TextureRegion(sprite, 201, 50, 100, 50);
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Bloborange.animation;
	}
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}

	@Override
	public ArrayList<Item> getDropPossible() {
		super.dropPossible.add(Item.selectItemFromItemID(1554));
		return super.dropPossible;
	}
}
