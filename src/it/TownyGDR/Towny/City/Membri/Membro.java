/**
 * 
 */
package it.TownyGDR.Towny.City.Membri;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.Towny.City.Area.Lotto;
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
 * Ogni membro ha dei appezzamenti di terra assegnati DA FARE ! *****************************########
 * 
 *********************************************************************/
public class Membro implements Salva<ConfigurationSection>{
	
	//Identificatore per il player, poichè potrebbe essere offline
	private UUID uuid;
	private ArrayList<MembroType> type; //Ruoli nella città, può avere più ruoli.
	
	private Lotto lotto;
	
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
						mem.add(new Membro(UUID.fromString(strUuid), MembroType.Sindaco));
					}break;
						
					case Cittadino:{
						mem.add(new Membro(UUID.fromString(strUuid), MembroType.Cittadino));
					}break;
					
					case ND:
					default:
						break;
				}
			}
		}
		return mem;
	}

}
