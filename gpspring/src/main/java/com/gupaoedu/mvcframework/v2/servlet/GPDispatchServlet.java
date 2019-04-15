package com.gupaoedu.mvcframework.v2.servlet;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shanwei on 2019/3/27.
 */
public class GPDispatchServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String,Object> IOC = new HashMap<String, Object>();

    private Map<String,Method> handlerMappngs = new HashMap<String, Method>();

    private List<Handler> handlerMappings = new ArrayList<Handler>();

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
               Handler handler = new Handler(Pattern.compile(url),method,entry.getValue());
               handlerMappings.add(handler);
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

        Handler handler= getHandler(req);

        Map<String,String[]> params =  req.getParameterMap();

        Class<?>[] parameterTypes =  handler.getParameterTypes();

        Object[] parameterValues = new Object[parameterTypes.length];

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s",",");
            if (!handler.paramIndexMapping.containsKey(entry.getKey())) continue;

            int index = handler.paramIndexMapping.get(entry.getKey());
            parameterValues[index]= convert(parameterTypes[index],value);
        }

        if (handler.paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            parameterValues[reqIndex] = req;
        }

        if (handler.paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            parameterValues[respIndex]= resp;
        }

        Object returnValue = handler.method.invoke(handler.controller,parameterValues);
        if (returnValue==null || returnValue instanceof Void){return;}
        resp.getWriter().write(returnValue.toString());


    }

    private Object convert(Class<?> parameterType, String value) {

        if (parameterType==Integer.class){
            return Integer.valueOf(value);
        }else if (parameterType ==Double.class){
            return Double.valueOf(value);
        }
        return value;
    }

    private Handler getHandler(HttpServletRequest req) {

        if (handlerMappings.isEmpty()) return null;

        String url = req.getRequestURI();

        String contextPath = req.getContextPath();

        url =url.replaceAll(contextPath,"").replaceAll("/+","/");
        for (Handler handler : handlerMappings){
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) continue;
            return handler;
        }

        return null;
    }

    public class Handler{

        private Pattern pattern;
        private Method method;
        private Object controller;
        private Class<?>[] parameterTypes;

        public Pattern getPattern() {
            return pattern;
        }

        public Method getMethod() {
            return method;
        }

        public Object getController() {
            return controller;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        private  Map<String,Integer> paramIndexMapping ;

        public Handler(Pattern pattern, Method method, Object controller){

            this.pattern = pattern;
            this.method = method;
            this.controller = controller;

            parameterTypes= method.getParameterTypes();
            paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {

           Annotation[][] pa =  method.getParameterAnnotations();
           for (int i=0;i<pa.length;i++){
               for (Annotation a:pa[i]){
                   if (a instanceof GPRequestParam){
                       String paramName = ((GPRequestParam) a).value();
                       if (!"".equals(paramName.trim())){
                            paramIndexMapping.put(paramName,i);
                       }
                   }
               }
           }

           Class<?>[] paramsTypes = method.getParameterTypes();
            for (int i=0;i<paramsTypes.length;i++){
                Class<?> clazz = paramsTypes[i];
                if (clazz==HttpServletResponse.class ||
                        clazz==HttpServletRequest.class){
                    paramIndexMapping.put(clazz.getName(),i);
                }
            }
        }
    }
}
