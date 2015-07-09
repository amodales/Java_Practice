import java.io.File;
import java.net.URL;

public class ResourceLoader{
	public static String getImagePath(String RelativePathname, Object o){
		String ret = o.getClass().getResource("ResourceLoader.class").toString();
		String name = "ResourceLoader.class";
		ret = ret.substring(0, ret.length()-name.length());
		ret = ret + RelativePathname;
		return ret;
	}
}
