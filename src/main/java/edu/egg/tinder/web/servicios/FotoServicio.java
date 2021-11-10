package edu.egg.tinder.web.servicios;

import edu.egg.tinder.web.entidades.Foto;
import edu.egg.tinder.web.errores.ErrorServicio;
import edu.egg.tinder.web.repositorios.FotoRepositorio;
import java.io.IOException;
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
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    //Multipartfile es un archivo donde se almacena la foto subida por el usuario.
    @Transactional //Esta anotación genera un comit y guarda en la BD si no se lanza ninguna excepcion fuera del método. Si se genera una excepción fuera del método se realiza un rollBack y no se hace el comit.
    public Foto guardar(MultipartFile archivo) throws ErrorServicio, IOException {

        if (archivo != null) {

            try {

                Foto foto = new Foto();
                foto.setMime(archivo.getContentType()); //Esto devuelve el tipo mime del archivo adjunto.
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return null;
    }
    
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {

        if (archivo != null) {

            try {

                Foto foto = new Foto();

                if (idFoto != null) {

                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);

                    if (respuesta.isPresent()) {

                        foto = respuesta.get();
                    }
                }

                foto.setMime(archivo.getContentType()); //Esto devuelve el tipo mime del archivo adjunto.
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return null;

    }
}
