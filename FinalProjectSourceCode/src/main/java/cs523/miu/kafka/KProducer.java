package cs523.miu.kafka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs523.miu.config.KafkaConfig;

public class KProducer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KProducer.class);
	
	private static final String DEFAULT_INPUT = "input/input.csv";

	public static void main(String[] args) {
		String input = args.length > 0 ? args[0] : null;
		if (input == null) {
			input = DEFAULT_INPUT;
		}
		
		publishMessages(input);
	}
	
	public static void publishMessages(String input) {
		BasicConfigurator.configure();
		try {
			List<String> lines = Files.lines(Paths.get(input)).skip(1).collect(Collectors.toList());
			publish(lines);
		} catch (IOException e) {
			System.out.println(e);
			LOGGER.error("Cannot read file. " + e);
		}
	}
	
	public static void publish(List<String> data) throws IOException {
		try (Producer<Long, String> producer = KafkaConfig.createProducer()) {
			data.stream()
			.map(d -> new ProducerRecord<Long, String>(KafkaConfig.TOPIC_NAME, d))
			.forEach(record -> {
				try {
					producer.send(record).get();
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error("Cannot send record. " + e);
				}
			});
		}
	}
}
