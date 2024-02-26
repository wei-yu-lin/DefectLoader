package wistron.defectEdx.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbUtils {
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String classforName;

	public DbUtils(int sqlVersion, String connectionURL) throws ClassNotFoundException, SQLException {
		this(sqlVersion);
		connectionDataBase(connectionURL);
	}

	public DbUtils(int sqlVersion) {
		/*
		 * sql version 1 => sql sevice sql version 2 => oracle
		 */
		switch (sqlVersion) {
		case 1:
			classforName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			break;
		case 2:
			classforName = "oracle.jdbc.driver.OracleDriver";
			break;
		}
	}

	public void connectionDataBase(String connectionUrl) throws ClassNotFoundException, SQLException {
		Class.forName(classforName);
		con = DriverManager.getConnection(connectionUrl);
		con.setAutoCommit(true);
		stmt = con.createStatement();

	}

	public void addBatch(String SQL) {
		try {
			stmt.addBatch(SQL);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void executeBatch() {
		try {
			stmt.executeBatch();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void update(String SQL) {
		try {
			stmt.execute(SQL);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
	}

	public ResultSet query(String SQL) {
		// Declare the JDBC objects.
		try {
			// Create and execute an SQL statement that returns some data.
			rs = stmt.executeQuery(SQL);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
		return rs;
	}

	public int queryInt(String SQL) {
		// Declare the JDBC objects.
		int out = 0;
		try {
			// Create and execute an SQL statement that returns some data.
			rs = stmt.executeQuery(SQL);
			rs.next();
			out = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
		return out;
	}

	public String queryString(String SQL) {
		// Declare the JDBC objects.
		String out = "";
		try {
			// Create and execute an SQL statement that returns some data.
			rs = stmt.executeQuery(SQL);
			rs.next();
			out = rs.getString(1);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
		return out;
	}

	public void closeConnection() {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
				log.error("DB resultSet close failed");
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (Exception e) {
				log.error("DB statement close failed");
			}
		if (con != null)
			try {
				con.close();
			} catch (Exception e) {
				log.error("DB connection close failed");
			}
	}

	public Connection getConnection() {
		return con;
	}
	
	public static String getDbLinkName(String jdbcUrl, String dbUsername, String dbPassword) {
		if (isWithPoint(dbUsername)) {
			dbUsername = changeToSpecialString(dbUsername);
		}
		if (isWithPoint(dbPassword)) {
			dbPassword = changeToSpecialString(dbPassword);
		}		
		return jdbcUrl.substring(0, jdbcUrl.indexOf('@')) + dbUsername + '/' + dbPassword
				+ jdbcUrl.substring(jdbcUrl.indexOf('@'));
	}
	
	private static boolean isWithPoint(String word) {
		return word.contains(".");
	}
	
	private static String changeToSpecialString(String word) {
		return String.format("\"%s\"", word);
	}	

}
