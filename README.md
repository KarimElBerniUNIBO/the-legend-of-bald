# The Legend of Bald - Progetto OOP UNIBO

## Descrizione del Gioco

**The Legend of Bald** è un videogioco sviluppato come progetto d’esame 
per il corso di **Programmazione ad Oggetti (OOP)** 
presso l’Università di Bologna, sede di Cesena. Il gioco si basa sul completamento
di diversi livelli, con mappe differenti, dove il personaggio principale è Bald, un
combattente che deve sconfiggere una serie di nemici ed evitare diverse trappole, aiutandosi
con armi speciali e pozioni.
Il gioco è studiato per essere una **run** cronometrata nello stesso stile dei giochi *Arcade*, dove ogni morte corrisponde al reset della partita, non esiste il concetto di salvataggio. Ogni nuova *run* è diversa da quelle precedenti.

## Tecnologie Utilizzate

- **Linguaggio:** Java
- **Build Tool:** Gradle

## Struttura del Progetto

- **Model:** logica di gioco, gestione entità (bald, nemici, boss), collisioni con mappa, IA dei nemici.
- **View e Controller:** interfaccia grafica e chiamate al model.

## Gameplay

All'avvio del gioco si presenta il menu principale dove è possibile: iniziare la partita, modificare le impostazioni (visive, audio e gestione dei tasti), visualizzare la leaderboard delle migliori run **locali** o infine uscire dal gioco.

## Caso Avvio
All'avvio è **obbligatorio** scegliere un nickname per l'eventuale vittoria e salvataggio della run. Dopo la scelta del nickname il gioco parte al di fuori di un castello, dove il player può muoversi **solo orizzontalmente** in attesa che esso entri. All'entrata il player si ritrova una serie di dinamiche pericolose dove può scegliere se combattere o ignorare i nemici passando direttamente al prossimo piano indicato dalla parete laterale bucata. Il giocatore inoltre può scegliere se aprire le casse passandoci sopra o comprare nuove armi all'interno dello shop. La vittoria consiste nello sconfiggere il boss situato nell'ultimo piano (il terzo).

## Shop
Se si vuole comprare un nuovo oggetto è necessario collezionare le monete dalle casse.

## Apertura Cassa
Le casse possono rilasciare: pozioni di vita, pozioni di forza e monete.

## Vittoria / Sconfitta
Solo in caso di vittoria la run viene salvata localmente e visualizzata nella leaderboard, in caso di sconfitta l'utente verrà riportato al menu principale.

## Impostazioni
- Video: L'utente può scegliere il formato di finestra del gioco, mostrare fps e timer.
- Audio: L'utente può scegliere il volume dell'audio principale, *l'audio musicale è ancora sviluppo*
- Controlli: L'utente può scegliere la configurazione dei tasti (**NB: Per modificare un tasto è necessario cliccare il bottone e premere il nuovo tasto da assegnare. Se si vuole annullare la procedura di modifica iniziata, basterà solamente uscire con il mouse dal bottone.**).

## Leaderboard
In questa schermata verranno visualizzate le migliori 10 run **locali**.

## Caso Quit
L'utente può scegliere se usare il bottone di chiusura delle finestra in alto a destra o in caso di fullscreen usare il bottone del menu principale.


## Sviluppatori

- **Karim El Berni** – [karim.elberni@studio.unibo.it](mailto:karim.elberni@studio.unibo.it)
- **Luca Dellasantina** – [luca.dellasantina@studio.unibo.it](mailto:luca.dellasantina@studio.unibo.it)
- **Davide Magyari** – [davideandrea.magyari@studio.unibo.it](mailto:davideandrea.magyari@studio.unibo.it)
- **Vincent Rey Ramos** – [vincentrey.ramos@studio.unibo.it](mailto:vincentrey.ramos@studio.unibo.it)

## Note

Il progetto è stato sviluppato per l’esame di OOP (A.A. 2024/25).