package edu.egg.tinder.web.controladores;

import edu.egg.tinder.web.entidades.Usuario;
import edu.egg.tinder.web.entidades.Zona;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.repositorios.ZonaRepositorio;
import edu.egg.tinder.web.servicios.UsuarioServicio;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author d.andresperalta
 */
@Controller //Indica que la clase es de tipo controlador
@RequestMapping("/") //Indica cuando se activa el controlador, en este caso, luego de la /.
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')") // Esto es para que solo se pueda acceder a la vista luego de logearse. Tambien hay que dar el permiso en UsuarioPermiso.
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }

    @GetMapping("/login2")
    public String login2(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) { //required indica que el parametro no es obligatorio.

        if (error != null) {
            modelo.put("error", "Credenciales incorrectas."); // Se lanza el error si se colocan mal el usuario o clave.
        }

        if (logout != null) {
            modelo.put("logout", "Vuelva prontos.");
        }

        return "login2.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {

        List<Zona> zonas = zonaRepositorio.findAll(); //Con esto se listan todos los elementos Zona mediante la variable zonas. Se utilizas findAll perteneciente al JpaRepo.

        modelo.put("zonas", zonas); //El primer "zonas" es la vinculación con el select del HTML registro.

        return "registro.html";
    }

    @PostMapping("/registrar")
    public String guardar(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) throws ErrorServicio, IOException {

        try {
            usuarioServicio.registrar(archivo, nombre, apellido, mail, clave1, clave2, idZona);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage()); //Sirve para mandar un mensaje a la pantalla. En este caso un Error.

            modelo.put("nombre", nombre); //Con estos modelos mantengo los datos correctos en la vista en vez de perderlos al actualizar por error.
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);

            return "registro.html";
        }
        modelo.put("titulo", "Bienvenido a Tinder de Mascotas.");
        modelo.put("descripcion", "Usuario registrado con exito.");
        return "exito.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

        List<Zona> zonas = zonaRepositorio.findAll();
        model.put("zonas", zonas);

        Usuario login = (Usuario) session.getAttribute("usuariosession"); //Con esto hacemos una doble validacion del ID recibido y el ID logeado. Evitamos hacking.
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {

            Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
            model.addAttribute("perfil", usuario);

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap model, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {

        Usuario usuario = null;

        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession"); //Con esto hacemos una doble validacion del ID recibido y el ID logeado. Evitamos hacking
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }

            usuario = usuarioServicio.buscarUsuarioPorId(id);
            usuarioServicio.modificar(archivo, id, nombre, apellido, mail, clave2, clave2, idZona);

            session.setAttribute("usuariosession", usuario); //Con esto modificamos los datos de logeo en la vista.

            return "redirect:/inicio"; // Redigirige a inicio.

        } catch (Exception e) {
            List<Zona> zonas = zonaRepositorio.findAll();
            model.put("zonas", zonas);
            model.put("error", e.getMessage());
            model.put("perfil", usuario);

            return "registro.html";
        }

    }

}
