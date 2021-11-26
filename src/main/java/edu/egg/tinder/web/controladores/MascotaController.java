package edu.egg.tinder.web.controladores;

import edu.egg.tinder.web.entidades.Mascota;
import edu.egg.tinder.web.entidades.Usuario;
import edu.egg.tinder.web.entidades.Zona;
import edu.egg.tinder.web.enumeraciones.Sexo;
import edu.egg.tinder.web.enumeraciones.Tipo;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.servicios.MascotaServicio;
import edu.egg.tinder.web.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/mascota")
public class MascotaController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private MascotaServicio mascotaServicio;

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {

        if (accion == null) {
            accion = "Crear";

        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");

        if (login == null) { // Esto es para que no se pueda acceder sin estar logeado previamente.
            return "redirect:/login";

        }

        Mascota mascota = new Mascota();

        if (id != null && !id.isEmpty()) { //Esto es para modificar una mascota existente. Si existe una mascota, busca su id.
            try {
                mascota = mascotaServicio.buscarMascotaPorId(id);
            } catch (Exception e) {
                Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        model.put("perfil", mascota);
        model.put("accion", accion);
        model.put("sexos", Sexo.values());
        model.put("tipos", Tipo.values());

        return "mascota.html";

    }

//    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap model, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam Tipo tipo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");

        if (login == null) { // Esto es para que no se pueda acceder sin estar logeado previamente.
            return "redirect:/inicio";

        }

        try {

            if (id == null || id.isEmpty()) {
                mascotaServicio.agregarMascota(archivo, login.getId(), nombre, sexo, tipo);

            } else {
                mascotaServicio.modificar(archivo, login.getId(), id, nombre, sexo, tipo);

            }

            return "redirect:/inicio"; // Redigirige a inicio.

        } catch (Exception e) {

            Mascota mascota = new Mascota();
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);

            model.put("accion", "Actualizar");
            model.put("sexos", Sexo.values());
            model.put("tipos", Tipo.values());
            model.put("error", e.getMessage());
            model.put("perfil", login);

            return "mascota.html";
        }

    }

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");

        if (login == null) { // Esto es para que no se pueda acceder sin estar logeado previamente.
            return "redirect:/login"; // Si es nulo, redirect te envia a la vista declarada (login).

        }

        List<Mascota> mascotas = mascotaServicio.buscarMascotasPorUsuario(login.getId()); //Obtengo en una lista, el listado de mascotas que posee un determinado usuario.
        model.put("mascotas", mascotas); //Muestro en la vista la List obtenida mediante model.

        return "mascotas.html";

    }

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {

        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaServicio.eliminar(login.getId(), id);

        } catch (ErrorServicio ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);

        }
        return "redirect:/mascota/mis-mascotas";
    }

}
