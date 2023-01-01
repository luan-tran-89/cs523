package cs523.miu.hive;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs523.miu.config.HiveConfig;
import cs523.miu.model.VehicleRecord;

public class HiveRepository implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(HiveRepository.class);
	
	public  static final String TABLE_NAME = "electric_vehicle";
	private String DROP_TABLE_SQL = "DROP TABLE IF EXISTS %s";
	private String CREATED_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s (vin STRING, county STRING, city STRING, state STRING, post_code STRING, year STRING, make STRING, model STRING, ev_type STRING, cafv_eligibility STRING) STORED AS PARQUET";
	private String INSERT_SQL = "INSERT INTO %s VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")";
		
	private static HiveRepository INSTANCE;
	
	private HiveRepository() {
		init();
	}
	
	public static HiveRepository getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HiveRepository();			
		}
		return INSTANCE;
	}
	
	private void init() {
		try {
			Statement stmt = HiveConfig.getHiveConnection().createStatement();
			// Execute DROP TABLE Query
			stmt.execute(String.format(DROP_TABLE_SQL, TABLE_NAME));
			// Execute CREATE Query
			stmt.execute(String.format(CREATED_TABLE_SQL, TABLE_NAME));
		} catch (SQLException e) {
			LOGGER.error("Cannot create Hive connection. " + e);
			System.exit(0);
		}
	}
	
	public void insertRecord(VehicleRecord record) {
		String sql = String.format(INSERT_SQL, 
				TABLE_NAME,
				record.getVin(),
				record.getCounty(),
				record.getCity(),
				record.getState(),
				record.getPostCode(),
				record.getYear(),
				record.getMake(),
				record.getModel(),
				record.getEvType(),
				record.getCafvEligibility());
		try {
			Statement stmt = HiveConfig.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			LOGGER.error("Cannot create Hive connection. " + e);
			System.exit(0);
		}
		
	}
	
}
