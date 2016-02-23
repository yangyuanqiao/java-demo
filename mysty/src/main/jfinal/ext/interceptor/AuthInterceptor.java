package main.jfinal.ext.interceptor;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * Created by reeco_000 on 2015/7/22.
 */
public class AuthInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        Controller controller = invocation.getController();
        Boolean loginUser = controller.getSessionAttr("flag");
        if (loginUser ==true )
            invocation.invoke();
        else
            controller.redirect("/");
    }
}