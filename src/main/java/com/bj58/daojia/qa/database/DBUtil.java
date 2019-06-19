package com.bj58.daojia.qa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.testng.Assert;


/**
 * DB Class
 * 
 * @author nihuaiqing
 */
public class DBUtil {
	public static QueryRunner queryRunner;

	private Connection conn;

	private DBConnection dbct;

	private DBType type;

	static{
		queryRunner = new QueryRunner();
	}
	
	public DBUtil() {
		String key =System.getProperty("DB.instance");
		Assert.assertTrue(key!=null&&!key.isEmpty(), String.format("无法获取数据库连接信息，请在配置文件中配置%s值", "DB.instance"));
		try {
			type = DBType.valueOf(System.getProperty("DB." + key + ".type",
					"MYSQL").toUpperCase());
		} catch (Exception e) {
			type = DBType.MYSQL;
		}
		dbct = new DBConnection(type,
				System.getProperty("DB." + key + ".host"),
				System.getProperty("DB." + key + ".username"),
				System.getProperty("DB." + key + ".password"),
				System.getProperty("DB." + key + ".database"),
				System.getProperty("DB." + key + ".port", ""));
	}
	
	public DBUtil(String key) {
		try {
			type = DBType.valueOf(System.getProperty("DB." + key + ".type",
					"MYSQL").toUpperCase());
		} catch (Exception e) {
			type = DBType.MYSQL;
		}
		dbct = new DBConnection(type,
				System.getProperty("DB." + key + ".host"),
				System.getProperty("DB." + key + ".username"),
				System.getProperty("DB." + key + ".password"),
				System.getProperty("DB." + key + ".database"),
				System.getProperty("DB." + key + ".port", ""));
	}


	/**
	 * 数据库连接
	 * @return 
	 */
	public void connection() {
		try {
			Class.forName(dbct.getJDBCDriver()).newInstance();
			conn = DriverManager.getConnection(dbct.getJDBCString(),
					dbct.getDbusername(), dbct.getDbpasswd());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭Connection
	 * @param conn
	 */
	public void closeConnection() {
		try {
			if (conn != null && conn.isValid(0)) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询一行多列记录，将结果封装成一个Map，key为字段名，value为字段值
	 * @param sql
	 * @param params
	 * @return
	 */
	public  Map<String, Object> queryForMap(String sql, Object... params) {
		Map<String, Object> result = null;
		try {
			result = queryRunner.query(conn, sql, new MapHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询多行多列记录，将结果列表中的每一行封装成一个Map，key为字段名，value为字段值
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryForMapList(String sql, Object... params) {
		List<Map<String, Object>> result = null;
		try {
			result = queryRunner.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询多行一列数据
	 * @param <T>
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> queryForObjectList(String sql, Class<T> c, Object... params) {
		List<T> result = null;
		try {
			result = queryRunner.query(conn, sql, new ColumnListHandler<T>(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询单个值
	 * @param <T>
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> T queryForObject(String sql, Class<T> c, Object... params) {
		T result = null;
		try {
			result = queryRunner.query(conn, sql, new ScalarHandler<T>(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 插入
	 * @param sql
	 * @param params
	 * @return
	 */
	public void insert(String sql, Object... params) {
		try {
			queryRunner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入(返回主键)
	 * @param sql
	 * @param params
	 * @return
	 */
	public Object insertReturnPK(String sql, Object... params) {
		try {
			return queryRunner.insert(conn, sql, new ScalarHandler<Object>(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 批量插入
	 * @param sql
	 * @param params
	 * @return
	 */
	public Object insertBatch(String sql, Object[][] params) {
		try {
			return queryRunner.batch(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 更新
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object... params) {
		try {
			return queryRunner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return 0;
	}

	/**
	 * 批量更新
	 * @param sql
	 * @param params
	 * @return
	 */
	public void updateBatch(String sql, Object[][] params) {
		try {
			queryRunner.batch(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 关闭PreparedStatement
	 * @param conn
	 */
	public void closePreparedStatement(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭Connection 和 Statement
	 * @param conn
	 * @param stmt
	 */
	public  void close(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PreparedStatement prepareStatement(String sql) {
		try {
			return conn.prepareStatement(sql);
		} catch (SQLException e) {
			return null;
		}
	}

	private  ThreadLocal<PreparedStatement> threadLocal = null;
	private  ThreadLocal<Short> threadLocal_cnt = null;

	/**
	 * 批量操作准备
	 * @param sql
	 */
	public void batchPrepare(final String sql) {
		if (threadLocal == null) {
			threadLocal = new ThreadLocal<PreparedStatement>() {
				@Override
				protected PreparedStatement initialValue() {
					try {
						return conn != null ? conn.prepareStatement(sql) : null;
					} catch (SQLException e) {
						return null;
					}
				}
			};
		}
	}

	/**
	 * 设置参数
	 * @param params
	 */
	public void batchAdd(Object... params) {
		try {
			if (threadLocal != null) {
				PreparedStatement stmt = threadLocal.get();
				for (int i = 0; i < params.length; i++) {
					if (params[i] != null) {
						stmt.setObject(i + 1, params[i]);
					} else {
						stmt.setNull(i + 1, Types.VARCHAR);
					}
				}
				stmt.addBatch();
				
				Short cnt = threadLocal_cnt.get();
				threadLocal_cnt.set(++cnt);
				if(cnt % 100 == 0){
					stmt.executeBatch();
					stmt.clearBatch();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量执行并且关闭连接
	 */
	public void batchExecuteAndClose() {
		try {
			if (threadLocal != null) {
				PreparedStatement stmt = threadLocal.get();
				stmt.executeBatch();
				stmt.clearBatch();
				stmt.closeOnCompletion();
				stmt = null;
				threadLocal.remove();
				threadLocal = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
