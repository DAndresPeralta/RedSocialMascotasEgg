package edu.egg.tinder.web.repositorios;

import edu.egg.tinder.web.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author d.andresperalta
 */

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {

}
