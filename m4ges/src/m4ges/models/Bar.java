package m4ges.models;

import m4ges.controllers.MyGame;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bar extends Actor{
	
	private static volatile TextureAtlas atlas;
	private static TextureRegion[] regionsSp;
	private static TextureRegion[] regionsHp;
	private Personnage player;
	public Bar(Personnage p){
		player=p;
		getInstance();
	}
	
	public TextureRegion getHpBar(){
		int val = (player.getHp()*100)/player.getHpMax();
		if(val <10)
			return regionsHp[9];
		else if(val <20)
			return regionsHp[8];
		else if(val <30)
			return regionsHp[7];
		else if(val <40)
			return regionsHp[6];
		else if(val <50)
			return regionsHp[5];
		else if(val <60)
			return regionsHp[4];
		else if(val <70)
			return regionsHp[3];
		else if(val <80)
			return regionsHp[2];
		else if(val <90)
			return regionsHp[1];
		else 
			return regionsHp[0];
		
	}
	public TextureRegion getSpBar(){
		int val = (player.getMana()*100)/player.getManaMax();
		if(val <10)
			return regionsSp[9];
		else if(val <20)
			return regionsSp[8];
		else if(val <30)
			return regionsSp[7];
		else if(val <40)
			return regionsSp[6];
		else if(val <50)
			return regionsSp[5];
		else if(val <60)
			return regionsSp[4];
		else if(val <70)
			return regionsSp[3];
		else if(val <80)
			return regionsSp[2];
		else if(val <90)
			return regionsSp[1];
		else 
			return regionsSp[0];
		
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	private final static TextureAtlas getInstance() {
		// Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
		// d'éviter un appel coûteux à synchronized,
		// une fois que l'instanciation est faite.
		if (Bar.atlas == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized (Bar.class) {
				if (Bar.atlas == null) {
					Bar.atlas = MyGame.manager.get("ui/battleui.pack",
							TextureAtlas.class);
					AtlasRegion atlasSp = atlas.findRegion("sp_bar");
					regionsSp = new TextureRegion[10]; 
					regionsSp[0] = new TextureRegion(atlasSp, 0, 0, 201, 14);
					regionsSp[1] = new TextureRegion(atlasSp, 0, 14, 201, 14);
					regionsSp[2] = new TextureRegion(atlasSp, 0, 28, 201, 14);
					regionsSp[3] = new TextureRegion(atlasSp, 0, 42, 201, 14);
					regionsSp[4] = new TextureRegion(atlasSp, 0, 56, 201, 14);
					regionsSp[5] = new TextureRegion(atlasSp, 0, 70, 201, 14);
					regionsSp[6] = new TextureRegion(atlasSp, 0, 84, 201, 14);
					regionsSp[7] = new TextureRegion(atlasSp, 0, 98, 201, 14);
					regionsSp[8] = new TextureRegion(atlasSp, 0, 112, 201, 14);
					regionsSp[9] = new TextureRegion(atlasSp, 0, 126, 201, 14);
					
					AtlasRegion atlasHp = atlas.findRegion("hp_bar");
					regionsHp = new TextureRegion[10]; 
					regionsHp[0] = new TextureRegion(atlasHp, 0, 0, 201, 14);
					regionsHp[1] = new TextureRegion(atlasHp, 0, 14, 201, 14);
					regionsHp[2] = new TextureRegion(atlasHp, 0, 28, 201, 14);
					regionsHp[3] = new TextureRegion(atlasHp, 0, 42, 201, 14);
					regionsHp[4] = new TextureRegion(atlasHp, 0, 56, 201, 14);
					regionsHp[5] = new TextureRegion(atlasHp, 0, 70, 201, 14);
					regionsHp[6] = new TextureRegion(atlasHp, 0, 84, 201, 14);
					regionsHp[7] = new TextureRegion(atlasHp, 0, 98, 201, 14);
					regionsHp[8] = new TextureRegion(atlasHp, 0, 112, 201, 14);
					regionsHp[9] = new TextureRegion(atlasHp, 0, 126, 201, 14);
				}
			}
		}
		return Bar.atlas;
	}

}
