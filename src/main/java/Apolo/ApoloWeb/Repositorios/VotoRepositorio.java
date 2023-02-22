package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto,String>{
    @Query("SELECT c FROM Voto c WHERE c.id = :id")
    public List<Voto> buscarVotoPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Voto c WHERE (c.votante = :idUsuario AND c.votado = :idArtista)")
    public List<Voto> buscarVotoPorIdUsuarioIdArtista(@Param("idUsuario") String idUsuario, @Param("idArtista") String idArtista);
}