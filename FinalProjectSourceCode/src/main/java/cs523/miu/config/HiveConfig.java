package cs523.miu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HiveConfig.class);
	
	private static final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
//	private static final String HOST_HIVE = System.getenv("HOST_HIVE");
//	private static final String PORT_HIVE = System.getenv("PORT_HIVE");
	private static final String HOST_HIVE = "localhost";
	private static final String PORT_HIVE = "10000";
	
	private static final String LOGIN = System.getenv("LOGIN");
	private static final String PASSWORD = System.getenv("PASSWORD");
	
	private static Connection connection;
	
	public static Connection getHiveConnection() {
		if (connection == null) {
			try {
				// Set JDBC Hive Driver
				Class.forName(JDBC_DRIVER_NAME);
				
				// jdbc:hive2://<hiveserver>:10000/;ssl=false
				StringBuilder config = new StringBuilder();
				config.append("jdbc:hive2://")
					.append(HOST_HIVE)
					.append(":")
					.append(PORT_HIVE)
					.append("/;ssl=false");
				
				// Connect to Hive
				// Choose a user that has the rights to write into /user/hive/warehouse/
				// (e.g. hdfs)
				connection = DriverManager.getConnection(config.toString(),"hdfs","");
//				connection = DriverManager.getConnection(config.toString(), LOGIN, PASSWORD);
			} catch (Exception e) {
				LOGGER.error("Cannot create Hive connection. " + e);
				System.exit(0);
			}
		}
		
		return connection;
	}
	
	public static Statement createStatement() {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			LOGGER.error("Cannot create Hive connection. " + e);
			System.exit(0);
		}
		return null;
	}
	
}
