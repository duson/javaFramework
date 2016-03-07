package duson.java.utils;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * 
 * 获取项目路径操作类
 */
public class PathTool {
    /**
     * 方法描述：根据class获取web应用的绝对路径 例如 D:\work\testPE\WebRoot\
     * 
     * @return String 绝对路径
     */
    public static String getWebRootPath() {
        String classPath = getClassPath();
        String webInfUrl = classPath.substring(0, classPath.indexOf("classes"));
        String webrootUrl = webInfUrl.substring(0, classPath.indexOf("WEB-INF"));
        return webrootUrl;
    }
    
    /**
     * 方法描述：获取web应用下的class路径绝对路径 例如 D:\work\testPE\WebRoot\WEB-INF\classes\
     * 
     * @return ClassPath 绝对路径
     */
    public static String getClassPath() {
        String classPath = getClassPath(PathTool.class);
        String classPathUrl = null;
        try {
            classPathUrl = classPath.substring(0, classPath.indexOf("classes"));
        }
        catch (Exception e) {
            File file = new File("");
            classPathUrl = file.getAbsolutePath() + "/WebRoot/WEB-INF/";
        }
        classPathUrl += "classes/";
        return classPathUrl.replace("%20", " ");
    }
    
    public static String getClassPathForJar() {
        String classPathUrl = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        return classPathUrl.replace("%20", " ");
    }
    
    /**
     * 方法描述：根据class获取web应用的路径绝对路径
     * 
     * @param c 用来定位的类
     * @return ClassPath 绝对路径
     */
    private static String getClassPath(Class<PathTool> c) {
        ProtectionDomain pd = c.getProtectionDomain();
        if (pd == null) {
            return null;
        }
        CodeSource cs = pd.getCodeSource();
        if (cs == null) {
            return null;
        }
        URL url = cs.getLocation();
        if (url == null) {
            return null;
        }
        String classPath = url.getPath();
        return classPath;
    }
    
    /**
     * 获取 String绝对路径
     */
    public static String getAbsolutePath(String resource) {
        return getUrl(resource).getPath();
    }
    
    /**
     * 获取 URL 绝对路径,此方法可能会不适合在那种自己写类装载器的环境中使用
     */
    public static URL getUrl(String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource);
    }
    
}
