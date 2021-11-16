package edu.egg.tinder.web.controladores;

import edu.egg.tinder.web.entidades.Mascota;
import edu.egg.tinder.web.entidades.Usuario;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.servicios.MascotaServicio;
import edu.egg.tinder.web.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private MascotaServicio mascotaServicio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {

        try {
            Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);

            if (usuario.getFoto() == null) {

                throw new ErrorServicio("El usuario no posee foto.");

            }

            byte[] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);

        } catch (ErrorServicio e) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, e);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) {

        try {
            Mascota mascota = mascotaServicio.buscarMascotaPorId(id);

            if (mascota.getFoto() == null) {

                throw new ErrorServicio("El usuario no posee foto.");

            }

            byte[] foto = mascota.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);

        } catch (ErrorServicio e) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, e);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
