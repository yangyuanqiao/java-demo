package main.jfinal.route;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Route ��Controllerע��<br>
 * ��controller��ʹ��
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RouteBind {
	/**��Ӧ��·���� ��/��ͷ*/
	String path() default "/";
	/**��ͼ����Ŀ¼*/
	String viewPath() default "";
}