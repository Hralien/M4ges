package m4ges.models.monster;

import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Item;
import m4ges.models.Personnage;
import m4ges.models.Skill;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Abyss extends Monstre{
	private final static String DESCRIPTION = "Le Abyss est ou n'est pas ...";
	protected static volatile Animation animation;

	public Abyss() {
		super();
		super.nom="Abyss";
		super.hp=50;
		super.intel=3;
		super.mana=5*super.intel;
		super.strength=3;
		super.speed=2;
		super.listSkills=Skill.getSkillForClass(Personnage.BOSS);

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
		if (Abyss.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Abyss.class) {
				if (Abyss.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("abyss");
					TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 256, 256);
					regions[1] = new TextureRegion(sprite, 256, 0, 259, 256);
					regions[2] = new TextureRegion(sprite, 505, 0, 270, 256);
					regions[3] = new TextureRegion(sprite, 775, 0, 247, 256);
					regions[4] = new TextureRegion(sprite, 0, 256, 256, 256);
					regions[5] = new TextureRegion(sprite, 256, 256, 250, 256);
					regions[6] = new TextureRegion(sprite, 506, 256, 270, 256);
					regions[7] = new TextureRegion(sprite, 776, 256, 247, 256);
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Abyss.animation;
	}
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}

	@Override
	public ArrayList<Item> getDropPossible() {
		super.dropPossible.add(Item.selectItemFromItemID(2609));
		return super.dropPossible;
	}
}
