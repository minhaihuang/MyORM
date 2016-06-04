package minhaihuang.jdbc.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * �����Լ���װ��JbbcUtils������
 * @author ��˧��
 *
 */
public class Test01 {

    public static void main(String[] args) {
	//���Բ�ѯ�ģ�����Ҫ�Լ��������ӣ��������ɹ�
	/*
	 Connection conn=null;
	 ResultSet rs=null;
	try {
	    conn=JdbcUtils.createConnection();
	    
	    String sql="select *from T_persons";
	    rs= JdbcUtils.executeQuery(conn, sql);//�ǵùرշ��������ps
	   
	   while(rs.next()){
	       String name=rs.getString("name");
	       int age=rs.getInt("age");
	       System.out.println(name+":"+age);
	   }
	} catch (SQLException e) {
	   System.out.println("��������ʧ��");
	}finally{
	    //�ǵùر�ps,ÿһ�ζ��ǵùر���������
	    try {
		CloseUtil.close(rs,rs.getStatement(),conn);
	    } catch (SQLException e) {
		System.out.println("�ر�preparesStatement��ps");
	    }
	}*/
	
	//���Ըı����ݿ�ģ�����Ҫ�Լ��������ӣ�����,�ɹ���
	//ע��㣬�ǵðѴ�����Ϣ��ӡ���������㴦���쳣
	/*Connection conn=null;
	 try {
	    conn=JdbcUtils.createConnection();
	
	    String sql="insert into T_persons(name,age,gender) values(?,?,?)";
	    
	    Object[] obj={"hhm",20,1};
	    JdbcUtils.executeUpdate(conn, sql, obj);
	} catch (SQLException e) {
	   System.out.println("��������ʧ��"+e.getMessage());
	}finally{
	 CloseUtil.close(conn);
	}*/
	
	//�������������ʱ��������Ҫ�Լ����������벻��Ҫ�Լ��������������ַ���˭����ʱ
	//1������Ҫ�Լ��������ӵĲ��뷽��������1000�����ݣ���ʱ39722���룬�����ԣ����ַ�ʽ��ʡʱ
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
	   System.out.println("��������ʧ��"+e.getMessage());
	}finally{
	 CloseUtil.close(conn);
	}*/
	
	//2��ÿһ�ζ���Ҫ�Լ��������ӵķ���������ʱ41809����
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
	   System.out.println("��������ʧ��"+e.getMessage());
	   
	}
    }
}
