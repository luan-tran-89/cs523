package cs523.miu;

import org.apache.log4j.BasicConfigurator;

import cs523.miu.kafka.KConsumer;


public class App {
    public static void main( String[] args ) throws InterruptedException {
        BasicConfigurator.configure();
        KConsumer.startConsumer();
    }
}
