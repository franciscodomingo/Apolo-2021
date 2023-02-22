package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Artista;
import Apolo.ApoloWeb.Entidades.Evento;
import Apolo.ApoloWeb.Entidades.Pedido;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ArtistaRepositorio;
import Apolo.ApoloWeb.Repositorios.EventoRepositorio;
import Apolo.ApoloWeb.Repositorios.PedidoRepositorio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class PedidoServicio {
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ArtistaRepositorio artistaRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    
    public void validar(Usuario usuario, Artista artista, Evento evento) throws ErrorServicio{
        if (usuario==null){
            throw new ErrorServicio("El usuario no puede estar vacío.");
        }
        if (artista==null){
            throw new ErrorServicio("El artista no puede estar vacío.");
        }
        if (evento==null){
            throw new ErrorServicio("El evento no puede estar vacío.");
        }
    }
    
    @Transactional
    public void guardarPedido(Usuario usuario, Artista artista, Evento evento) throws ErrorServicio{
        validar(usuario, artista, evento);
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setArtista(artista);
        pedido.setEvento(evento);
        pedidoRepositorio.save(pedido);
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @Transactional
    public void pedir(String idUsuario, String idArtista, String idEvento) throws ErrorServicio{
        Optional<Usuario> respuesta1 = usuarioRepositorio.findById(idUsuario);
        Optional<Artista> respuesta2 = artistaRepositorio.findById(idArtista);
        Optional<Evento> respuesta3 = eventoRepositorio.findById(idEvento);
        if (respuesta1.isPresent() && respuesta2.isPresent() && respuesta3.isPresent()){
            guardarPedido(respuesta1.get(), respuesta2.get(), respuesta3.get());
            notificacionServicio.enviarMail("¡Hola "+respuesta1.get().getNombre()+"! \n Su pedido ha sido realizado con éxito.", "Apolo Web: Pedido" , respuesta1.get().getEmail());
            
            
        }
        else {
            throw new ErrorServicio("El usuario, el artista o el evento no son válidos.");
        }
    }
    
}
