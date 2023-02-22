package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Artista;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Entidades.Voto;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ArtistaRepositorio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import Apolo.ApoloWeb.Repositorios.VotoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class VotoServicio {
    @Autowired
    private VotoRepositorio votoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ArtistaRepositorio artistaRepositorio;

    
    public void validar(Integer puntuacion, Usuario votante, Artista votado) throws ErrorServicio{
        if (puntuacion==null || puntuacion<1 || puntuacion>5){
            throw new ErrorServicio("El púntaje debe ser un número entre 1 y 5.");
        }
        if (votante==null){
            throw new ErrorServicio("El votante no puede estar vacío.");
        }
        if (votado==null){
            throw new ErrorServicio("El votado no puede estar vacío.");
        }
    }
    
    @Transactional
    public void guardarVoto(Integer puntuacion, Usuario votante, Artista votado) throws ErrorServicio{
        validar(puntuacion, votante, votado);
        Voto voto = new Voto();
        voto.setPuntuacion(puntuacion);
        voto.setVotante(votante);
        voto.setVotado(votado);
        votoRepositorio.save(voto);
    }
    
    @Transactional
    public void modificarVoto(String id, Integer puntuacion, Usuario votante, Artista votado) throws ErrorServicio{
        Optional<Voto> respuesta = votoRepositorio.findById(id);
        if (respuesta.isPresent()){
            validar(puntuacion, votante, votado);
            Voto voto = new Voto();
            voto.setPuntuacion(puntuacion);
            voto.setVotante(votante);
            voto.setVotado(votado);
            votoRepositorio.save(voto);
        }
        else{
            throw new ErrorServicio("No se encontró el voto para modificar.");
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @Transactional
    public void votar(String idUsuario, String idArtista, Integer puntuacion) throws ErrorServicio{
        List<Voto> lista = new ArrayList();
        lista = votoRepositorio.buscarVotoPorIdUsuarioIdArtista(idUsuario, idArtista);
        if (lista==null){
            Optional<Usuario> respuesta1 = usuarioRepositorio.findById(idUsuario);
            Optional<Artista> respuesta2 = artistaRepositorio.findById(idArtista);
            if (respuesta1.isPresent() && respuesta2.isPresent()){
                guardarVoto(puntuacion, respuesta1.get(), respuesta2.get());
            }
            else{
                throw new ErrorServicio("Usuario o artista no encontrado.");
            }
        }
        else{
            String id = lista.get(0).getId();
            Optional<Usuario> respuesta1 = usuarioRepositorio.findById(idUsuario);
            Optional<Artista> respuesta2 = artistaRepositorio.findById(idArtista);
            if (respuesta1.isPresent() && respuesta2.isPresent()){
                modificarVoto(id, puntuacion, respuesta1.get(), respuesta2.get());
            }
            else{
                throw new ErrorServicio("Usuario o artista no encontrado.");
            }
        }
    }
    
    
    
}
