package Apolo.ApoloWeb.Controladores;

import Apolo.ApoloWeb.Entidades.Artista;
import Apolo.ApoloWeb.Entidades.Evento;
import Apolo.ApoloWeb.Entidades.Pedido;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.EventoRepositorio;
import Apolo.ApoloWeb.Repositorios.PedidoRepositorio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import Apolo.ApoloWeb.Repositorios.ZonaRepositorio;
import Apolo.ApoloWeb.Servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/configurarUsuario")
    public String configurarUsuario(ModelMap modelo, HttpSession session, 
            @RequestParam String idUsuario) throws ErrorServicio{
        
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado==null || !logeado.getId().equals(idUsuario)){
            return "redirect:/inicio";
        }
        
        List<Zona> listaZonas = zonaRepositorio.findAll();
        modelo.put("listaZonas", listaZonas);
        try {
            Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(idUsuario);
            modelo.addAttribute("perfil", usuario);
        }
        catch(Exception ex){
            modelo.addAttribute("error", "Usuario no encontrado.");
        }
        
        return "configurarUsuario.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizarUsuario")
    public String actualizarUsuario(ModelMap modelo, HttpSession session,
            @RequestParam("id") String id,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("email") String email, 
            @RequestParam(required=false) MultipartFile foto, 
            @RequestParam("idZona") String idZona,
            @RequestParam("clave") String clave) throws ErrorServicio{
        
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado==null || !logeado.getId().equals(id)){
            return "redirect:/inicio";
        }
        
        if (clave!=null && new BCryptPasswordEncoder().matches(clave, logeado.getClave()) && logeado.getId().equals(id)){
            try {    
                if (nombre==null || nombre.isEmpty()){
                    throw new ErrorServicio("El nombre no puede estar vacío.");
                }
                if (apellido==null || apellido.isEmpty()){
                    throw new ErrorServicio("El apellido no puede estar vacío.");
                }
                List<Usuario> repetido = usuarioRepositorio.buscarUsuariosPorEmail(email);
                if (repetido.size()>1 ){
                    throw new ErrorServicio("El correo electrónico ya está en uso.");
                }
                if (email==null || email.length()<3 || email.isEmpty() || !email.contains("@")){
                    throw new ErrorServicio("Correo electrónico inválido.");
                }
                if (idZona==null || idZona.isEmpty()){
                    throw new ErrorServicio("Seleccione una zona.");
                }

                usuarioServicio.modificarUsuario(id, nombre, apellido, email, foto, idZona);
                Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(id);
                session.setAttribute("usuariosession", usuario);
                modelo.put("titulo", "¡Ha actualizado su usuario con éxito!");
                return "redirect:/inicio";
            } catch (ErrorServicio ex) {
                List<Zona> listaZonas = zonaRepositorio.findAll();
                modelo.put("listaZonas", listaZonas);
                modelo.put("error", ex.getMessage());
                modelo.put("perfil", logeado);
                return "configurarUsuario.html";
            }
        }
        else{
            List<Zona> listaZonas = zonaRepositorio.findAll();
            modelo.put("listaZonas", listaZonas);
            modelo.put("error", "La clave actual ingresada no es válida.");
            modelo.put("perfil", logeado);
            return "configurarUsuario.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/configurarContraseña")
    public String configurarContraseña(ModelMap modelo, HttpSession session, 
            @RequestParam String idUsuario) throws ErrorServicio{
        
        try {
            Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(idUsuario);
            modelo.addAttribute("perfil", usuario);
        }
        catch(Exception ex){
            modelo.addAttribute("error", ex.getMessage());
        }
        return "configurarContraseña.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizarContraseña")
    public String actualizarContraseña(ModelMap modelo, HttpSession session,
            @RequestParam("id") String id,
            @RequestParam("clave1") String clave1,
            @RequestParam("clave2") String clave2,
            @RequestParam("clave") String clave) throws ErrorServicio{
        
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        
        if (clave!=null && new BCryptPasswordEncoder().matches(clave, logeado.getClave()) && logeado.getId().equals(id)){
            try {    
                usuarioServicio.cambiarContraseña(id, clave1, clave2);
                Usuario usuario = usuarioRepositorio.buscarUsuarioPorId(id);
                session.setAttribute("usuariosession", usuario);
                modelo.addAttribute("titulo", "¡Ha actualizado su usuario con éxito!");
                return "inicio.html";
            } catch (ErrorServicio ex) {
                modelo.put("error", ex.getMessage());
                modelo.put("perfil", logeado);
                modelo.put("clave1", clave1);
                modelo.put("clave2", clave1);
                return "configurarContraseña.html";
            }
        }
        else{
            modelo.put("error", "La clave actual ingresada no es válida.");
            modelo.put("perfil", logeado);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave1);
            return "configurarContraseña.html";
        }
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/solicitudesEnviadas")
    public String solicitudesEnviadas(ModelMap modelo, HttpSession session){
        String idUsuario = ((Usuario) session.getAttribute("usuariosession")).getId();
        List<String> pedidosId = pedidoRepositorio.buscarPedidoPorUsuario(idUsuario);
        ArrayList<Pedido> pedidos = new ArrayList();
        
        for (String id : pedidosId){
            Pedido x = pedidoRepositorio.buscarPedidoPorId(id);
            pedidos.add(x);
        }
        modelo.put("pedidos", pedidos);
        if (pedidos.isEmpty()){
            modelo.put("estado", "No ha hecho pedidos aún.");
        }
        else{
            modelo.put("estado", null);
        }
        return "listaPedidos.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/solicitudesRecibidas")
    public String solicitudesRecibidas(ModelMap modelo, HttpSession session){
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        String idUsuario = logeado.getId();
        List<Artista> artistas = logeado.getArtistasAdministrados();
        
        ArrayList<Pedido> pedidos = new ArrayList();
        for (Artista var : artistas){
            List<String> pedidosId = pedidoRepositorio.buscarPedidoPorArtista(var.getId());
            for (String id : pedidosId){
                Pedido x = pedidoRepositorio.buscarPedidoPorId(id);
                pedidos.add(x);
            }
        }
        modelo.put("pedidos", pedidos);
        if (pedidos.isEmpty()){
            modelo.put("estado", "No ha hecho pedidos aún.");
        }
        else{
            modelo.put("estado", null);
        }
        return "listaPedidos.html";
    }
}
