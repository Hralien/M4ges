package m4ges.models.monster;

import java.util.ArrayList;

import m4ges.models.Item;
import m4ges.models.Personnage;
import m4ges.models.Skill;


public abstract class Monstre extends Personnage{

	protected ArrayList<Item> dropPossible;


	protected  String description;
	
	public Monstre() {
		super();
		dropPossible = new ArrayList<Item>();
		listSkills = Skill.getSkillForClass(Personnage.MONSTRE);
	}

	public int attaque(){
		return super.strength*3;
	}

	//UNIQUEMENT POUR LES ATTAQUES D'UN NPC VERS UN JOUEURS
	public void attaque(Personnage p){
//		System.out.println("Methode d'attaque NPC > joueurs appelee");
		
		p.setHp(p.getHp() - this.attaque());
	}
	public abstract ArrayList<Item> getDropPossible();

	public void setDropPossible(ArrayList<Item> dropPossible) {
		this.dropPossible = dropPossible;
	}
}
