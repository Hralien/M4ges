package m4ges.models;

import java.util.ArrayList;
import java.util.Iterator;

import m4ges.views.BattleScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Classe representant un personnae
 * 
 * @author Florian
 * 
 */
public abstract class Personnage extends Actor {

	// classe
	/**
	 * constante du chamane
	 */
	public static final int CHAMANE = 0;
	/**
	 * constante du necromancien
	 */
	public static final int NECROMANCIEN = 1;
	/**
	 * constante du pyromancien
	 */
	public static final int PYROMANCIEN = 2;
	/**
	 * constante du aquamancien
	 */
	public static final int AQUAMANCIEN = 3;
	/**
	 * constante des monstres
	 */
	public static final int MONSTRE = 4;
	/**
	 * constante du boss
	 */
	public static final int BOSS = 5;

	// etat
	/**
	 * constante animation complete
	 */
	public static final int COMPLETE = 0;
	/**
	 * constante animation mort
	 */
	public static final int MORT = 1;
	/**
	 * constante animation attente
	 */
	public static final int WAIT = 2;
	// stats
	/**
	 * les hp du personnage
	 */
	protected int hp;
	/**
	 * les hp max
	 */
	protected int hpMax;
	/**
	 * le mana
	 */
	protected int mana;
	/**
	 * le mana max
	 */
	protected int manaMax;
	/**
	 * la force
	 */
	protected int strength;
	/**
	 * la vitesse
	 */
	protected int speed;
	/**
	 * l'intelligence
	 */
	protected int intel;
	/**
	 * le nom
	 */
	protected String nom;
	/**
	 * l'element
	 */
	protected int element;
	/**
	 * la liste de competences
	 */
	protected ArrayList<Skill> listSkills;
	/*
	 * liste des effets actif sur le personnage
	 */
	protected ArrayList<Integer> effet;
	/**
	 * permet de connaitre le tour de jeu
	 */
	protected boolean token;
	/**
	 * etat du personnage
	 */
	protected int state;
	/**
	 * image active
	 */
	protected TextureRegion currentFrame;
	/**
	 * temps correspondant
	 */
	protected float stateTime;

	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
		this.effet = new ArrayList<Integer>();
		this.state = WAIT;
		this.stateTime = 0;
		this.currentFrame = null;
		setTouchable(Touchable.enabled);
		this.token = false;
		this.setOrigin(50, 50);
	}

	/**
	 * Permet d'ajouter un effet
	 * 
	 * @param effet
	 *            : l'effet a ajouter
	 */
	public void addEffect(int effet) {
		if (effet == Effect.GEL || effet == Effect.RESISTANCE
				|| effet == Effect.MALEDICTION
				|| effet == Effect.COMBUSTION
				|| effet == Effect.EMPOISONNEMENT)
			this.effet.add(effet);
		//		this.getStage().addActor(Effect.selectEffectFromEffectID(effet));
//		for(Integer it:this.effet)
//			System.out.println("EFFET : " + it);
	}

	public void delEffect(int effet) {
		this.effet.remove((Object) effet);
		//		this.getStage().getActors().removeValue(Effect.selectEffectFromEffectID(effet), true);
	}

	public boolean isGele() {
		return this.effet.contains(Effect.GEL);
	}

	public boolean isResistant() {
		return this.effet.contains(Effect.RESISTANCE);
	}

	public void traiteEffet(BattleScreen bs) {

		Iterator<Integer> e = this.effet.iterator();
		while (e.hasNext()) {
			switch (e.next()) {
			case Effect.COMBUSTION:
				this.perdreVie(15);
				bs.afficheSkill(
						Skill.selectSkillFromSkillID(Effect.COMBUSTION),
						this, this);
				break;
			case Effect.MALEDICTION:
				this.perdreVie(25);
				e.remove();
				bs.afficheSkill(
						Skill.selectSkillFromSkillID(Effect.MALEDICTION),
						this, this);
				break;
			case Effect.EMPOISONNEMENT:
				this.perdreVie(10);
				bs.afficheSkill(
						Skill.selectSkillFromSkillID(Effect.EMPOISONNEMENT),
						this, this);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * methode permettant le calcul de la vie restante
	 * 
	 * @param degat
	 */
	public void perdreVie(int degat) {
		int hp = this.getHp();
		hp -= degat;
		if (hp <= 0) {
			this.setHp(0);
			this.setState(MORT);
		} else {
			this.setHp(hp);
		}
	}

	/**
	 * dessine le personnage en fonction de son etat et du statetime
	 */
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime();

		switch (state) {
		case COMPLETE:
			currentFrame = animate().getKeyFrame(stateTime, true);
			batch.draw(currentFrame, getOriginX(), getOriginY(), getWidth(), getHeight());
			break;
		case MORT:
			currentFrame = animate().getKeyFrame(animate().getKeyFrameIndex(8));
			batch.draw(currentFrame, getOriginX(), getOriginY(), getWidth(), getHeight());
			break;
		case WAIT:
			currentFrame = animate().getKeyFrame(0, true);
			batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
			break;
		default:
			break;
		}

		for(Integer e : effet){
			try{
				TextureRegion currentEffectTexture= Effect.selectEffectFromEffectID(e).getEffectAnimation().getKeyFrame(stateTime, true);
				batch.draw(currentEffectTexture,getX(), getY());
			}catch (NullPointerException e1){
				//animation de l'effet null
			}

		}
		if(nom.equals("Abyss")){
			if(getWidth()==0)
				this.setWidth(currentFrame.getRegionWidth());
			if(getHeight()==0)
				this.setHeight(currentFrame.getRegionHeight());
		}
		else{
			if(getWidth()==0)
				this.setWidth(currentFrame.getRegionWidth()*2);
			if(getHeight()==0)
				this.setHeight(currentFrame.getRegionHeight()*2);
		}
		//		this.setSize(currentFrame.getRegionWidth(),currentFrame.getRegionHeight());
		//		this.setBounds(getX(), getY(), getWidth(),getHeight());
	}

	/**
	 * methode à redefinir pour savoir si on selectionne un personnage
	 * 
	 * @return l'actor s'il est hit sinon null
	 */
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight() ? this
				: null;
	}

	@Override
	public String toString() {
		return "Personnage [hp=" + hp + ", mana=" + mana + ", strength="
				+ strength + ", speed=" + speed + ", intel=" + intel
				+ ", name=" + nom + ", listSkills=" + listSkills + ", state="
				+ state + ", currentFrame=" + currentFrame + ", stateTime="
				+ stateTime + "]";
	}

	/**
	 * 
	 * @return la description du monstre / classe
	 */
	public abstract String getDesc();

	/**
	 * 
	 * @return l'animation correspondante à l'etat
	 */
	public abstract Animation animate();

	public String getNom() {
		return nom;
	}

	public void setNom(String name) {
		this.nom = name;
	}

	public ArrayList<Skill> getListSkills() {
		return listSkills;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hp_max) {
		this.hpMax = hp_max;
	}

	public int getManaMax() {
		return manaMax;
	}

	public void setManaMax(int mana_max) {
		this.manaMax = mana_max;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isToken() {
		return token;
	}

	public void setToken(boolean t) {
		this.token = t;
	}

}
