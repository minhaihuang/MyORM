package minhaihuang.jdbc.Util;

import java.sql.PreparedStatement;

public class CloseUtil {

    public static void close(AutoCloseable... obj){
	for(int i=0;i<obj.length;i++){
	    if(obj[i]!=null){
		try {
		    obj[i].close();
		} catch (Exception e) {
		    System.out.println("关闭流失败");
		}
	    }
	    
	}
    }
}
