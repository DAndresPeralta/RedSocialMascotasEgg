//package edu.egg.tinder.web.controladores;
//
//import edu.egg.tinder.web.entidades.Usuario;
//import edu.egg.tinder.web.entidades.Zona;
//import edu.egg.tinder.web.repositorios.ZonaRepositorio;
//import edu.egg.tinder.web.servicios.UsuarioServicio;
//import java.util.List;
//import javax.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// *
// * @author d.andresperalta
// */
//@Controller
//@RequestMapping("/usuario")
//public class UsuarioControlador {
//
//    @Autowired
//    private UsuarioServicio usuarioServicio;
//
//    @Autowired
//    private ZonaRepositorio zonaRepositorio;
//
//    @GetMapping("/editar-perfil")
//    public String editarPerfil(@RequestParam String id, ModelMap model) {
//
//        List<Zona> zonas = zonaRepositorio.findAll();
//        model.put("zonas", zonas);
//
//        try {
//
////            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
////
////            if (respuesta.isPresent()) {
////
////                Usuario usuario = respuesta.get();
////                model.addAttribute("perfil", usuario);
////
////            }
//            Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
//            model.addAttribute("perfil", usuario);
//
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//        }
//        return "perfil.html";
//    }
//
//    @PostMapping("/actualizar-perfil")
//    public String registrar(ModelMap model,HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {
//
//        Usuario usuario = null;
//
//        try {
//
//            usuario = usuarioServicio.buscarUsuarioPorId(id);
//            usuarioServicio.modificar(archivo, id, nombre, apellido, mail, clave2, clave2, idZona);
//            
//            session.setAttribute("usuariosession", usuario); //Con esto modificamos los datos de logeo en la vista.
//            
//            return "redirect:/inicio"; // Redigirige a inicio.
//
//        } catch (Exception e) {
//            List<Zona> zonas = zonaRepositorio.findAll();
//            model.put("zonas", zonas);
//            model.put("error", e.getMessage());
//            model.put("perfil", usuario);
//
//            return "registro.html";
//        }
//
//    }
//
//}
