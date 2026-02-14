package com.destimatch.entity;

import com.destimatch.common.utils.BudgetLevel;
import com.destimatch.common.utils.Continent;
import com.destimatch.common.utils.Location;
import com.destimatch.common.utils.TravelStyle;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MongoEntity(collection = "users")
@Getter
@Setter
@AllArgsConstructor
public class UserEntity extends PanacheMongoEntity {

     // Identité et sécurité
    @BsonProperty("name")
    private String name;
    @BsonProperty("email")
    private String email; // Devra être unique
    @BsonProperty("hashed_password")
    private String password; // Hashed
    @BsonProperty("phone")
    private String phone;

    // Localisation et contexte géographique
    @BsonProperty("location")
    private Location location;

    @BsonProperty("roles")
    private List<String> roles = new ArrayList<>();

    /* ------- PROFIL UTILISATEUR ------- */

    // Liste des tags préférés
    @BsonProperty("preferences")
    private List<String> preferences = new ArrayList<>();

    // Qui voyage ? (Enum stocké)
    @BsonProperty("travel_style")
    private TravelStyle travelStyle; // ex: SOLO, COUPLE, FAMILY

    // Niveau de budget global
    @BsonProperty("budget_level")
    private BudgetLevel budgetLevel; // ex: 1=Eco, 2=Moyen, 3=Luxe

    /* ------- COMPORTEMENT ------- */

    @BsonProperty("favorite_continents")
    private Set<Continent> favoriteContinents = new HashSet<>();

    public UserEntity() {}

    public void addRole(String role) {
        if (!this.roles.contains(role))
            this.roles.add(role);
    }
}
