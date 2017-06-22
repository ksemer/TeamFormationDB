package inputHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {
	//String is node1+","+node2
	private HashMap<String,Integer> edges = new HashMap<String,Integer>();
	private ArrayList<Integer> nodes = new ArrayList<Integer>();
	
	public Network(HashMap<String,Integer> edges, ArrayList<Integer> nodes){
		this.edges=edges;
		this.nodes=nodes;
	}
	
	public HashMap<String,Integer> getEdges(){
		return edges;
	}
	
	public ArrayList<Integer> getNodes(){
		return nodes;
	}
}
