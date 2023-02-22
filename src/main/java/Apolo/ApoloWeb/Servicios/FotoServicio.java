package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Foto;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {
    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    public void validar(MultipartFile archivo) throws ErrorServicio{
        if (archivo==null || archivo.isEmpty()){
            throw new ErrorServicio("El archivo está vacío.");
        }
    }
    
    @Transactional
    public Foto guardarFoto(MultipartFile archivo) throws ErrorServicio{
        validar(archivo);
        try {
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setContenido(archivo.getBytes());
            fotoRepositorio.save(foto);
            return foto;
        } 
        catch (IOException ex) {
            System.err.print(ex.getMessage());
            return null;
        }
    }
    
    @Transactional
    public Foto actualizarFoto(String id, MultipartFile archivo) throws ErrorServicio{
        validar(archivo);
        try {
            Foto foto = new Foto();
            Optional<Foto> respuesta = fotoRepositorio.findById(id);
            if (respuesta.isPresent()){
                foto.setMime(archivo.getContentType());
                foto.setContenido(archivo.getBytes());
                fotoRepositorio.save(foto);
            }
            else {
                throw new ErrorServicio("Foto a reemplazar no encontrada.");
            }
            return foto;
        } 
        catch (IOException ex) {
            System.err.print(ex.getMessage());
            return null;
        }
    }
    
    
    
    

}
