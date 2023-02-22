package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Foto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto,String>{
    @Query("SELECT c FROM Foto c WHERE c.id = :id")
    public List<Foto> buscarFotoPorId(@Param("id") String id);
}
