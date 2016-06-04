package minhaihuang.jdbc.Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtils {

    private static final String driverName;
    private static final String url;
    private static final String userName;
    private static final String passWord;
    
    //�������ļ��л�ȡ������ƣ���ݿ���ƣ��û����������Ϣ
    //��static������װ��ʹ���ڳ���һ��ʼ���е�ʱ����ܹ���ɳ�ʼ��
    static{
	//��������ȡ�����ļ�
	InputStream is=null;
	//��ȡ�����ļ�
	try{	
	    Properties prop=new Properties();

	    is=JdbcUtils.class.getClassLoader().getResourceAsStream(
			"minhaihuang/jdbc/Util/confige.properties");
	    prop.load(is);
	    
	    //��ʼ������
	    driverName=prop.getProperty("driverName");
	     url=prop.getProperty("url");
	    userName=prop.getProperty("userName");
	    passWord=prop.getProperty("passWord");
	
	  //����jdbc�����Ϊֻ�����һ�Σ����Բ����ڷ���������
	    Class.forName(driverName);
	} catch (IOException e) {
	   throw new RuntimeException("���������ļ�ʧ��",e);
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException("����jdbc���ʧ��",e);
	}finally{
	    CloseUtil.close(is);
	}
    }
  
    //������ȡ���ӵķ���
    public static Connection createConnection() throws SQLException{
	return DriverManager.getConnection(url,userName,passWord);
    }
    
    //����������ݿ�ķ���
    public static int executeUpdate(Connection conn,String sql,Object... obj) throws SQLException {
	//��װҪִ�е�sql���
	PreparedStatement ps=null;;
	try {
	    ps = conn.prepareStatement(sql);
	    
	        //���ⲿ���ݲ�������sql���
		for(int i=0;i<obj.length;i++){
		    ps.setObject(i+1, obj[i]);//�˷������ڲ�ͬ���±��1��ʼ��
		}
	
		//ִ��sql���,������ֵ
		return ps.executeUpdate();
	}finally{
	    CloseUtil.close(ps);
	}
    }
    
    /**
     * ������ѯ��ݿ�ķ���
     * @throws SQLException 
     */
    public static ResultSet executeQuery(Connection conn,String sql,Object... obj) throws SQLException{
	//��װҪִ�е�sql���
	PreparedStatement ps=conn.prepareStatement(sql);
	
	//���ⲿ���ݲ�������sql���
	for(int i=0;i<obj.length;i++){
	    ps.setObject(i+1, obj[i]);//�˷������ڲ�ͬ���±��1��ʼ��
	}
	
	return ps.executeQuery();
    }
    
    /**
     * ��װ��Ҫ�Լ��������ӵĸ�����ݿ�ķ���
     * @throws SQLException 
     */
    public static int executeUpdate(String sql,Object... obj) throws SQLException {
	Connection conn=null;
	

	    conn=createConnection();
	 

	return executeUpdate(conn,sql,obj);
    }
    
    /**
     * ��װ��Ҫ�Լ��������ӵĲ�ѯ��ݿ�ķ���
     * @throws SQLException 
     */
    
    public static ResultSet executeQuery(String sql,Object... obj) throws SQLException{
	Connection conn=createConnection();
	
	return executeQuery(conn,sql,obj);
    }
    
    /**
     * �Զ���رյĹ���
     */
    public static void close(AutoCloseable... obj){
  	for(int i=0;i<obj.length;i++){
  	    if(obj[i]!=null){
  		try {
  		    obj[i].close();
  		} catch (Exception e) {
  		    System.out.println("�ر���ʧ��");
  		}
  	    }
  	    
  	}
      }
    
    /**
     * �Զ���һ���ر����еķ���
     */
    
    public static void closeAll(ResultSet rs){
	 Connection conn=null;
	 Statement st=null;
	try {
	    st=rs.getStatement();
	    conn=rs.getStatement().getConnection();
	} catch (SQLException e) {
	   System.out.println("������ݿ�ʧ��"+e.getMessage());
	}finally{
	    close(rs,st,conn);
	}
    }
    
    /**
     * �Զ���һ���ع�����
     */
    public static void rollBack(Connection conn){
	
	try {
	    conn.rollback();
	} catch (SQLException e) {
	   System.out.println("�ع�ʧ��");
	}
    }
}
