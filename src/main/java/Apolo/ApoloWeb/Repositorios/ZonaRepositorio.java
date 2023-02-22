package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Zona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona,String>{
    @Query("SELECT c FROM Zona c WHERE c.id = :id")
    public Zona buscarZonaPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Zona c WHERE (c.ciudad = :ciudad AND c.provincia = :provincia)")
    public List<Zona> buscarZonaPorCiudadYProvincia(@Param("ciudad") String ciudad, @Param("provincia") String provincia);
    
}
