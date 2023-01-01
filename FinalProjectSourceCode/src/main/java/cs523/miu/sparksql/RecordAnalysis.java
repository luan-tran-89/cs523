package cs523.miu.sparksql;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs523.miu.hive.HiveRepository;
import cs523.miu.model.FavouriteEVModelRecord;
import cs523.miu.model.VehicleRecord;
import cs523.miu.model.VehicleTypeReport;
import cs523.miu.model.VehiclesEachYearRecord;


public class RecordAnalysis {
	private static final Logger LOGGER = LoggerFactory.getLogger(RecordAnalysis.class);
	
	private static SparkSession session;
	
	public static void init() throws IOException, AnalysisException {
		final SparkConf sparkConf = new SparkConf();

        sparkConf.setMaster("local");
        sparkConf.set("hive.metastore.uris", "thrift://localhost:9083");
        session = SparkSession
        		.builder()
        		.appName("Spark SQL-Hive")
        		.config(sparkConf)
        		.enableHiveSupport()
        		.getOrCreate();
	}
	
	public static List<VehicleRecord> getData() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT vin, county, city, state, post_code AS postCode, year, make, model, ev_type as evType, cafv_eligibility as cafvEligibility")
			.append(" FROM ").append(HiveRepository.TABLE_NAME)
			.append(" LIMIT 10");
		
		Dataset<Row> sqlDF = session.sql(sql.toString());
		
		List<VehicleRecord> list = sqlDF.as(Encoders.bean(VehicleRecord.class)).collectAsList();
		
		sqlDF.show();
		return list;
	}
	
	public static List<VehiclesEachYearRecord> getNumberOfVehiclesEachYear() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT year, COUNT(1) AS count ")
			.append(" FROM ").append(HiveRepository.TABLE_NAME)
			.append(" GROUP BY year ")
			.append(" ORDER BY year DESC ");
		
		session.sql("DROP TABLE IF EXISTS vehicle_count_analysis ");
		Dataset<Row> sqlDF = session.sql(sql.toString());
		sqlDF.write().saveAsTable("vehicle_count_analysis");
		sqlDF.show();
		
		List<VehiclesEachYearRecord> list = sqlDF.as(Encoders.bean(VehiclesEachYearRecord.class)).collectAsList();		
		return list;
	}
	
	public static List<VehicleTypeReport> getVehicleTypeGroup() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ev_type AS evType, COUNT(1) AS count ")
			.append(" FROM ").append(HiveRepository.TABLE_NAME)
			.append(" GROUP BY ev_type");
		
		session.sql("DROP TABLE IF EXISTS vehicle_type_analysis ");
		Dataset<Row> sqlDF = session.sql(sql.toString());
		sqlDF.write().saveAsTable("vehicle_type_analysis");
		sqlDF.show();

		List<VehicleTypeReport> list = sqlDF.as(Encoders.bean(VehicleTypeReport.class)).collectAsList();
		return list;
	}
	
	public static List<FavouriteEVModelRecord> getEVModelEachYear() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT make, model, ev_type AS evType, COUNT(1) AS count ")
	        .append(" FROM ")
	        .append(HiveRepository.TABLE_NAME)
	        .append(" GROUP BY make, model, ev_type ");
		
		session.sql("DROP TABLE IF EXISTS vehicle_model_analysis ");
		Dataset<Row> sqlDF = session.sql(sql.toString());
		sqlDF.write().saveAsTable("vehicle_model_analysis");
		sqlDF.show();
		
		List<FavouriteEVModelRecord> list = sqlDF.as(Encoders.bean(FavouriteEVModelRecord.class)).collectAsList();
		return list;
	}
	
	private static void printMenu() {
		System.out.println("================= Welcome to Electric Vehicle Application  =====================");
		System.out.println("Please select program:");
		System.out.println("1. Calculate the growth of EV in each year ");
		System.out.println("2. Favourite EV model each year and its number");
		System.out.println("3. How many Plug-in Hybrid Electric Vehicles (PHEV), vs Battery Electric Vehicle (BEV)");
		System.out.println("Type 'exit' to stop program.");
	}
	
	
	public static void main(String[] args) throws IOException, AnalysisException {
		BasicConfigurator.configure();
		
		try (Scanner scanner = new Scanner(System.in)) {
			init();
			
			while (true) {
				printMenu();
				String option = scanner.nextLine();

				switch (option) {
					case "1":
						getNumberOfVehiclesEachYear();
						break;
					case "2":
						getEVModelEachYear();
						break;
					case "3":
						getVehicleTypeGroup();
						break;
					case "exit":
						System.exit(1);
					default:
				}
			}
			
		} catch (IOException e) {
			LOGGER.info("================== PROCESSING TO EXIT APPLICATION ... ====================");
			LOGGER.error("An error occur while running Electric Vehicle Application. " + e);
			System.exit(0);
		}
	}
}
