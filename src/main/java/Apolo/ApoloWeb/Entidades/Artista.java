package Apolo.ApoloWeb.Entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;


@Entity
public class Artista {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    
    private String nombre;
    private String genero;
    @ManyToOne
    private Zona zona;
    private String descripcion;
    @OneToMany
    private List<Evento> eventos;
    @OneToOne
    private Foto foto;
    private String urlSocial;
    private String urlMusica;
    private Boolean activo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;

    public Artista() {
    }

    public Artista(String id, String nombre, String genero, Zona zona, String descripcion, List<Evento> eventos, Foto foto, String urlSocial, String urlMusica, Boolean activo, Date alta, Date baja) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.zona = zona;
        this.descripcion = descripcion;
        this.eventos = eventos;
        this.foto = foto;
        this.urlSocial = urlSocial;
        this.urlMusica = urlMusica;
        this.activo = activo;
        this.alta = alta;
        this.baja = baja;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public String getUrlSocial() {
        return urlSocial;
    }

    public void setUrlSocial(String urlSocial) {
        this.urlSocial = urlSocial;
    }

    public String getUrlMusica() {
        return urlMusica;
    }

    public void setUrlMusica(String urlMusica) {
        this.urlMusica = urlMusica;
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

    

    
}
