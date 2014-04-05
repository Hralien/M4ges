package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;

import m4ges.controllers.MyGame;
import m4ges.models.MapPerso;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Chamane;
import m4ges.models.classes.Joueur;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.models.monster.Monstre;
import m4ges.util.Constants;
import m4ges.views.BattleScreen;
import m4ges.views.ChatWindow;

import com.badlogic.gdx.Gdx;

/**
 * Classe permettant l'envoye de donnees et de se connecter aux autres joueurs
 */
public class UnicastClient {

	/**
	 * Datagram socket permettant l'envoie
	 */
	private DatagramSocket ds;
	/**
	 * Datagram socket permettant la recepetion
	 */
	private DatagramSocket dsR;
	/**
	 * Port sur lequel les joueurs les vont communiquer
	 */
	public final static int PORT = 12345;
	/**
	 * Hashmap des joueurs de la forme <IP, JOUEURS>
	 */
	private MapPerso<String, Joueur> joueurs;
	/**
	 * Liste des monstres
	 */
	private ArrayList<Monstre> monstres;
	/**
	 * Le jeu
	 */
	public MyGame game;
	/**
	 * datagram packet permettant l'envoie
	 */
	private DatagramPacket dp;
	/**
	 * datagram packet permettant la recepetion
	 */
	private DatagramPacket dpr;
	/**
	 * ip d'un joueur
	 */
	private String ip;
	/**
	 * ip de l'user
	 */
	public String monIp;
	/**
	 * Chat window, utile pour le chat
	 */
	public ChatWindow chatWindow;

	/**
	 * Si true : pas d'attaque de monstre Si false : attaque de monstres
	 */
	private static final boolean DEBUG = false;

	public static final int NB_JOUEUR_MINIMUM = 1;

	/**
	 * Constructeur
	 * 
	 * @param g
	 *            : Le jeu
	 */
	public UnicastClient(MyGame g) {
		// initialisation
		this.game = g;
		joueurs = new MapPerso<String, Joueur>();
		monstres = new ArrayList<Monstre>();

		monIp = this.getLocalIpAddress();
		joueurs.put(monIp, game.player);
		game.playersConnected.add(game.player);
		receive();

	}

	public void lancerClient() throws IOException {
		sendConnection(null, false);
		chatWindow.addName(game.player.getNom() + " : "
				+ game.player.getNameClass());
	}

	/**
	 * Permet d'envoyer un rapport de connection aux autres
	 * 
	 * @param ipNouveau
	 *            : lors de la co null mais permet de dire aux nouveaux connecte
	 *            qu'on est la
	 * @param nouveau
	 *            : lors de la co false mais permet de dire aux nouveaux
	 *            connecte qu'on est la
	 * @throws IOException
	 */
	public void sendConnection(String ipNouveau, boolean nouveau)
			throws IOException {
		byte[] data;

		data = ((Joueur) this.game.player).getBytes();
		// Si c'est un nouveau on ne repond qu'a lui

		if (nouveau) {

			data[0] = Constants.NOUVEAU;
			if (ipNouveau.replace('/', '\0').trim().equals(monIp)
					|| ipNouveau.replace('/', '\0').trim().equals("127.0.0.1")) {

				return;
			}

			dp = new DatagramPacket(data, data.length);
			dp.setAddress(InetAddress.getByName(ipNouveau));
			dp.setPort(PORT);
			ds.send(dp);

			// Sinon on repond a tout le monde
		} else {

			data[0] = Constants.CONNEXION;
			String[] broadcastTab = this.monIp.split("\\.");
			String broadcast = broadcastTab[0] + "." + broadcastTab[1] + "."
					+ broadcastTab[2] + ".255";
			dp = new DatagramPacket(data, data.length,
					InetAddress.getByName(broadcast), PORT);
			ds.send(dp);

			dp = new DatagramPacket(data, data.length,
					InetAddress.getByName("255.255.255.255"), PORT);
			ds.send(dp);

		}
	}

