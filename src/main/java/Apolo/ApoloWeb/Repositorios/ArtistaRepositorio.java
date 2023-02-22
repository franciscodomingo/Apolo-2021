package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Artista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepositorio extends JpaRepository<Artista,String>{
    @Query("SELECT c FROM Artista c WHERE c.id = :id")
    public Artista buscarArtistaPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Artista c")
    public List<Artista> todos();
    
}
