package m4ges.models;

import java.util.ArrayList;

public class Citation {
	
	private String phrase;
	private String auteur;
	private static ArrayList<Citation> citationList = new ArrayList<Citation>();
    private static final int index = (int)( Math.random()*citationList.size());

	public Citation(String phrase, String auteur) {
		super();
		this.phrase = phrase;
		this.auteur = auteur;
	}
	public static void buildList(){
		citationList = new ArrayList<Citation>();
        citationList.add(new Citation("Elle est où la poulette ? ", "Kaamelott - Cadoc"));
        citationList.add(new Citation("C’est pas faux ! ", "Kaamelott - Perceval"));
        citationList.add(new Citation("Je ne mange pas de graines !! ", "Kaamelott - Maitre d'arme"));
        citationList.add(new Citation("Le gras c’est la vie !", "Kaamelott - Karadoc"));
	}
	public static Citation citationAuHasard(){
        return citationList.get(index);	
	}
	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	

}
