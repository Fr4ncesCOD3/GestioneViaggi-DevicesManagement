package com.example.demo.shell;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ShellCommandGroup;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;
import com.example.demo.validation.DataValidator;
import com.example.demo.validation.ValidationException;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.Availability;
import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeParseException;

@ShellComponent
@ShellCommandGroup("Gestione Viaggi Aziendali")
public class GestioneViaggiShell {

    @Autowired
    private ViaggioRepository viaggioRepository;
    
    @Autowired
    private DipendenteRepository dipendenteRepository;
    
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    private boolean isInitialized = false;

    @ShellMethod(key = "inizia", value = "Inizia a utilizzare il programma")
    public String inizia() {
        isInitialized = true;
        return mostraMenu();
    }

    @ShellMethodAvailability({"menu", "lista-viaggi", "nuovo-viaggio", "completa-viaggio", 
                             "lista-dipendenti", "nuovo-dipendente", "prenota", "lista-prenotazioni"})
    public Availability checkAvailability() {
        return isInitialized 
            ? Availability.available()
            : Availability.unavailable("Esegui prima il comando 'inizia' per utilizzare il programma");
    }

    @ShellMethod(key = "menu", value = "Mostra il menu principale")
    public String mostraMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("\n╔════════════════════════════════════╗\n");
        menu.append("║     GESTIONE VIAGGI AZIENDALI      ║\n");
        menu.append("╚════════════════════════════════════╝\n\n");
        menu.append("Comandi disponibili:\n\n");
        menu.append("VIAGGI:\n");
        menu.append("  📋 lista-viaggi          - Visualizza tutti i viaggi\n");
        menu.append("  ➕ nuovo-viaggio         - Crea un nuovo viaggio\n");
        menu.append("  ✓  completa-viaggio      - Segna un viaggio come completato\n\n");
        menu.append("DIPENDENTI:\n");
        menu.append("  👥 lista-dipendenti      - Visualizza tutti i dipendenti\n");
        menu.append("  👤 nuovo-dipendente      - Registra un nuovo dipendente\n\n");
        menu.append("PRENOTAZIONI:\n");
        menu.append("  🎫 prenota               - Crea una prenotazione viaggio\n");
        menu.append("  📋 lista-prenotazioni    - Visualizza tutte le prenotazioni\n\n");
        menu.append("❓ Per maggiori dettagli su un comando, digita: help nome-comando\n");
        menu.append("❌ Per uscire dal programma, digita: exit\n");
        return menu.toString();
    }

    @ShellMethod(key = "lista-viaggi", value = "Mostra tutti i viaggi")
    public String listaViaggi() {
        List<Viaggio> viaggi = viaggioRepository.findAll();
        if (viaggi.isEmpty()) {
            return "Nessun viaggio trovato.\nUsa 'nuovo-viaggio' per crearne uno.";
        }
        
        StringBuilder result = new StringBuilder("=== ELENCO VIAGGI ===\n\n");
        for (Viaggio v : viaggi) {
            result.append(String.format("ID: %d\nDestinazione: %s\nData: %s\nStato: %s\n-------------------\n", 
                v.getId(), v.getDestinazione(), v.getData(), v.getStato()));
        }
        return result.toString();
    }

    @ShellMethod(key = "nuovo-viaggio", value = "Crea un nuovo viaggio")
    public String nuovoViaggio(
            @ShellOption(help = "Destinazione del viaggio") String destinazione,
            @ShellOption(help = "Data del viaggio (formato: YYYY-MM-DD)") String data) {
        try {
            LocalDate dataViaggio = LocalDate.parse(data);
            DataValidator.validateViaggio(destinazione, dataViaggio);

            Viaggio viaggio = new Viaggio();
            viaggio.setDestinazione(destinazione);
            viaggio.setData(dataViaggio);
            viaggio.setStato(StatoViaggio.IN_PROGRAMMA);
            
            viaggio = viaggioRepository.save(viaggio);
            return formatSuccessMessage("Viaggio creato con successo!", 
                String.format("ID: %d\nDestinazione: %s\nData: %s", 
                    viaggio.getId(), viaggio.getDestinazione(), viaggio.getData()));
        } catch (DateTimeParseException e) {
            return formatErrorMessage("Data non valida", "Usa il formato YYYY-MM-DD (es: 2024-04-01)");
        } catch (ValidationException e) {
            return formatErrorMessage("Errore di validazione", e.getMessage());
        } catch (Exception e) {
            return formatErrorMessage("Errore imprevisto", e.getMessage());
        }
    }

    @ShellMethod(key = "lista-prenotazioni", value = "Mostra tutte le prenotazioni")
    public String listaPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        if (prenotazioni.isEmpty()) {
            return "Nessuna prenotazione trovata.\nUsa 'prenota' per crearne una.";
        }
        
        StringBuilder result = new StringBuilder("=== ELENCO PRENOTAZIONI ===\n\n");
        for (Prenotazione p : prenotazioni) {
            result.append(String.format(
                "ID: %d\nDipendente: %s %s\nViaggio: %s (%s)\nData: %s\nNote: %s\n-------------------\n",
                p.getId(), 
                p.getDipendente().getNome(),
                p.getDipendente().getCognome(),
                p.getViaggio().getDestinazione(),
                p.getViaggio().getData(),
                p.getDataRichiesta(),
                p.getNote() != null ? p.getNote() : "Nessuna nota"
            ));
        }
        return result.toString();
    }

    @ShellMethod(key = "nuovo-dipendente", value = "Registra un nuovo dipendente")
    public String nuovoDipendente(
            @ShellOption(help = "Username del dipendente") String username,
            @ShellOption(help = "Nome del dipendente") String nome,
            @ShellOption(help = "Cognome del dipendente") String cognome,
            @ShellOption(help = "Email del dipendente") String email) {
        try {
            Dipendente dipendente = new Dipendente();
            dipendente.setUsername(username);
            dipendente.setNome(nome);
            dipendente.setCognome(cognome);
            dipendente.setEmail(email);
            
            dipendente = dipendenteRepository.save(dipendente);
            return String.format("✓ Dipendente registrato con successo!\nID: %d\nNome: %s %s\nEmail: %s", 
                dipendente.getId(), dipendente.getNome(), dipendente.getCognome(), dipendente.getEmail());
        } catch (Exception e) {
            return "✗ Errore nella registrazione del dipendente: " + e.getMessage();
        }
    }

    @ShellMethod(key = "lista-dipendenti", value = "Mostra tutti i dipendenti")
    public String listaDipendenti() {
        List<Dipendente> dipendenti = dipendenteRepository.findAll();
        if (dipendenti.isEmpty()) {
            return "Nessun dipendente trovato.\nUsa 'nuovo-dipendente' per registrarne uno.";
        }
        
        StringBuilder result = new StringBuilder("=== ELENCO DIPENDENTI ===\n\n");
        for (Dipendente d : dipendenti) {
            result.append(String.format(
                "ID: %d\nNome: %s %s\nUsername: %s\nEmail: %s\n-------------------\n",
                d.getId(), d.getNome(), d.getCognome(), d.getUsername(), d.getEmail()));
        }
        return result.toString();
    }

    @ShellMethod(key = "prenota", value = "Crea una prenotazione viaggio")
    public String prenotaViaggio(
            @ShellOption(help = "ID del viaggio") Long idViaggio,
            @ShellOption(help = "ID del dipendente") Long idDipendente,
            @ShellOption(help = "Note o preferenze (opzionale)", defaultValue = "") String note) {
        try {
            Viaggio viaggio = viaggioRepository.findById(idViaggio)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato"));
                
            Dipendente dipendente = dipendenteRepository.findById(idDipendente)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato"));
                
            List<Prenotazione> prenotazioniEsistenti = 
                prenotazioneRepository.findByDipendenteAndDataRichiesta(dipendente, viaggio.getData());
                
            if (!prenotazioniEsistenti.isEmpty()) {
                return "✗ Il dipendente ha già una prenotazione per questa data";
            }

            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setViaggio(viaggio);
            prenotazione.setDipendente(dipendente);
            prenotazione.setDataRichiesta(viaggio.getData());
            prenotazione.setNote(note);
            
            prenotazione = prenotazioneRepository.save(prenotazione);
            return String.format("✓ Prenotazione creata con successo!\nID: %d\nDipendente: %s %s\nViaggio: %s (%s)", 
                prenotazione.getId(), 
                dipendente.getNome(), 
                dipendente.getCognome(),
                viaggio.getDestinazione(),
                viaggio.getData());
        } catch (Exception e) {
            return "✗ Errore nella creazione della prenotazione: " + e.getMessage();
        }
    }

    @ShellMethod(key = "completa-viaggio", value = "Segna un viaggio come completato")
    public String completaViaggio(@ShellOption(help = "ID del viaggio") Long idViaggio) {
        try {
            Viaggio viaggio = viaggioRepository.findById(idViaggio)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato"));
                
            viaggio.setStato(StatoViaggio.COMPLETATO);
            viaggioRepository.save(viaggio);
            return String.format("✓ Viaggio %d (%s) segnato come completato", 
                viaggio.getId(), viaggio.getDestinazione());
        } catch (Exception e) {
            return "✗ Errore nel completamento del viaggio: " + e.getMessage();
        }
    }

    private String formatSuccessMessage(String title, String content) {
        return String.format("""
            ✅ %s
            ──────────────────────────
            %s
            ──────────────────────────
            """, title, content);
    }

    private String formatErrorMessage(String title, String content) {
        return String.format("""
            ❌ %s
            ──────────────────────────
            %s
            ──────────────────────────
            """, title, content);
    }

    private String formatListHeader(String title) {
        return String.format("""
            ╔════════════════════════════════════╗
            ║ %s
            ╚════════════════════════════════════╝
            """, centerText(title, 36));
    }

    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
}

@Component
class CustomPromptProvider implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("gestione-viaggi:> ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }
} 