package paquete;

import jakarta.persistence.*;

@Entity
@Table(name = "POKEDEX_COMPETITIVA", schema = "tornepkm")
public class PokedexCompetitiva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pokedex", nullable = false)
    private Integer id;

    @Column(name = "nombre_pokemon", nullable = false, length = 50)
    private String nombrePokemon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombrePokemon() {
        return nombrePokemon;
    }

    public void setNombrePokemon(String nombrePokemon) {
        this.nombrePokemon = nombrePokemon;
    }

}