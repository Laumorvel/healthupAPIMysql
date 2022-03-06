package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Logro;
import com.example.demo.model.User;

public interface LogroRepo extends JpaRepository<Logro, Long> {

	/**
	 * Consigue el id de un logro que tenga una fecha, id de usuario y tipo
	 * concretos
	 * 
	 * @param tipo
	 * @param fecha
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT id FROM logro WHERE tipo = ?1 AND fecha = ?2 AND user_id = ?3", nativeQuery = true)
	Long getLogro(String tipo, String fecha, Long idUser);

	/**
	 * Consigue la lista de logros de un usuario por el usuario
	 * 
	 * @param user
	 * @return
	 */
	public List<Logro> findByUser(User user);

	/**
	 * Subconsulta Consigue los id de usuarios que no han marcado el logro (ya sea
	 * como true o false) el día anterior, de tipo food
	 * 
	 * @param fecha
	 * @return
	 */
	@Query(value = "SELECT DISTINCT user_id FROM logro WHERE user_id NOT IN (SELECT user_id FROM logro WHERE fecha=?1 AND tipo='food') ", nativeQuery = true)
	List<Long> getIdUsersWithoutFoodRegister(String fecha);

	/**
	 * Subconsulta Consigue los id de usuarios que no han marcado el logro (ya sea
	 * como true o false) el día anterior, de tipo fecha
	 * 
	 * @param fecha
	 * @return
	 */
	@Query(value = "SELECT DISTINCT user_id FROM logro WHERE user_id NOT IN (SELECT user_id FROM logro WHERE fecha=?1 AND tipo='sport') ", nativeQuery = true)
	List<Long> getIdUsersWithoutSportRegister(String fecha);

	/**
	 * No se puede usar * puesto que JPA no sabría qué devolver. De esta manera,
	 * sabe que debe devolver instancias de logro.
	 * 
	 * @param user_id
	 * @param tipo
	 * @return lista de logros que coincidan con los parámetros introducidos
	 */
	@Query(value = "SELECT l FROM Logro l WHERE user_id = ?1 AND tipo = ?2")
	List<Logro> getLogrosTipo(Long user_id, String tipo);

	/**
	 * Selecciona el id de un logro donde coincida el id del logro y el del usuario
	 * que le indicamos
	 * 
	 * @param id
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT id FROM logro WHERE id = ?1 AND user_id = ?2", nativeQuery = true)
	Long getIdLogroFromUser(Long id, Long idUser);

	/**
	 * Selecciona el id de un premio al que se haga referencia en un logro.
	 * Debe coincidir el id del logro y el del premio
	 * @param idLogro
	 * @param idPremio
	 * @return
	 */
	@Query(value = "SELECT premio_id FROM logro WHERE id = ?1 AND premio_id = ?2", nativeQuery = true)
	Long getPremio(Long idLogro, Long idPremio);

	/**
	 * Modifica un logro. Setea la id del premio y la pone a null
	 * @param idPremio
	 */
	@Query(value = "UPDATE logro SET premio_id = null WHERE premio_id = ?1", nativeQuery = true)
	void eliminaPremioDeLogro(Long idPremio);

}
