package Apolo.ApoloWeb.Controladores;

import Apolo.ApoloWeb.Entidades.Artista;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ArtistaRepositorio;
import Apolo.ApoloWeb.Repositorios.FotoRepositorio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import Apolo.ApoloWeb.Servicios.UsuarioServicio;
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

@Controller
@RequestMapping("/foto")
public class FotoControlador {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ArtistaRepositorio artistaRepositorio;
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String idUsuario) throws ErrorServicio{
        try {
            Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(idUsuario);
            if (usuario.getFoto()==null){
                throw new ErrorServicio("Este usuario no tiene foto");
            }
            byte[] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto,headers,HttpStatus.OK);
        }
        catch(Exception ex){
            System.out.println("ERROR_FOTO");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
    @GetMapping("/artista/{idArtista}")
    public ResponseEntity<byte[]> fotoArtista(@PathVariable String idArtista) throws ErrorServicio{
        try {
            Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
            if (artista.getFoto()==null){
                throw new ErrorServicio("Este usuario no tiene foto");
            }
            byte[] foto = artista.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto,headers,HttpStatus.OK);
        }
        catch(Exception ex){
            System.out.println("ERROR_FOTO");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
}
