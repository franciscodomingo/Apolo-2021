package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido,String>{
    @Query("SELECT c FROM Pedido c WHERE c.id = :id")
    public Pedido buscarPedidoPorId(@Param("id") String id);
    
    @Query(value="SELECT id FROM pedido WHERE usuario_id= :idUsuario", nativeQuery=true)
    public List<String> buscarPedidoPorUsuario(@Param("idUsuario") String idUsuario);
    
    @Query(value="SELECT id FROM pedido WHERE artista_id= :idArtista", nativeQuery=true)
    public List<String> buscarPedidoPorArtista(@Param("idArtista") String idArtista);
}
