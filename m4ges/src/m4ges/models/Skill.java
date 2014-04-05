package m4ges.models;

import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Skill extends Actor implements Cloneable {

	private int id;
	private int spCost;
	private int damage;
	private String skillName;
	// private ParticleEffect effect;
	private String soundPath;

	public static ArrayList<Skill> listSkill;;

	/** Skill sprite management */
	private static volatile TextureAtlas atlas;

	private Animation skillAnimation; // #3
	private TextureRegion currentFrame; // #7

	private float stateTime; // #8
	private boolean playingSound;

	public Skill() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @param spCost
	 * @param skillName
	 * @param skillEffect
	 *            old name of skillsheet before packing
	 * @param frame_cols
	 *            number of colone
	 * @param frame_rows
	 *            number of rows
	 */
	public Skill(int id, int spCost, String skillName, String skillEffect,
			int damage, int frame_cols, int frame_rows) {
		super();
		this.id = id;
		this.spCost = spCost;
		this.damage = damage;
		this.skillName = skillName;
		this.soundPath = "sound/" + skillEffect + ".wav";

		TextureRegion animsheet = new TextureRegion(Initialisation()
				.findRegion(skillEffect));
		TextureRegion[][] tmp = animsheet.split(animsheet.getRegionWidth()
				/ frame_cols, animsheet.getRegionHeight() / frame_rows);

		TextureRegion[] effectFrames = new TextureRegion[frame_cols
				* frame_rows];
		int index = 0;
		for (int i = 0; i < frame_rows; i++) {
			for (int j = 0; j < frame_cols; j++) {
				effectFrames[index++] = tmp[i][j];
			}
		}
		skillAnimation = new Animation(0.08f, effectFrames);
		skillAnimation.setPlayMode(Animation.NORMAL);
		stateTime = 0f; // #13
		currentFrame = skillAnimation.getKeyFrame(0);
		super.setSize(currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());
	}

	/**
	 * return la liste des skills d'une classe de personnage
	 * 
	 * @param classID
	 * @return
	 */
	public static ArrayList<Skill> getSkillForClass(int classID) {
		ArrayList<Skill> list = new ArrayList<Skill>();
		switch (classID) {
		case Personnage.CHAMANE:
			/** Chamane */
			list.add(Skill.selectSkillFromSkillID(1));
			list.add(Skill.selectSkillFromSkillID(2));
			list.add(Skill.selectSkillFromSkillID(3));
			list.add(Skill.selectSkillFromSkillID(4));
			break;
		case Personnage.NECROMANCIEN:
			/** Necromencien */
			list.add(Skill.selectSkillFromSkillID(5));
			list.add(Skill.selectSkillFromSkillID(6));
			list.add(Skill.selectSkillFromSkillID(7));
			list.add(Skill.selectSkillFromSkillID(8));

			break;
		case Personnage.PYROMANCIEN:
			/** Mage chaud */
			list.add(Skill.selectSkillFromSkillID(9));
			list.add(Skill.selectSkillFromSkillID(10));
			list.add(Skill.selectSkillFromSkillID(11));
			list.add(Skill.selectSkillFromSkillID(12));

			break;
		case Personnage.AQUAMANCIEN:
			/** Mage froid */
			list.add(Skill.selectSkillFromSkillID(13));
			list.add(Skill.selectSkillFromSkillID(14));
			list.add(Skill.selectSkillFromSkillID(15));
			list.add(Skill.selectSkillFromSkillID(16));

			break;
		case Personnage.MONSTRE:
			/** monstre */
			list.add(Skill.selectSkillFromSkillID(18));
			break;
		case Personnage.BOSS:
			/** boss */
			list.add(Skill.selectSkillFromSkillID(17));
			break;
		}
		return list;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (!playingSound) {
			FileHandle s = Gdx.files.internal(soundPath);
			if (s.exists()) {
				Sound m = Gdx.audio.newSound(s);
				m.setVolume(0, 0.2f);
				m.play();
				playingSound = true;
			}
		}
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		if (!skillAnimation.isAnimationFinished(stateTime)) {
			currentFrame = skillAnimation.getKeyFrame(stateTime, false); // #16
			batch.draw(currentFrame, this.getX(), this.getY()); // #17
		} else {
			// animation finie, on la vire du parent

			this.remove();
			playingSound = false;
		}
	}

	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	public final static TextureAtlas Initialisation() {
		// Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
		// d'éviter un appel coûteux à synchronized,
		// une fois que l'instanciation est faite.
		if (Skill.atlas == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized (Skill.class) {
				if (Skill.atlas == null) {
					Skill.atlas = MyGame.manager.get("effects/skill.pack",
							TextureAtlas.class);
					buildListSkill();
				}
			}
		}
		return Skill.atlas;
	}

	/**
	 * 
	 * construit la list des skills possibles
	 */
	public static void buildListSkill() {
		listSkill = new ArrayList<Skill>();

		/** Shaman */
		listSkill.add(new Skill(1, 5, "soin", "Soin", -10, 5, 4));
		listSkill.add(new Skill(2, 7, "motivation", "Motivation", 0, 5, 7));
		listSkill.add(new Skill(3, 4, "ressistance", "Resistance", 0, 5, 4));
		listSkill
				.add(new Skill(4, 25, "resurrection", "Resurrection", 0, 5, 7));

		/** Necromencien */
		listSkill.add(new Skill(5, 8, "abimes", "Abimes", 20, 5, 6));
		listSkill.add(new Skill(6, 12, "malédiction", "Malediction", 15, 5, 6));
		listSkill.add(new Skill(7, 8, "empoisonement", "Empoisonnement", 10, 5,
				5));
		listSkill
				.add(new Skill(8, 25, "perturbation", "Perturbation", 50, 5, 5));

		/** Mage chaud */
		listSkill
				.add(new Skill(9, 5, "deflagration", "Deflagration", 10, 5, 4));
		listSkill.add(new Skill(10, 10, "foudre", "Foudre", 10, 5, 10));
		listSkill
				.add(new Skill(11, 6, "choc sismique", "Earth_Spike", 10, 5, 2));
		listSkill.add(new Skill(12, 25, "combustion", "Combustion", 15, 5, 8));

		/** Mage froid */
		listSkill.add(new Skill(13, 6, "geyser", "Geyser", 10, 5, 6));
		listSkill.add(new Skill(14, 6, "gel", "Gel", 5, 5, 6));
		listSkill.add(new Skill(15, 15, "tornade", "Tornade", 20, 5, 6));
		listSkill.add(new Skill(16, 25, "rafale", "Rafale", 30, 5, 5));

		/** Boss */
		listSkill.add(new Skill(17, 6, "soufle", "Dragon_Breath", 10, 4, 3));
		
		/** Monstre */
		listSkill.add(new Skill(18, 6, "slash", "Slash", 10, 5, 2));

	}

	public static Skill selectSkillFromSkillID(int skillnum) {
		for (Skill it : listSkill) {
			if (it.id == skillnum) {
				return it;
			}
		}
		return null;
	}

	/**
	 * Reset animation.
	 * 
	 * You can use this to ensure the animation plays from the start again. It's
	 * handy if you have one-shot animations like explosions but you are using
	 * re-usable Sprites. You must reset the animation to ensure the animation
	 * plays back again.
	 */
	public void resetAnimation() {
		stateTime = 0;
	}

	/**
	 * Check to see if animation finished.
	 * 
	 * @param stateTime
	 * 
	 * @return True if finished.
	 */
	public boolean isAnimationFinished() {
		return skillAnimation.isAnimationFinished(stateTime);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getId() {
		return id;
	}

	public int getDamage() {
		return this.damage;
	}

	public String getSkillName() {
		return skillName;
	}

	public int getSpCost() {
		return spCost;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public byte[] getBytes() {
		byte[] data = new byte[5];
		data[0] = Constants.LANCERSKILL;
		data[1] = (byte) id;
		// data[2] = lanceur.;
		return data;
	}
}
