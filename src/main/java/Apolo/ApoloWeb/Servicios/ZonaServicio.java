package Apolo.ApoloWeb.Servicios;

import Apolo.ApoloWeb.Entidades.Zona;
import Apolo.ApoloWeb.Errores.ErrorServicio;
import Apolo.ApoloWeb.Repositorios.ZonaRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZonaServicio {
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    
    public void validar(String ciudad, String provincia) throws ErrorServicio{
        if (ciudad==null || ciudad.isEmpty()){
            throw new ErrorServicio("Ingrese una ciudad válida.");
        }
        if (provincia==null || provincia.isEmpty()){
            throw new ErrorServicio("Ingrese una provincia válida.");
        }
    }
    
    @Transactional
    public void guardarZona(String ciudad, String provincia) throws ErrorServicio{
        String c = ciudad.toLowerCase();
        String p = provincia.toLowerCase();
        List<Zona> respuesta = zonaRepositorio.buscarZonaPorCiudadYProvincia(c, p);
        if (!respuesta.isEmpty()){
            Zona zona = new Zona();
            zona.setCiudad(ciudad);
            zona.setProvincia(provincia);
            zonaRepositorio.save(zona);
        }
        else{
            throw new ErrorServicio("La zona ya está en nuestra base de datos.");
        }
    }
}
