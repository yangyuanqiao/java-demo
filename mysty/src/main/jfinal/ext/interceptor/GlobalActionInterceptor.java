package main.jfinal.ext.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;

import main.jfinal.utils.StringUtils;

/**
 * 异常处理日志 全局拦截器 
 */
public class GlobalActionInterceptor implements Interceptor {

	private static final Logger log = Logger.getLogger(GlobalActionInterceptor.class);

	@Override
	public void intercept(Invocation invocation) {
		 Controller controller=invocation.getController();
		 HttpServletRequest request=controller.getRequest();
		 boolean successed = false;  
		try {
			invocation.invoke(); // 一定要注意，把处理放在invoke之后，因为放在之前的话，是会空指针
			
			successed = true;  
		} catch (Exception e) {
			// log 处理
			logWrite(invocation, e);
			//判断是否ajax请求  
            String header = request.getHeader("X-Requested-With");  
            boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);  
			String msg = formatException(e);  
            if(isAjax){  
                msg = new StringBuilder().append("{\"status\":\"0\",\"msg\":\"")  
                        .append(msg).append("\"}").toString();  
                controller.renderJson(msg);  
            }else{  
                String redirctUrl = request.getHeader("referer");  
                if(StringUtils.isBlank(redirctUrl)){  
                    redirctUrl = request.getRequestURI();  
                }  
                controller.setAttr("message", msg);  
                controller.setAttr("redirctUrl",redirctUrl);  
                controller.render("/ErorPage.jsp");  
            }  
		} finally {
			
			try {

			} catch (Exception ee) {

			}
		}

	}
	/** 
     * 格式化异常信息，用于友好响应用户 
     * @param e 
     * @return 
     */  
    private static String formatException(Exception e){  
         String message = null;  
            Throwable ourCause = e;  
            while ((ourCause = e.getCause()) != null) {  
                e = (Exception) ourCause;  
            }  
            String eClassName = e.getClass().getName();  
            //一些常见异常提示  
            if("java.lang.NumberFormatException".equals(eClassName)){  
                message = "请输入正确的数字"; 
            }else if(e instanceof RuntimeException){
            	 message = e.getMessage();  
            }
            
            /*else if (e instanceof BaseBussException || e instanceof RuntimeException) {  
                message = e.getMessage();  
                if(StringUtils.isBlank(message))message = e.toString();  
            }  
              */
            //获取默认异常提示  
            if (StringUtils.isBlank(message)){  
                message = "系统繁忙,请稍后再试";  
            }  
            //替换特殊字符  
            message = message.replaceAll("\"", "'");  
            return message;  
    }  
	private void logWrite(Invocation inv, Exception e) {
		// 开发模式
		if (JFinal.me().getConstants().getDevMode()) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder("\n---Exception Log Begin---\n");
		sb.append("Controller:").append(inv.getController().getClass().getName()).append("\n");
		sb.append("Method:").append(inv.getMethodName()).append("\n");
		sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
		sb.append("Exception Details:");
		log.error(sb.toString(), e);

	}
}
