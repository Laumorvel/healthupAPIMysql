package com.example.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.error.MessageWronglyFormedException;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.MensajeRepo;

/**
 * Servicio creado para gestionar los mensajes de la aplicación.
 * @author laura
 *
 */
@Service
public class MensajeService {

	@Autowired
	private MensajeRepo mensajeRepo;

	@Autowired
	private EmailServiceImpl emailService;

//	/**
//	 * Crea un nuevo mensaje utilizando la inyección de emailService.
//	 * @param mensaje
//	 * @return
//	 * @throws MessagingException
//	 */
//	public Mensaje newMensaje(Mensaje mensaje)  {
//		try {
//			this.emailService.sesendSimpleMessage(mensaje.getEmail(), "HelathUp!", mensaje.getMssg());
//			return mensajeRepo.save(mensaje);
//		} catch (Exception e) {
//			throw new MessageWronglyFormedException();
//		}
//	}

}
