package cs523.miu.kafka;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;

import cs523.miu.config.KafkaConfig;
import cs523.miu.hive.HiveRepository;
import cs523.miu.model.VehicleRecord;
import cs523.miu.utils.RecordParser;

public class KConsumer {
	
	public static void startConsumer() throws InterruptedException {
		try {
			HiveRepository repo = HiveRepository.getInstance();
			
			JavaStreamingContext streamingContext = new JavaStreamingContext(
					new SparkConf().setAppName("SparkStreaming").setMaster("local[*]"), new Duration(250));
			
			Map<String, Object> kafkaParams = new HashMap<>();
			kafkaParams.put("bootstrap.servers", "localhost:9092");
			kafkaParams.put("key.deserializer", StringDeserializer.class);
			kafkaParams.put("value.deserializer", StringDeserializer.class);
			kafkaParams.put("group.id", "group");
			kafkaParams.put("auto.offset.reset", "latest");
			kafkaParams.put("enable.auto.commit", false);

			Collection<String> topics = Arrays.asList(KafkaConfig.TOPIC_NAME);

			JavaInputDStream<ConsumerRecord<String, String>> stream =
			  KafkaUtils.createDirectStream(
			    streamingContext,
			    LocationStrategies.PreferConsistent(),
			    ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
			  );
			
			// Insert data into Hive from Kafka
			stream.foreachRDD(rdd -> rdd.foreach(x -> {
				VehicleRecord record = RecordParser.parse(x.value());
				if (record != null) {
					System.out.println(x.value());
					System.out.println(record);	
					repo.insertRecord(record);
				}
			}));	
			
			streamingContext.start();
			streamingContext.awaitTermination();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
