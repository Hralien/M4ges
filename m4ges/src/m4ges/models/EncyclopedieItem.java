package m4ges.models;

import m4ges.models.monster.Monstre;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * TO to hold item's data
 * 
 * @author Daldegan
 * 
 */
public final class EncyclopedieItem {


	private int id;
	private Monstre monster;
	private Label nom;
	private Label description;
	private Image image;
	
	public EncyclopedieItem(Monstre m, Skin skin) {
		this.monster = m;
		this.id = monster.hashCode();
		this.setNom(new Label(monster.getName(), skin));
		this.description = new Label(monster.getDesc(), skin);
		this.image = new Image(monster.animate().getKeyFrame(0f));
		image.addListener(new ImageClick(this));
	}
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public final int getId() {
		return id;
	}

	public final Label getDescription() {
		return description;
	}

	public Monstre getMonster() {
		return monster;
	}

	public void setMonster(Monstre monster) {
		this.monster = monster;
	}
	
	public Label getNom() {
		return nom;
	}

	public void setNom(Label nom) {
		this.nom = nom;
	}

	private final class ImageClick extends ClickListener {
		private EncyclopedieItem item;

		public ImageClick(EncyclopedieItem item) {
			this.item = item;
		}

		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.log("SELECTED ITEM", "ID: " + item.getId() + " Description: " + item.getDescription().getText());
		}
	}
}
