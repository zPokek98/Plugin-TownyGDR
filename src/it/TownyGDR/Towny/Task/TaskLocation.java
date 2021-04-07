/**
 * 
 */
package it.TownyGDR.Towny.Task;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import it.TownyGDR.TownyGDR;
import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.Luogo;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Event.EventEnterInCity;
import it.TownyGDR.Towny.City.Event.EventExitInCity;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Sector;
import it.TownyGDR.Towny.Zone.Zona;

/*********************************************************************
 * @author: Elsalamander
 * @data: 30 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Task ASINCRONO, che controlla la posizione di un player alla volta
 * iterando una mappa in cui li contiene tutti quelli presenti.
 * 
 * Associato al player ci sarà un oggetto di supporto che conterrà
 * - Settore
 * - Elemento d'area
 * - Se è all'interno di una fazione, il puntatore di esso, altrimenti null.
 * 	 Serve non solo per avere un get Rapido della fazione ma anche per
 *   capire se è entrato in una fazione quindi da null -> pointer(!=null)
 *   per inviare il messaggio che è entrato in un'area definita.
 *   
 * 
 * La mappa che conterrà tutti i player verrà aggiornata ogni qual volta
 * un player joina o quitta, per facilità e per aver maggior informazioni
 * l'oggetto chiave sarà il playerData.
 * 
 * 
 * Dato che la mappa risiede su un thread distaccato devo garantire
 * un ADT con funzioni non sincrone l'unico ADT che fa da mappa
 * con funzioni non sincrone è ConcurrentHashMap
 * 
 * La nostra prima scelta dovrebbe sempre essere quella di utilizzare 
 * la ConcurrentHashMapclasse se desideriamo utilizzare una mappa in un 
 * ambiente simultaneo. ConcurrentHashMap supportare l'accesso simultaneo 
 * alle sue coppie chiave-valore in base alla progettazione. Non è necessario 
 * eseguire ulteriori modifiche al codice per abilitare la sincronizzazione 
 * sulla mappa.
 * Si prega di notare che l' iteratore ottenuto da ConcurrentHashMapnon lancia 
 * ConcurrentModificationException. Tuttavia, gli iteratori sono progettati per 
 * essere utilizzati da un solo thread alla volta. Significa che ogni iteratore 
 * che otteniamo da ConcurrentHashMap è progettato per essere utilizzato da un
 * singolo thread e non deve essere passato.
 * 
 * Se lo facciamo, non vi è alcuna garanzia che un thread vedrà le modifiche alla 
 * mappa eseguite dall'altro thread (senza ottenere un nuovo iteratore dalla mappa). 
 * È garantito che l' iteratore rifletta lo stato della mappa al momento della sua 
 * creazione .
 * 
 * 	 Iterator<Integer> itr = concurrHashMap.keySet().iterator();
 *        synchronized (concurrHashMap) 
 *        {
 *            while(itr.hasNext()) {
 *                //...
 *            }
 *        }
 * 
 * Anche HashMap sincronizzato funziona in modo molto simile a ConcurrentHashMap,
 * con poche differenze.
 * SynchronizedHashMapconsente a un solo thread di eseguire operazioni di 
 * lettura/scrittura alla volta perché tutti i suoi metodi sono dichiarati
 * sincronizzati. ConcurrentHashMapconsente a più thread di lavorare in modo
 * indipendente su diversi segmenti nella mappa. Ciò consente un maggiore grado
 * di concorrenza in ConcurrentHashMap e quindi migliora le prestazioni dell'applicazione
 * nel suo complesso.
 * 
 * Gli iteratori di entrambe le classi dovrebbero essere usati all'interno del
 * synchronizedblocco, ma l'iteratore di SynchronizedHashMap è veloce . Gli iteratori
 * ConcurrentHashMap non sono veloci.
 * 
 * 
 * 
 * Differenza tra Synchronized HashMap e ConcurrentHashMap:
 * Identifichiamo alcune differenze tra le due versioni delle mappe in modo
 * da poter decidere quale scegliere in quale condizione:
 * 
 * 1 - Più thread possono aggiungere / rimuovere coppie chiave-valore da ConcurrentHashMap,
 *     mentre solo un thread può apportare modifiche in caso di SynchronizedHashMap. Ciò si
 *     traduce in un maggiore grado di concorrenza in ConcurrentHashMap.
 *     
 * 2 - Non è necessario bloccare la mappa per leggere un valore in ConcurrentHashMap.
 *     Un'operazione di recupero restituirà il valore inserito dall'operazione di
 *     inserimento completata più di recente. È necessario un blocco anche per l'operazione
 *     di lettura in SynchronizedHashMap.
 *     
 * 3 - ConcurrentHashMap non genera un ConcurrentModificationExceptionse un thread tenta di
 *     modificarlo mentre un altro sta iterando su di esso. L'iteratore riflette lo stato della
 *     mappa al momento della sua creazione. SynchronizedHashMap restituisce Iterator, che non riesce
 *     rapidamente in caso di modifiche simultanee.
 * 
 * 
 * Scelgo per quello che devo fare ConcurrentHashMap !
 * Ciò comporta che:
 * - La chiave, ovvero PlayerData deve implementare Comperable
 * 	 e realizzare in modo efficente un hashCode.
 *   (Idea di base usare l'hashCode dell'UUID)
 *   
 * 
 * ----------------------------------------------------- /\ Scelta della mappatura playerData e posizione /\
 * 
 * Ora dobbiamo però abbiamo un problema:
 * La lista dei player o meglio la lista del playerData deve essere gestita in modo da essere
 * anchessa thread-safe, quindi la cambio da ArrayList<PlayerData> a CopyOnWriteArrayList<PlayerData>
 * 
 * Questo oggetto mi garantisce letture/scrittura da thread diversi(asincroni)
 * 
 * Per l'iteratore è analogo alla ConcurrentHashMap.
 * DEVO METTERLO DENTRO UN BLOCCO syncronized
 * 
 * 
 * ----------------------------------------------------- /\ Scelta della lista cache playerData /\
 * 
 * Ora manca un ultimo problema: la chiamata ad eventi è una funzione sincrona al Task principale
 * Per avviarlo si farà riferimento alla Scheduler principale con:
 * 
 * TownyGDR.getInstance().getServer().getScheduler().runTask(TownyGDR.getInstance(), 
 *													() -> TownyGDR.getInstance().getServer().getPluginManager()
 *													.callEvent( "Evento da lanciare" ));
 * 
 * 
 * 
 * La scelta di fare un Task Asincrono non ha solo vantaggi, infatti
 * non si potrà usare quasi la totalità delle funzioni del server
 * dovrebbero essere ammesse funzioni di Get
 * 
 * Testare:
 * - PlayerPointer.getLocation();
 * 
 * Creare le funzioni:
 * - Sector GetSector(PlayerData pd);
 * - ElementoArea GetElementoArea(PlayerData pd);
 * - Fazione GetFazione(PlayerData pd);
 * 
 * - void addPlayerData(PlayerData pd);
 * - void removePlayerData(PlayerData pd);
 * 
 * - Map<PlayerData, Data> getMap();
 * 
 * 
 *********************************************************************/
