package com.spring.pattern.proxy.dynamicproxy.gpproxy;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shanwei on 2019/4/9.
 */
public class GPProxy {

    public static final String ln ="\r\n";

    public static Object newProxyInstance(GPClassLoader loader,
                                          Class<?>[] interfaces,
                                          GPInvocationHandler h) throws Exception {

        String src = generateSrc(interfaces);

        String filePath = GPProxy.class.getResource("").getPath();
        File file = new File(filePath+"$Proxy0.java");
        FileWriter fw = new FileWriter(file);
        fw.write(src);
        fw.flush();
        fw.close();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manage = compiler.getStandardFileManager(null,null,null);
        Iterable iterable = manage.getJavaFileObjects(file);

        JavaCompiler.CompilationTask task = compiler.getTask(null,manage,null,null,null,iterable);
        task.call();
        manage.close();

        Class proxyClass = loader.findClass("$Proxy0");
        Constructor constructor = proxyClass.getConstructor(GPInvocationHandler.class);
        return constructor.newInstance(h);
    }

    private static String generateSrc(Class<?>[] interfaces) {

        StringBuffer sb = new StringBuffer();
        sb.append("package com.spring.pattern.proxy.dynamicproxy.gpproxy;"+ln);
        sb.append("import java.lang.reflect.*;"+ln);
        sb.append("public class $Proxy0 implements "+ interfaces[0].getName()+"{"+ln);
            sb.append("GPInvocationHandler h;"+ln);
            sb.append("public $Proxy0(GPInvocationHandler h){"+ln);
                sb.append("this.h = h;"+ln);
            sb.append("}"+ln);

            for (Method m : interfaces[0].getMethods()){
                Class<?>[] params = m.getParameterTypes();

                StringBuffer paramNames = new StringBuffer();
                StringBuffer paramValues = new StringBuffer();
                StringBuffer paramClasses = new StringBuffer();

                for (int i=0; i< params.length;i++){
                    Class clazz = params[i];
                    String type = clazz.getName();
                    String paramName = toLowerFirstCase(clazz.getSimpleName());
                    paramClasses.append(type+".class");
                    paramNames.append(type+" "+paramName);
                    paramValues.append(paramName);
                    if (i>0 && i<params.length-1){
                        paramClasses.append(",");
                        paramNames.append(",");
                        paramValues.append(",");
                    }
                }

                sb.append("public "+m.getReturnType().getName()+" "+m.getName()+"("+paramNames.toString()+"){"+ln);
                    sb.append("try{"+ln);
                        sb.append("Method m ="+interfaces[0].getName()+".class.getMethod(\""+m.getName()+"\",new Class[]{"+paramClasses.toString()+"});"+ln);
                        sb.append((hasReturnValue(m.getReturnType())?"return ":"")+getCaseCode("this.h.invoke(this,m,new Object[]{"+paramValues+"})",m.getReturnType())+";"+ln);
                    sb.append("}catch(Exception _ex){}"+ln);
                    sb.append("catch(Throwable e){"+ln);
                        sb.append("throw new UndeclaredThrowableException(e);"+ln);
                    sb.append("}"+ln);
                    sb.append(getReturnEmptyCode(m.getReturnType()));
                sb.append("}"+ln);
            }
        sb.append("}");

        return sb.toString();
    }

    private static String getCaseCode(String code,Class<?> returnClass){
        if(mappings.containsKey(returnClass)){
            return "((" + mappings.get(returnClass).getName() +  ")" + code + ")." + returnClass.getSimpleName() + "Value()";
        }
        return code;
    }

    private static boolean hasReturnValue(Class<?> clazz){
        return clazz != void.class;
    }

    private static Map<Class,Class> mappings = new HashMap<Class, Class>();
    static {
        mappings.put(int.class,Integer.class);
    }

    private static String getReturnEmptyCode(Class<?> returnClass){
        if(mappings.containsKey(returnClass)){
            return "return 0;";
        }else if(returnClass == void.class){
            return "";
        }else {
            return "return null;";
        }
    }

    private static String toLowerFirstCase(String simpleName) {

        char[] ch = simpleName.toCharArray();
        if (ch[0]>='A' && ch[0]<='Z'){
            ch[0]+=32;
        }

        return String.valueOf(ch);
    }
}
