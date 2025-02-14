// Questo è il package principale dell'applicazione
package com.example.demo;

// Importiamo le classi necessarie da Spring Boot
// SpringApplication è la classe che avvia l'applicazione Spring Boot
import org.springframework.boot.SpringApplication;
// Questa annotazione abilita la configurazione automatica di Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication è un'annotazione che combina tre annotazioni:
// - @Configuration: indica che questa classe può definire bean Spring
// - @EnableAutoConfiguration: abilita la configurazione automatica di Spring Boot
// - @ComponentScan: permette di trovare automaticamente altri componenti, configurazioni e servizi
@SpringBootApplication
public class DemoApplication {

	// Questo è il metodo main, il punto di ingresso dell'applicazione
	// Come ogni applicazione Java, inizia da qui quando viene avviata
	public static void main(String[] args) {
		// Questo comando avvia l'applicazione Spring Boot
		// Crea il contesto dell'applicazione, inizializza tutti i bean
		// e avvia il server web incorporato
		SpringApplication.run(DemoApplication.class, args);
	}

}
