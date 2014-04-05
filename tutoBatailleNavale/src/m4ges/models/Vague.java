package m4ges.models;

import java.util.ArrayList;

import m4ges.models.monster.Monstre;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;


/**
 * Classe permettant de charger une vague de monstre depuis un fichier
 * Attention a bien respecter la syntaxe lors de la creation
 * @author Florian
 *
 */
public class Vague {
	private int idVague;
	private String nameVague;
	private ArrayList<Monstre> monsters;

	private Vague(){
		
	}

	public static Vague loadVague(int number){
		//creation d'un objet json
		Json json = new Json();
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.internal("files/level_"+number+".txt" );
		//lecture du fichier
		String text = file.readString();
		//creation de la vague
		Vague vague = json.fromJson(Vague.class, text);

		return vague;
	}

	
	
	public String getNameVague() {
		return nameVague;
	}
	public void setNameVague(String nameVague) {
		this.nameVague = nameVague;
	}
	public int getIdVague() {
		return idVague;
	}
	public void setIdVague(int idVague) {
		this.idVague = idVague;
	}
	public ArrayList<Monstre> getMonsters() {
		return monsters;
	}

	public void setMonsters(ArrayList<Monstre> monsters) {
		this.monsters = monsters;
	}
	@Override
	public String toString() {
		return "Vague [idVague=" + idVague + ", nameVague=" + nameVague
				+ ", monsters=" + monsters + "]";
	}
}
