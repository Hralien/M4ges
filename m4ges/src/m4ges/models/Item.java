package m4ges.models;

import java.util.ArrayList;

import m4ges.controllers.MyGame;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Item {
	//attributs de classe
	/**
	 * arraylist permettant de repertorier les items existants
	 */
	public static ArrayList<Item> listItem;
	private static volatile TextureAtlas atlas;

	//attribut d'un objet
	/**
	 * identifiant
	 */
	private int id;
	/**
	 * nom de l'objet
	 */
	private String name;
	/**
	 * est il utilisable?
	 */
	private boolean utilisable;
	/**
	 * si oui ItemId correspondant
	 */
	private int SkillId;
	/**
	 * l'image de l'item
	 */
	private Image image;
	/**
	 * rate de l'item , à partir duquel on peut l'avoir
	 */
	private int rate;

	/**
	 * constructeur 
	 * @param id
	 * @param name
	 * @param usable
	 * @param ItemId
	 */
	public Item(int id, String name, boolean usable, int SkillId, int rate) {
		super();
		this.id = id;
		this.name = name;
		this.utilisable = usable;
		this.SkillId = SkillId;
		this.rate = rate;
		this.image = new Image(getInstance().findRegion(""+id));

	}
	/**
	 * initialise la liste d'item
	 */
	public static ArrayList<Item> buildListItem() {
		if(listItem == null){
			listItem = new ArrayList<Item>();
			listItem.add(new Item(7026,"cle",false, -1,30));
			listItem.add(new Item(501,"potion",true, 1,50));
			listItem.add(new Item(1553,"Livre des mers",false,-1,50));
			listItem.add(new Item(1554,"Livre des Terres",false,-1,50));
			listItem.add(new Item(1555,"Livre des Flammes",false,-1,50));
			listItem.add(new Item(1557,"Livre des Ombres",false,-1,50));
			listItem.add(new Item(2235,"Couronne",false,-1,50));
			listItem.add(new Item(2234,"Tiatre",false,-1,50));
			listItem.add(new Item(2373,"Robe",false,-1,50));
			listItem.add(new Item(2370,"Tunique",false,-1,50));
			listItem.add(new Item(2432,"Botines",false,-1,50));
			listItem.add(new Item(2426,"Bottes Maléfique",false,-1,50));
			listItem.add(new Item(2617,"Gants",false,-1,50));
			listItem.add(new Item(2609,"Bague Maléfique",false,-1,50));

			 
		}
		return listItem;

	} 
	/**
	 * retrouve un item parmis les existant
	 * @param itemId
	 * @return
	 */
	public static Item selectItemFromItemID(int itemId) {
		for (Item it : buildListItem()) {
			if (it.id == itemId) {
				return it;
			}
		}
		return null;
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
		if (Item.atlas == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized (Item.class) {
				if (Item.atlas == null) {
					Item.atlas = MyGame.manager.get("items/item.pack",
							TextureAtlas.class);
					buildListItem();
				}
			}
		}
		return Item.atlas;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUsable() {
		return utilisable;
	}

	public void setUsable(boolean usable) {
		this.utilisable = usable;
	}

	public int getSkillId() {
		return SkillId;
	}

	public void setSkillId(int ItemId) {
		this.SkillId = ItemId;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", usable=" + utilisable
				+ ", SkillId=" + SkillId + ", image=" + image + ", rate="
				+ rate + "]";
	}
	
}
