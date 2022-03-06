package com.example.demo.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender emailSender;

	/**
	 * Envía un mensaje de confirmación para indicar que se ha recibido el mensaje.
	 * @param to
	 * @param msg
	 * @param name
	 * @throws MessagingException 
	 */
	public void sendSimpleMessage(String to, String subject, String body) throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true);
		
		//helper.setFrom("lauramorelez@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);
		
		emailSender.send(message);
	}

}
