package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Foto;
import Apolo.ApoloWeb.Entidades.Usuario;
import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.UsuarioRepositorio;
import Apolo.ApoloWeb.Repositorios.ZonaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private FotoServicio fotoServicio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    
    public void validar(String nombre, String apellido, String email, String clave, String idZona) throws ErrorServicio{
        if (nombre==null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre no puede estar vacío.");
        }
        if (apellido==null || apellido.isEmpty()){
            throw new ErrorServicio("El apellido no puede estar vacío.");
        }
        List<Usuario> repetido = usuarioRepositorio.buscarUsuariosPorEmail(email);
        if (repetido.size()!=0){
            throw new ErrorServicio("El correo electrónico ya está en uso.");
        }
        if (email==null || email.length()<3 || email.isEmpty() || !email.contains("@")){
            throw new ErrorServicio("Correo electrónico inválido.");
        }
        if (clave==null || clave.length()<8 || clave.isEmpty()){
            throw new ErrorServicio("Clave inválida.");
        }
        if (idZona==null || idZona.isEmpty()){
            throw new ErrorServicio("Seleccione una zona.");
        }
    }

    @Transactional
    public void guardarUsuario(String nombre, String apellido, String email, String clave1, String clave2, MultipartFile archivo, String idZona) throws ErrorServicio{
        if (!clave1.equals(clave2)){
            throw new ErrorServicio("Las claves no coinciden.");
        }
        validar(nombre,apellido,email,clave1,idZona);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        
        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        usuario.setClave(encriptada);
        
        if (archivo!=null && !archivo.isEmpty()){
            Foto foto = fotoServicio.guardarFoto(archivo);
            usuario.setFoto(foto);
        }
        
        Optional<Zona> respuesta = zonaRepositorio.findById(idZona);
        if (respuesta.isPresent()){
            usuario.setZona(respuesta.get());
        }
        else {
            throw new ErrorServicio("La zona ingresada no es válida.");
        }
        usuario.setAlta(new Date());
        usuarioRepositorio.save(usuario);
        notificacionServicio.enviarMail("¡Bienvenido a Apolo!\nBienvenido, "+nombre+".", "Apolo Web - Registro de usuarios" , email);
    }
    
    
    @Transactional
    public void modificarUsuario(String idUsuario, 
            String nombre, 
            String apellido, 
            String email, 
            MultipartFile archivo, 
            String idZona) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            
            if (archivo!=null && !archivo.isEmpty()){
                if (usuario.getFoto()!=null){
                    String idFoto = usuario.getFoto().getId();
                    Foto foto = fotoServicio.actualizarFoto(idFoto, archivo);
                    usuario.setFoto(foto);
                }
                else {
                    Foto foto = fotoServicio.guardarFoto(archivo);
                    usuario.setFoto(foto);
                }
            }
            Optional<Zona> respuestaZ = zonaRepositorio.findById(idZona);
            if (respuestaZ.isPresent()){
                usuario.setZona(respuestaZ.get());
            }
            else {
                throw new ErrorServicio("La zona ingresada no es válida.");
            }
               
            usuarioRepositorio.save(usuario);
            notificacionServicio.enviarMail("¡Apolo le notifica!\n "+nombre+": su usuario ha sido modificado con éxito. \n ¡Gracias!", "Apolo Web - Modificación de usuarios" , email);
            
        }
        else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }
    
    @Transactional
    public void cambiarContraseña(String idUsuario, String clave1, String clave2) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()){
            if (!clave1.equals(clave2)){
                throw new ErrorServicio("Las claves no coinciden.");
            }
            if (clave1==null || clave1.length()<8 || clave1.isEmpty()){
                throw new ErrorServicio("Clave inválida");
            }
            Usuario usuario = respuesta.get();
            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);
            usuarioRepositorio.save(usuario);
            notificacionServicio.enviarMail("¡Apolo le notifica!\n Su contraseña ha sido modificada con éxito. \n ¡Gracias!", "Apolo Web - Cambio de contraseña" , usuario.getEmail());
        }
        else{
            throw new ErrorServicio("Usuario no encontrado");
        }
    }
    
    
    @Transactional
    public void bajaUsuario(String id) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
            notificacionServicio.enviarMail("¡Adios!\n" +usuario.getNombre()+", vuelva prontos.", "Apolo Web" , usuario.getEmail());
        }
        else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }
    
    @Transactional
    public void habilitarUsuario(String id) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
            notificacionServicio.enviarMail("¡Bienvenido a Apolo!\n Estábamos esperándolo, " +usuario.getNombre()+".", "Apolo Web" , usuario.getEmail());
        }
        else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = (Usuario) usuarioRepositorio.buscarUsuarioPorEmail(email);
        
        if (usuario!=null){
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            
            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        }
        else {
            throw new UsernameNotFoundException("El usuario no ha sido encontrado.");
        }
    }
}    