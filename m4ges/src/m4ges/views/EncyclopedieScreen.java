package m4ges.views;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Encyclopedie;
import m4ges.models.EncyclopedieItem;
import m4ges.models.monster.Monstre;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * 
 * @author Daldegan
 * 
 */
public class EncyclopedieScreen extends AbstractScreen {

	/**
	 * Distance between items (in pixels)
	 */
	private static final float PAD = 20f;
	private Stage stage;

	/**
	 * All items available
	 */
	private ArrayMap<Integer, EncyclopedieItem> items;

	/**
	 * Just items to be displayed on the screen
	 */
	private ArrayMap<Integer, EncyclopedieItem> itemsDisplayed;

	private Table scrollTable;
	private TextField textSearch;

	private String searchLastCriteria;
	
	public EncyclopedieScreen(MyGame game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		stage = new Stage();
		items = new ArrayMap<Integer, EncyclopedieItem>();
		itemsDisplayed = new ArrayMap<Integer, EncyclopedieItem>();

		// Defines the inputprocessor to be used to catch input actions (scroll
		// and text on the search TextField)
		Gdx.input.setInputProcessor(this.stage);



		// Table used to position all the items:
		scrollTable = new Table();

		// Creates all item list:
		recupListMonstre(Encyclopedie.loadEncyclopedie());
		
		// Creates the ui window:
		Window window = new Window("Encyclopedie des monstres", skin);
		// The window shall fill the whole window:
		window.setFillParent(true);

		// This table groups the Search label and the TextField used to gather
		// the search criteria:
		Table table = new Table();
		TextButton retour = new TextButton("retour", skin);
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.changeScreen(MyGame.MENUSCREEN);
			}
		});
		table.add(retour).padRight(20f);
		table.add(new Label("Search", skin)).spaceRight(10f);

		textSearch = new TextField("", skin);

		// This event waits untilk the RETURN key is pressed to reorganize the
		// intens inside the grid:
		textSearch.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER)
					rearrangeTable();

				// Gdx.app.log("KEY", String.valueOf(keycode));

				return super.keyDown(event, keycode);
			}
		});
		
		table.add(textSearch).minSize(400f, 15f);

		// The search field will be aligned at the right of the window:
		window.add(table).right();
		window.row();

		rearrangeTable();

		// Prepares the scroll manager:
		ScrollPane scroll = new ScrollPane(scrollTable, skin);

		// Only scroll vertically:
		scroll.setScrollingDisabled(true, false);

		window.add(scroll).fill().expand();

		new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};

		window.pack();
		stage.addActor(window);
	}

	@Override
	public void render(float delta) {

		stage.act(delta);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Rendering start:
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0f, 0f, Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);

		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, false);

		rearrangeTable();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// Don't forget to free unmanaged objects:

		batch.dispose();
		skin.dispose();
		stage.dispose();
	}

	private final void recupListMonstre(Encyclopedie dico) {
		items.clear();
		itemsDisplayed.clear();

		for(final Monstre it: dico.getMonsters()){
			items.put(it.hashCode(), new EncyclopedieItem(it, skin));
		}
	}

	/**
	 * Recalculates the display items list (to fit the search criteria
	 * specified)
	 */
	private final void rearrangeTable() {
		scrollTable.clear();

		computeDisplayedItems(textSearch.getText());

		/**
		 * The horizontal size of each image
		 */
//		int textureSizeX = assetManager.get(TEXTURE_PATH, Texture.class).getWidth();
		int textureSizeX = 120;

		/**
		 * Maximum itens to be displayed on a single line
		 */
		int itemsMaxPerLine = (int) (Constants.VIEWPORT_GUI_WIDTH / textureSizeX);
		itemsMaxPerLine -= (int) (PAD * itemsMaxPerLine / textureSizeX) + 1;

		/**
		 * How many lines are needed to render all items
		 */
		int linesTotal = itemsDisplayed.size / itemsMaxPerLine;

		/**
		 * Items to be rendered on the last line
		 */
		int furtherRecords = itemsDisplayed.size % itemsMaxPerLine;

		int itemsCount = 0;
		for (int i = 0; i < linesTotal; i++) {

			/*
			 * PARTE IMPORTANTE: Here we use two's while, this happens because
			 * it is necessary to position the labels in a row while the images
			 * are positioned on the next line. This effect "gives the felling"
			 * to the user that labels in fact image's title, although
			 * technically there is no relationship between them.
			 */

			itemsCount = 0;

			// Posiciona os Label's:
			while (itemsCount < itemsMaxPerLine) {

				// Adds the item's Label:
				addText((i * itemsMaxPerLine) + itemsCount);

				itemsCount++;
			}

			// Finishes the line, preparing the table to insert new items to the
			// next line:
			scrollTable.row();

			itemsCount = 0;

			// Places TextField's:
			while (itemsCount < itemsMaxPerLine) {

				addImage((i * itemsMaxPerLine) + itemsCount);

				itemsCount++;
			}

			// Finishes the line, preparing the table to insert new items to the
			// next line:
			scrollTable.row();
		}

		// Place last line's labels:
		for (int i = 0; i < furtherRecords; i++) {
			addText((linesTotal * itemsMaxPerLine) + i);
		}
		scrollTable.row();

		// Places last line's TextFields:
		for (int i = 0; i < furtherRecords; i++) {
			addImage((linesTotal * itemsMaxPerLine) + i);
		}
	}

	private final void addText(int indice) {
		scrollTable.add(itemsDisplayed.getValueAt(indice).getNom()).center();
	}

	public final void addImage(int indice) {
		float imagemTamanhoX = itemsDisplayed.getValueAt(indice).getImage().getWidth();
		float imagemTamanhoY = itemsDisplayed.getValueAt(indice).getImage().getHeight();

		//@formatter:off
		scrollTable.add(itemsDisplayed.getValueAt(indice).getImage())
			.minHeight(imagemTamanhoY)	//Image's minimal vertical size (prevents the image to be distort)
			.minWidth(imagemTamanhoX)	//Image's horizontal size (prevents the image to be distort)
			.spaceBottom(PAD)			//Padding between images below this one
			.spaceLeft(PAD)				//Padding between images to the left of this one
			.spaceRight(PAD) 			//Padding between images to the right if this one
			.center(); 					//Centers the image on the cell
		//@formatter:on		
	}

	private final void computeDisplayedItems(String searchCriteria) {
		if ((searchCriteria == null && searchLastCriteria == null) || searchCriteria.equals(searchLastCriteria)) {
			if (items.size != itemsDisplayed.size) {
				itemsDisplayed.clear();
				itemsDisplayed.putAll(items);
			}
			return;
		}

		itemsDisplayed.clear();

		if (searchCriteria == null || searchCriteria.isEmpty()) {
			itemsDisplayed.putAll(items);
			return;
		}

		for (int i = 0; i < items.size; i++) {

			if (items.getValueAt(i).getDescription().getText().toString().contains(searchCriteria))
				itemsDisplayed.put(items.getKeyAt(i), items.getValueAt(i));
		}

		searchLastCriteria = searchCriteria;
	}
}