public class TaskLocation implements Runnable{
	
	protected static ConcurrentHashMap<PlayerData, Posiction> map;
	private boolean stop;
	
	/**
	 * Costruttore del Task, inizializzazione
	 */
	public TaskLocation() {
		map = new ConcurrentHashMap<PlayerData, Posiction>();
		this.stop = false;
		
		//upDate();
	}

	/**
	 * Aggiorna la lista dei player
	 */
	private void upDate() {
		//Controlla che la lista è della stessa lunghezza
		CopyOnWriteArrayList<PlayerData> list = PlayerData.getListPlayerData();
		
		Iterator<PlayerData> iter = list.iterator();
		
		//Check 1: tutti i player presenti sulla cache di playerData sono presenti come
		//chiave nella mappa?
		synchronized(iter) {
			while(iter.hasNext())
			{
				PlayerData pd = iter.next();
				Player p = pd.getPlayer();
				if(p == null ) {
					continue;
				}
				Location loc = p.getLocation();
				Chunk ck = loc.getChunk();
				Sector s = Sector.getSectorByLocation(ck);
				ElementoArea e = new ElementoArea(ck);
				Zona zona = Zona.getZonaByLocation(ck.getX(), ck.getZ());
				if(zona != null) {
					map.putIfAbsent(pd, new Posiction(s, e, zona.getLuogo(), loc));
				}else{
					map.putIfAbsent(pd, new Posiction(s, e, null, loc));
				}
			 }
		 }
		
		//Check 1: tutti i player presenti sulla mappa sono anche sulla cache?
		//prima di ciclare player non presenti.
		if(map.keySet().size() != list.size()) {
			iter = map.keySet().iterator();
			synchronized(map) {
				while(iter.hasNext()) {
					PlayerData pd = iter.next();
					if(!list.contains(pd)) {
						//la mappa ha un player non presente nella cache
						//togliolo dalla mappa
						map.remove(pd);
					}
				}
			}
		}
		
	}
	
