package com.gupaoedu.mvcframework.v1.servlet;

import com.gupaoedu.mvcframework.annotation.*;

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

/**
 * Created by shanwei on 2019/3/27.
 */
public class GPDispatchServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String,Object> IOC = new HashMap<String, Object>();

    private Map<String,Method> handlerMappngs = new HashMap<String, Method>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        doScanner(contextConfig.getProperty("scanPackage"));

        doInstance();

        doAutowired();

        initHandlerMapping();
    }

    private void initHandlerMapping() {

        if (IOC.isEmpty())return;
        for (Map.Entry<String, Object> entry : IOC.entrySet()) {

            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(GPController.class)) continue;

            String baseUrl = "";
            if (clazz.isAnnotationPresent(GPRequestMapping.class)){
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            for (Method method : clazz.getMethods()){
                if (!method.isAnnotationPresent(GPRequestMapping.class)) continue;
                GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                String url = ("/"+baseUrl+"/"+requestMapping.value()).replaceAll("/+","/");
                handlerMappngs.put(url,method);
                System.out.println("Mapped:"+url+":"+method);
            }

        }

    }

    private void doAutowired() {

        if (IOC.isEmpty()) return;
        for (Map.Entry<String, Object> entry : IOC.entrySet()) {

            Field[] fields = entry.getKey().getClass().getFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(GPAutowired.class)) continue;
                GPAutowired autowired = field.getAnnotation(GPAutowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)){
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);

                try {
                    field.set(entry.getValue(),IOC.get(beanName));
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
                    IOC.put(beanName,instance);

                }else if (clazz.isAnnotationPresent(GPService.class)){

                    GPService service = clazz.getAnnotation(GPService.class);
                    String beanName = service.value();
                    if (!"".equals(beanName.trim())){
                        beanName = toLowerFirstCase(beanName);
                    }

                    Object instance = clazz.newInstance();
                    IOC.put(beanName,instance);

                    for (Class<?> i : clazz.getInterfaces()){

                        if (IOC.containsKey(i.getName())){
                            throw new Exception("The “"+i.getName()+"“ is exists!!!!");
                        }
                        IOC.put(i.getName(),instance);
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
        File classPath = new File(url.getFile());
        for (File files : classPath.listFiles()){
            if (files.isDirectory()){
                doScanner(scanPackage+"."+ files.getName());
            }else {
                if (!files.getName().endsWith(".class")){continue;}
                String className = scanPackage+"."+files.getName().replace(".class","");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {

        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);

        try {
            contextConfig.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String url = req.getRequestURI();

        String contextPath = req.getContextPath();

        url = url.replaceAll(contextPath,"").replaceAll("/+","/");
        if (!handlerMappngs.containsKey(url)){
            resp.getWriter().write("404 Not Found!!!!!!!!");
            return;
        }

        Method method = handlerMappngs.get(url);

        Map<String,String[]> params =  req.getParameterMap();

        Class<?>[] parameterTypes =  method.getParameterTypes();

        Object[] parameterValues = new Object[parameterTypes.length];

        for (int i=0 ; i< parameterTypes.length;i++){

           Class parameterType= parameterTypes[i];
           if (parameterType == HttpServletRequest.class){
               parameterValues[i]= req;
               continue;
           }else if (parameterType==HttpServletResponse.class){
               parameterValues[i] = resp;
               continue;
           }else {
                GPRequestParam requestParam = null;
                Annotation[][] pa = method.getParameterAnnotations();
                for (int j=0;j<pa.length;j++){
                    for (Annotation a : pa[j]){
                        if (a instanceof GPRequestParam){
                            String paramName = ((GPRequestParam) a).value();
                            String value = Arrays.toString(params.get(paramName))
                                    .replaceAll("\\[|\\]","")
                                    .replaceAll("\\s",",");
                            parameterValues[j] = convter(parameterTypes[j],value);
                        }
                    }
                }
           }

        }

        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        method.invoke(IOC.get(beanName),parameterValues);

    }

    private Object convter(Class<?> parameterType, String value) {

        if (parameterType==Integer.class){
            return Integer.valueOf(value);
        }else if (parameterType==Double.class){
            return Double.valueOf(value);
        }

        return value;
    }
}
