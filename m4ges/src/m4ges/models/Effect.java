package m4ges.models;

import java.util.ArrayList;
import m4ges.controllers.MyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Effect extends Actor {
	private int id;
	private String nom;
	private Animation effectAnimation;
	private TextureRegion currentFrame;
	private float stateTime;

	private static ArrayList<Effect> listEffect;

	private static volatile TextureAtlas atlas;

	public static final int RESISTANCE=3;
	public static final int MALEDICTION=6;
	public static final int EMPOISONNEMENT=7;
	public static final int COMBUSTION=12;
	public static final int GEL=14;

	public Effect(int id,String effectName, int frame_cols, int frame_rows) {
		this.setId(id);
		this.setNom(effectName);
		TextureRegion animsheet = new TextureRegion(Initialisation().findRegion(effectName));
		TextureRegion[][] tmp = animsheet.split(animsheet.getRegionWidth()
				/ frame_cols, animsheet.getRegionHeight() / frame_rows); // #10
		TextureRegion[] effectFrames = new TextureRegion[frame_cols
		                                                 * frame_rows];
		int index = 0;
		for (int i = 0; i < frame_rows; i++) {
			for (int j = 0; j < frame_cols; j++) {
				effectFrames[index++] = tmp[i][j];
			}
		}
		effectAnimation = new Animation(0.08f, effectFrames); // #11
		stateTime = 0f;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = effectAnimation.getKeyFrame(stateTime, true); // #16
		if(currentFrame!=null)
			batch.draw(currentFrame, this.getX(), this.getY()); // #17
	}

	public static Effect selectEffectFromEffectID(int effectId) {
		for (Effect it : listEffect) {
			if(it.getId()==effectId)
				return it;
		}
		return null;
	}
	private static void buildListEffect() {
		listEffect = new ArrayList<Effect>();
		listEffect.add(new Effect(COMBUSTION, "ef_Combustion", 6,1));
		listEffect.add(new Effect(GEL, "ef_Gel", 6,1));
		listEffect.add(new Effect(EMPOISONNEMENT, "ef_Empoisonnement", 6,1));
		listEffect.add(new Effect(MALEDICTION, "ef_Malediction", 6,1));

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
		if (Effect.atlas == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized (Effect.class) {
				if (Effect.atlas == null) {
					Effect.atlas = MyGame.manager.get("effects/skill.pack",TextureAtlas.class);
					buildListEffect();
				}
			}
		}
		return Effect.atlas;
	}
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Animation getEffectAnimation() {
		return effectAnimation;
	}

	public void setEffectAnimation(Animation effectAnimation) {
		this.effectAnimation = effectAnimation;
	}

}
