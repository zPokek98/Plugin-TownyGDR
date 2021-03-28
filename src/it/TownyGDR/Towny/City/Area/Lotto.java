/**
 * 
 */
package it.TownyGDR.Towny.City.Area;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Appezzamento di terra di una data grandezza per i membri della città
 * un lotto è assegnato a uno o più membri.
 * 
 * 
 *********************************************************************/
public class Lotto{
	
	//Variabili oggetto
	private int id;
	private ArrayList<ElementoArea> lotto;
	private ArrayList<Membro> admin; //Titolari lotto

	/**
	 * @param x
	 * @param z
	 */
	public Lotto(City city) {
		this.id = getMaxID(city) + 1;
		this.lotto = new ArrayList<ElementoArea>();
		this.admin = new ArrayList<Membro>();
		city.getArea().addLotto(this);
	}

	/**
	 * @param city 
	 * @return
	 */
	private static int getMaxID(City city) {
		int max = 0;
		for(Lotto lt : city.getArea().getLotti()) {
			if(lt.getId() > max) {
				max = lt.getId();
			}
		}
		return max;
	}

	/**
	 * @return
	 */
	public int getSize() {
		return this.lotto.size();
	}

	/**
	 * @param configurationSection
	 * @param area 
	 * @return
	 */
	public static ArrayList<Lotto> loadData(ConfigurationSection config, Area area, City city) {
		ArrayList<Lotto> lotti = new ArrayList<Lotto>();
		
		String[] list = city.getConfig().getConfig().getConfigurationSection("Area.Lotti" ).getKeys(false).stream().toArray(String[] :: new);
		for(String str : list) {
			
			//crea il lotto
			Lotto tmp = new Lotto(city);
			
			//id lotto
			tmp.id = Integer.parseInt(str);
			
			//Prendi gli elementi area che formano il lotto
			Scanner scan = new Scanner(config.getString("Lotti." + str));
			scan.useDelimiter("|");
			while(scan.hasNext()) {
				Scanner tp = new Scanner(scan.next());
				tp.useDelimiter(";");
				int x = 0;
				int z = 0;
				try {
					x = tp.nextInt();
					z = tp.nextInt();
				}catch(InputMismatchException e) {
					
				}
				tmp.lotto.add(new ElementoArea(x,z));
				
			}
			scan.close();
			lotti.add(tmp);
			
			
		}
		return lotti;
	}
	
	/**
	 * @param configurationSection
	 */
	public void save(ConfigurationSection config) {
		if(this.lotto.size() == 0) {
			return;
		}
		
		String str = "";
		for(ElementoArea area : this.lotto) {
			str += area.getX() + ";" + area.getZ() + "@";
		}
		str = str.substring(0, str.length() - 1);
		config.set("Lotti." + this.id, str);
	}

	/**
	 * Agiungi questo membro come titolare del lotto
	 * @param membro
	 */
	public void addMembro(Membro membro) {
		if(!this.admin.contains(membro)) {
			this.admin.add(membro);
		}
	}
	
	/**
	 * Rimuovi il membro
	 * @param membro
	 */
	public void removeMembro(Membro membro) {
		this.admin.remove(membro);
	}
	
	/**
	 * Ritorna true se il membro dato è un titolare del lotto
	 * @param membro
	 * @return
	 */
	public boolean hasMembro(Membro membro) {
		return this.admin.contains(membro);
	}

	/**
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param city 
	 * @param int1
	 * @return
	 */
	public static Lotto loadDataById(int id_, City city) {
		if(id_ == -1) {
			return new Lotto(city);
		}
		
		for(Lotto lot : city.getArea().getLotti()) {
			if(lot.getId() == id_) {
				return lot;
			}
		}
		
		//ArrayList<Membro> membri = city.getMembri();
		//for(Membro mem : membri) {
		//	if(mem.getLotto().id == id_) {
		//		//trovato il lotto
		//		return mem.getLotto();
		//	}
		//}
		
		//Carica il lotto se non è caricato
		String str = city.getConfig().getConfig().getString("Area.Lotti." + id_, null);
		if(str != null) {
			Lotto lot = new Lotto(city);
			Scanner scan = new Scanner(str);
			scan.useDelimiter("@");
			while(scan.hasNext()) {
				Scanner tp = new Scanner(scan.next());
				tp.useDelimiter(";");
				int x = 0;
				int z = 0;
				try {
					x = tp.nextInt();
					z = tp.nextInt();
				}catch(InputMismatchException e) {
					
				}
				lot.lotto.add(new ElementoArea(x,z));
				lot.id = id_;
				
			}
			scan.close();
			return lot;
		}
		
		return null;
	}

	/**
	 * aggiungi un elemento d'area al lotto
	 * @param ele
	 * @return
	 */
	public boolean addElementoArea(ElementoArea ele) {
		return this.lotto.add(ele);
	}
	
	/**
	 * rimuovi l'elento d'area
	 * @param ele
	 * @return
	 */
	public boolean removeElementoArea(ElementoArea ele) {
		return this.lotto.remove(ele);
	}
	
	/**
	 * compra un lotto/aggiungi il lotto dato a quelli gia presenti, ogni membro del lotto
	 * può comprare un lotto
	 * @param lv
	 * @param mem
	 * @return
	 */
	public boolean compraLotto(LottoVendita lv, Membro mem) {
		//mi serve i dati del player
		PlayerData pd = PlayerData.getFromUUID(mem.getUUID());
		
		//controlla i soldi
		if(pd.getBalance() >= lv.getPrezzo()) {
			//aggiungi al lotto
			this.lotto.add(lv.getEle());
			
			//dai i soldi a chi vende il lotto
			PlayerData.getFromUUID(lv.getSid().getUUID()).addMoney(lv.getPrezzo());
			
			pd.withdrawMoney(lv.getPrezzo());
			
			//rimuovi l'area dalla vendita
			pd.getCity().getArea().removeLottoVendita(lv);
		}
		return false;
	}
	
	
	
	
	
}
