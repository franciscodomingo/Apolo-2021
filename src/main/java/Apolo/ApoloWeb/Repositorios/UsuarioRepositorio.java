package Apolo.ApoloWeb.Repositorios;

import Apolo.ApoloWeb.Entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,String>{
    @Query("SELECT c FROM Usuario c WHERE c.email = :email")
    public Usuario buscarUsuarioPorEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Usuario c WHERE c.email = :email")
    public List<Usuario> buscarUsuariosPorEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Usuario c WHERE c.id = :id")
    public Usuario buscarUsuarioPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Usuario c WHERE (c.activo = true)")
    public List<Usuario> buscarUsuariosActivos();
    
    @Query(value="SELECT usuario_id FROM usuario_artistas_administrados WHERE artistas_administrados_id= :idArtista", nativeQuery=true)
    public List<String> buscarUsuarioQueAdministra(@Param("idArtista") String idArtista);
}


