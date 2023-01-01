package cs523.miu.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConfig {

	public static final String KAFKA_BROKERS = "localhost:9092";
	public static final Integer MESSAGE_COUNT = 1000;
	public static final String TOPIC_NAME = "vehicle-topic";
	public static final Integer MESSAGE_SIZE = 20971520;
	
	
	public static Map<String, String> generateKafkaParams() {
		Map<String, String> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", KAFKA_BROKERS);
		kafkaParams.put("fetch.message.max.bytes", String.valueOf(MESSAGE_SIZE));
//		kafkaParams.put("key.deserializer", StringDeserializer.class);
//		kafkaParams.put("value.deserializer", StringDeserializer.class);
//		kafkaParams.put("group.id", "group");
//		kafkaParams.put("auto.offset.reset", "latest");
//		kafkaParams.put("enable.auto.commit", false);
		return kafkaParams;
	}
	
	public static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, KafkaConfig.MESSAGE_SIZE);
		
		return new KafkaProducer<>(props);
	}
}
