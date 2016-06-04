package minhaihuang.jdbc.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 测试自己封装的JbbcUtils工具类
 * @author 黄帅哥
 *
 */
public class Test01 {

    public static void main(String[] args) {
	//测试查询的（不需要自己创建连接）方法，成功
	/*
	 Connection conn=null;
	 ResultSet rs=null;
	try {
	    conn=JdbcUtils.createConnection();
	    
	    String sql="select *from T_persons";
	    rs= JdbcUtils.executeQuery(conn, sql);//记得关闭方法里面的ps
	   
	   while(rs.next()){
	       String name=rs.getString("name");
	       int age=rs.getInt("age");
	       System.out.println(name+":"+age);
	   }
	} catch (SQLException e) {
	   System.out.println("创建连接失败");
	}finally{
	    //记得关闭ps,每一次都记得关闭三个东西
	    try {
		CloseUtil.close(rs,rs.getStatement(),conn);
	    } catch (SQLException e) {
		System.out.println("关闭preparesStatement的ps");
	    }
	}*/
	
	//测试改变数据库的（不需要自己创建连接）方法,成功，
	//注意点，记得把错误信息打印出来，方便处理异常
	/*Connection conn=null;
	 try {
	    conn=JdbcUtils.createConnection();
	
	    String sql="insert into T_persons(name,age,gender) values(?,?,?)";
	    
	    Object[] obj={"hhm",20,1};
	    JdbcUtils.executeUpdate(conn, sql, obj);
	} catch (SQLException e) {
	   System.out.println("创建连接失败"+e.getMessage());
	}finally{
	 CloseUtil.close(conn);
	}*/
	
	//当插入多条数据时，测试需要自己创建连接与不需要自己创建连接着两种方法谁更耗时
	//1，不需要自己创建连接的插入方法，插入1000条数据：耗时39722毫秒，经测试，此种方式更省时
	/*Connection conn=null;
	long start=System.currentTimeMillis();
	 try {
	    conn=JdbcUtils.createConnection();
	
	    String sql="insert into T_persons(name,age,gender) values(?,?,?)";
	    
	    Object[] obj={"hhm",20,1};
	    for(int i=0;i<1000;i++){
	    JdbcUtils.executeUpdate(conn, sql, obj);
	    }
	    
	    long end=System.currentTimeMillis();
	    long time=end-start;
	    System.out.println(time);
	} catch (SQLException e) {
	   System.out.println("创建连接失败"+e.getMessage());
	}finally{
	 CloseUtil.close(conn);
	}*/
	
	//2，每一次都需要自己创建连接的方法，共耗时41809毫秒
	long start=System.currentTimeMillis();
	 try {

	    String sql="insert into T_persons(name,age,gender) values(?,?,?)";
	    
	    Object[] obj={"hhm",20,1};
	    for(int i=0;i<1000;i++){
	    JdbcUtils.executeUpdate(sql, obj);
	    }
	    
	    long end=System.currentTimeMillis();
	    long time=end-start;
	    System.out.println(time);
	} catch (SQLException e) {
	   System.out.println("创建连接失败"+e.getMessage());
	   
	}
    }
}
