package com.example.demo.service;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.error.MessageWronglyFormedException;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.MensajeRepo;

@Service
public class MensajeService {

	@Autowired
	private MensajeRepo mensajeRepo;

	@Autowired
	private EmailServiceImpl emailService;

	public Mensaje newMensaje(Mensaje mensaje) throws MessagingException {
		try {
			this.emailService.sendSimpleMessage(mensaje.getEmail(), "HelathUp!", mensaje.getMssg());
			return mensajeRepo.save(mensaje);
		} catch (Exception e) {
			throw new MessageWronglyFormedException();
		}
	}

}
