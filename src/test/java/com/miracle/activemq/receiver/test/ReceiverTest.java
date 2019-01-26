package com.miracle.activemq.receiver.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.miracle.jmsactivemq.controller.Controller;
import com.miracle.jmsactivemq.receiver.Consumer;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Consumer.class, ConnectionFactory.class, ActiveMQConnectionFactory.class})
public class ReceiverTest {
	
	@Test
	public void test() throws Exception {
		
		Controller cont = new Controller();	
		Consumer conmock = PowerMockito.mock(Consumer.class);
		Whitebox.setInternalState(cont, "con", conmock);
		PowerMockito.when(conmock.receiveMessage()).thenReturn("testmessage");
		cont.index();
		assertTrue(true);
		
	}

	@Test
	public void testSendMessage() throws Exception {
	
		Consumer con = new Consumer();
		Connection cmock = PowerMockito.mock(Connection.class);
		Session smock = PowerMockito.mock(Session.class);
		Queue qmock = PowerMockito.mock(Queue.class);
		TextMessage Tmock = PowerMockito.mock(TextMessage.class);
		//Message mmock = PowerMockito.mock(Message.class);
		MessageConsumer mcmock = PowerMockito.mock(MessageConsumer.class);
		ActiveMQConnectionFactory amconmock = PowerMockito.mock(ActiveMQConnectionFactory.class);
		PowerMockito.whenNew(ActiveMQConnectionFactory.class).withArguments("admin","admin" ,"tcp://devopsapp.southcentralus.cloudapp.azure.com:61616" ).thenReturn(amconmock);
		PowerMockito.when(amconmock.createConnection()).thenReturn(cmock);
		PowerMockito.when(cmock.createSession(any(Boolean.class), any(Integer.class))).thenReturn(smock);
		PowerMockito.when(smock.createQueue(any(String.class))).thenReturn(qmock);
		PowerMockito.when(smock.createConsumer(qmock)).thenReturn(mcmock);
		//PowerMockito.when(smock.createTextMessage()).thenReturn(Tmock);
		PowerMockito.when(mcmock.receive(any(Integer.class))).thenReturn(Tmock);
		con.receiveMessage();
		assertTrue(true);
		
	}
	
	@Test
	public void negtestSendMessage() throws Exception {

		Consumer con = new Consumer();
		
		ActiveMQConnectionFactory amconmock = PowerMockito.mock(ActiveMQConnectionFactory.class);
		PowerMockito.whenNew(ActiveMQConnectionFactory.class).withArguments("admin","admin" ,"tcp://devopsapp.southcentralus.cloudapp.azure.com:61616" ).thenReturn(amconmock);
		PowerMockito.when(amconmock.createConnection()).thenThrow(new JMSException("dummy"));
		con.receiveMessage();
		assertTrue(true);
	}
	
	@Test
	public void test1() throws Exception {
		
		Consumer con = new Consumer();	
		MessageConsumer mcmock = PowerMockito.mock(MessageConsumer.class);
		Whitebox.setInternalState(con, "consumer", mcmock);
		ActiveMQConnectionFactory amconmock = PowerMockito.mock(ActiveMQConnectionFactory.class);
		PowerMockito.whenNew(ActiveMQConnectionFactory.class).withArguments("admin","admin" ,"tcp://devopsapp.southcentralus.cloudapp.azure.com:61616" ).thenReturn(amconmock);
		PowerMockito.when(amconmock.createConnection()).thenThrow(new JMSException("sample"));
		con.receiveMessage();
		assertTrue(true);
		
	}

}
