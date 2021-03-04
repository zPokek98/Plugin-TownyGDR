/**
 * 
 */
package it.TownyGDR.Towny.City.Membri;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.Towny.City.Membri.Ruoli.Cittadino;
import it.TownyGDR.Towny.City.Membri.Ruoli.Sindaco;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe astratta per generalizzare il membro della città.
 * Si usa come sempre l'UUID dei player!
 * Tiene conto anche del suo titolo all'interno della città.
 * (Che è diverso da come si fa con la nazione)
 * 
 *********************************************************************/
public abstract class Membro implements Salva<ConfigurationSection>{
	
	//Identificatore per il player, poichè potrebbe essere offline
	private UUID uuid;
	private ArrayList<MembroType> type; //Ruoli nella città, può avere più ruoli.
	
	/**
	 * Costruttore membro, con assegnazione uuid del player e il ruolo
	 * @param uuid
	 * @param ruolo
	 */
	public Membro(UUID uuid, MembroType ruolo) {
		this.type = new ArrayList<MembroType>();
		this.type.add(ruolo);
		this.uuid = uuid;
	}
	
	/**
	 * Ritorna l'uuid di questo membro
	 * @return
	 */
	public UUID getUUID() {
		return this.uuid;
	}
	
	/**
	 * Ritorna i ruoli del membro.
	 * @return
	 */
	public ArrayList<MembroType> getType(){
		return this.type;
	}
	
	@Override
	public void save(ConfigurationSection config) {
		//devo salvare il singolo membro sotto la sezione/ruolo assegnatosi
		for(MembroType ty : this.type) {
			config.set(ty.toString(), this.uuid);
		}
	}

	@Override
	/**
	 * Load un po' fasullo ma facciamoli fare qualcosa xD
	 * Sapendo almeno l'uuid, carica i ruoli del membro dato il file di salvataggio.
	 */
	public void load(ConfigurationSection config) {
		//Array che contiene la lista dei ruoli presenti sotto "Membri" nel file di salvtaggio
		String[] ruoli = config.getKeys(false).stream().toArray(String[] :: new);
		for(String str : ruoli) {
			//controlla se c'è l'uuid
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) config.getList(str);
			if(list.contains(this.uuid.toString())) {
				this.type.add(MembroType.valueOf(str));
			}
		}
	}
	
	/**
	 * Carica tutti i membri della città sapendo il suo salvataggio
	 * e ritorna l'arrayList.
	 * @param configurationSection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Membro> loadMembri(ConfigurationSection config) {
		ArrayList<Membro> mem = new ArrayList<Membro>();
		//Array che contiene la lista dei ruoli presenti sotto "Membri" nel file di salvtaggio
		String[] ruoli = config.getKeys(false).stream().toArray(String[] :: new);
		for(String str : ruoli) {
			List<String> list = (List<String>) config.getList(str);
			for(String strUuid : list) {
				switch(MembroType.valueOf(str)) {
					case Sindaco:{
						aggiungi(mem, MembroType.Sindaco, new Sindaco(UUID.fromString(strUuid)));
					}break;
						
					case Cittadino:{
						aggiungi(mem, MembroType.Cittadino, new Cittadino(UUID.fromString(strUuid)));
					}break;
					case ND:
					default:
						break;
				}
			}
		}
		return mem;
	}

	/**
	 * Funzione di supporto per il caricamento dei dati dei membri per la città
	 * @param mem
	 * @param ruolo 
	 * @param sindaco
	 */
	private static void aggiungi(ArrayList<Membro> mem, MembroType ruolo, Membro agg) {
		//Controlla se è gia stato caricato con un altro ruolo
		for(Membro tmp : mem) {
			if(tmp.getUUID().equals(agg.getUUID())) {
				//esiste gia, basta aggiungere il ruolo
				if(!tmp.type.contains(ruolo)) {
					tmp.type.add(ruolo);
				}
			}
		}
		//questo membro non è stato caricato, lo aggiungo
		mem.add(agg);
	}

}
