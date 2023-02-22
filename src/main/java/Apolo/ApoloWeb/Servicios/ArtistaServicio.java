package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Artista;
import Apolo.ApoloWeb.Entidades.Foto;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ArtistaRepositorio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import Apolo.ApoloWeb.Repositorios.ZonaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArtistaServicio {
    @Autowired
    private ArtistaRepositorio artistaRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    @Autowired
    private FotoServicio fotoServicio;
    
    public void validar(String nombre, String genero, String idZona, String descripcion) throws ErrorServicio{
        if (nombre==null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre no puede estar vacío.");
        }
        if (genero==null || genero.isEmpty()){
            throw new ErrorServicio("Ingrese un género musical.");
        }
        if (idZona==null || idZona.isEmpty()){
            throw new ErrorServicio("Ingrese una zona.");
        }
        if (descripcion==null || descripcion.isEmpty() || descripcion.length()<10 || descripcion.length()>200){
            throw new ErrorServicio("Descríbase en al menos 10 caracteres, y no más de 200.");
        }
    }
    
    @Transactional
    public Artista guardarArtista(String nombre, String genero, String idZona, String descripcion, MultipartFile archivo, String urlSocial, String urlMusica) throws ErrorServicio{
        validar(nombre, genero, idZona, descripcion);
        Artista artista = new Artista();
        artista.setNombre(nombre);
        artista.setGenero(genero);
        artista.setUrlMusica(urlMusica);
        artista.setUrlSocial(urlSocial);
        
        Optional<Zona> respuestaZ = zonaRepositorio.findById(idZona);
        if (respuestaZ.isPresent()){
            artista.setZona(respuestaZ.get());
        }
        else {
            throw new ErrorServicio("La zona ingresada no es válida.");
        }
        
        artista.setDescripcion(descripcion);
        if (archivo!=null && !archivo.isEmpty()){
            Foto foto = fotoServicio.guardarFoto(archivo);
            artista.setFoto(foto);
        }
        
        artista.setActivo(Boolean.TRUE);
        artista.setAlta(new Date());
        artistaRepositorio.save(artista);
        return artista;
    }
    
    
    @Transactional
    public void modificarArtista(String idArtista, String nombre,String genero, String idZona, String descripcion, MultipartFile archivo, String urlSocial, String urlMusica) throws ErrorServicio{
        validar(nombre, genero, idZona, descripcion);
        Optional<Artista> respuesta1 = artistaRepositorio.findById(idArtista);
        if (respuesta1.isPresent()){
            Artista artista = respuesta1.get();
            artista.setNombre(nombre);
            artista.setGenero(genero);
            artista.setDescripcion(descripcion);
            artista.setUrlMusica(urlMusica);
            artista.setUrlSocial(urlSocial);
            
            Optional<Zona> respuestaZ = zonaRepositorio.findById(idZona);
            if (respuestaZ.isPresent()){
                artista.setZona(respuestaZ.get());
            }
            else {
                throw new ErrorServicio("La zona ingresada no es válida.");
            }
            
            if (archivo!=null && !archivo.isEmpty()){
                if (artista.getFoto()!=null){
                    String idFoto = artista.getFoto().getId();
                    Foto foto = fotoServicio.actualizarFoto(idFoto, archivo);
                    artista.setFoto(foto);
                }
                else {
                    Foto foto = fotoServicio.guardarFoto(archivo);
                    artista.setFoto(foto);
                }
            }
            artistaRepositorio.save(artista);
            System.out.println("modificarArtista.SALIO");
        }
        else {
            throw new ErrorServicio("No se encontró el artista solicitado.");
        }
    }
    
    @Transactional
    public void bajaArtista(String idArtista) throws ErrorServicio{
        Optional<Artista> respuesta = artistaRepositorio.findById(idArtista);
        if (respuesta.isPresent()){
            Artista artista = respuesta.get();
            artista.setBaja(new Date());
            artistaRepositorio.save(artista);
        }
        else {
            throw new ErrorServicio("No se encontró el artista solicitado.");
        }
    }
    
}
