package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;
import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Skill;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.models.classes.Chamane;
import m4ges.util.Constants;
import m4ges.util.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Scaling;

/**
 * Menu principal selection de ce qu'on veut faire
 * 
 * @author Florian
 * 
 */
public class CreateCharacterScreen extends AbstractScreen {
	/**
	 * {@link Stage}
	 */
	private Stage stage;


	/**
	 * window new player
	 */
	private Window winNewCharacter;
	/**
	 * input pour le pseudo du joueur
	 */
	private TextField tfPlayerName;
	/**
	 * list des classes jouables
	 */
	private List listClasses;
	/**
	 * window class description
	 */
	private Window winClassDesc;

	private Group fg;

	public CreateCharacterScreen(MyGame myGame) {
		super(myGame);
		this.fg = new Group();
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT, true);

	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, width, height);
		int viewportX = (int)(width - size.x) / 2;
		int viewportY = (int)(height - size.y) / 2;
		int viewportWidth = (int)size.x;
		int viewportHeight = (int)size.y;
		Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, true, viewportX, viewportY, viewportWidth, viewportHeight);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		stage.act(delta);
		stage.draw();
		//		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);

		this.stage.clear();
		this.stage.addActor(buildBackgroundLayer());
		this.stage.addActor(buildCreateCharacterWindow());
		this.stage.addActor(buildClassDescWindow());
		this.stage.addActor(fg);


	}
	private Window buildCreateCharacterWindow(){

		winNewCharacter = new Window("Selection Perso", skin);
		winNewCharacter.getButtonTable().pad(5);
		winNewCharacter.setPosition(650, 200);
		winNewCharacter.defaults().pad(20, 20, 20, 20);
		winNewCharacter.row().fill().expandX();
		winNewCharacter.add(buildTfPlayerName()).minWidth(100).expandX().fillX().colspan(4);
		winNewCharacter.row();
		winNewCharacter.add(buildClasseSelection()).minWidth(100).expandX().fillX().colspan(4);
		winNewCharacter.row();
		winNewCharacter.add(buildTbConnecter());
		winNewCharacter.row();
		winNewCharacter.pack();
		showNewCharacterWindow(true, true);
		return winNewCharacter;
	}

	private TextField buildTfPlayerName(){
		tfPlayerName = new TextField("", skin);
		tfPlayerName.setMessageText("Saisir un pseudo!");

		tfPlayerName.setTextFieldListener(new TextFieldListener() {
			public void keyTyped(TextField textField, char key) {
				if (key == '\n')
					textField.getOnscreenKeyboard().show(false);
			}
		});

		return tfPlayerName;
	}
	
	/**
	 * 
	 * @return
	 */
	private TextButton buildTbConnecter(){
		TextButton tbConnecter = new TextButton("Valider", skin);
		tbConnecter.pad(5);
		tbConnecter.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if (game.player != null) {
					game.player.setNom(tfPlayerName.getText());
					game.changeScreen(MyGame.CHATSCREEN);	
				} 
				else{
					game.androidUI.showAlertBox("Erreur",
							"Veuillez créer un personnage avant", "ok", stage);
				}
			}
		});
		return tbConnecter;
	}
	/**
	 * 
	 * @return
	 */
	private ScrollPane buildClasseSelection(){
		// creation d'un tableau pour stocker les classes
		String[] tabPersonnage = { "Chamane", "Necromancien", "Pyromancien","Aquamancien" };
		// creation d'une select box (appele List ici) avec le tableau ci dessus
		listClasses = new List(tabPersonnage, skin);
		listClasses.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stage.getRoot().removeActor(game.player);
				GamePreferences.instance.load();
				switch (listClasses.getSelectedIndex()) {
				case 0:
					// initialisation du player
					game.player = new Chamane();
					GamePreferences.instance.nbChamanPlayed++;
					break;
				case 1:
					// initialisation du player
					game.player = new Necromancien();
					GamePreferences.instance.nbNecroPlayed++;
					break;
				case 2:
					// initialisation du player
					game.player = new Pyromancien();
					GamePreferences.instance.nbPyroPlayed++;
					break;
				case 3:
					// initialisation du player
					game.player = new Aquamancien();
					GamePreferences.instance.nbAquaPlayed++;
					break;
				default:
//					System.err.println("switch personnage error");
				}
				GamePreferences.instance.save();
				game.player.setNom(tfPlayerName.getText());
				stage.addActor(game.player);
				showClassDescWindow(true, true);		
			}
		});
		// ajout de la List dans un scrollPane, pour pouvoir derouler,
		// descendre, monter
		ScrollPane scrollPaneClasses = new ScrollPane(listClasses, skin);
		return scrollPaneClasses;
	}
	/**
	 * 
	 * @return
	 */
	private Window buildClassDescWindow() {
		winClassDesc = new Window("Description Perso", skin);
		showClassDescWindow(false, false);
		return winClassDesc;
	}
	private void classDescUpdate(){
		winClassDesc.clear();
		winClassDesc.add(buildSpClasseDesc()).minWidth(200).minHeight(200);
		winClassDesc.row();
		winClassDesc.add(buildSkillLayout());
		winClassDesc.setPosition(100, 200);
		winClassDesc.pack();
	}
	private Table buildSkillLayout(){
		Table tSkill = new Table();
		for (final Skill it : super.game.player.getListSkills()) {
			TextButton skillButton = new TextButton(it.getSkillName()
					+ " cost:" + it.getSpCost(), skin);
			skillButton.pad(5);
			skillButton.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					fg.clear();
					it.resetAnimation();
					it.setSize(it.getCurrentFrame().getRegionWidth(), it.getCurrentFrame().getRegionHeight());
					fg.addActor(it);

				}
			});
			tSkill.add(skillButton).fillX();
			tSkill.row();
		}
		return tSkill;
	}
	/**
	 * 
	 * @return
	 */
	private ScrollPane buildSpClasseDesc(){
		Label ldesc = new Label(super.game.player.getDesc(), skin);
		ldesc.setWrap(true);
		ScrollPane scrollPaneDesc = new ScrollPane(ldesc, skin);
		return scrollPaneDesc;
	}
	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showNewCharacterWindow (boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		winNewCharacter.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
	}
	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showClassDescWindow (boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		if(super.game.player!=null)
			classDescUpdate();
		winClassDesc.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
	}
	/**
	 * 
	 */
	private Image buildBackgroundLayer() {
		TextureAtlas atlas = MyGame.manager.get("ui/scroll.pack",
				TextureAtlas.class);
		Image scrollingImage = new Image(atlas.findRegion("Scroll_neige"));
		scrollingImage.setHeight(Constants.VIEWPORT_GUI_HEIGHT);
		scrollingImage.setPosition(0, 0);
		RepeatAction ra = new RepeatAction();
		ra.setAction(sequence(moveTo(0, 0), moveBy((int)(-scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear),
				moveBy((int)(scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear)));
		ra.setCount(RepeatAction.FOREVER);
		scrollingImage.addAction(ra);

		return scrollingImage;
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
}
