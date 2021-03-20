/**
 * 
 */
package it.TownyGDR.Gui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import it.TownyGDR.PlayerData.PlayerData;

/*********************************************************************
 * @author: Elsalamander
 * @data: 20 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Per facilitare la creazione delle gui e varie iterazioni.
 * 
 * Dato che ci posso essere gui la cui c'è possibilità di tornare in dietro
 * verrà tenuta traccia delle gui che ha usato il player.
 * 
 * Verrà tenuta traccia anche la coppia UUID-Inventory(attualmente aprti)
 * per identificare la gui correntemente aperta dal player senza effettuare
 * strane ricerche dato che il titolo può contenere di TAG e quindi può risultare
 * indefinito.
 * 
 * Ogni classe gui dovra avere anche la parte per le sue iterazioni!
 * Implementato con Listener e almento gli eventi seguenti:
 * - onInventoryClickEvent
 * - onInventoryDragEvent
 * - onInventoryCloseEvent
 * - onInventoryMoveEvent
 * 
 * Impratica tutte le gui verranno registrate in modo statico qua tramite funzione
 * la struttura della gui e le sue iterazioni sono demandate alle singole classi
 * che si occupperanno della gui.
 * (Scartata l'idea di fare una gui configurabile poichè molto lungho e difficile da implementare
 *  e dato che è più facile realizzarli singolarmente anche se un po più laborioso, farlo generale
 *  non garantisce una maggiore fluidità nella stusura di gui...)
 *  
 * Come già detto le funzioni sono demandate alle sottoclassi, esse devono rispettare
 * la interfaccia data da questa classe astratta!
 * Funzioni:
 * - void open(PlayerData p); 	//Lo passo con il playerData dato che ho molte più informazioni
 * 						 		//riguardanti il player in sè.
 * - String getTitle(PlayerData p);	//Ritorna il titolo che avrebba la GUI abbinato al quel player
 * 									//dato che il titolo può dipendere da un TAG
 * - void register(PlayerData p, GuiGeneral gui);	//Metti nella cache
 * - void unregister(PlayerData p, GuiGeneral gui);	//Rimuovi dalla cache
 * 
 * L'oggetto Gui extends GuiGeneral è un oggetto che non viene creato per ogni
 * singolo player! Al suo interno si crea l'oggetto inventory che poi va mostrato
 * al player, NON fare confusione sul funzionamento della classe che fa da pattern.
 * 
 * 
 *********************************************************************/
public abstract class GuiGeneral {
	
	//Variabili statiche
	protected static ArrayList<GuiGeneral> MapGui = new ArrayList<GuiGeneral>(); 
	protected static Map<UUID, ArrayDeque<GuiGeneral>> Story = new TreeMap<UUID, ArrayDeque<GuiGeneral>>();
	
	//Inserisci tutte le gui
	static {
		//MapGui.add(new GuiExample());
	}
	
	//Variabili oggetto
	private int id; //id gui
	private static int ID_ = 0;;
	protected String name; //nome gui

	/**
	 * Costruttore per la classe astratta
	 * Le altre classi non devono avere parametro!
	 * @param name
	 */
	protected GuiGeneral(int id,  String name) {
		this.id = ID_ + 1;
		ID_++;
		this.name = name;
	}
	
	/**
	 * Aprire la gui al player pd
	 * @param pd
	 */
	public abstract void open(PlayerData pd);
	
	/**
	 * Ritorna il titolo della gui, con eventuale sostituzione di TAG
	 * @param pd
	 * @return
	 */
	public abstract String getTitle(PlayerData pd);
	
	/**
	 * Metti in cache la gui
	 * @param p
	 * @param gui
	 */
	protected abstract void register(PlayerData p, GuiGeneral gui);
	
	/**
	 * Rimuovi dalla cache la gui
	 * @param p
	 * @param gui
	 */
	protected abstract void unregister(PlayerData p, GuiGeneral gui);
	

}
