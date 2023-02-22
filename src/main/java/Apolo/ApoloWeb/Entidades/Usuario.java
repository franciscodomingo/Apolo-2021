package Apolo.ApoloWeb.Entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    @ManyToOne
    private Zona zona;
    @OneToOne
    private Foto foto;
    private Boolean activo;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Artista> artistasAdministrados;
    @ManyToMany
    private List<Artista> artistasGuardados;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String apellido, String email, String clave, Zona zona, Foto foto, Boolean activo, Date alta, Date baja, List<Artista> artistasAdministrados, List<Artista> artistasGuardados) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.clave = clave;
        this.zona = zona;
        this.foto = foto;
        this.activo = activo;
        this.alta = alta;
        this.baja = baja;
        this.artistasAdministrados = artistasAdministrados;
        this.artistasGuardados = artistasGuardados;
    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

    public Date getBaja() {
        return baja;
    }

    public void setBaja(Date baja) {
        this.baja = baja;
    }


    public List<Artista> getArtistasAdministrados() {
        return artistasAdministrados;
    }

    public void setArtistasAdministrados(List<Artista> artistasAdministrados) {
        this.artistasAdministrados = artistasAdministrados;
    }

    public List<Artista> getArtistasGuardados() {
        return artistasGuardados;
    }

    public void setArtistasGuardados(List<Artista> artistasGuardados) {
        this.artistasGuardados = artistasGuardados;
    }
    
}
