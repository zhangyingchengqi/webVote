package com.hcq.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


import com.hcq.dao.MyProperties;

public class DBUtil {
	private static MyProperties mp=MyProperties.getInstance();
	
	static {
		try{
			Class.forName(mp.getProperty("driverName"));
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}
	// 数据库连接
	public static Connection getConn() throws SQLException {
		Connection con = null;
		try{
			Context context= new InitialContext();
			DataSource source =(DataSource)context.lookup(mp.getProperty("jndiName"));
			con =source.getConnection();
		}catch(Exception e){
			//如果web容器（tomcat）连接池没有配置，则使用传统jdbc来操作
			try{
			con=DriverManager.getConnection(mp.getProperty("url"),
					mp.getProperty("username"),mp.getProperty("password"));
			}catch(SQLException e1){
				LogUtil.logger.error("could not connect the server",e1);
				throw e1;
			}
		}
		return con;
	}

	// 数据库关闭
	public static void close(Connection con, Statement st, ResultSet rs) throws SQLException {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LogUtil.logger.error("could not close the resultset",e);
				throw e;
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				LogUtil.logger.error("could not close the st",e);
				throw e;
			}
		}

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.logger.error("could not close the connection",e);
				throw e;
			}
		}
	}
	/**
	 * 执行insert, update, delete的sql语句
	 * 
	 * @param sql
	 *            :要执行的sql语句
	 * @param params
	 *            : sql语句中占位符对应的值
	 * @return 执行sql语句受影响的行数
	 * @throws SQLException 
	 */
	public static int doUpdate(String sql, List<Object> params) throws SQLException {
		int result=-1;
		Connection con = null;
		PreparedStatement ps = null; // 预处理sql语句执行工具
		try {

			con = getConn();
			ps = con.prepareStatement(sql); // 预处理sql语句
			setParams(ps, params);
			result= ps.executeUpdate();
		} catch (SQLException e1) {
			LogUtil.logger.error("could not update",e1);
			throw e1;
		} finally {
			DBUtil.close(con, ps, null);
		}
		return result;
	}

	/**
	 * 执行带事务的insert, update, delete的sql语句
	 * 
	 * @param sqls
	 *            :要执行的事务sql语句集合
	 * @param params
	 *            : sql语句中占位符对应的值
	 * @return 执行sql语句受影响的行数
	 * @throws SQLException 
	 */
	public static int doUpdate(List<String> sqls, List<List<Object>> params) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null; // 预处理sql语句执行工具
		int rows = -1;
		try {
			con = getConn();
			con.setAutoCommit(false);
			for (int i = 0; i < sqls.size(); i++) {
				ps = con.prepareStatement(sqls.get(i)); // 预处理sql语句
				setParams(ps, params.get(i));
				rows += ps.executeUpdate();
			}
				con.commit();
		} catch (SQLException e) {
				con.rollback();
				rows = 0;
				LogUtil.logger.error("could not update more than one statement",e);
				throw e;
		} finally {
			DBUtil.close(con, ps, null);
		}
		return rows;
	}

	/**
	 * 针对聚合函数和大数据的查询
	 * @param sql
	 *            : 要执行的sql语句
	 * @param params
	 *            : sql语句中占位符对应的值
	 * @return : 一条数据
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public Map<String, Object> doQueryOne(String sql, List<Object> params) throws SQLException, IOException {
		Connection con = null;
		PreparedStatement ps = null; // 预处理sql语句执行工具
		ResultSet rs = null;
		Map<String, Object> results = null;
		try {
			con = getConn();
			ps = con.prepareStatement(sql); // 预处理sql语句
			setParams(ps, params);
			rs = ps.executeQuery();
			if (rs.next()) {
				results = new HashMap<String, Object>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if ("BLOB".equals(rsmd.getColumnTypeName(i)))
					{
						Blob b = rs.getBlob(i);
						BufferedInputStream bin = new BufferedInputStream(rs.getBinaryStream(i));
						if(b!=null){
						byte[] bs = new byte[(int) b.length()];
						bin.read(bs);
						results.put(rsmd.getColumnName(i).toLowerCase(),bs);
					}else{
					results.put(rsmd.getColumnName(i).toLowerCase(),null);
					}
				}else{
					results.put(rsmd.getColumnName(i).toLowerCase(),rs.getObject(i));
					}
				}
			}
		} catch (SQLException e1) {
			LogUtil.logger.error("could not update more than one statment",e1);
			throw e1;
		}finally {
			DBUtil.close(con, ps, rs);
		}
		return results;
	}

	/**
	 * 针对多条数据的查询
	 * 
	 * @param sql
	 *            : 要执行的sql语句
	 * @param params
	 *            : sql语句中占位符对应的值
	 * @return : 多条数据
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static List<Map<String, Object>> doQueryList(String sql, List<Object> params) throws SQLException, IOException {
		Connection con = null;
		PreparedStatement ps = null; // 预处理sql语句执行工具
		ResultSet rs = null;
		List<Map<String, Object>> results = null;
		try {
			con = getConn();
			ps = con.prepareStatement(sql); // 预处理sql语句
			setParams(ps, params);
			rs = ps.executeQuery();
			if (rs.next()) {
				results = new ArrayList<Map<String, Object>>();
				ResultSetMetaData rsmd = rs.getMetaData();
				do {
					Map<String, Object> result = new HashMap<String, Object>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						if ("BLOB".equals(rsmd.getColumnTypeName(i)))
						{
							Blob b = rs.getBlob(i);
							BufferedInputStream bin = new BufferedInputStream(b.getBinaryStream());
							byte[] bs = new byte[(int) b.length()];
							bin.read(bs);
							result.put(rsmd.getColumnName(i).toLowerCase(),bs);
						}
						else
						{
							result.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
						}
					}
					results.add(result);

				} while (rs.next());
			}
		} catch (SQLException e1) {
			LogUtil.logger.error("could not query",e1);
		} finally {
			DBUtil.close(con, ps, rs);
		}
		return results;
	}

	/**
	 * 给预处理sql语句中占位符赋值
	 * 
	 * @param ps
	 *            预处理sql语句执行工具
	 * @param params
	 *            预处理sql语句中占位符的值
	 */
	private static void setParams(PreparedStatement ps, List<Object> params) {
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				Object obj = params.get(i);
				try {
					if (obj instanceof Integer) {
						ps.setInt(i + 1, Integer.parseInt(String.valueOf(obj)));
					} else if (obj instanceof Double) {
						ps.setDouble(i + 1, Double.parseDouble(String.valueOf(obj)));
					} else if (obj instanceof String) {
						ps.setString(i + 1, (String) obj);
					} else if (obj instanceof Date) {
						ps.setTimestamp(i + 1, new Timestamp(((Date) obj).getTime()));
					} else if (obj instanceof InputStream) {
						ps.setBlob(i + 1, (InputStream)obj);
					}else {
						ps.setObject(i + 1, obj);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
	 * 基于对象的查询
	 * @param sql
	 * @parmas parmas
	 * @return
	 */
	public static <T>List<T>find(String sql,List<Object>params,Class<T> c) throws Exception{
		List<T>list =new ArrayList<T>();
		Connection con =null;
		PreparedStatement ps= null;
		ResultSet rs=null;
		try{
			con =getConn();
			ps =con.prepareStatement(sql);
			setParams(ps,params);
			rs =ps.executeQuery();
			//获取元数据
			ResultSetMetaData rsmd =rs.getMetaData();
			
			int length =rsmd.getColumnCount();  //获取列的数量
			
			//取出所有列名存放在一个数组中
			String[] colNames =new String[length];
			
			//循环取出所有的列
			for(int i=0;i<length;i++){
				colNames[i]=rsmd.getColumnName(i+1);
			}
			//获取给定对象的所有方法
			Method[] methods=c.getMethods();
			T t;
			String cname;
			String colName;
			String methodName=null;
			
			while(rs.next()){
				t=c.newInstance(); //根据类信息，实例化一个对象， new Dept（）;
				for(int i=0;i<length;i++){
					cname =colNames[i];
					colName="set"+cname;
					if(methods!=null &&methods.length>0){
						for(Method m:methods){//循环所有方法
							methodName=m.getName();
							if(colName.equalsIgnoreCase(methodName)&&rs.getObject(cname)!=null){
								
								//修改：1，取m这个方法（setXX（））的参数的类型 
									 //2，因为setXXX是java方法，它的数据类型是固定
								String parameterTypeName=m.getParameterTypes()[0].getName(); 
								if("int".equals(parameterTypeName)||"java.lang.Integer".equals(parameterTypeName)){
									m.invoke(t,rs.getInt(cname));
								}else if("float".equals(parameterTypeName)||"java.lang.Float".equals(parameterTypeName)){
									m.invoke(t,rs.getFloat(cname));
								}else if("double".equals(parameterTypeName)||"java.lang.Double".equals(parameterTypeName)){
									m.invoke(t,rs.getDouble(cname));
								}else if("String".equals(parameterTypeName)||"java.lang.String".equals(parameterTypeName)){
									m.invoke(t,rs.getString(cname));
								}else if ("long".equals(parameterTypeName)||"java.lang.Long".equals(parameterTypeName)){
									m.invoke(t,rs.getLong(cname));
								}else{
									m.invoke(t, rs.getObject(cname));
								}
								break;
							}
						}
					}
				}
				list.add(t);
			}
		}catch(Exception e){
			LogUtil.logger.error("could not query",e);
			throw e;
		}finally{
			DBUtil.close(con, ps, rs);
		}
		return list;
	}
}