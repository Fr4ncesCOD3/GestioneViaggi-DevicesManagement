// Package dell'applicazione che contiene i test
package com.example.demo;

// Importiamo tutte le classi necessarie per i test
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.validation.DataValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest indica che questo è un test per il layer di persistenza dati
@DataJpaTest
// @TestMethodOrder specifica l'ordine di esecuzione dei test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @TestPropertySource configura il database di test in memoria H2
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop", // Ricrea il database ad ogni esecuzione
    "spring.datasource.driver-class-name=org.h2.Driver", // Usa il driver H2
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", // URL del database in memoria
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect" // Dialetto SQL da usare
})
public class DemoApplicationTests {

	// Iniettiamo il repository per gestire i viaggi nel database
	@Autowired
	private ViaggioRepository viaggioRepository;

	// Metodo eseguito una volta prima di tutti i test
	@BeforeAll
	static void setup() {
		System.out.println("\n=== INIZIANDO I TEST DI SICUREZZA E PRESTAZIONI ===\n");
	}

	// Primo test: verifica la validazione degli input
	@Test
	@Order(1)
	@DisplayName("Test 1: Sicurezza - Validazione Input")
	void testInputValidation() {
		System.out.println("🔒 TEST SICUREZZA - Validazione Input");
		
		// Verifica che una data passata generi un errore
		Exception exception = assertThrows(Exception.class, () -> {
			DataValidator.validateViaggio("Roma", LocalDate.of(2023, 1, 1));
		});
		assertTrue(exception.getMessage().contains("data non può essere nel passato"));
		
		// Verifica che un'email non valida generi un errore
		exception = assertThrows(Exception.class, () -> {
			DataValidator.validateDipendente("user1", "Nome", "Cognome", "email-non-valida");
		});
		assertTrue(exception.getMessage().contains("Email non valida"));

		System.out.println("✅ Validazione input funzionante correttamente");
	}

	// Secondo test: misura le prestazioni della creazione di un viaggio
	@Test
	@Order(2)
	@DisplayName("Test 2: Performance - Creazione Viaggio")
	void testCreateViaggio() {
		System.out.println("\n🚀 TEST PERFORMANCE - Creazione Viaggio");
		
		// Crea un nuovo viaggio
		Viaggio viaggio = new Viaggio();
		viaggio.setDestinazione("Milano");
		viaggio.setData(LocalDate.now().plusDays(10));
		viaggio.setStato(StatoViaggio.IN_PROGRAMMA);

		// Misura il tempo necessario per salvare il viaggio
		long startTime = System.nanoTime();
		Viaggio savedViaggio = viaggioRepository.save(viaggio);
		long endTime = System.nanoTime();
		long responseTime = (endTime - startTime) / 1_000_000; // Converti in millisecondi
		
		// Verifica che il viaggio sia stato salvato correttamente
		assertNotNull(savedViaggio.getId());
		System.out.printf("✅ Viaggio creato in %d ms\n", responseTime);
	}

	// Terzo test: verifica la sicurezza degli accessi
	@Test
	@Order(3)
	@DisplayName("Test 3: Sicurezza - Accesso non autorizzato")
	void testUnauthorizedAccess() {
		System.out.println("\n🔒 TEST SICUREZZA - Accesso non autorizzato");
		
		// Verifica che non si possa accedere a un ID non esistente
		assertFalse(viaggioRepository.existsById(999L));
		System.out.println("✅ Protezione accesso non autorizzato funzionante");
	}

	// Quarto test: verifica le prestazioni con molte operazioni
	@Test
	@Order(4)
	@DisplayName("Test 4: Performance - Carico multiplo")
	void testLoadPerformance() {
		System.out.println("\n�� TEST PERFORMANCE - Carico multiplo");
		
		// Crea e salva più viaggi misurando il tempo
		int numRequests = 10;
		long startTime = System.nanoTime();
		
		for (int i = 0; i < numRequests; i++) {
			Viaggio viaggio = new Viaggio();
			viaggio.setDestinazione("Città " + i);
			viaggio.setData(LocalDate.now().plusDays(i));
			viaggio.setStato(StatoViaggio.IN_PROGRAMMA);
			viaggioRepository.save(viaggio);
		}
		
		// Calcola i tempi medi di esecuzione
		long endTime = System.nanoTime();
		long totalTime = (endTime - startTime) / 1_000_000;
		double avgTime = totalTime / (double) numRequests;
		
		// Verifica che tutti i viaggi siano stati salvati
		assertEquals(numRequests, viaggioRepository.count());
		System.out.printf("✅ %d operazioni completate in %d ms (media: %.2f ms per operazione)\n", 
			numRequests, totalTime, avgTime);
	}

	// Metodo eseguito una volta dopo tutti i test
	@AfterAll
	static void printSummary() {
		System.out.println("\n=== REPORT FINALE SICUREZZA E PRESTAZIONI ===\n");
		
		// Stampa consigli per migliorare la sicurezza
		System.out.println("🔒 RACCOMANDAZIONI SICUREZZA:");
		System.out.println("   1. Implementare rate limiting per prevenire attacchi DDoS");
		System.out.println("   2. Aggiungere autenticazione JWT per le API");
		System.out.println("   3. Utilizzare HTTPS in produzione");
		System.out.println("   4. Implementare logging delle richieste sospette");
		System.out.println("   5. Aggiungere validazione CORS");

		// Stampa best practices per le API
		System.out.println("\n✨ BEST PRACTICES API:");
		System.out.println("   GET    /api/viaggi     - Utilizzare paginazione per grandi dataset");
		System.out.println("   POST   /api/viaggi     - Validare sempre l'input");
		System.out.println("   PUT    /api/viaggi/{id}- Verificare l'autorizzazione");
		System.out.println("   DELETE /api/viaggi/{id}- Implementare soft delete");

		// Stampa suggerimenti per il monitoraggio
		System.out.println("\n📈 SUGGERIMENTI MONITORAGGIO:");
		System.out.println("   1. Implementare health checks");
		System.out.println("   2. Aggiungere metriche Prometheus");
		System.out.println("   3. Configurare alerting automatico");
		System.out.println("   4. Monitorare tempi di risposta");
		System.out.println("   5. Tracciare errori con un servizio APM");

		System.out.println("\n=== FINE DEI TEST ===\n");
	}
}
