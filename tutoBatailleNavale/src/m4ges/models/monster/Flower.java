package m4ges.models.monster;

import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Item;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Flower extends Monstre {
	
	protected static volatile Animation animation;

	public Flower() {
		super();
		super.nom="Flower";
		super.hp=50;
		super.intel=3;
		super.mana=5*super.intel;
		super.strength=3;
		super.speed=2;		
	}

	@Override
	public String getDesc() {
		return "Le Flower est ou n'est pas ...";
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public Animation animate() {
		//Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
		//d'éviter un appel coûteux à synchronized, 
		//une fois que l'instanciation est faite.
		if (Flower.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Flower.class) {
				if (Flower.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("flower");
					TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 64, 64);
					regions[1] = new TextureRegion(sprite, 64, 0, 64, 64);
					regions[2] = new TextureRegion(sprite, 128, 0, 64, 64);
					regions[3] = new TextureRegion(sprite, 192, 0, 64, 64);
					regions[4] = new TextureRegion(sprite, 0, 64, 64, 64);
					regions[5] = new TextureRegion(sprite, 64, 64, 64, 64);
					regions[6] = new TextureRegion(sprite, 128, 64, 64, 64);
					regions[7] = new TextureRegion(sprite, 192, 64, 64, 64);
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Flower.animation;
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
