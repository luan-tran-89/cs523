package cs523.miu.sparksql;

import java.io.File;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSqlHive {

	public static void main(String[] args) {
		//configure

		SparkSession spark = SparkSession

		.builder()

		.master("local[*]")

		.appName("Java Spark Hive Example")

		.config("hive.metastore.warehouse.dir", "/user/hive/warehouse")

		.config("hive.metastore.uris", "thrift://localhost:9083")

		.config("spark.sql.warehouse.dir", "/user/hive/warehouse")

		.config("hive.exec.scratchdir",
				
		"/tmp/the-current-user-has-permission-to-write-in")

		.config("spark.yarn.security.credentials.hive.enabled", "true")

		.config("spark.sql.hive.metastore.jars", "maven")

		.config("spark.sql.hive.metastore.version", "1.2.1")

		.config("spark.sql.catalogImplementation", "hive")

		.enableHiveSupport()

		.getOrCreate();
		
		spark.sql("DROP TABLE IF EXISTS electricVehicles");
		spark.sql("DROP TABLE IF EXISTS countcar");
		spark.sql("DROP TABLE IF EXISTS vehicleType");
		// read data and create tables
		
		spark.sql("CREATE EXTERNAL TABLE electricVehicles(vin String, county String,city String, "
				+ "state String, post_code int, year int, make String, "
				+ "model String, ev_type String,cafv_eligibility String) "
				+ "LOCATION 'hdfs://localhost:8020/user/cloudera/input'"
				+ " ROW FORMAT DELIMITED " + "FIELDS TERMINATED BY ','");

		//queries
		
		Dataset<Row> carYear = spark.sql("select year, COUNT(*) as count from electricVehicles group by year");
		carYear.write().saveAsTable("countcar");
		
		Dataset<Row> vehicleType = spark.sql("select model, ev_type  from electricVehicles");
		vehicleType.write().saveAsTable("vehicleType");
		
		//chaining of dataset 
		
		Dataset<Row> modelYear = spark.sql("select model, year, COUNT(*) as count from electricVehicles group by model,year");
		modelYear.createOrReplaceTempView("modelYear");
		Dataset<Row> table = spark.sql("select *from modelYear where count>200 ");
		table.show();
		
		spark.sql("SELECT * FROM electricVehicles LIMIT 10").show();
		//close spark connection
		spark.close();

	}

}