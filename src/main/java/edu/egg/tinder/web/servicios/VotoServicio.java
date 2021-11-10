package edu.egg.tinder.web.servicios;

import edu.egg.tinder.web.entidades.Mascota;
import edu.egg.tinder.web.entidades.Voto;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.repositorios.MascotaRepositorio;
import edu.egg.tinder.web.repositorios.UsuarioRepositorio;
import edu.egg.tinder.web.repositorios.VotoRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author d.andresperalta
 */
@Service
public class VotoServicio {

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VotoRepositorio votoRepositorio;

    public void votar(String idUsuario, String idMascota1, String idMascota2) throws ErrorServicio {

        Voto voto = new Voto();

        voto.setFecha(new Date());

        if (idMascota1.equals(idMascota2)) {
            throw new ErrorServicio("No puede votarse a si mismo.");
        }

        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota1);

        if (respuesta.isPresent()) {

            Mascota mascota1 = respuesta.get();

            if (mascota1.getUsuario().getId().equals(idUsuario)) {
                voto.setMascota1(mascota1);

            } else {
                throw new ErrorServicio("No tiene permisos para realizar la operación.");
            }

        } else {
            throw new ErrorServicio("La mascota no existe.");
        }

        Optional<Mascota> respuesta2 = mascotaRepositorio.findById(idMascota2);

        if (respuesta2.isPresent()) {

            Mascota mascota2 = respuesta2.get();

            voto.setMascota2(mascota2);

        } else {
            throw new ErrorServicio("No existe la mascota.");

        }

        votoRepositorio.save(voto);

    }

    public void responder(String idUsuario, String idVoto) throws ErrorServicio {

        Optional<Voto> respuesta = votoRepositorio.findById(idVoto);

        if (respuesta.isPresent()) {

            Voto voto = respuesta.get();

            voto.setRespuesta(new Date());

            if (voto.getMascota2().getUsuario().getId().equals(idUsuario)) {

                votoRepositorio.save(voto);

            } else {
                throw new ErrorServicio("No tiene permiso para realizar esta acción.");
            }

        } else {
            throw new ErrorServicio("No existe el voto solicitado.");
        }

    }

}
