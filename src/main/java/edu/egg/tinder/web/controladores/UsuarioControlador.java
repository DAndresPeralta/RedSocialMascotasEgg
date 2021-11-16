//package edu.egg.tinder.web.controladores;
//
//import edu.egg.tinder.web.entidades.Usuario;
//import edu.egg.tinder.web.entidades.Zona;
//import edu.egg.tinder.web.errores.ErrorServicio;
//import edu.egg.tinder.web.repositorios.ZonaRepositorio;
//import edu.egg.tinder.web.servicios.UsuarioServicio;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
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
//    public String editarPerfil(@RequestParam String id, ModelMap model) {
//
//        List<Zona> zonas = zonaRepositorio.findAll();
//        model.put("zonas", zonas);
//
//        try {
//
//            Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
//            model.addAttribute("perfil", usuario);
//
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//        }
//        return "perfil.html";
//    }
//
//    public String actualizarPerfil(ModelMap model, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) throws ErrorServicio{
//
//        Usuario usuario = null;
//
//        try {
//            usuario = usuarioServicio.buscarUsuarioPorId(id);
//            usuarioServicio.modificar(archivo, id, id, id, id, id, id, id);
//            return "redirect:/inicio";
//
//        } catch (ErrorServicio e) {
//            List<Zona> zonas = zonaRepositorio.findAll();
//            model.put("zonas", zonas);
//            model.put("error", e.getMessage());
//            model.put("perfil", usuario);
//
//            return "registro.html";
//
//        }
//
//    }
//
//}
