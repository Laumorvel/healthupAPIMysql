package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.error.AwardNotFoundInLogroException;
import com.example.demo.error.LogroAlreadyContainingPremioException;
import com.example.demo.error.LogroNoRegistradoException;
import com.example.demo.error.PremioNotFoundException;
import com.example.demo.error.PremioNotRegisteredInLogroException;
import com.example.demo.model.Logro;
import com.example.demo.model.Premio;
import com.example.demo.repository.LogroRepo;
import com.example.demo.repository.PremioRepo;

/**
 * Servicio que gestiona los logros y premios de los usuarios.
 * Se hacen comprobaciones en todos los métodos. 
 * @author laura
 *
 */
@Service
public class PremioService {

	@Autowired
	private PremioRepo premioRepo;

	@Autowired
	private LogroRepo logroRepo;

	/**
	 * Obtiene un premio concreto. Se comprueba que tanto el logro como el premio
	 * existan. También se comprueba que ese premio exista en el logro concreto
	 * indicado.
	 * 
	 * @param idPremio
	 * @param idLogro
	 * @return
	 */
	public Premio getPremio(Long idPremio, Long idLogro) {
		// Compruebo que el logro existe
		try {
			logroRepo.findById(idLogro).get();
		} catch (Exception e) {
			throw new LogroNoRegistradoException();
		}

		// Compruebo que el premio existe
		try {
			premioRepo.findById(idPremio).get();
		} catch (Exception e) {
			throw new PremioNotFoundException();
		}

		// Compruebo que ese premio existe en ese logro y devuelvo el premio
		try {
			Long premioId = logroRepo.getPremio(idLogro, idPremio);
			return premioRepo.getById(premioId);
		} catch (Exception e) {
			throw new AwardNotFoundInLogroException();
		}
	}

	/**
	 * Crea un nuevo premio. Para ello se comprueba que el logro exista. Si el logro
	 * ya contiene un premio, no se le incluirá. En caso contrario se le seteará el
	 * premio.
	 * 
	 * @param premio
	 * @param idLogro
	 * @return
	 */
	public Premio publicaPremio(Premio premio, Long idLogro) {
		// Compruebo que el logro existe
		Logro logro;
		try {
			logro = logroRepo.findById(idLogro).get();
		} catch (Exception e) {
			throw new LogroNoRegistradoException();
		}

		// Guardo el premio y el logro con el premio en caso de que el logro no tenga
		// premio
		if (logro.getPremio() == null) {
			premioRepo.save(premio);
			logro.setPremio(premio);
			logroRepo.save(logro);
			return premioRepo.save(premio);
		} else {
			throw new LogroAlreadyContainingPremioException();
		}
	}

	/**
	 * Modifica un prmio, buscando el logro al que pertenece también. Comprueba que
	 * el logro y el premio existan.
	 * 
	 * @param idPremio
	 * @param premio
	 * @param idLogro
	 * @return
	 */
	public Premio modificaPremio(Long idPremio, Premio premio, Long idLogro) {
		// Compruebo que el logro existe
		Logro logro;
		try {
			logro = logroRepo.findById(idLogro).get();
		} catch (Exception e) {
			throw new LogroNoRegistradoException();
		}

		// Compruebo que el premio existe
		try {
			premioRepo.findById(idPremio).get();
		} catch (Exception e) {
			throw new PremioNotFoundException();
		}

		// Guardo el premio con las modificaciones en caso de que el premio esté
		// asociado al logro indicado.
		if (logro.getPremio() != null && logroRepo.getPremio(idLogro, idPremio) != null) {
			premio.setId(idPremio);
			return premioRepo.save(premio);
		} else {
			throw new LogroAlreadyContainingPremioException();
		}
	}

	/**
	 * Borra un premio, el cual está o puede estar referenciado en un logro. Se
	 * comprueba que el logro y el premio existan. También se comprueba que ese
	 * logro concreto exista con ese premio. Debe borrarse su referencia en todos
	 * los logros en los que aparezca.
	 * 
	 * @param idPremio
	 * @param idLogro
	 */
	public void borraPremio(Long idPremio, Long idLogro) {
		// Compruebo que el logro existe
		Logro logro;
		try {
			logro = logroRepo.findById(idLogro).get();
		} catch (Exception e) {
			throw new LogroNoRegistradoException();
		}

		// Compruebo que el premio existe
		Premio premio;
		try {
			premio = premioRepo.findById(idPremio).get();
		} catch (Exception e) {
			throw new PremioNotFoundException();
		}

		if (logro.getPremio() == null) {
			throw new PremioNotRegisteredInLogroException();
		}
		// Guardo el premio con las modificaciones en caso de que el premio esté
		// asociado al logro indicado.
		if (logroRepo.getPremio(idLogro, idPremio) != null) {
			logroRepo.eliminaPremioDeLogro(idPremio);// elimino todas las referencias en las que aparece el premio

			premioRepo.delete(premio);
		} else {
			throw new LogroAlreadyContainingPremioException();
		}
	}
}
