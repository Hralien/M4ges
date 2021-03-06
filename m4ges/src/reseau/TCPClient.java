package reseau;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import m4ges.controllers.MyGame;
import m4ges.models.MapPerso;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.classes.Joueur;
import m4ges.models.monster.Monstre;
import m4ges.util.Constants;
import m4ges.views.ChatWindow;

public class TCPClient {

	ServerSocket serverSocket;
	
	/**
	 * Port sur lequel les joueurs les vont communiquer
	 */
	public final static int PORT = 12345;
	
	/**
	 * Hashmap des joueurs de la forme <IP, JOUEURS>
	 */
	private MapPerso<Socket, Joueur> joueurs;
	
	/**
	 * Liste des monstres
	 */
	private ArrayList<Monstre> monstres;
	
	/**
	 * Le jeu
	 */
	public MyGame game;

	/**
	 * ip de l'user
	 */
	public String monIp;
	/**
	 * Chat window, utile pour le chat
	 */
	public ChatWindow chatWindow;
	
	/**
	 * Mon socket
	 */
	Socket monSocket;
	
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
	 * @throws IOException 
	 */
	public TCPClient(MyGame g){
		// initialisation
		this.game = g;
		joueurs = new MapPerso<Socket, Joueur>();
		monstres = new ArrayList<Monstre>();
		game.playersConnected.add(game.player);		
		try {
			lancerServeur(PORT);
			receiveBroadcast();
			sendBroadcast();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * @TODO 
	 */
	public void deco(){
		
	}
	
	public void lancerSort(Personnage p, Skill s) {
		
	}
	
	public void estPret(){
		
	}
	
	
	public void envoieMessage(String m) throws IOException {
		sendToAll(m);
	}
	
	/**
	 * @TODO la m�thode
	 * @param o : objet recu
	 */
	private void dealWithObject(Object o, String ip) {
		//Player who sent the object (USELESS ?)
		Joueur j = getPlayerByIP(ip);
		
		if (o instanceof String) {
			messageRecu((String)o, j.getName());
		}
		
	}
	
	private void messageRecu(String m, String pseudo) {
		this.chatWindow.addMessage(pseudo + " : " + m);
	}
	
	private void receiveData(final Socket s) throws IOException {
		final String ip = s.getInetAddress().toString();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
						Object o = ois.readObject();
						System.out.println("objet recu");
						dealWithObject(o, ip);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}
	

	private void lancerServeur(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket s = serverSocket.accept();
						joueurs.put(s, null);
						System.out.println("Connexion au serveur");
						receiveData(s);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}
	
	private Socket connection(InetAddress ip, int port) throws UnknownHostException, IOException{
		System.out.println(ip);
		Socket s= new Socket(ip, port);
		System.out.println("Connection ok");
		return s;
	}
	
	private void sendBroadcast() throws IOException{
		DatagramSocket ds = new DatagramSocket();
		String s = "Nouveau joueur";
		DatagramPacket dp = new DatagramPacket(s.getBytes(), s.length(), InetAddress.getByName("255.255.255.255"), 32144);
		ds.send(dp);
		String[] broadcastTab = TCPClient.getLocalIpAddress().split("\\.");
		String broadcast = broadcastTab[0] + "." + broadcastTab[1] + "."
				+ broadcastTab[2] + ".255";
		dp = new DatagramPacket(s.getBytes(), s.length(),
				InetAddress.getByName(broadcast), 32144);
		ds.close();
	}
	
	/**
	 * Permet de recevoir les donnees
	 */
	private void receiveBroadcast() {
		final DatagramSocket ds;
		// Permet de recevoir les donnes
		try {
			ds = new DatagramSocket(32144);
			ds.setBroadcast(true);
		} catch (SocketException e1) {
			game.androidUI.showAlertBox("D�j� connect�",
					"Vous �tes d�j� connect�", "ok", null);
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// tableau de 128 octets (et c'est large)
					byte[] data = new byte[128];
					DatagramPacket dp = new DatagramPacket(data, data.length);
					try {
						// recepetion
						ds.receive(dp);
						System.out.println("Broadcast recu");
						data = dp.getData();
						String ip = dp.getAddress().toString();
						boolean exist = false;
						for(Socket s:joueurs.keySet()){
							if (s.getInetAddress().toString().equals(ip)){
								exist = true;
								break;
							}
						}
						if (!exist) {
							Socket s = connection(dp.getAddress(), PORT);
//							sendPlayer(s);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}
	
//	private void sendPlayer(Socket s) throws IOException {
//		sendTo(s, game.player);
//	}
	
	private void sendTo(Socket s, Object o) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeObject(o);
	}
	
	private void sendToAll(Object o) throws IOException {
		for (Socket s:joueurs.keySet()) {
			System.out.println(s.getInetAddress().toString());
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			os.writeObject(o);
		}
	}
	
	/**
	 * Get a player by his ip
	 * @param ip
	 * @return
	 */
	private Joueur getPlayerByIP(String ip) {
		Joueur j = null;
		for(Socket s:joueurs.keySet()){
			if (s.getInetAddress().toString().equals(ip)){
				j = joueurs.get(s);
				break;
			}
		}
		return j;
	}
	
	public static String getLocalIpAddress() {
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
	
	public ArrayList<Monstre> getMonstres() {
		return monstres;
	}

	public void setMonstres(ArrayList<Monstre> monstres) {
		System.out.println(monstres.size());
		this.monstres = monstres;
	}
	
}
