package edu.egg.tinder.web.servicios;

import edu.egg.tinder.web.entidades.Foto;
import edu.egg.tinder.web.entidades.Mascota;
import edu.egg.tinder.web.entidades.Usuario;
import edu.egg.tinder.web.enumeraciones.Sexo;
import edu.egg.tinder.web.enumeraciones.Tipo;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.repositorios.MascotaRepositorio;
import edu.egg.tinder.web.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author d.andresperalta
 */
@Service
public class MascotaServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void agregarMascota(MultipartFile archivo, String idUsuario, String nombre, Sexo sexo, Tipo tipo) throws ErrorServicio, IOException {

        Usuario usuario = usuarioRepositorio.findById(idUsuario).get();

        validar(nombre, sexo);

        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setTipo(tipo);
        mascota.setAlta(new Date());
        mascota.setUsuario(usuario);

        //Guardo la foto y la seteo en el atributo foto de mascota.
        Foto foto = fotoServicio.guardar(archivo);
        mascota.setFoto(foto);

        mascotaRepositorio.save(mascota);

    }

    @Transactional
    public void modificar(MultipartFile archivo, String idUsuario, String idMascota, String nombre, Sexo sexo, Tipo tipo) throws ErrorServicio {

        String idFoto = null;

        validar(nombre, sexo);

        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);

        if (respuesta.isPresent()) {

            Mascota mascota = respuesta.get();

            if (mascota.getUsuario().getId().equals(idUsuario)) {

                mascota.setNombre(nombre);
                mascota.setSexo(sexo);

                if (mascota.getFoto() != null) {

                    idFoto = mascota.getFoto().getId();

                }

                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                mascota.setTipo(tipo);

                mascotaRepositorio.save(mascota);

            } else {
                throw new ErrorServicio("Usted no tiene los permisos necesarios.");
            }

        } else {
            throw new ErrorServicio("La mascota no existe.");
        }
    }

    @Transactional
    public void eliminar(String idUsuario, String idMascota) throws ErrorServicio {

        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);

        if (respuesta.isPresent()) {

            Mascota mascota = respuesta.get();

            if (mascota.getUsuario().getId().equals(idUsuario)) {

                mascota.setBaja(new Date());

                mascotaRepositorio.save(mascota);

            }

        } else {
            throw new ErrorServicio("La mascota no existe.");
        }

    }

    public void validar(String nombre, Sexo sexo) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        if (sexo == null) {
            throw new ErrorServicio("El sexo no puede ser nulo.");
        }
    }

    public Mascota buscarMascotaPorId(String id) {

        return mascotaRepositorio.getOne(id);

    }

    public List<Mascota> buscarMascotasPorUsuario(String id) {

        return mascotaRepositorio.buscarMascotaPorUsuario(id);

    }

}
