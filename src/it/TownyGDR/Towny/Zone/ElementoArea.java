/**
 * 
 */
package it.TownyGDR.Towny.Zone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.TownyGDR;

/*********************************************************************
 * @author: Elsalamander
 * @data: 9 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Descrizione dell'elemento d'area più piccolo che compone poi una 
 * zona, un lotto, ecc.
 * Essa è in realtà la rappresentazione in coordinare di un chunk
 * se esso non è caricato, se è caricato anche il suo puntatore.
 * 
 * Descrizione di un chunk:
 * Sono Element di Volume 16x16x256, le cordinate di essi sono ottenuti
 * dividendo per 16 la coordinata desiderata X o Z e presa la parte intera
 * Esempio: Coordinata X | Coordinata Z | Coordinata Chunk(x:z)
 * 				5		 |		8		|		0 : 0
 * 				-5		 |		-17		|		-1 : -2
 * 				-6		 |      24      |		-1 : 1
 * I chunck a loro volta sono divisi in blocchi con la stessa base
 * ma alti 16 blocchi, poco rilevante, ma da tenere presente per i
 * debug che verranno eseguiti.
 * 
 * Le funzioni di getX e getZ dell'oggetto chuck ritornano le coordinate
 * del chunk, da non confondere con la location.
 * 
 * Salvare le coordinate X e Z secondo un pattern Facile da scrivere e da leggere
 * il pattern per gli elementi d'area è formata principalmente da una lista
 * 	"ConfigurationSectionDato":
 * 								ListAree:
 * 									- X:Z
 * 									- X:Z
 * 									...
 * Creare una sotto sezione ListAree, nel caso si voglia in futuro mettere altre
 * sezioni di configurazione sotto la sezione fornita, la lista conterrà la coppia
 * coordinataX : coordinataZ.
 * 
 * Nota: pre eliminare una voce sul file yml basta fare config.set("path", NULL);
 * se la entry è null rimuove la voce e tutto quello che sta sotto il "path".
 * 
 * 
 *********************************************************************/
public class ElementoArea {
	
	//Variabili oggetto
	private int coord_x;
	private int coord_z;
	
	private Chunk chunk;	//Variabile di cache.
	
	
	/**
	 * Creazione elemento d'area, controlla se il chunk è stato già
	 * precedentemente caricato, se è stato caricato aggiorna la vaiabile di
	 * cache, altrimenti la cache è NULL.
	 * @param x
	 * @param z
	 */
	public ElementoArea(int x, int z) {
		this.coord_x = x;
		this.coord_z = z;
		
		//Controlla se il chuck è carico
		if(TownyGDR.getTownyWorld().isChunkLoaded(x, z)) {
			//Carica nella cache il chunk.
			this.chunk = TownyGDR.getTownyWorld().getChunkAt(x, z);
		}else{
			//Carica il chunk in modo forzato e mettilo nella cache.
			//TownyGDR.getTownyWorld().loadChunk(x, z);
			//this.chunk = TownyGDR.getTownyWorld().getChunkAt(x, z);
		}
	}
	
	/**
	 * Creazione elemento d'area, dato che è passato come
	 * parametro il chucnk non controllo se è stato già caricato
	 * poichè lo considero attualmente caricato.
	 * @param chunk
	 */
	public ElementoArea(Chunk chunk) {
		this.coord_x = chunk.getX();
		this.coord_z = chunk.getZ();
		this.chunk   = chunk;
	}
	
	//
	///**
	// * Costruttore PRIVATO, serve per la prima instanzazione per
	// * il load degli elementi d'area.
	// */
	//private ElementoArea() {
	//	this.coord_x = 0;
	//	this.coord_z = 0;
	//	this.chunk   = null;
	//}
	
	/**
	 * Salva l'elemento d'area.
	 * La classe non ha l'implementazione Salva<T> dato che non si puo
	 * associare un implementazione di tipo LOAD al singolo :(
	 * @param config
	 * @param sec 
	 * @throws IOException
	 */
	public static void save(ConfigurationSection config,  Sector sec, ArrayList<ElementoArea> list) throws IOException {		
		//Creazione List per formalità
		List<String> tmp = new ArrayList<String>();
		for(ElementoArea ele : list) {
			tmp.add(ele.coord_x + ":" + ele.coord_z);
		}
		
		//elimina i dati sotto ListAree
		config.set("Settore" + sec.toString() +".List", null);
		
		//aggiungi alla lista su ListAree
		config.set("Settore" + sec.toString() +".List", tmp);
	}
	
	/**
	 * Comparazione delle coordinate del chunk
	 * @param elementoArea
	 * @return
	 */
	public boolean equals(ElementoArea elementoArea) {
		return ((this.coord_x == elementoArea.getX()) && (this.coord_z == elementoArea.getZ()));
	}

	/**
	 * Ritorna la coordinata Z
	 * @return
	 */
	public int getZ() {
		return this.coord_z;
	}

	/**
	 * Ritorna la coordinata X
	 * @return
	 */
	public int getX() {
		return this.coord_x;
	}
	
	/**
	 * Ritorna il chunk dell'elemento d'area, se il chunk non
	 * era carico, lo carica.
	 * @return
	 */
	public Chunk getChunk() {
		if(this.chunk != null) return this.chunk;
		
		//Carica il chunk.
		TownyGDR.getTownyWorld().loadChunk(this.coord_x, this.coord_z);
		this.chunk = TownyGDR.getTownyWorld().getChunkAt(this.coord_x, this.coord_z);
		return this.chunk;
	}

	/**
	 * Carica in blocco tutti gli elementi d'area presenti sotto la configurazione data
	 * @param settori 
	 * @param configurationSection
	 * @return
	 */
	public static ArrayList<ElementoArea> loadData(ConfigurationSection config) {
		ArrayList<ElementoArea> arr = new ArrayList<ElementoArea>();
		
		List<String> list = config.getStringList("List");
		
		//Variabile Scanner
		Scanner scan = null;
		int x = 0;
		int z = 0;
		for(String str : list) {
			scan = new Scanner(str);
			scan.useDelimiter(":");
			
			try {
				x = scan.nextInt();
				z = scan.nextInt();
			}catch(InputMismatchException e) {
				//e.printStackTrace();
				continue;
			}
			ElementoArea ele = new ElementoArea(x, z);
			arr.add(ele);
		}
		if(scan != null) scan.close();
		return arr;
	}

}
