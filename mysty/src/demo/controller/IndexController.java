package demo.controller;

import main.jfinal.controller.BaseController;

/**
 * Created by reeco_000 on 2015/7/22.
 */
public class IndexController extends BaseController{

    public void index(){
    	System.out.println("===================");
    	render("/demo.jsp");
    }
    
	public void haha(){
		System.out.println("hahahahha");
	
		//UserService serv = new UserService();
		//serv.add("aa", "");
		render("/demo/demo.jsp");
	}
	
	
    public void hehe(){
    	String userName = this.getAttr("userName");
    	String sayHello = "Hello " + userName + "3333.";
    	this.setAttr("a", sayHello);
    	
    	render("/demo.jsp");
    }
}