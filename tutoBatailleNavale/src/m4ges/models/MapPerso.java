package m4ges.models;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("hiding")
public class MapPerso<String,Personnage> extends HashMap<String, Personnage>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Override
//	public boolean containsKey(Object e){
//		Set<String> key = this.keySet();
//		for (String it : key) {
//			System.out.println("it : " + it + " e : " + e );
//			if(it.equals(e)){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public String getKey(Personnage p){
		Set<String> key = this.keySet();
		for (String it : key) {
			if(p == this.get(it))
				return it;
		}
		return null;
	}
	
//	
//	public Personnage getValue(String ip){
//		Set<String> key = this.keySet();
//		for (String it : key) {
//			if(ip.equals(it))
//				return this.get(it);
//		}
//		return null;
//	}

}
