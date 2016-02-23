package demo.controller;


import java.util.ArrayList;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import demo.interceptor.AuthInterceptor;
import demo.service.UserService;
import demo.validator.LoginValidator;
import main.jfinal.controller.BaseController;

/**
 * Created by reeco_000 on 2015/7/22.
 */
public class UserController extends BaseController{

    private UserService userService = new UserService();

    
    
    public void index(){
    	
    	/*String username = getPara("username");
    	String password=getPara("age");
    	System.out.println(username);
    	//userService.add(username, password);
    	this.setAttr("aa", username);*/
    	renderJsp("login.jsp");
    }
    

    @Before(LoginValidator.class)
    public void login(){
        String username = getPara("username");
        String password = getPara("password");
        boolean loginCheck = true;//userService.login(username,password);
        if(loginCheck){
            getSession().setAttribute("flag",true);
            renderJsp("index.jsp");
        }
            
    }
    /**
     * 获取分页的用户信息
     */
    public void getAllUser(){
    	int cur_page = Integer.parseInt(getPara("page")==null ? "1" : getPara("page") );//当前页码
    	int pageSize=2;
    	ArrayList arry = new ArrayList();
    	arry.add("a");
    	arry.add("bb");
    	Page<Record> userList = userService.getByPage(cur_page+1, pageSize);
    	this.setAttr("userList",userList.getList() );
    	this.setAttr("message", "dddd");
    	this.setAttr("curPage", userList.getPageNumber()-1);
    	this.setAttr("pageSize", userList.getPageSize());
    	this.setAttr("pageCount", userList.getTotalRow());
    	renderFreeMarker("query.html");
    	
    }
    
    
    public void register(){
        String username = getPara("username");
        String password = getPara("password");
        boolean result = true;
        if(result)
            renderJson("10010");
        else
            renderJson("10011");
    }

    @Before(AuthInterceptor.class)
    public void show(){
        renderJsp("user.jsp");
    }

    public void image(){
        try{
            getFile(getPara("img"),"UTF-8");
            renderJson("20010");
        } catch (Exception e){
            renderJson("20012");
        }
    }
}