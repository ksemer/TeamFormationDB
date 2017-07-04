package inputHandler;

import java.util.ArrayList;
import java.util.HashMap;

import mainPackage.Database;

public class DatabaseInput {
	private Database db = new Database();
	
	public void init(){
		db.init();
	}
	
	public HashMap<Integer,Integer> getCompatibles(String table, int node){
		
		HashMap<Integer,Integer> compatibles = new HashMap<Integer,Integer>();
		
		String where = "node1="+node;
		
		ArrayList<ArrayList<String>> t1=db.selectAllFrom(table, 3, where);
		for(int i=0;i<t1.get(0).size();i++){
			compatibles.put(Integer.parseInt(t1.get(1).get(i)), Integer.parseInt(t1.get(2).get(i)));
			//System.out.println(t.get(0).get(i)+"-"+t.get(1).get(i)+"-"+t.get(2).get(i));
		}
		
		where = "node2="+node;
		
		ArrayList<ArrayList<String>> t2 = db.selectAllFrom(table, 3, where);
		for(int i=0;i<t2.get(0).size();i++){
			compatibles.put(Integer.parseInt(t2.get(0).get(i)), Integer.parseInt(t2.get(2).get(i)));
		}
		
		return compatibles;
	}
	
	public int getNumOfCompatibles(String table, int node){
		
		return db.getNumOfCompatibles(table, node);
	}

}