	/**
	 * Permet de recevoir les donnees
	 */
	private void receive() {
		// Permet de recevoir les donnes
		try {
			dsR = new DatagramSocket(PORT);
			ds = new DatagramSocket();
			ds.setBroadcast(true);
		} catch (SocketException e1) {
			game.androidUI.showAlertBox("Déjà connecté",
					"Vous êtes déjà connecté", "ok", null);
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// tableau de 128 octets (et c'est large)
					byte[] data = new byte[128];
					dpr = new DatagramPacket(data, data.length);

					try {
						// recepetion
						dsR.receive(dpr);
						// System.out.println("RECU");
						data = dpr.getData();
						traiterData(data);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

	/**
	 * Traite les donnees
	 * 
	 * @param data
	 *            donnees a traiter
	 * @throws IOException
	 */
	private void traiterData(byte[] data) throws IOException {
		// On recupere l'action de la data
		int action = (int) data[0];
		//		System.out.println("[UNICASTClient-TraiterData]:Donnees recu  : "
		//				+ action);
		switch (action) {
		case Constants.CONNEXION:
		case Constants.NOUVEAU:
			actionTraiterNouveau(action, data);
			break;
		case Constants.LANCERSKILL:
			actionTraiterLancerSkill(data);
			break;
		case Constants.ATTAQUEMONSTRE:
			actionTraiterAttaqueMonstre(data);
			break;
		case Constants.TOKEN:
		case Constants.TOKENTOUR:
			actionToken(data, action);
			break;
		case Constants.MESSAGE:
			actionRecoit(data);
			break;
		case Constants.PRET:
			actionPret();
			break;
		case Constants.LANCERSOIN:
			actionLancerSoin(data);
			break;
		case Constants.DECO:
			actionDeco();
			break;
		default:
			//			System.err.println("[UNICASTClient-DEFAULT]:Action non reconnue : "
			//					+ action);
			break;
		}
	}

	/**
	 * Traite l'envoie d'un sort d'un joueur vers un autre
	 * 
	 * @param data
	 *            : les donnees a traiter
	 */
	private void actionLancerSoin(byte[] data) {
		// l'ip du lanceur
		ip = dpr.getAddress().toString().replace('/', '\0').trim();
		// l'ip de la cible
		String ipCible = new String(data, 2, data.length - 2).trim();
		joueurs.get(ip).attaque(joueurs.get(ipCible),
				Skill.selectSkillFromSkillID(data[1]));
		// si le joueur est mort (sort hostile), il a joue ce tour (c'est une
		// image)
		if (joueurs.get(ipCible).getHp() <= 0)
			joueurs.get(ipCible).setaJoueCeTour(true);
		// on affiche le skill
		((BattleScreen) game.getScreen()).afficheSkill(
				Skill.selectSkillFromSkillID(data[1]), joueurs.get(ip),
				joueurs.get(ipCible));
	}

	/**
	 * Methode pour les messages
	 */
	private void actionRecoit(byte[] data) {

		String pseudoMsg = new String(data, 2, data[1]);
		String msg = new String(data, 2 + data[1], data.length - data[1] - 2)
		.trim();
		this.chatWindow.addMessage(pseudoMsg + " : " + msg);

	}

	/**
	 * methode appele en cas de creation de nouveau
	 * 
	 * @param action
	 * @param data
	 * @throws IOException
	 */
	private void actionTraiterNouveau(int action, byte[] data)
			throws IOException {
		//		System.out.println("NOUVEAU JOUEUR");
		String pseudo;
		pseudo = new String(data, 3, data[2]);
		Joueur p = null;
		switch (data[1]) {
		case Personnage.AQUAMANCIEN:
			p = new Aquamancien();
			p.setNom(pseudo);
			break;
		case Personnage.NECROMANCIEN:
			p = new Necromancien();
			p.setNom(pseudo);
			break;
		case Personnage.CHAMANE:
			p = new Chamane();
			p.setNom(pseudo);
			break;
		case Personnage.PYROMANCIEN:
			p = new Pyromancien();
			p.setNom(pseudo);
			break;
		}
		// On récup l'ip (trim sert à enlever les char null
		ip = dpr.getAddress().toString().replace('/', '\0').trim();
		// Si l'ip est valide et qu'il n'est pas dans la map
		if (ip.length() > 0 && !joueurs.containsKey(ip)
				&& !ip.equals("127.0.0.1")) {

			game.playersConnected.add(p);
			joueurs.put(ip, p);
			this.chatWindow.addName(p.getNom() + " : " + p.getNameClass());
		}
		// si c'est une connexion, il faut donc renvoye une action 2 !
		if (action == Constants.CONNEXION)
			sendConnection(ip, true);

		// DEBUG
		System.out.println("[UNICAST]\n-- Affichage de(s) " + joueurs.size()
				+ " joueur(s) --");
		Set<String> key = joueurs.keySet();
		for (String it : key) {
			System.out.println("ip : " + it + " Pseudo : "
					+ joueurs.get(it).getNom());
		}

	}

	/**
	 * methode appele en cas de lancement de sorts
	 * 
	 * @param data
	 */
	private void actionTraiterLancerSkill(byte[] data) {
		final Skill s = Skill.selectSkillFromSkillID(data[1]);
		// l'ip commence a 3 et la taille est de : Taille data - l'id du
		// monstre - action - id skill
		ip = dpr.getAddress().toString().replace('/', '\0').trim();

		/*
		 * On recupere la cible et l'attaquant
		 */
		joueurs.get(ip).attaque(monstres.get(data[2]), s);
		final int idMonstre = data[2];

		((BattleScreen) game.getScreen()).afficheSkill(s, joueurs.get(ip),
				monstres.get(idMonstre));

		boolean vagueFinie = true;
		for (Personnage p : monstres) {
			if (p.getHp() > 0) {
				vagueFinie = false;
				break;
			}
		}

		if (vagueFinie) {
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					((BattleScreen) game.getScreen()).clearScreen();
					game.changeScreen(MyGame.RESULTSCREEN);
				}
			});
		}

	}

