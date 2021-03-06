package edu.egg.tinder.web.repositorios;

import edu.egg.tinder.web.entidades.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author d.andresperalta
 */
@Repository
public interface VotoRepositorio extends JpaRepository<Voto, String> {

    @Query("SELECT c FROM Voto c WHERE c.mascota1.id = :id ORDER BY c.fecha DESC")
    public List<Voto> buscarVotoPropio(@Param("id") String id);

    @Query("SELECT c FROM Voto c WHERE c.mascota2.id = :id ORDER BY c.fecha DESC")
    public List<Voto> buscarVotoRecibido(@Param("id") String id);

}
