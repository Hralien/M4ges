package m4ges.models.monster;

import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Item;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Skeleton extends Monstre {
	
	private final static String DESCRIPTION = "Le Skeleton est ou n'est pas ...";
	protected static volatile Animation animation;

	
	public Skeleton() {
		super();
		super.nom="Skeleton";
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
	 * M�thode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public Animation animate() {
		//Le "Double-Checked Singleton"/"Singleton doublement v�rifi�" permet 
		//d'�viter un appel co�teux � synchronized, 
		//une fois que l'instanciation est faite.
		if (Skeleton.animation == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized(Skeleton.class) {
				if (Skeleton.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("skeleton");
					TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 128, 100);
					regions[1] = new TextureRegion(sprite, 128, 0, 128, 100);
					regions[2] = new TextureRegion(sprite, 256, 0, 128, 100);
					regions[3] = new TextureRegion(sprite, 384, 0, 128, 100);
					regions[4] = new TextureRegion(sprite, 0, 100, 128, 100);
					regions[5] = new TextureRegion(sprite, 128, 100, 128, 100);
					regions[6] = new TextureRegion(sprite, 256, 100, 128, 100);
					regions[7] = new TextureRegion(sprite, 384, 100, 128, 100);

					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Skeleton.animation;
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
