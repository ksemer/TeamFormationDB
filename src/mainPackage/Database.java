package mainPackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSet;
import java.sql.Statement;

public class Database {
	private String mode="";
	 private String url = "";//"jdbc:mysql://localhost:3306/negative_teams?user=root&password=root";
	 private Connection con;
	
	public void init(String mode){
		
		if(mode.equals("sbp")){
			url="jdbc:mysql://localhost:3306/negative_teams?user=root&password=root";
		}
		else{
			url="jdbc:mysql://localhost:3306/negativeteams?user=root&password=root";
		}
		
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		try{
            con = DriverManager.getConnection(url);
       }
       catch(SQLException ex){
            System.err.println("SQLException: "+ex.getMessage());
       }
	}	
	
	public ArrayList<ArrayList<String>> selectAllFrom(String table,int col, String where){
    	ArrayList<ArrayList<String>> stringResult= new ArrayList<ArrayList<String>>();
    	for(int i=0;i<col;i++){
    		ArrayList<String> temp = new ArrayList<String>();
    		stringResult.add(temp);
    	}
    	Statement state;
        ResultSet res;
        String query="select * from "+table+" where "+where;
        try{
            state=con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            while(res.next()){
            	for(int i=1;i<=col;i++){
            		stringResult.get(i-1).add(res.getString(i));
            	}
            }
            state.close();
        }
        catch(SQLException ex)
        {
        	 System.out.println(ex.getMessage());
        }
        return stringResult;
	}
	
	public int getNumOfCompatibles(String table, int node){
    	Statement state;
        ResultSet res;
        int result=-1;
        
        String where="node1="+node+" or node2="+node;
        
        String query="select count(*) from "+table+" where "+where;
        try{
            state=con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            while(res.next()){
            	result=res.getInt(1);
            }
            state.close();
        }
        catch(SQLException ex)
        {
        	 System.out.println(ex.getMessage());
        }
        return result;
	}
	
	public boolean isCompatible(String table, int node1, int node2){
		Statement state;
        ResultSet res;
 
        String where="node1="+node1+" and node2="+node2;
        
        String query="select * from "+table+" where "+where;
        
        try{
            state=con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            if(res.next()){
            	return true;
            }
            state.close();
            
            where="node1="+node2+" and node2="+node1;
            query="select * from "+table+" where "+where;
            
            state=con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            if(res.next()){
            	return true;
            }
            state.close();
            
        }
        catch(SQLException ex)
        {
        	 System.out.println(ex.getMessage());
        }
        
        return false;
	}
}
