/**
 * 
 */
package it.TownyGDR.Towny.City.Area;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Util.Save.Salva;

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
	private Lotto() {
		this.id = -1;
		this.lotto = new ArrayList<ElementoArea>();
		this .admin = new ArrayList<Membro>();
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
			Lotto tmp = new Lotto();
			tmp.id = Integer.parseInt(str);
			Scanner scan = new Scanner(config.getString("Area.Lotti." + str));
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
		String str = "";
		for(ElementoArea area : this.lotto) {
			str += area.getX() + ";" + area.getZ() + "|";
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
		ArrayList<Membro> membri = city.getMembri();
		for(Membro mem : membri) {
			if(mem.getLotto().id == id_) {
				//trovato il lotto
				return mem.getLotto();
			}
		}
		
		//Carica il lotto se non è caricato
		String str = city.getConfig().getConfig().getString("Area.Lotti." + id_, null);
		if(str != null) {
			Lotto lot = new Lotto();
			Scanner scan = new Scanner(str);
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
				lot.lotto.add(new ElementoArea(x,z));
				lot.id = id_;
				
			}
			scan.close();
			return lot;
		}
		
		return null;
	}

}
