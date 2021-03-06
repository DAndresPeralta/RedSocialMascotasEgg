package edu.egg.tinder.web.servicios;

import edu.egg.tinder.web.entidades.Foto;
import edu.egg.tinder.web.entidades.Usuario;
import edu.egg.tinder.web.entidades.Zona;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.repositorios.UsuarioRepositorio;
import edu.egg.tinder.web.repositorios.ZonaRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author d.andresperalta
 */
@Service
public class UsuarioServicio implements UserDetailsService {

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

        String encriptada = new BCryptPasswordEncoder().encode(clave); // Se encripta la clave del usuario.
        usuario.setClave(encriptada);

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

            String encriptada = new BCryptPasswordEncoder().encode(clave); // Se encripta la clave del usuario.
            usuario.setClave(encriptada);

            if (usuario.getFoto() != null) { //Pregunto si el usuario tiene foto o no

                idFoto = usuario.getFoto().getId(); //Si tiene foto obtengo el Id de esa foto

            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo); //Llamo al m??todo actualizar foto.

            usuario.setFoto(foto); //Modifico la foto del usuario.

            Zona zona = zonaRepositorio.getOne(idZona);
            usuario.setZona(zona);

            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontr?? el usuario.");
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
            throw new ErrorServicio("No se encontr?? le usuario.");
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
            throw new ErrorServicio("No se encontr?? le usuario.");
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
            throw new ErrorServicio("La clave no puede ser nula o contener menos de 7 d??gitos.");
        }

        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no son iguales.");
        }

    }

    public Usuario buscarUsuarioPorId(String id) {

        return usuarioRepositorio.getOne(id);

    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException { // Este m??todo se llama cuando algun usuario quiera autenticarse en la p??gina.

        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);

        if (usuario != null) { // Si el usuario se encuentra en la base de datos se crean los siguientes permisos y se les otorgan al usuario.

            System.out.println("SI EXISTE USUARIO!!!!!!!");
            System.out.println(usuario.getMail());
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); //Esto nos permite reutilizar los datos del usuario logeado.
            HttpSession session = attr.getRequest().getSession(true);// los cuales se pueden utilizar en la vista, por ejemplo.
            session.setAttribute("usuariosession", usuario); // En esta linea guardamos los datos recuperados anteriormente de la BD en el objeto session.

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);

            return user;

        }
        return null;

    }

}
