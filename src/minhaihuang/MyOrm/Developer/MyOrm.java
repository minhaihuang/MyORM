package minhaihuang.MyOrm.Developer;

import minhaihuang.jdbc.Util.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 要求：自己写一个ORM，让用户能够往数据库中插入数据。
 * 
 * 对调用者的要求：
 * 1，首先在数据库中建立一个表，表明与类名一致
 * 2，必须将id设为主键，并且自动递增，必须是int类型
 * 3，列名与字段名（以前一直以为是属性）要一致
 * @author 黄帅哥
 *
 */
public class MyOrm {

	/**
	 * 插入数据的方法，什么数据都能够插入
	 * @param obj
	 * @throws SQLException 
	 */
	public static void insert(Object obj) throws SQLException{
		//拼接字符串：insert into 表名（列名） values(参数)；
		
		//利用StringBuilder来拼接字符串
		StringBuilder sbSql=new StringBuilder();
		sbSql.append("insert into ");
		
		//利用反射获取类名(表名)；
		Class clazz=obj.getClass();
		String chartName=clazz.getSimpleName();
		//加入表名
		sbSql.append(chartName);
		
		//利用内省来获取类的字段，要去掉类的关键字class,以及id。,用一个List容器来存储
		List<String> list=new ArrayList<String>();
		try {
			
			BeanInfo beanInfo=Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propDecs=beanInfo.getPropertyDescriptors();
			//遍历获取到的数组，往list中加入字段，除开id以及class关键字
			for(PropertyDescriptor prop:propDecs){
				
				String fieldName=prop.getName();
				if(!fieldName.equals("id")&&!fieldName.equals("class")){
					list.add(fieldName);
				}
			}	
			//再次拼接
			sbSql.append(list.toString().
					replace("[", "(").replace("]", ")"));
			
			sbSql.append(" values");
	
			int len=list.size();
			String[] questionMark=new String[len];
			for(int i=0;i<len;i++){
				questionMark[i]="?";
			}
			
			sbSql.append(Arrays.toString(questionMark).replace("[", "(").replace("]", ")"));
			System.out.println(sbSql);
			
			List<Object> values=new ArrayList<Object>();
			Object value=null;
			//拼接完成后，传入值
			for(int i=0;i<len;i++){
				String fieldName=list.get(i);
				PropertyDescriptor shuXing=getValue(fieldName,propDecs);
				value=shuXing.getReadMethod().invoke(obj);
				values.add(value);
			}
			
			System.out.println(values.toString());
			
			String s=sbSql.toString();
			JdbcUtils.executeUpdate(s, values.toArray());
		} catch (IntrospectionException e) {
			System.out.println("获取java内省失败"+e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 提供删除数据的方法，指定表和id值，按照这两者来删除
	 * @param clazz
	 * @param id
	 */
	public static void delete(Class clazz,int id){
		 //拼接Sql语句
		StringBuilder sql=new StringBuilder();
		String chartName=clazz.getSimpleName();
		sql.append("delete from ").append(chartName).append(" where id=").append(id);
		
		try {
			System.out.println(sql.toString());
			JdbcUtils.executeUpdate(sql.toString());
		} catch (SQLException e) {
			System.out.println("操作数据库失败"+e.getMessage());
		}
	}
	

	/**
	 * 初始化数据的方法。根据指定的类和id，在表中获得对应id行的数据，然后根据列的信息分别初始化一个对象的字段
	 * 例如：表名为Person，id为1，表中的数据age=21,name="hhm"
	 * 在执行完Person p1=(Person) initObject(Person.class,1),后
	 * 当调用p1.getAge()时，获得21，调用p1.getName()时，获得hhm
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static Object initObject(Class clazz,int id){
		//拼接sql语句
		StringBuilder sql=new StringBuilder();
		
		Object obj=null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e1) {
			
		} catch (IllegalAccessException e1) {
	
		}
		//获取表名
		String chartName=clazz.getSimpleName();
		sql.append("select * from ").append(chartName).append(" where id=").append(id);
		ResultSet result=null;
		try {
			//获取返回的结果
			 result=JdbcUtils.executeQuery(sql.toString());
			
			//注意，一定要将result指向下一条数据，因为当前什么都没有，而我们已获得的数据就在下一行
			if(result.next()){//如果数据存在才执行初始化的语句
			
			//利用内省来初始化数据
			BeanInfo beanInfo=Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propArray=beanInfo.getPropertyDescriptors();
			
			for(PropertyDescriptor prop:propArray){
				String fieldName=prop.getName();
				
				//忽略掉class这个字段
				if(!fieldName.equals("class")){
					prop.getWriteMethod().invoke(obj, result.getObject(fieldName));
				}
			}
			}		
		} catch (SQLException e) {
			System.out.println("操作数据库失败"+e.getMessage());
		} catch (IntrospectionException e) {
			System.out.println("获取内省失败"+e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JdbcUtils.closeAll(result);
		}
		
		return obj;
	}
	//获得传入的值，?对应的值
	static PropertyDescriptor getValue(String fieldName,
			PropertyDescriptor[] propDecs){
		
		for(PropertyDescriptor proDes:propDecs){
			
			if(proDes.getName().equals(fieldName)){
				
				return proDes;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static int UpDate(Object obj){
		//拼接sql语句
		StringBuilder sql=new StringBuilder();
		sql.append("Update ").append(obj.getClass().getSimpleName()).append(" set ");
		
		//获取obj的各个字段，只提取age和name
		try {
			BeanInfo beanInfo=Introspector.getBeanInfo(obj.getClass());
			//获取属性集合
			PropertyDescriptor[] propArray=beanInfo.getPropertyDescriptors();
		
			//创建一个容器，用来存储列名，同时在列名后加上？例 age=?
			List<String> list=new ArrayList<String>();
			//再次创建一个数组，用来存储传入的值
			List<Object> values=new ArrayList<Object>();
			//遍历获取字段（列名）
			int id=0;
			for(PropertyDescriptor prop:propArray){
				String fieldName=prop.getName();
				if(!fieldName.equals("class")&&!fieldName.equals("id")){
					//往容器中加入列名和?
					list.add(fieldName+"=?");
					//往值容器中加入值
					values.add(prop.getReadMethod().invoke(obj));
				}
				
				if(fieldName.equals("id")){
					id=(Integer) prop.getReadMethod().invoke(obj);
				}
			}
			//往values容器中加入id的值
			values.add(id);
			
			//再次拼接sql语句
			sql.append(list.toString().replace("[", "").replace("]", ""));
			//要求获取对象的id，根据id来更新数据
			sql.append(" where id=?");
			
			System.out.println(values.toString());
			//传入数据，执行语句
			JdbcUtils.executeUpdate(sql.toString(), values.toArray());
			
		} catch (IntrospectionException e) {
			System.out.println("加载内省失败"+e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("操作数据库失败"+e.getLocalizedMessage());
		}
		return 1;
	}
	public static void main(String[] args) throws SQLException {
//		Person p=new Person();
//		p.setName("hhm");
//		p.setAge(20);
		
		//delete(Person.class,5);
		
		Person p1=(Person) initObject(Person.class,1);
//		System.out.println(p1.getId());
//		System.out.println(p1.getAge());
//		System.out.println(p1.getName());
		p1.setAge(20);
		UpDate(p1);
		
	}
}
