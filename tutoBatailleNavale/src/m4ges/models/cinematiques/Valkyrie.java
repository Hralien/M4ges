package m4ges.models.cinematiques;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Valkyrie extends Actor{
	private static final int        FRAME_COLS = 4;         // #1
	private static final int        FRAME_ROWS = 2;         // #2

	private static TextureRegion[] walkFrames;          // #3

	private int state;
	
	private Sprite sprite;
	
	public  Valkyrie(){
		state = 0;
		setPosition(0, 0);
		setOrigin(0, 0);
		getInstance();
		sprite = new Sprite(walkFrames[0]);

	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		TextureRegion currentFrame = walkFrames[state];
		batch.draw(currentFrame, getX(), getY());                         // #17
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	private final static void getInstance() {
		// Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
		// d'éviter un appel coûteux à synchronized,
		// une fois que l'instanciation est faite.
		if (Valkyrie.walkFrames== null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized (Valkyrie.class) {
				if (Valkyrie.walkFrames == null) {
					Texture walkSheet = new Texture(Gdx.files.internal("ui/valkyrie.png"));     // #9
					TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 
							FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);                                // #10
					walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
					int index = 0;
					for (int i = 0; i < FRAME_ROWS; i++) {
						for (int j = 0; j < FRAME_COLS; j++) {
							walkFrames[index++] = tmp[i][j];
						}
					}
				}
			}
		}
	}
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
