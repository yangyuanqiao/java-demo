/**
 * @author Flio
 * @email powerflio@163.com
 */
package demo.interceptor;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
 
public class AutoTableBindPlugin extends ActiveRecordPlugin {
    private ActiveRecordPlugin arp;
    List<File> classList=new ArrayList<File>();
     
    public AutoTableBindPlugin(DataSource dataSource,ActiveRecordPlugin arp) {
        super(dataSource);
        this.arp=arp;
        try {
            scanModel(AutoTableBindPlugin.class.getResource("/").getFile(), "classes/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    @Override
    public boolean stop() {
        return true;
    }
 
    private void scanModel(String fileDir, String pre) throws Exception {
        File dir = new File(fileDir);
        listPath(dir,".class", 0);
        Class clazz=null;
        String className="";
        TableBind tb=null;
        for (File classFile : classList) {
            className = getClassName(classFile, pre);
            clazz = Class.forName(className);
            if (clazz.getSuperclass() == Model.class) {
                tb=(TableBind) clazz.getAnnotation(TableBind.class);
                if(null!=tb.pkName() && ! "".equals(tb.pkName())){
                    arp.addMapping(tb.tableName(),tb.pkName(),clazz);
                }else{
                    arp.addMapping(tb.tableName(),clazz);
                }
            }
        }
    }
     
    private static String getClassName(File classFile, String pre) {
        String objStr = classFile.toString().replaceAll("\\\\", "/");
        String className;
        className = objStr.substring(objStr.indexOf(pre)+pre.length(),objStr.indexOf(".class"));
        if (className.startsWith("/")) {
            className = className.substring(className.indexOf("/")+1);
        }
        return className.replaceAll("/", ".");
    }
 
    public void listPath(File path,String type, int level){  
        File files[] = path.listFiles();
        for (int i = 0; i < files.length; i++){ 
            if(files[i].getName().endsWith(type)){
                classList.add(files[i]);  
            }
            if (files[i].isDirectory())
            {  
                listPath(files[i],type, (level + 1));  
            }  
        }
    }
     
}