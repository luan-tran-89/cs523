package cs523.miu.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkConfig {

	public static final String APP_NAME = "ProjectSpark";
	public static final String MASTER_LOCAL = "local[*]";

	private static JavaSparkContext sparkContext;
	
	public static JavaSparkContext getSparkContext() {
		if (sparkContext == null) {
			SparkConf sparkConf = new SparkConf()
					.setAppName(APP_NAME)
					.setMaster(MASTER_LOCAL);
			
			sparkContext = new JavaSparkContext(sparkConf);
		}
		
		return sparkContext;
	}
	
}
