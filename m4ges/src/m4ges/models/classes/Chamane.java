package m4ges.models.classes;

import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.util.Constants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Chamane extends Joueur {

	private final static String DESCRIPTION = "Le chamane est l'élément vital du groupe, son but étant de garder ses alliés en vie par tous les moyens.";
	
	protected static volatile Animation animation;

	
	public Chamane() {
		super();
		super.hp=100;
		super.hpMax =100;
		super.mana=100;
		super.manaMax =100;
		super.strength=10;
		super.speed=10;
		super.intel=10;
		
		super.listSkills=Skill.getSkillForClass(Personnage.CHAMANE);

	}

	

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
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
		if (Chamane.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Chamane.class) {
				if (Chamane.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("shaman");
//					Texture sprite = new Texture(Gdx.files.internal("character/shaman.png"));
					TextureRegion[] regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 32, 44);
					regions[1] = new TextureRegion(sprite, 32, 0, 32, 44);
					regions[2] = new TextureRegion(sprite, 64, 0, 32, 44);
					regions[3] = new TextureRegion(sprite, 96, 0, 32, 44);
					regions[4] = new TextureRegion(sprite, 128, 0, 28, 44);
					regions[5] = new TextureRegion(sprite, 160, 0, 26, 44);
					regions[6] = new TextureRegion(sprite, 191, 0, 30, 44);
					regions[7] = new TextureRegion(sprite, 0, 44, 41, 20);
					regions[8] = new TextureRegion(sprite, 64, 44, 41, 20);
					
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Chamane.animation;
	}
	
	/**
	 *  Sur les deux premiers octets : les infos
	 *  sur le reste le pseudo du joueur
	 * @return les donnees a envoyer
	 */
	public byte[] getBytes(){
		byte data[] = new byte[3+this.nom.length()];
		data[0] = Constants.CONNEXION;
		data[1] = Personnage.CHAMANE;
		//Et oui, on peut doit l'ip apres :(
		data[2] = (byte) this.nom.length();
		byte[] pseudo = this.nom.getBytes();
		for(int i = 3; i < pseudo.length+3;i++){
			data[i] = pseudo[i-3];
		}
		return data;
	}


	@Override
	public String getNameClass() {
		return "Chamane";
	}

	
	@Override
	public String toString() {
		return "Chamane [hp=" + hp + ", hpMax=" + hpMax + ", mana=" + mana
				+ ", manaMax=" + manaMax + ", strength=" + strength
				+ ", speed=" + speed + ", intel=" + intel + ", name=" + nom
				+ ", element=" + element + ", listSkills=" + listSkills
				+ ", token=" + token + ", state=" + state + ", currentFrame="
				+ currentFrame + ", stateTime=" + stateTime + ", x=" + getX()
				+ ", y=" + getY() + ", width=" + getWidth() + ", height=" + getHeight()
				+ ", originX=" + getOriginX() + ", originY=" + getOriginY() + ", scaleX="
				+ getScaleX() + ", scaleY=" + getScaleY() + ", rotation=" + getRotation()
				+ ", color=" + getColor() + "]";
	}

}
