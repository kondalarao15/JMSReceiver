package com.miracle.jmsactivemq.receiver;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

/**
 * @author SuRyA
 *
 */
@Component
public class Consumer{  
	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;
	private TextMessage message = null;

	
	@Metric
	static final MetricRegistry metrics = new MetricRegistry();
	private static final String GRAPHITE_HOST = "http://192.168.1.230/";
	private static final int GRAPHITE_PORT = 8080;
	
	/**
	 * 
	 */
	public Consumer() {

	}
	
	/**
	 * @return
	 * @throws JMSException
	 */
	public String receiveMessage() throws JMSException 
	{
		Timer.Context time = null;
			try {
				
				//context = MetricsReporterConfig.metricRegistry().timer(MetricRegistry.name(Consumer.class, "Sample")).time();
				Timer requests = metrics.timer("request-processed");
				time = requests.time();
				//Making sure that only one connection is made.
				if(consumer == null){
				//Creating a factory object
				factory = new ActiveMQConnectionFactory("admin","admin","tcp://devopsapp.southcentralus.cloudapp.azure.com:61616"); 
				//factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
				
				//Creating a connection object - Need to make connection to ActiveMQ
				connection = factory.createConnection();
				connection.start();
				//Creating a session object
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				//Creating a Destination Queue by name "SAMPLEQUEUE"
				destination = session.createQueue("SAMPLEQUEUE");
				consumer = session.createConsumer(destination);
				System.out.println("Connection to Queue is made");
				}
			
				message = (TextMessage) consumer.receive(1000);
				Consumer c  = new Consumer();
				c.startReport1(metrics);
			} 
			catch (JMSException e) {
				e.printStackTrace();
			}
			
			finally
			{
			
				time.stop();
			}
			
			//Checking Whether the queue is empty
			if (message == null){
				return "No new Messages found. Queue is Empty.";
			}
			else{	
				return message.getText();
			}
			
			
		}
	
	public void startReport1(MetricRegistry m) {
		Graphite graphite = (Graphite) new Graphite(new InetSocketAddress(  GRAPHITE_HOST, GRAPHITE_PORT));
	        
		 GraphiteReporter reporter =GraphiteReporter.forRegistry(m)
	                .convertRatesTo(TimeUnit.SECONDS)
	                .convertDurationsTo(TimeUnit.MILLISECONDS)
	                .build(graphite);
	        (reporter).start(1, TimeUnit.MINUTES);
	    }
  	
}


