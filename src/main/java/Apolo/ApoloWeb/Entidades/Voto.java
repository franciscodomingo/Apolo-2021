package Apolo.ApoloWeb.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;


@Entity
public class Voto {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    private Integer puntuacion;
    @ManyToOne
    private Usuario votante;
    @ManyToOne
    private Artista votado;

    public Voto() {
    }

    public Voto(String id, Integer puntuacion, Usuario votante, Artista votado) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.votante = votante;
        this.votado = votado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Usuario getVotante() {
        return votante;
    }

    public void setVotante(Usuario votante) {
        this.votante = votante;
    }

    public Artista getVotado() {
        return votado;
    }

    public void setVotado(Artista votado) {
        this.votado = votado;
    }
    
    
    
}
