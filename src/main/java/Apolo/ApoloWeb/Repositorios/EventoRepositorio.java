package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Evento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepositorio extends JpaRepository<Evento,String>{
    @Query("SELECT c FROM Evento c WHERE c.id = :id")
    public List<Evento> buscarEventoPorId(@Param("id") String id);
}
