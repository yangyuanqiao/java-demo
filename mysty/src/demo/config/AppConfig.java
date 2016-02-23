package demo.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

import main.jfinal.ext.interceptor.GlobalActionInterceptor;
import main.jfinal.plugin.tablebind.AutoTableBindPlugin;
import main.jfinal.plugin.tablebind.TableNameStyle;
import main.jfinal.route.MyRoutesUtil;

/**
 * Created by reeco_000 on 2015/7/22.
 */
public class AppConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants constants) {
		constants.setEncoding("UTF-8");
		constants.setDevMode(true);
//		me.setViewType(ViewType.JSP); 							// 设置视图类型为Jsp，否则默认为FreeMarker
		constants.setError404View("/404.html");
		constants.setError500View("/ErorPage.jsp");
		//constants.setBaseViewPath("/WEB-INF/pages/");
		constants.setViewType(ViewType.JSP);
	}
	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes routes) {

		MyRoutesUtil.add(routes);//自动注册
		
		//手动注册
//		routes.add("/hello", IndexController.class);
		//routes.add("/user", UserController.class);
	}

	@Override
	public void configPlugin(Plugins plugins) {
		// 这里启用Jfinal插件

		PropKit.use("jdbc.properties");
		final String URL = PropKit.get("jdbcUrl");
		final String USERNAME = PropKit.get("user");
		final String PASSWORD = PropKit.get("password");
		final Integer INITIALSIZE = PropKit.getInt("initialSize");
		final Integer MIDIDLE = PropKit.getInt("minIdle");
		final Integer MAXACTIVEE = PropKit.getInt("maxActivee");

		DruidPlugin druidPlugin = new DruidPlugin(URL, USERNAME, PASSWORD);
		druidPlugin.set(INITIALSIZE, MIDIDLE, MAXACTIVEE);
		//druidPlugin.setFilters("stat,wall");
		druidPlugin.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		druidPlugin.addFilter(wall);
		//ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		//activeRecordPlugin.setShowSql(true);
		plugins.add(druidPlugin);
		
		 AutoTableBindPlugin autoTableBindPlugin = new AutoTableBindPlugin(
				 druidPlugin, TableNameStyle.LOWER);
		    autoTableBindPlugin.setShowSql(true);//这句话就是ShowSql
		plugins.add(autoTableBindPlugin);
		
		

	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		// 这里用于配置全局的拦截器，对所有请求进行拦截
		//全局拦截器，对所有请求拦截

        //添加控制层全局拦截器
        //interceptors.addGlobalActionInterceptor(new GlobalActionInterceptor());
        interceptors.addGlobalActionInterceptor(new GlobalActionInterceptor());
        
        //添加业务层全局拦截器
        //interceptors.addGlobalServiceInterceptor(new GlobalServiceInterceptor());
        //interceptors.addGlobalServiceInterceptor(new ExceptionIntoLogInterceptor());

        //兼容老版jfinal写法
        //interceptors.add(new GlobalActionInterceptor());
      //声明式事务处理
        //interceptors.add(new TxByActionMethods("save","update","delete","batch"));
	}
	
	
	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(new ContextPathHandler("basePath"));
	}
	
	/**

	 * 运行此 main 方法可以启动项目

	 * 说明：

	 * 1. linux 下非root账户运行端口要>1024

	 * 2. idea 中运行webAppDir路径可能需要适当调整，可以设置为WebContent的绝对路径

	 */
	public static void main(String[] args) {
		JFinal.start("WebContent", 99, "/", 5);
		//JFinal.start("D:\\DevelopmentTool\\IntelliJIDEA14.1.4\\IdeaProjects\\JFinalUIBV2\\JFinalUIBV2\\WebContent", 99, "/", 5);


	}
}