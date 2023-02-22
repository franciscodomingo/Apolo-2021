package Apolo.ApoloWeb.Controladores;

import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ZonaRepositorio;
import Apolo.ApoloWeb.Servicios.NotificacionServicio;
import Apolo.ApoloWeb.Servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    
    @GetMapping("/")
    public String index(ModelMap modelo){
        List<Zona> listaZonas = zonaRepositorio.findAll();
        modelo.put("listaZonas", listaZonas);
        return "index.html";
    }
    
    @GetMapping("/registro")
    public String registro(ModelMap modelo){
        List<Zona> listaZonas = zonaRepositorio.findAll();
        modelo.put("listaZonas", listaZonas);
        return "registro.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, 
            @RequestParam String nombre, 
            @RequestParam String apellido, 
            @RequestParam String email, 
            @RequestParam String clave1, 
            @RequestParam String clave2, 
            @RequestParam(required=false) MultipartFile foto, 
            @RequestParam String idZona){
        List<Zona> listaZonas = zonaRepositorio.findAll();
        modelo.put("listaZonas", listaZonas);
        try {
            usuarioServicio.guardarUsuario(nombre, apellido, email, clave1, clave2, foto, idZona);
            modelo.put("titulo", nombre+", ¡se ha registrado con éxito!");
            modelo.put("descripcion", "Le hemos enviado un mail a "+email+" con la confirmación del registro.");
            return "exito.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre",nombre);
            modelo.put("apellido",apellido);
            modelo.put("email",email);
            modelo.put("clave1",clave1);
            modelo.put("clave2", clave2);
            modelo.put("foto",foto);
            return "registro.html";
        }
    }
    
    @GetMapping("/login")
    public String login(ModelMap modelo, @RequestParam(required=false) String error, @RequestParam(required=false) String logout){
        if (error!=null){
            modelo.put("error", "Correo electrónico o clave incorrectos.");
        }
        if (logout!=null){
            modelo.put("logout", "Ha salido correctamente");
        }
        return "login.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session){
        if (modelo.getAttribute("titulo")==null || modelo.getAttribute("titulo")=="" || !modelo.containsAttribute("titulo")){
            modelo.addAttribute("titulo", "¡Usted ha ingresado con éxito!");
        }
        return "inicio.html";
    }
    
    @GetMapping("/logout")
    public String logout(){
        return "logout.html";
    }
    
    
















    
    @GetMapping("/FAQ")
    public String FAQ(){
        return "FAQ.html";
    }
    
    @GetMapping("/TyC")
    public String TyC(){
        return "TyC.html";
    }
    
    @GetMapping("/informacion")
    public String informacion(){
        return "informacion.html";
    }
    
    @PostMapping("/mailsender/{id}")
    public String enviarMail(@PathVariable String id, @RequestParam String mail) {
            try {
                    notificacionServicio.enviarMail(mail, mail, mail);
                    return "redirect:/";
            } catch (Exception e) {
                    return "redirect:/";
            }
    }


}
