package reseau;


public class NetworkActions {
	
	
	public class Connexion{
		
		
	}
	public class Nouveau{
		String pseudo;
		int CharType;
		
		public Nouveau(String pseudo, int charType) {
			super();
			this.pseudo = pseudo;
			CharType = charType;
		}
		
	}
	public class LancerSkill{
		int skillId;
		int monsterId;
		
		public LancerSkill(int skillId, int monsterId) {
			super();
			this.skillId = skillId;
			this.monsterId = monsterId;
		}
		
		
	}
	public class AttaqueMonstre{
		int monsterId;

		public AttaqueMonstre(int monsterId) {
			super();
			this.monsterId = monsterId;
		}
		
	}
	public class Token{
		int action;

		public Token(int action) {
			super();
			this.action = action;
		}
		
		
	}
	public class TokenTour{
		
	}
	public class Message{
		String playerName;
		String message;
		
		public Message(String playerName, String message) {
			super();
			this.playerName = playerName;
			this.message = message;
		}
		
		
	}
	public class Pret{
		
	}
	public class LancerSoin{
		String ipCible;
		
	}
	public class Deco{
		
	}

}
