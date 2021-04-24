/**
 * 
 */
package it.TownyGDR.Towny.City.Membri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Area.Lotto;

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
 * Ogni membro ha dei appezzamenti di terra assegnati 
 * 
 *********************************************************************/
public class Membro{
	
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
		this.lotto = new Lotto(PlayerData.getFromUUID(uuid).getCity());
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Membro other = (Membro) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
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
	
	/**
	 * Salva tutti i giocatori nella sezione data
	 * @param config
	 * @param list
	 * @throws IOException 
	 */
	public static void save(ConfigurationSection config, ArrayList<Membro> list) throws IOException {
		//devo salvare il singolo membro sotto la sezione/ruolo assegnatosi
		for(Membro mem : list) {
			String str = "";
			for(MembroType ty : mem.getType()) {
				str += ty.toString() + ";";
			}
			str = str.substring(0, str.length() - 1);
			config.set(mem.getUUID() + ".Ruolo", str);
			if(mem.lotto != null) {
				if(mem.lotto.getSize() != 0) {
					config.set(mem.getUUID() + ".Lotto", mem.lotto.getId());
				}
			}
			
		}
	}
	
	/**
	 * Carica tutti i membri della città sapendo il suo salvataggio
	 * e ritorna l'arrayList.
	 * @param configurationSection
	 * @return
	 */
	public static ArrayList<Membro> loadMembri(ConfigurationSection config, City city) {
		ArrayList<Membro> mem = new ArrayList<Membro>();
		//Array che contiene la lista dei ruoli presenti sotto "Membri" nel file di salvtaggio
		String[] ruoli = config.getKeys(false).stream().toArray(String[] :: new);
		
		for(String str : ruoli) {
			UUID uuid = UUID.fromString(str);
			String ty = config.getString(str + ".Ruolo");
			
			ArrayList<MembroType> type = new ArrayList<MembroType>();
			Scanner scan = new Scanner(ty);
			scan.useDelimiter(";");
			while(scan.hasNext()) {
				type.add(MembroType.valueOf(scan.next()));
			}
			scan.close();
			
			Membro membro = new Membro(uuid, null);
			membro.type   = type;
			
			//ottieni i lotti del player
			membro.lotto  = Lotto.loadDataById(config.getInt(str + ".Lotto", -1), city);
			if(membro.lotto != null) {
				membro.lotto.addMembro(membro);
			}
			
			mem.add(membro);
		}
		return mem;
	}

	/**
	 * 
	 */
	public Lotto getLotto() {
		return this.lotto;
	}
	

}
