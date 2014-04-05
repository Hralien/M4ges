package m4ges.models;

import java.util.ArrayList;

import m4ges.models.monster.Monstre;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * liste des monstres déjà rencontré
 * @author Florian
 *
 */
public class Encyclopedie {

	private ArrayList<Monstre> monsters;
	private Encyclopedie(){

	}

	public static Encyclopedie loadEncyclopedie(){
		//creation d'un objet json
		Json json = new Json();
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.internal("files/encyclopedie.txt" );
		//lecture du fichier
		String text = file.readString();
		//creation de la vague
		Encyclopedie dico = json.fromJson(Encyclopedie.class, text);
		return dico;
	}

	public ArrayList<Monstre> getMonsters() {
		return monsters;
	}

	public void setMonsters(ArrayList<Monstre> monsters) {
		this.monsters = monsters;
	}

}
