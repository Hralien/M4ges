package m4ges.models.classes;

import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.util.Constants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Necromancien extends Joueur {

	private final static String DESCRIPTION = "Le nécromancien est un adepte de la magie noire et utilise la manipulation pour parvenir à détruire plus facilement son adversaire.";
	protected static volatile Animation animation;


	public Necromancien() {
		super();
		super.hp=1000;
		super.hpMax = 1000;
		super.mana=110;
		super.manaMax =110;
		super.strength=8;
		super.speed=10;
		super.intel=13;
		super.listSkills=Skill.getSkillForClass(Personnage.NECROMANCIEN);		
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
		if (Necromancien.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Necromancien.class) {
				if (Necromancien.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("necromancien");
//					Texture sprite = new Texture(Gdx.files.internal("character/necromancien.png"));
					TextureRegion[] regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 31, 46);
					regions[1] = new TextureRegion(sprite, 31, 0, 32, 46);
					regions[2] = new TextureRegion(sprite, 64, 0, 32, 46);
					regions[3] = new TextureRegion(sprite, 95, 0, 33, 46);
					regions[4] = new TextureRegion(sprite, 128, 0, 33, 46);
					regions[5] = new TextureRegion(sprite, 160, 0, 26, 46);
					regions[6] = new TextureRegion(sprite, 192, 0, 30, 46);
					regions[7] = new TextureRegion(sprite, 0, 46, 48, 26);
					regions[8] = new TextureRegion(sprite, 69, 46, 48, 26);

					animation = new Animation(0.1f, regions);              // #11
				}
			}
		}
		return Necromancien.animation;
	}

	/**
	 *  Sur les deux premiers octets : les infos
	 *  sur le reste le pseudo du joueur
	 * @return les donnees a envoyer
	 */
	public byte[] getBytes(){
		byte data[] = new byte[3+this.nom.length()];
		data[0] = Constants.CONNEXION;
		data[1] = Personnage.NECROMANCIEN;
		//Et oui, on doit mettre l'ip apres :(
		data[2] = (byte) this.nom.length();
		for(int i = 3; i < nom.length()+3;i++){
			data[i] = (byte) nom.charAt(i-3);
		}
		return data;
	}

	@Override
	public String getNameClass() {
		return "Necromancien";
	}

	@Override
	public String toString() {
		return "Necromancien [hp=" + hp + ", hpMax=" + hpMax + ", mana=" + mana
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
