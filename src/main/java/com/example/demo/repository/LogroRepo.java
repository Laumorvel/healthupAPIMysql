package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Logro;
import com.example.demo.model.User;

public interface LogroRepo extends JpaRepository<Logro, Long>{
	
	@Query(value= "SELECT id FROM logro WHERE tipo = ?1 AND fecha = ?2 AND user_id = ?3", nativeQuery = true)
	Long getLogro(String tipo, String fecha, Long idUser);
	
	
	public List<Logro> findByUser(User user);
	
	@Query (value = "SELECT DISTINCT user_id FROM logro WHERE user_id NOT IN (SELECT user_id FROM logro WHERE fecha=?1 AND tipo='food') ", nativeQuery = true)
	List<Long>getIdUsersWithoutFoodRegister(String fecha);
	
	@Query (value = "SELECT DISTINCT user_id FROM logro WHERE user_id NOT IN (SELECT user_id FROM logro WHERE fecha=?1 AND tipo='sport') ", nativeQuery = true)
	List<Long>getIdUsersWithoutSportRegister(String fecha);
	
	/**
	 * No se puede usar * puesto que JPA no sabría qué devolver. De esta manera, sabe que debe devolver instancias de logro.
	 * @param user_id
	 * @param tipo
	 * @return lista de logros que coincidan con los parámetros introducidos
	 */
	@Query(value = "SELECT l FROM Logro l WHERE user_id = ?1 AND tipo = ?2")
	List<Logro>getLogrosTipo(Long user_id, String tipo);
	
	@Query (value= "SELECT id FROM logro WHERE id = ?1 AND user_id = ?2", nativeQuery = true)
	Long getIdLogroFromUser(Long id, Long idUser);
	
}