	/**
	 * action appele en cas d'attaque de monstre
	 * 
	 * @param data
	 */
	private void actionTraiterAttaqueMonstre(byte[] data) {

		// l'id du monstre
		int idMonstre = (int) data[1];

		// On vérifie si le monstre n'est pas mort
		if (monstres.get(idMonstre).getHp() < 0)
			return;

		// l'ip de la cible
		ip = new String(data, 2, data.length - 2).trim();

		/*
		 * On a l'id du monstre a attaque et l'ip de la cible, on lance
		 * l'attaque
		 */
		((Monstre) monstres.get(idMonstre)).attaque(joueurs.get(ip));

		((BattleScreen) game.getScreen()).afficheSkill(monstres.get(idMonstre)
				.getListSkills().get(0), monstres.get(idMonstre),
				joueurs.get(ip));

		// DEBUG
		//		System.out.println("[UNICAST] " + monstres.get(idMonstre).getNom()
		//				+ " attaque " + joueurs.get(ip).getNom());

		boolean joueursMort = true;
		for (Joueur j : joueurs.values()) {
			if (j.getHp() > 0) {
				joueursMort = false;
			} else {
				// si il est mort, il a joue ce tour
				j.setaJoueCeTour(true);
			}
		}
		if (joueursMort) {
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					game.changeScreen(MyGame.FINALSCREEN);
				}
			});
		}
	}

	/**
	 * action appele en cas de passage du token
	 * 
	 * @param data
	 */
	private void actionToken(byte[] data, int action) {
		//		System.out.println("la");
		/*
		 * Si action = token tour alors tout le monde a joue ce tour; il faut
		 * mettre aJoueCeTour a false et enlever le token a celui qui l'a (même
		 * sans tokentour)
		 */
		for (Joueur it : joueurs.values()) {
			it.setToken(false);
			if (action == Constants.TOKENTOUR) {
				it.setaJoueCeTour(false);
				if (game.getScreen() instanceof BattleScreen)
					it.traiteEffet((BattleScreen) game.getScreen());
			}
		}

		/*
		 * Si c'est la fin du tour, alors il faut traiter les effets de tout les
		 * monstres et joueurs
		 */
		if (action == Constants.TOKENTOUR) {

			// on en profite pour voir si tout les monstres sont mort
			boolean monstresMort = true;
			for (Personnage it : monstres) {
				if (it.getHp() > 0) {
					if (game.getScreen() instanceof BattleScreen) {
						it.traiteEffet((BattleScreen) game.getScreen());
						if (it.getHp() > 0)
							monstresMort = false;
					}
				}
			}

			// si ils y sont on change
			if (monstresMort) {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						if(game.getScreen() instanceof BattleScreen)
							((BattleScreen) game.getScreen()).currentVague
							.setMonsters(null);
						game.changeScreen(MyGame.RESULTSCREEN);
					}
				});
			}

			// on regarde ensuite pour les joueurs
			boolean joueursMort = true;
			for (Personnage it : joueurs.values()) {
				// Si tout les montres sont mort, on passera pas ici
				if (game.getScreen() instanceof BattleScreen) {
					it.traiteEffet((BattleScreen) game.getScreen());
					if (it.getHp() > 0)
						joueursMort = false;
				}
			}

			// Partie fini
			if (joueursMort) {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						game.changeScreen(MyGame.FINALSCREEN);
					}
				});
			}

		}

		// on recupere l'ip de celui qui doit l'avoir
		ip = new String(data, 1, data.length - 1).trim();
		//		System.err.println("IP TOKEN :" + ip);

		// et on lui met
		joueurs.get(ip).setToken(true);

		// on indique qu'il a joue ce tour
		joueurs.get(ip).setaJoueCeTour(true);

		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				if (game.getScreen() instanceof BattleScreen){
					((BattleScreen) game.getScreen()).updateSkillWindow();
					if(ip.equals(monIp))
						((BattleScreen) game.getScreen()).buildTokenInfo();
				}
			}
		});

	}

	/**
	 * Permet d'envoyer un sort
	 * 
	 * @param mechant
	 *            : Le monstre a attaquer
	 * @param s
	 *            : Le skill qu'on lui lance
	 * @throws IOException
	 */
	public void lancerSort(Personnage mechant, Skill s) throws IOException {

		// Si le joueur est mort ou qu'il n'a pas de mana, il ne peut pas lancer
		// de sort
		if (game.player.getHp() < 0 || game.player.getMana() < s.getSpCost())
			return;

		if (mechant instanceof Joueur) {
			lancerSoin((Joueur) mechant, s);
			return;
		}
		byte[] data = new byte[3];
		// action + skill's id + monstre
		data[0] = Constants.LANCERSKILL;
		data[1] = (byte) s.getId();
		data[2] = (byte) monstres.indexOf(mechant);
		//		System.out.println("ENVOIE");
		sendToAll(data);
		game.player.setToken(false);
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				if (game.getScreen() instanceof BattleScreen)
					((BattleScreen) game.getScreen()).updateSkillWindow();
			}
		});

		// indispensable pour que les degats infligé soient effectif
		// (empeche un monstre mort d'attaquer)
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//		System.out.println("ENVOyE");
		passerToken();
	}

	/**
	 * Permet de lancer un soin marche aussi pour les attaques hostiles sur
	 * allie
	 * 
	 * @param j
	 *            : Joueur cible
	 * @param s
	 *            : le skill
	 * @throws IOException
	 */
	public void lancerSoin(Joueur j, Skill s) throws IOException {
		byte[] data = new byte[2 + joueurs.getKey(j).length()];
		data[0] = Constants.LANCERSOIN;
		data[1] = (byte) s.getId();
		for (int i = 2; i < data.length; i++) {
			data[i] = (byte) joueurs.getKey(j).charAt(i - 2);
		}
		sendToAll(data);
		game.player.setToken(false);
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				if (game.getScreen() instanceof BattleScreen)
					((BattleScreen) game.getScreen()).updateSkillWindow();
			}
		});
		passerToken();
	}

	/**
	 * Permet d'avertir les autres joueurs qu'un monstre a lance un sort a un
	 * joueur
	 * 
	 * @param mechant
	 *            : Le monstre qui attaque
	 * @param cible
	 *            : Le joueur attaque(ey)
	 * @throws IOException
	 */
	public void npcAttaque(Personnage mechant, Joueur cible) throws IOException {
		ip = joueurs.getKey(cible);

		// DEBUG
		if (ip == null)
			System.err.println("Erreur joueur inexistant");

		int idMonstre = monstres.indexOf(mechant);

		// Action, cible, sort, ip
		byte[] data = new byte[1 + 1 + ip.length()];
		data[0] = Constants.ATTAQUEMONSTRE;
		data[1] = (byte) idMonstre;

		for (int i = 2; i < data.length; i++)
			data[i] = (byte) ip.charAt(i - 2);

		sendToAll(data);
	}

	/**
	 * Methode permettant aux monstre d'attaquer
	 * 
	 * @throws IOException
	 */
	private void attaqueMonstre() throws IOException {

		for (Personnage m : monstres) {
			if (m.getHp() > 0) {
				Joueur cible = (Joueur) game.playersConnected.get((int) Math
						.round(Math.random()
								* (game.playersConnected.size() - 1)));
				npcAttaque(m, cible);
			}
		}
	}

	/**
	 * Permet d'envoyer un message
	 * 
	 * @param m
	 *            : le message
	 * @throws IOException
	 */
	public void envoieMessage(String m) throws IOException {
		// on prend le pseudo du type
		String pseudo = game.player.getNom();
		byte[] data = new byte[2 + pseudo.length() + m.length()];
		data[0] = Constants.MESSAGE;
		// On joind la taille du pseudo pour le traitement
		data[1] = (byte) pseudo.length();

		for (int i = 2; i < pseudo.length() + 2; i++)
			data[i] = (byte) pseudo.charAt(i - 2);

		for (int i = 2 + pseudo.length(); i < data.length; i++)
			data[i] = (byte) m.charAt(i - 2 - pseudo.length());
		// et on envoie
		sendToAll(data);
	}

	/**
	 * quand tout les joueurs sont pret on peut continuer vers la vague suivante
	 */
	public void pretPourVagueSuivante(final String ip) {
		boolean pret = true;
		for (Joueur j : joueurs.values()) {
			if (!j.estPret()) {
				pret = false;
				break;
			}
		}
		if (pret) {
			// Tout le monde est pret, il faut donc reinitialiser le boolean

			for (Joueur j : joueurs.values()) {
				j.setPret(false);
			}

			//on regen les joueurs
			regen();

			// ici, il faut passer le token au premier joueur
			// on va le donner au dernier qui s'est mit pret
			joueurs.get(ip).setToken(true);



			joueurs.get(ip).setaJoueCeTour(true);
			//			System.out.println("A JOUE CE TOUR : " + ip);

			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					if(game.currentVagueIndex == 5){
						game.changeScreen(MyGame.FINALSCREEN);
					}
					else{
						game.currentVagueIndex++;
						game.changeScreen(MyGame.BATTLESCREEN);
						if(ip.equals(monIp)){
							((BattleScreen) game.getScreen()).buildTokenInfo();
						}
					}
					//					((BattleScreen) game.getScreen()).update();

				}
			});
			// }
			return;
		}

	}

	/**
	 * Permet à dire aux autres joueurs qu'on est pret
	 * 
	 * @throws IOException
	 */
	public void estPret() throws IOException {
		byte data[] = new byte[1];
		data[0] = (byte) Constants.PRET;
		sendToAll(data);
	}

	/**
	 * traite l'action "pret" d'un autre joueur
	 * 
	 * @param data
	 */
	public void actionPret() {
		String ip = dpr.getAddress().toString().replace('/', '\0').trim();
		joueurs.get(ip).setPret(true);
		pretPourVagueSuivante(ip);
	}

	/**
	 * Traite le deconnexion
	 */
	public void actionDeco(){
		String ip = dpr.getAddress().toString().replace('/', '\0').trim();
		this.chatWindow.removeName(joueurs.get(ip).getName());
		game.playersConnected.remove(joueurs.get(ip));
		this.joueurs.remove(ip);
	}

	/**
	 * Permet de passer le token a un joueur
	 * 
	 * @throws IOException
	 */
	public void passerToken() throws IOException {

		String ipChoisi = "";

		/*
		 * On passe le token au premiere joueur qui n'a pas joue
		 */
		for (Joueur j : joueurs.values()) {

			if (!j.aJoueCeTour()) {
				//				System.out.println(j.getNom() + " a joue ce tour : "
				//						+ j.aJoueCeTour());
				ipChoisi = joueurs.getKey(j);
				break;
			}

		}

		byte data[];

		/*
		 * Si tout les joueurs ont joué ce tour au passe le token au dernier
		 * joueur de la liste (random quoi...)
		 */
		if (ipChoisi.length() < 1) {
			if (!DEBUG)
				attaqueMonstre();
			ipChoisi = joueurs.getKey((Joueur) game.playersConnected
					.get(game.playersConnected.size() - 1));
			data = new byte[ipChoisi.length() + 1];
			data[0] = Constants.TOKENTOUR;

		} else {
			data = new byte[ipChoisi.length() + 1];
			data[0] = Constants.TOKEN;
		}
		for (int i = 1; i < data.length; i++) {
			data[i] = (byte) ipChoisi.charAt(i - 1);
		}

		sendToAll(data);
	}

	/**
	 * Envoie des donnees a tout le monde
	 * 
	 * @param data
	 *            : tableau de byte a envoyer
	 * @throws IOException
	 */
	private void sendToAll(byte[] data) throws IOException {
		dp = new DatagramPacket(data, data.length);
		for (String ips : joueurs.keySet()) {

			dp.setAddress(InetAddress.getByName(ips));
			dp.setPort(PORT);
			ds.send(dp);
		}
	}

	/**
	 * Methode permettant le déconnexion
	 * @throws IOException 
	 */
	public void deco() throws IOException{
		byte data[] = new byte[1];
		data[0] = Constants.DECO;
		sendToAll(data);
	}
	/**
	 * Permet de regenerer tout les joueurs
	 */
	private void regen() {
		for (Joueur j : joueurs.values()) {
			if (j.getHp() > 0)
				j.setHp(j.getHpMax());
			j.setMana(j.getManaMax());
		}
	}



	public MapPerso<String, Joueur> getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(MapPerso<String, Joueur> joueurs) {
		this.joueurs = joueurs;
	}

	public ArrayList<Monstre> getMonstres() {
		return monstres;
	}

	public void setMonstres(ArrayList<Monstre> monstres) {
		System.out.println(monstres.size());
		this.monstres = monstres;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress.getHostAddress().length() <= 15) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return "";
	}
}
