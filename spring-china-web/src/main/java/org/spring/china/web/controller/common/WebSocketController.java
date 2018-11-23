package org.spring.china.web.controller.common;



import org.spring.china.web.model.Model_RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller()
public class WebSocketController {

	@Autowired 
	private SimpMessagingTemplate msgTemplate; 
	
	@MessageMapping(value="/send-msg-to-user")
	public void SendMsgToUser(Model_RequestMessage model){
		msgTemplate.convertAndSendToUser(model.getUser().getId().toString(), "/msg/getResponse",model.getMsgType());
	}
}
