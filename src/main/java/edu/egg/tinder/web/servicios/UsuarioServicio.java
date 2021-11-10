package edu.egg.tinder.web.servicios;

import edu.egg.tinder.web.entidades.Foto;
import edu.egg.tinder.web.entidades.Usuario;
import edu.egg.tinder.web.entidades.Zona;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.repositorios.UsuarioRepositorio;
import edu.egg.tinder.web.repositorios.ZonaRepositorio;
import java.io.IOException;
import java.util.Date;
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
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave, String clave2, String idZona) throws ErrorServicio, IOException {

        validar(nombre, apellido, mail, clave, clave2);

        Usuario usuario = new Usuario();

        //seteo los datos.
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setClave(clave);
        usuario.setAlta(new Date());

        //seteo la foto
        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        Zona zona = zonaRepositorio.getOne(idZona); //Buscamos la zona ingresada por el usuario mediante el formulario y la guardamos en una variable de tipo Zona.
        usuario.setZona(zona); //Seteamos Zona en el usuario.

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave, String clave2, String idZona) throws ErrorServicio {

        String idFoto = null;

        validar(nombre, apellido, mail, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setClave(clave);

            if (usuario.getFoto() != null) { //Pregunto si el usuario tiene foto o no

                idFoto = usuario.getFoto().getId(); //Si tiene foto obtengo el Id de esa foto

            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo); //Llamo al método actualizar foto.

            usuario.setFoto(foto); //Modifico la foto del usuario.

            Zona zona = zonaRepositorio.getOne(idZona);
            usuario.setZona(zona);

            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontró el usuario.");
        }

    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setBaja(new Date());

            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontró le usuario.");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setBaja(null);

            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontró le usuario.");
        }
    }

    public void validar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apeliido no puede ser nulo.");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave no puede ser nula o contener menos de 7 dígitos.");
        }

        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no son iguales.");
        }

    }

}
