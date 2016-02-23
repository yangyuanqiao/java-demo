package main.jfinal.route;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

/**
 * Routes 工具类 自动绑定Controller
 * 
 *  
 */
public class MyRoutesUtil{
	
	private static final Logger log = Logger.getLogger(MyRoutesUtil.class);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void add(Routes me){
		log.info("==================init my routes====================");
		List<Class> list= ClassSearcher.findClasses();
		if(list!=null&&list.isEmpty()==false){
			for(Class clz:list){
				RouteBind rb=(RouteBind)clz.getAnnotation(RouteBind.class);
				if(rb!=null){
					me.add(rb.path(),clz,rb.viewPath());
				}else if(clz.getSuperclass()!=null){
					if(clz.getSuperclass()==Controller.class||clz.getSuperclass().getSuperclass()==Controller.class){
						me.add("/"+clz.getSimpleName().replace("Controller", "").toLowerCase(),clz);
					}
				}
			}
		}
	}
	
}