
import java.util.Date;  
import java.util.Map;  
import java.util.Set;  
  
import javax.servlet.http.HttpServletRequest;  
  
import org.apache.commons.lang.StringUtils;  
  
import com.smhaochi.exception.BaseBussException;  
import com.smhaochi.model.ActionLog;  
import com.smhaochi.model.Menu;  
import com.smhaochi.vo.ActionLogVo;  
import com.smhaochi.web.controller.BaseController;  
import com.jfinal.aop.Interceptor;  
import com.jfinal.core.ActionInvocation;  
import com.jfinal.core.JFinal;  
import com.jfinal.log.Logger;  
  
/** 
 * @title: ��־/�쳣���������� 
 * @className: ActionLogInterceptor 
 * @description: TODO 
 * @company: FOREVEROSS 
 * @author: <a href="http://www.smhaochi.com">vakin jiang</a> 
 * @createDate: 2014��4��2�� 
 * @version: 1.0 
 */  
public class ExceptionAndLogInterceptor implements Interceptor {  
  
    private static final Logger log = Logger.getLogger(ExceptionAndLogInterceptor.class);  
    @Override  
    public void intercept(ActionInvocation ai) {  
        BaseController controller = (BaseController)ai.getController();  
        HttpServletRequest request = controller.getRequest();  
          
        boolean successed = false;  
        try {  
            ai.invoke();  
            successed = true;  
        } catch (Exception e) {  
            //  
            doLog(ai,e);  
            //�ж��Ƿ�ajax����  
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
                controller.render("../public/failed.ftl");  
            }  
        }finally{  
            //��¼��־  
            try {  
                Menu menu = matchDefineRecordLogMenu(request);  
                if(menu != null){  
                    ActionLogVo actionLog = controller.wrapActionLog();  
                    new ActionLog().set(ActionLog.ACT_NAME, menu.getName())//  
                                   .set(ActionLog.ACT_TIME, new Date())//  
                                   .set(ActionLog.DATA, actionLog.getData())//  
                                   .set(ActionLog.IP, actionLog.getIp())//  
                                   .set(ActionLog.SUCESSED, successed)//  
                                   .set(ActionLog.URI, actionLog.getUri())//  
                                   .set(ActionLog.USER_ID, actionLog.getUserId())//  
                                   .set(ActionLog.USER_NAME, actionLog.getUserName())//  
                                   .save();  
                }  
            } catch (Exception e2) {}  
              
        }  
    }  
      
    /** 
     * @methodName: matchDefineRecordLogMenu 
     * @description: ƥ���Ѷ�����Ҫ��¼��־�Ĳ˵� 
     * @author: vakinge 
     * @createDate: 2014��4��3�� 
     * @param request 
     * @return  
     */  
    private Menu matchDefineRecordLogMenu(HttpServletRequest request) {  
        Map<String, Menu> menus = Menu.getAllUrlMenus();  
        if(menus != null){  
            String uri = request.getRequestURI();  
            Set<String> urls = menus.keySet();  
            for (String url : urls) {  
                if(url == null)continue;  
                //urlƥ�� && �в��� && �����ü�¼��־  
                if(url.contains(uri)   
                        && request.getParameterNames().hasMoreElements()  
                        && menus.get(url).isRecordLog()){  
                    return menus.get(url);  
                }  
            }  
        }  
        return null;  
    }  
  
    /** 
     * @methodName: doLog 
     * @description: ��¼log4j��־ 
     * @author: vakinge 
     * @createDate: 2014��4��3�� 
     * @param ai  
     * @param e  
     */  
    private void doLog(ActionInvocation ai,Exception e) {  
        //����ģʽ  
        if(JFinal.me().getConstants().getDevMode()){  
            e.printStackTrace();  
        }  
        //ҵ���쳣����¼ ����  
        if( e instanceof BaseBussException)return;  
        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");  
        sb.append("Controller:").append(ai.getController().getClass().getName()).append("\n");  
        sb.append("Method:").append(ai.getMethodName()).append("\n");  
        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");  
        sb.append("Exception Details:");  
        log.error(sb.toString(), e);  
    }  
  
    /** 
     * ��ʽ���쳣��Ϣ�������Ѻ���Ӧ�û� 
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
            //һЩ�����쳣��ʾ  
            if("java.lang.NumberFormatException".equals(eClassName)){  
                message = "��������ȷ������";  
            }else if (e instanceof BaseBussException || e instanceof RuntimeException) {  
                message = e.getMessage();  
                if(StringUtils.isBlank(message))message = e.toString();  
            }  
              
            //��ȡĬ���쳣��ʾ  
            if (StringUtils.isBlank(message)){  
                message = "ϵͳ��æ,���Ժ�����";  
            }  
            //�滻�����ַ�  
            message = message.replaceAll("\"", "'");  
            return message;  
    }  
  
}
package main.jfinal.ext.interceptor;

public class vv {

}
