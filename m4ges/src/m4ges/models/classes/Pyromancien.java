package m4ges.models.classes;

import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.util.Constants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Pyromancien extends Joueur {
	
	private final static String DESCRIPTION = "Le pyromancien est un adepte de la puissance. Aspirant à la destruction, mieux vaut ne pas l'énerver";
	protected static volatile Animation animation;

	public Pyromancien() {
		super();
		super.hp=90;
		super.hpMax = 90;
		super.mana=110;
		super.manaMax =110;
		super.strength=8;
		super.speed=11;
		super.intel=12;
		
		super.listSkills=Skill.getSkillForClass(Personnage.PYROMANCIEN);

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
		if (Pyromancien.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Pyromancien.class) {
				if (Pyromancien.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("pyromancien");
//					Texture sprite = new Texture(Gdx.files.internal("character/pyromancien.png"));
					TextureRegion[] regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 32, 44);
					regions[1] = new TextureRegion(sprite, 32, 0, 29, 44);
					regions[2] = new TextureRegion(sprite, 61, 0, 33, 44);
					regions[3] = new TextureRegion(sprite, 93, 0, 32, 44);
					regions[4] = new TextureRegion(sprite, 125, 0, 34, 44);
					regions[5] = new TextureRegion(sprite, 160, 0, 26, 44);
					regions[6] = new TextureRegion(sprite, 190, 0, 32, 44);
					regions[7] = new TextureRegion(sprite, 0, 44, 49, 27);
					regions[8] = new TextureRegion(sprite, 70, 44, 49, 27);
					
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Pyromancien.animation;
	}
	
	/**
	 *  Sur les deux premiers octets : les infos
	 *  sur le reste le pseudo du joueur
	 * @return les donnees a envoyer
	 */
	public byte[] getBytes(){
		byte data[] = new byte[3+this.nom.length()];
		data[0] = Constants.CONNEXION;
		data[1] = Personnage.PYROMANCIEN;
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
		return "Pyromancien";
	}

	@Override
	public String toString() {
		return "Pyromancien [hp=" + hp + ", hpMax=" + hpMax + ", mana=" + mana
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