	@Override
	public void run() {
		while(!stop) {
			Iterator<PlayerData> iter = map.keySet().iterator();
			
			//Sincronizza per l'iteratore
			synchronized(map) {
				//Oggetto Title, da mostrare poi ai player
				//Title tit = new Title();
				
				//Per tutte le chiavi della mappa, quindi per tutti i player
				while(iter.hasNext()) {
					//prendi il player
					PlayerData pd = iter.next();
					
					//Ottieni puntatore player
					Player p = pd.getPlayer();

					if(p == null ) {
						continue;
					}
					
					//controlla che è online
					if(!p.isOnline()) {
						//non è online salta questo ciclo.
						continue;
					}
					
					//check di sicurezza
					if(map.get(pd) == null) {
						map.remove(pd);
						continue;
					}
					
					//Ottieni i dati della posizione del player
					
					Location loc = p.getLocation();
					Chunk ck = loc.getChunk();
					Sector s = Sector.getSectorByLocation(ck);
					ElementoArea e = new ElementoArea(ck);
					Zona zona = Zona.getZonaByLocation(ck.getX(), ck.getZ());
					Luogo luogo = zona == null ? null : zona.getLuogo();
					
					Posiction pos = new Posiction(s, e, luogo, loc);
					
					//La zona è cambiata?(o meglio è uscito o entrato in un'altra citta?)
					if((map.get(pd).getLuogo() == null && luogo != null) ||
					   (map.get(pd).getLuogo() != null && luogo != null && !map.get(pd).getLuogo().equals(luogo))) {
						//è entrato in una citta
						//tit.send(pd.getPlayer(), faz.getNome(), "", 1, 3, 1);
						if(luogo instanceof City) {
							//Bukkit.getPluginManager().callEvent(new EventEnterInCity(pd, (City)luogo, pos));
							TownyGDR.getInstance().getServer().getScheduler().runTask(TownyGDR.getInstance(), 
													() -> TownyGDR.getInstance().getServer().getPluginManager()
													.callEvent(new EventEnterInCity(pd, (City)luogo, pos)));
						}
						
					}else if(map.get(pd).getLuogo() != null && luogo == null){
						//è uscito da una citta
						//tit.send(pd.getPlayer(), "WildNess", "", 1, 3, 1);
						if(map.get(pd).getLuogo() instanceof City) {
							//Bukkit.getPluginManager().callEvent(new EventExitInCity(pd, (City)(map.get(pd).getLuogo()), pos));
							TownyGDR.getInstance().getServer().getScheduler().runTask(TownyGDR.getInstance(), 
									() -> TownyGDR.getInstance().getServer().getPluginManager()
									.callEvent(new EventExitInCity(pd, (City)(map.get(pd).getLuogo()), pos)));
						}
					}
					
					//Si è mosso di un millimetro?
					Location tmpLocation = map.get(pd).getLocation();
					if(!loc.equals(tmpLocation) || loc.getPitch() != tmpLocation.getPitch() || loc.getYaw() != tmpLocation.getYaw()) {
						this.movePlayer(pd, map.get(pd));
					}
					
					//Aggiorna la Entry assegnata al player
					map.put(pd, pos);
				}
			}
			this.upDate();
		}
		
	}

	public void stop() {
		this.stop = true;
	}
	
	
	private void movePlayer(PlayerData pd, Posiction posiction) {
		//Si è mosso il player fai qualcosa?
		
		
		//Controllo se il player è apposto
		this.firstJoinCheck(pd, posiction);
	}
	
	private void firstJoinCheck(PlayerData pd, Posiction posiction) {
		// eseguo un controllo per i player che nella selezione etnia e casata non hanno completato...
		// Controlla se è nella lista
		CopyOnWriteArrayList<PlayerData> listJoin = PlayerData.getListFirstJoin();
		
		synchronized(listJoin) {
			if(listJoin.contains(pd)) {
				//è uno che si è mosso è deve completare il firstJoin
				//apri la gui di selezione
				//quale selezione?
				if(pd.getEtnia() == null) {
					TownyGDR.getInstance().getServer().getScheduler().runTask(TownyGDR.getInstance(), 
													() -> pd.openSelectionFirstJoin());
				}else{
					TownyGDR.getInstance().getServer().getScheduler().runTask(TownyGDR.getInstance(), 
													() -> pd.openSelectionFirstJoin(pd.getEtnia()));
				}
			}
		}
	}
	
	
	
}