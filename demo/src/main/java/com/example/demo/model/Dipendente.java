// Questo è il package che contiene la classe
package com.example.demo.model;

// Importiamo le librerie necessarie
// jakarta.persistence contiene le annotazioni per mappare la classe al database
import jakarta.persistence.*;
// lombok fornisce annotazioni per generare automaticamente getter, setter e costruttori
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

// @Entity indica che questa classe rappresenta una tabella nel database
@Entity
// @Getter e @Setter generano automaticamente i metodi get e set per tutti i campi
@Getter
@Setter
// @NoArgsConstructor crea un costruttore vuoto
@NoArgsConstructor
// @AllArgsConstructor crea un costruttore con tutti i campi
@AllArgsConstructor
public class Dipendente implements UserDetails {
    // @Id indica che questo è il campo chiave primaria
    @Id
    // @GeneratedValue fa generare automaticamente l'ID dal database
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @Column personalizza come il campo viene salvato nel database
    // nullable = false significa che il campo non può essere vuoto
    // unique = true significa che non ci possono essere duplicati
    @Column(nullable = false, unique = true)
    private String username;
    
    // Il nome del dipendente non può essere vuoto
    @Column(nullable = false)
    private String nome;
    
    // Il cognome del dipendente non può essere vuoto
    @Column(nullable = false)
    private String cognome;
    
    // L'email deve essere unica e non può essere vuota
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
} 