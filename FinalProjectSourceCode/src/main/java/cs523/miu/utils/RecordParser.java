package cs523.miu.utils;

import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs523.miu.model.VehicleRecord;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import scala.Tuple2;

public class RecordParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(RecordParser.class);
	
	private static final CSVParser PARSER = new CSVParserBuilder().withSeparator(',').withQuoteChar('"').build();

	
	public static VehicleRecord parse(Tuple2<String, String> tuple2) {
		return parse(tuple2._2());
	}
	
	public static VehicleRecord parse(String line) {
		try {
			String[] fields = PARSER.parseLine(line);
			
			if (fields.length < 10) {
				return null;				
			}

			String vin = fields[0];
			String county = fields[1];
			String city = fields[2];
			String state = fields[3];
			String postCode = fields[4];
			String year = fields[5];
			String make = fields[6];
			String model = fields[7];
			String evType = fields[8];
			String cafvEligibility = fields[9];

			return new VehicleRecord(vin, county, city, state, postCode, year, make, model, evType, cafvEligibility);
		} catch (Exception e) {
			LOGGER.warn("Cannot parse record. [" + line + "] " + e);
			return null;
		}
	}
	
	public static VehicleRecord parse(Row row) {
		String vin = row.getString(0);
		String county = row.getString(1);
		String city = row.getString(2);
		String state = row.getString(3);
		String postCode = row.getString(4);
		String year = row.getString(5);
		String make = row.getString(6);
		String model = row.getString(7);
		String evType = row.getString(8);
		String cafvEligibility = row.getString(9);
		return new VehicleRecord(vin, county, city, state, postCode, year, make, model, evType, cafvEligibility);
	}
}
