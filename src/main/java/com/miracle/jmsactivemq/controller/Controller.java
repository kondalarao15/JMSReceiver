package com.miracle.jmsactivemq.controller;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.jmsactivemq.receiver.Consumer;

/**
 * @author SuRyA
 *
 */
@RestController
public class Controller {
	@Autowired
	Consumer con;
    
	//Takes the request from the web and makes a call to receiveMessage method of the Consumer	
    /**
     * @return
     */
    @RequestMapping("/receive")
    @ResponseBody
    public String index() {
        try {
			return "Received Message: "+con.receiveMessage();
		} catch (JMSException e) {
			e.printStackTrace();
			return "The is a error with JMS Connection";
		}
    }

}