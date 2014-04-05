package m4ges.models.cinematiques;

public class Message {
    private float x, y;
    private String contenu;
    
    
    public Message(String contenu) {
		super();
		this.contenu = contenu;
		this.x = 0;
		this.y = 0;
	}

	public Message(float x, float y, String contenu) {
		super();
		this.x = x;
		this.y = y;
		this.contenu = contenu;
	}

	public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public void setX(float newValues) {
        this.x = newValues;
    }

    public void setY(float newValues) {
        this.y = newValues;
    }

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
}