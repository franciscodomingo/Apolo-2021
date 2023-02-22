package Apolo.ApoloWeb.Controladores;

import Apolo.ApoloWeb.Entidades.Artista;
import Apolo.ApoloWeb.Entidades.Evento;
import Apolo.ApoloWeb.Entidades.Pedido;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ArtistaRepositorio;
import Apolo.ApoloWeb.Repositorios.EventoRepositorio;
import Apolo.ApoloWeb.Repositorios.PedidoRepositorio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import Apolo.ApoloWeb.Repositorios.ZonaRepositorio;
import Apolo.ApoloWeb.Servicios.ArtistaServicio;
import Apolo.ApoloWeb.Servicios.NotificacionServicio;
import Apolo.ApoloWeb.Servicios.UsuarioServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@Controller
@RequestMapping("/artista")
public class ArtistaControlador {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ArtistaServicio artistaServicio;
    @Autowired
    private ArtistaRepositorio artistaRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/registroArtista")
    public String registroArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idUsuario) throws ErrorServicio{
        
        List<Zona> listaZonas = zonaRepositorio.findAll();
        modelo.put("listaZonas", listaZonas);
        return "registroArtista.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/registrarArtista")
    public String registrarArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String genero,
            @RequestParam String idZona,
            @RequestParam String urlSocial,
            @RequestParam String urlMusica,
            @RequestParam MultipartFile foto
            ) throws ErrorServicio{
        
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado==null){
            return "redirect:/inicio";
        }
        
        try {
            Artista artista = artistaServicio.guardarArtista(nombre, genero, idZona, descripcion, foto, urlSocial, urlMusica);
            logeado.getArtistasAdministrados().add(artista);
            usuarioRepositorio.save(logeado);
            
            String email = logeado.getEmail();
            String nombreU = logeado.getNombre();
            notificacionServicio.enviarMail("¡Registro exitoso!\n"+nombreU+", hemos registrado a "+nombre+" con éxito.", "Apolo Web - Registro de Artistas", email);
            modelo.addAttribute("titulo", nombreU+", ¡ha registrado a "+nombre+" con éxito!");
            modelo.addAttribute("descripcion", "Le hemos enviado un mail a "+email+" con la confirmación del registro del artista.");
            return "/inicio.html";
        }
        catch (ErrorServicio e){
            List<Zona> listaZonas = zonaRepositorio.findAll();
            modelo.put("listaZonas", listaZonas);
            modelo.put("error",e.getMessage());
            modelo.put("nombre",nombre);
            modelo.put("descripcion",descripcion);
            modelo.put("genero",genero);
            modelo.put("idZona",idZona);
            modelo.put("urlSocial",urlSocial);
            modelo.put("urlMusica",urlMusica);
            modelo.put("foto",foto);
            return "registroArtista.html";
        }
    }
    
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/listarArtistas")
    public String listarArtistas(ModelMap modelo, HttpSession session, 
            @RequestParam String idUsuario) throws ErrorServicio{
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        List<Artista> artistas = logeado.getArtistasAdministrados();
        modelo.addAttribute("artistas",artistas);
        return "listaArtista.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editarArtista")
    public String editarArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idUsuario,
            @RequestParam String idArtista) throws ErrorServicio{
        return null;
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminaArtista")
    public String eliminaArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idArtista) throws ErrorServicio{
        
        Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
        modelo.put("nombreArtista", artista.getNombre());
        modelo.put("idArtista",artista.getId());
        return "eliminaArtista.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/eliminarArtista")
    public String eliminarArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idArtista) throws ErrorServicio{
        
        artistaServicio.bajaArtista(idArtista);
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
        List<Artista> artistas = logeado.getArtistasAdministrados();
        artistas.remove(artista);
        modelo.put("artistas",artistas);
        session.setAttribute("usuariosession", logeado);
        return "listaArtista.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/configurarArtista")
    public String configurarArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idArtista) throws ErrorServicio{

        try {
            Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
            modelo.addAttribute("perfil", artista);
            List<Zona> listaZonas = zonaRepositorio.findAll();
            modelo.addAttribute("listaZonas", listaZonas);
        }
        catch(Exception ex){
            modelo.addAttribute("error", "Artista no encontrado.");
        }
        return "configurarArtista.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizarArtista")
    public String actualizarArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idArtista,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String genero,
            @RequestParam String idZona,
            @RequestParam String urlSocial,
            @RequestParam String urlMusica,
            @RequestParam(required=false) MultipartFile foto
            ) throws ErrorServicio{
        System.out.println("1");
        
        try {
            Usuario logeado = (Usuario) session.getAttribute("usuariosession");
            artistaServicio.modificarArtista(idArtista, nombre, genero, idZona, descripcion, foto, urlSocial, urlMusica);
            session.setAttribute("usuariosession", logeado);
            modelo.put("titulo", "¡Ha actualizado su artista con éxito!");
            //notificacionServicio.enviarMail("¡Apolo le notifica!\n "+nombre+" ha sido modificado con éxito. \n ¡Gracias!", "Apolo Web - Modificación de artistas" , logeado.getEmail());
            return "redirect:/inicio";
        }
        catch(ErrorServicio e){
            List<Zona> listaZonas = zonaRepositorio.findAll();
            modelo.put("listaZonas", listaZonas);
            modelo.put("error",e.getMessage());
            Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
            modelo.put("perfil",artista);
            return "configurarArtista.html";
        }
    }
    
    @GetMapping("/perfilArtista")
    public String perfilArtista(ModelMap modelo, HttpSession session, 
            @RequestParam String idArtista) throws ErrorServicio{
        
        try {
            Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
            modelo.addAttribute("perfil",artista);
            List<Zona> listaZonas = zonaRepositorio.findAll();
            modelo.put("listaZonas", listaZonas);
            return "perfilArtista.html";
        }
        catch (Exception e){
            modelo.put("titulo", e.getMessage());
            return "redirect:/inicio";
        } 
    }
    
    public void validarPedido(String idArtista, String fecha, String descripcionEvento, String idZona, String mensaje) throws ErrorServicio{
        if (idArtista==null || idArtista.isEmpty()){
            throw new ErrorServicio("Artista vacío.");
        }
        Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
        if (artista==null){
            throw new ErrorServicio("Artista no encontrado.");
        }
        if (fecha==null || fecha.isEmpty()){
            throw new ErrorServicio("Ingrese una fecha.");
        }
        
        try {
            Date fechaa = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            if (fechaa.before(new Date())){
                throw new ErrorServicio("La fecha ingresada no es válida.");
            }
        }
        catch (ParseException ex) {
            throw new ErrorServicio("Fecha mal ingresada.");
        }
        
        if (descripcionEvento==null || descripcionEvento.isEmpty()){
            throw new ErrorServicio("Describa el evento por favor.");
        }
        Zona zona = zonaRepositorio.buscarZonaPorId(idZona);
        if (zona==null){
            throw new ErrorServicio("La zona ingresada no es válida.");
        }
        
        
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/contratar")
    public String contratarArtista(ModelMap modelo, HttpSession session,
            @RequestParam String idArtista,
            @RequestParam String fecha,
            @RequestParam String descripcionEvento,
            @RequestParam String idZona,
            @RequestParam String mensaje) throws ErrorServicio{
        
        try {
            validarPedido(idArtista, fecha, descripcionEvento, idZona, mensaje);
            Zona zona = zonaRepositorio.buscarZonaPorId(idZona);
            Artista artista = artistaRepositorio.buscarArtistaPorId(idArtista);
            Usuario logeado = (Usuario) session.getAttribute("usuariosession");
            Date fechaa;
            try {
                fechaa = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            } catch (ParseException ex) {
                throw new ErrorServicio("Fecha mal ingresada2.");
            }
            
            Evento evento = new Evento();
            evento.setFecha(fechaa);
            evento.setZona(zona);
            evento.setDescripcion(descripcionEvento);
            eventoRepositorio.save(evento);
            
            Pedido pedido = new Pedido();
            pedido.setEvento(evento);
            pedido.setArtista(artista);
            pedido.setUsuario(logeado);
            pedidoRepositorio.save(pedido);
            
            List<String> idAdministradores = usuarioRepositorio.buscarUsuarioQueAdministra(idArtista);
            ArrayList<Usuario> administradores = new ArrayList();
            for (String x : idAdministradores){
                administradores.add(usuarioRepositorio.buscarUsuarioPorId(x));
            }
            
            for (Usuario x : administradores){
                notificacionServicio.enviarMail("¡Hola, "+x.getNombre()+"!\nTenemos buenas noticias.\n"+logeado.getNombre()+" "+logeado.getApellido()+" quiere contratar a "+artista.getNombre()+". \nLa información es la siguiente: \nMensaje del usuario: "+mensaje+"\nDescripción del evento: \n"+descripcionEvento+"\nFecha:\n"+fecha+"\nEscríbele pronto al siguiente correo: "+logeado.getEmail()+"\n¡Gracias, y suerte!", "Apolo web - Nuevo evento", x.getEmail());
            }
            notificacionServicio.enviarMail("Su pedido para "+artista.getNombre()+" se ha realizado con éxito.", "Apolo Web - Nueva contratación", logeado.getEmail());
            
            modelo.put("titulo", "¡Su pedido se ha realizado con éxito!");
            modelo.put("descripcion", "Le hemos enviado un mail a "+logeado.getEmail()+" con la información correspondiente.");
            return "inicio.html";
            
        }
        catch (ErrorServicio e){
            modelo.put("error", e.getMessage());
            modelo.put("idArtista", idArtista);
            modelo.put("descripcionEvento", descripcionEvento);
            modelo.put("fecha",fecha);
            modelo.put("idZona", idZona);
            modelo.put("mensaje", mensaje);
            return "redirect:/artista/perfilArtista?=idArtista"+idArtista;
        }
    }
    
    @GetMapping("/buscarArtistas")
    public String buscarArtista(ModelMap modelo, HttpSession session,
        @RequestParam(required=false) String BidZona,
        @RequestParam(required=false) String Bgenero){
        
        List<Artista> lista = artistaRepositorio.todos();
        List<Artista> artistas = new ArrayList();
        List<Artista> artistasMismaZona = new ArrayList();
        List<Artista> artistasMismoGenero = new ArrayList();
        List<Artista> artistasOtros = new ArrayList();
        
        
        for (Artista x : lista){
            if(BidZona!= null && x.getZona().getId().equals(BidZona)){
                if (Bgenero!= null && x.getGenero().equals(Bgenero)){
                    artistas.add(x);
                }
                else {
                    artistasMismaZona.add(x);
                }
            }
            else{
                if (Bgenero!= null && x.getGenero().equals(Bgenero)){
                    artistasMismoGenero.add(x);
                }
                else {
                    artistasOtros.add(x);
                }
            }
        }
        
        if (artistas.isEmpty()){
            modelo.put("error", "No se encontraron artistas con tu criterio de búsqueda.");
        }
        else{
            modelo.put("error", null);
            modelo.put("artistas",artistas);
        }
        modelo.put("artistasMismaZona", artistasMismaZona);
        modelo.put("artistasMismoGenero", artistasMismoGenero);
        modelo.put("artistasOtros", artistasOtros);
        return "buscarArtistas.html";
    }
    
}
