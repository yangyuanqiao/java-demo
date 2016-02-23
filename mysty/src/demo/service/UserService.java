package demo.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by reeco_000 on 2015/7/22.
 */
public class UserService {
	/**
	 * add
	 * @param username
	 * @param password
	 * @return
	 */
    public boolean add(String username,String password){
        String SQL = "SELECT password FROM user WHERE name =?";
        Integer result = Db.queryFirst(SQL, username);
        if(result==null){
            Record user = new Record().set("name", username).set("password", password);
            Db.save("user", user);
            return true;
        }
        return false;
    }
	/**
	 * 
	 * @param username
	 * @return
	 */
    public boolean deleteUserById(String username){
    	   String SQL = "SELECT name FROM user WHERE name =?";
           Integer result = Db.queryFirst(SQL, username);
           if(result==null){
               Record user = new Record().set("name", username);
               Db.delete("user", user);
               return true;
           }
    	return false;
    }
    
    public Page<Record> getByPage(int pageNumber,int pageSize){
    	    
    	        Page<Record> list = Db.paginate(pageNumber,  
    	        		pageSize, "select name,password ", "from user order by name desc"); 
    	        
    	        
    	   return list;
    	  
    }
    
    public boolean login(String username,String password){
        String SQL = "SELECT userid FROM user WHERE username =? and password=?";
        Integer result = Db.queryFirst(SQL, username, password);
        if(result!=null)
            return true;
        else return false;
    }
}