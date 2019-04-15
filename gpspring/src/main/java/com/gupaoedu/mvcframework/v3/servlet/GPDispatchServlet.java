package com.gupaoedu.mvcframework.v3.servlet;

import com.gupaoedu.mvcframework.annotation.GPAutowired;
import com.gupaoedu.mvcframework.annotation.GPController;
import com.gupaoedu.mvcframework.annotation.GPRequestParam;
import com.gupaoedu.mvcframework.annotation.GPService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by shanwei on 2019/3/28.
 */
public class GPDispatchServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String,Object> ioc = new HashMap<String, Object>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        doScanner(properties.getProperty("scanPackage"));

        doInstance();
        
        doAutowired();
        
        initHandlerMapping();
    }

    private void initHandlerMapping() {
    }

    private void doAutowired() {

        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
           Field[] fields = entry.getClass().getFields();
           for (Field field : fields){
               if (!field.getClass().isAnnotationPresent(GPAutowired.class)) continue;
               GPAutowired autowired = field.getAnnotation(GPAutowired.class);
               String beanName = autowired.value().trim();
               if ("".equals(beanName)){
                   beanName= field.getType().getName();
               }

               field.setAccessible(true);

               try {
                   field.set(entry.getValue(),ioc.get(beanName));
               } catch (IllegalAccessException e) {
                   e.printStackTrace();
               }
           }
        }

    }

    private void doInstance() {

        if (classNames.isEmpty()) return;
        try {
            for (String className : classNames){

                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(GPController.class)){

                    Object instance = clazz.newInstance();
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName,instance);
                }else if (clazz.isAnnotationPresent(GPService.class)){

                    GPService service = clazz.getAnnotation(GPService.class);
                    String beanName = service.value();
                    if (!"".equals(beanName.trim())){
                        beanName= toLowerFirstCase(beanName);
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName,instance);

                    for (Class<?> i : clazz.getInterfaces()){
                        if (ioc.containsKey(i.getName())){
                            throw new Exception("This "+i.getName()+"is EXIST!!!");
                        }

                        ioc.put(i.getName(),instance);
                    }
                }else {
                    continue;
                }
            }
        }catch (Exception e){

            e.printStackTrace();

        }

    }

    private String toLowerFirstCase(String simpleName) {

        char[] ch = simpleName.toCharArray();

        if (ch[0]>='A' && ch[0]<='Z'){
            ch[0]+=32;
        }

        return String.valueOf(ch);
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource("/"+scanPackage.replaceAll("\\.","/"));

        File files = new File(url.getFile());
        for (File file : files.listFiles()){

            if (file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else {
                if (!file.getName().endsWith(".class")) continue;
                String className = (scanPackage+"."+file.getName()).replaceAll(".class","");
                classNames.add(className);
            }
        }


    }

    private void doLoadConfig(String contextConfigLocation) {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Handler{

        private Pattern pattern;
        private Method method;
        private Object controller;
        private Class<?>[] paramTypes;

        public Pattern getPattern() {
            return pattern;
        }

        public Method getMethod() {
            return method;
        }

        public Object getController() {
            return controller;
        }

        public Class<?>[] getParamTypes() {
            return paramTypes;
        }

        private Map<String,Integer> paramIndexMap = new HashMap<String, Integer>();

        public Handler(Pattern pattern,Method method,Object controller){

            this.pattern = pattern;
            this.method = method;
            this.controller = controller;

            paramTypes = method.getParameterTypes();
            paramIndexMapping(method);
        }

        private void paramIndexMapping(Method method) {

            Annotation[][] pa = method.getParameterAnnotations();
            for (int i =0 ;i<pa.length;i++){
                for (Annotation a :pa[i]){
                    if (a instanceof GPRequestParam){
                        String value =((GPRequestParam) a).value();
                        if (!"".equals(value.trim())){
                            paramIndexMap.put(value,i);
                        }
                    }
                }
            }
            
            for (int i=0;i<paramTypes.length;i++){
                
                if (paramTypes[i] == HttpServletRequest.class || 
                        paramTypes[i]==HttpServletResponse.class){
                    paramIndexMap.put(paramTypes[i].getName(),i);
                }
            }
        }
    }
}
