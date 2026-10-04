# Web Information Technologies Project

## English Version

### Overview
This repository contains a web application project split into two distinct versions. Both versions deliver the exact same functionalities but are built using different technological approaches. You can reference the main project files under vcristiano2000/progettotecnologieinoformaticheweb.

### Versions
* **Version 1 (Pure HTML & Java):** The front-end is built using pure HTML, while the back-end is powered by Java and the Spring framework. It relies on server-side navigation to route users between views.
* **Version 2 (Client-Side Scripting):** This version implements dynamic client-side scripting using JavaScript for the front-end interface. It dynamically fetches data like courses, exam sessions, and grades from the back-end.

### Core Functionalities
The application serves as a university exam management system, featuring distinct capabilities based on user roles:

* **Authentication & Security:** Secure login and logout functionalities, protected by filters that ensure users are logged in and correctly routed based on their role (Professor or Student).
* **Professor Features:**
  * View assigned courses and related exam sessions (appelli).
  * View lists of enrolled students.
  * Insert grades for students, including a multiple-insertion feature.
  * Modify previously entered grades.
  * Publish exam results.
  * Finalize and formally record exam reports (verbalizza).
* **Student Features:**
  * Browse enrolled courses and upcoming exam sessions.
  * View published grades and exam outcomes.
  * Choose to reject a recorded grade (rifiuta voto).

### Project Structure
The repository includes standard Java web application components such as Controllers, Data Access Objects (DAOs), Beans, and Filters for user authentication and session management.

---

## Versione Italiana

### Panoramica
Questo repository contiene un progetto per un'applicazione web suddiviso in due versioni distinte. Entrambe le versioni offrono le stesse identiche funzionalità ma sono sviluppate utilizzando approcci tecnologici differenti. Puoi fare riferimento ai file principali del progetto sotto vcristiano2000/progettotecnologieinoformaticheweb.

### Versioni
* **Versione 1 (Puro HTML e Java):** Il front-end è realizzato in puro HTML, mentre il back-end è gestito tramite Java e il framework Spring. Si affida alla navigazione lato server per indirizzare gli utenti tra le varie schermate.
* **Versione 2 (Scripting Lato Client):** Questa versione implementa un'interfaccia dinamica utilizzando JavaScript per lo scripting lato client. Recupera dinamicamente dati come corsi, appelli e voti dal back-end.

### Funzionalità Principali
L'applicazione funge da sistema di gestione degli esami universitari, offrendo capacità distinte in base al ruolo dell'utente:

* **Autenticazione e Sicurezza:** Funzionalità di login e logout sicure, protette da filtri che verificano l'accesso e indirizzano correttamente gli utenti in base al loro ruolo (Professore o Studente).
* **Funzionalità Professore:**
  * Visualizzazione dei corsi assegnati e dei relativi appelli.
  * Visualizzazione degli studenti iscritti agli esami.
  * Inserimento dei voti, inclusa una funzione per l'inserimento multiplo.
  * Modifica dei voti precedentemente inseriti.
  * Pubblicazione degli esiti degli esami.
  * Chiusura e verbalizzazione ufficiale dei voti (verbalizza).
* **Funzionalità Studente:**
  * Consultazione dei corsi e degli appelli disponibili.
  * Visualizzazione dei voti pubblicati e degli esiti.
  * Possibilità di rifiutare un voto registrato (rifiuta voto).

### Struttura del Progetto
Il repository include i componenti standard di un'applicazione web Java, come Controller, Data Access Object (DAO), Bean e Filtri per l'autenticazione degli utenti e la gestione delle sessioni.
