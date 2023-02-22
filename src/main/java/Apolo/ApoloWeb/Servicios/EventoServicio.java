package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Evento;
import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.EventoRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoServicio {
    @Autowired
    private EventoRepositorio eventoRepositorio;
    
    public void validar(Date fecha, Zona zona, String descripcion) throws ErrorServicio{
        if (fecha==null){
            throw new ErrorServicio("La fecha ingresada no es válida.");
        }
        if (zona==null){
            throw new ErrorServicio("Ingrese una zona.");
        }
        if (descripcion==null){
            throw new ErrorServicio("Describa al evento en más de 10 y menos de 200 caracteres.");
        }
    }
    
    @Transactional
    public void guardarEvento(Date fecha, Zona zona, String descripcion) throws ErrorServicio{
        validar(fecha, zona, descripcion);
        Evento evento = new Evento();
        evento.setFecha(fecha);
        evento.setZona(zona);
        evento.setDescripcion(descripcion);
        eventoRepositorio.save(evento);
    }
    
    @Transactional
    public void modificarEvento(String id, Date fecha, Zona zona, String descripcion) throws ErrorServicio{
        validar(fecha, zona, descripcion);
        Optional<Evento> respuesta = eventoRepositorio.findById(id);
        if (respuesta.isPresent()){
            Evento evento = respuesta.get();
            evento.setFecha(fecha);
            evento.setZona(zona);
            evento.setDescripcion(descripcion);
            eventoRepositorio.save(evento);
        }
        else{
            throw new ErrorServicio("No fue posible encontrar el evento.");
        }
    }
    
    @Transactional
    public void eliminarEvento(String id) throws ErrorServicio{
        Optional<Evento> respuesta = eventoRepositorio.findById(id);
        if (respuesta.isPresent()){
            eventoRepositorio.deleteById(id);
        }
        else{
            throw new ErrorServicio("No fue posible encontrar el evento.");
        }
    }
    
}
