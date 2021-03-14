/**
 * 
 */
package it.TownyGDR.Towny.Zone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.Towny.LuoghiType;
import it.TownyGDR.Towny.Luogo;
import it.TownyGDR.Util.Util;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 9 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe generale per la descrizione di una Zona:
 * Una zona è l'insieme degli elementi di area più piccolo che c'è,
 * ogni area avrà un contrassegno che indica di che tipo è, questi valori
 * saranno rappresentati da una opportuna enumerazione di supporto.
 * 
 * L'area finita ovviamente non sarà costruita da questo oggetto
 * poichè astratto ma da una sua sotto classe che esprimerà tutta la
 * sua natura in quanto Area particolare, qua ci saranno solo le caratteristiche
 * che avranno in comune.
 * 
 * Per il loro salvataggio e poi accesso sarebbe buono avere:
 * - Per ogni area un file .yml in cui si segnano le coordinate
 *   dei vari elementi di area
 * - Natura area
 * - Altre info per l'area specifica.
 * 
 * Dato che le Zone sono necessarie al fine della modalità esse
 * saranno sempre presenti nella RAM in un opportuno ArrayList
 * in modo anche per trovare facilmente se una posizione rientra
 * in una area o no.
 * 
 *
 * Dato che ci saranno tante zone, e queste possono essere vaste,
 * quando cerco un chunk in specifico la cosa non va bene e troppo
 * complessa(temporalmente parlando) bisogna suddividere in megaBlocchi
 * il modo in modo da fare il check solo di alcune zone e non tutte
 * quante.
 * Si fa suddividendo il mondo in quadrati di lato SizeSector ~ 20 => 400 chunk?
 * Il settore sarà identificato con il stesso metodo con l'identificazione dei
 * chunk ovvero divisione intera del numero delle coordinata.
 * Questa suddivisione sarà supportata dalla classe "Sector" che conterrà
 * banalmente 2 valori, le zone presenti e funzioni per controllare se un data posizione
 * rientra nel settore e in caso ritornare la zona.
 * 
 * Una zona fa capo ad un Settore che la contiene.
 * 
 *********************************************************************/
public class Zona implements Salva<CustomConfig>{
	
	//ArrayList di cache per tenere a memoria tutte le zone
	private static ArrayList<Zona> ListZona = new ArrayList<Zona>();
	
	//Variabili oggetto
	private int id; //id zona
	private String name;
	
	private ZonaType type; //A cosa è adibita fare?
	
	private Map<Sector, ArrayList<ElementoArea>> area; //Per usufruire dei settori al messimo e ridurre il più
	//possibile le complessità uso un albero binario di ricerca => Sector implementa Comparable
	//l'elemento d'area deve essere inserito secondo un criterio
	
	private LuoghiType luogo; //Il tipo di luogo che è.
	private Luogo luogoCache; //Se è assegnato un luogo il suo puntatore per cache.
	
	
	/**
	 * Costruttore di una zona con la tipologia assegnata e nome.
	 * @param type
	 */
	public Zona(String nome, ZonaType type) {
		this.name = nome;
		this.id   = Zona.getMaxID() + 1;
		this.area = new TreeMap<Sector, ArrayList<ElementoArea>>();
		this.type = type;
		
		//Aggiungi alla cache
		ListZona.add(this);
		
		//Salva il file iniziale.
		try{
			if(!(new File("Zone" + File.pathSeparator + this.name + "(" + this.id + ")")).exists()) {
				this.save(new CustomConfig("Zone" + File.pathSeparator + this.name + "(" + this.id + ")"));
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Ritorna l'id massimo presente fra tutte le zone
	 * @return
	 */
	private static int getMaxID() {
		int id = 0;
		String[] list = Util.getListNameFile("Zone"); //Lista dei nomi dei file dentro la cartella "Zone"
		for(String str : list) {
			CustomConfig customConfig = new CustomConfig("Zone" + File.pathSeparator + str );
			FileConfiguration config = customConfig.getConfig();
			int tmp = config.getInt("ID"); //Prendi l'ID nel file letto
			if(tmp > id) {
				id = tmp;
			}
		}
		return id;
	}
	
	/**
	 * Aggiungi l'elemento d'area alla zona, serve quando si setterà la zona
	 * @param elementoArea
	 */
	public boolean addElementoArea(ElementoArea elementoArea) {
		//prendi il settore per quell'area
		Sector sec = Sector.getSectorByLocation(elementoArea.getX(), elementoArea.getZ());
		if(sec.getZone().size() == 0) {
			sec.addZona(this);
		}
		
		//la zona ha questo settore?
		ArrayList<ElementoArea> sezione = this.area.get(sec);
		
		//Controlla se è Null, se lo è crea e alloca
		if(sezione == null) {
			sezione = new ArrayList<ElementoArea>();
			sezione.add(elementoArea);
			this.area.put(sec, sezione);
			return true;
		}else{
			//Controlla se l'elemento d'area c'è già
			if(!sezione.contains(elementoArea)) {
				return sezione.add(elementoArea);
			}
			return false;
		}	
	}
	
	/**
	 * Ritorna il tipo della zona.
	 * @return
	 */
	public ZonaType getType() {
		return this.type;
	}
	
	/**
	 * Ritorna il nome della zona
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Ritorna l'id della zona
	 * @return
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Controlla se l'elemento d'area dato è dentro la zona
	 * @param ele
	 * @return
	 */
	public boolean contain(ElementoArea ele) {
		for(Sector sec : this.area.keySet()) {
			ArrayList<ElementoArea> list = this.area.get(sec);
			return list.contains(ele);
		}
		return false;
	}
	
	/**
	 * Rimuovi l'elemento d'area
	 * @param elementoArea
	 * @return
	 */
	public boolean removeElementoArea(ElementoArea elementoArea) {
		//prendi il settore per quell'area
		Sector sec = Sector.getSectorByLocation(elementoArea.getX(), elementoArea.getZ());
		
		//la zona ha questo settore?
		ArrayList<ElementoArea> sezione = this.area.get(sec);
		
		//Controlla se è Null, non posso rimuover quello che non c'è
		if(sezione == null) {
			return false;
		}else{
			//Controlla se l'elemento d'area c'è
			if(sezione.contains(elementoArea)) {
				boolean tmp = sezione.remove(elementoArea);
				if(sezione.size() == 0) {
					this.area.remove(sec);
				}
				return tmp;
			}
			return false;
		}	
	}
	
	/**
	 * Ritorna la lista dei settori in cui risiede la zona
	 * @return
	 */
	public Sector[] getSector(){
		return this.area.keySet().toArray(new Sector[this.area.keySet().size()]);
	}
	
	
	@Override
	public void save(CustomConfig database) throws IOException {
		CustomConfig customConfig;
		if(database == null) {
			customConfig = new CustomConfig("Zone" + File.pathSeparator + this.name + "(" + this.id + ")");
		}else{
			customConfig = database;
		}
		FileConfiguration config = customConfig.getConfig();
		
		config.set("ID", this.id);
		config.set("Nome", this.name);
		config.set("Type", this.type.toString());
		if(this.luogo != null) {
			config.set("Luogo", this.luogo.toString());
			config.set("LuogoID", this.luogoCache.getId());
		}
		
		
		//Salva tutti gli elementi d'area
		ConfigurationSection section = config.getConfigurationSection("Area");
		for(Sector tmp : this.area.keySet()) {
			ElementoArea.save(section.getConfigurationSection("Settore." + section.toString()), this.area.get(tmp));
		}
		
		customConfig.save();
	}

	@Override
	public void load(CustomConfig database) throws IOException {
		CustomConfig customConfig;
		if(database == null) {
			customConfig = new CustomConfig("Zone" + File.pathSeparator + this.name + "(" + this.id + ")");
		}else{
			customConfig = database;
		}
		FileConfiguration config = customConfig.getConfig();
		
		this.id    = config.getInt("ID");
		this.name  = config.getString("Nome");
		this.type  = ZonaType.valueOf(config.getString("Type"));
		this.luogo = LuoghiType.valueOf(config.getString("Luogo"));
		if(this.luogo != null) {
			this.luogoCache = Luogo.getById(luogo, config.getInt("LuogoID"));
		}
		
		String[] list = config.getConfigurationSection("Area").getKeys(false).stream().toArray(String[] :: new);
		for(String str : list) {
			Sector sec = Sector.valueOf(str.substring(0,7));
			if(sec != null) {
				ArrayList<ElementoArea> ele = ElementoArea.loadData(config.getConfigurationSection("Area." + str));
				this.area.put(sec, ele);
				sec.addZona(this);
			}
		}
	}


	/**
	 * Ritorna le arree della zona
	 * @return
	 */
	protected Map<Sector, ArrayList<ElementoArea>> getArea() {
		return this.area;
	}
	
	/**
	 * Ritorna la Zona data una posizione se esiste
	 * @param loc
	 * @return
	 */
	public static Zona getZonaByLocation(Location loc) {
		int x = loc.getChunk().getX();
		int z = loc.getChunk().getZ();
		return getZonaByLocation(x, z);
	}
	
	/**
	 * Ritorna la Zona data una posizione se esiste
	 * @param loc
	 * @return
	 */
	public static Zona getZonaByLocation(int x, int z) {
		Sector sec = Sector.getSectorByLocation(x, z); //Prendi il settore
		return sec.getZonaByArea(x, z);
	}
	
	/**
	 * Ritorna il luogo della zona
	 * @return
	 */
	public Luogo getLuogo() {
		return this.luogoCache;
	}
	
	/**
	 * Imposta il luogo della zona
	 * @param luogo
	 */
	public void setLuogo(Luogo luogo) {
		this.luogoCache = luogo;
	}

	/**
	 * Carica tutte le zone
	 * @throws IOException 
	 */
	public static void initZona() throws IOException {
		String[] files = Util.getListNameFile("Zone");
		for(String str : files) {
			CustomConfig customConfig = new CustomConfig("Zone" + File.pathSeparator +str);
			FileConfiguration config = customConfig.getConfig();
			String nome = config.getString("Nome");
			ZonaType type = ZonaType.valueOf(config.getString("Type"));
			Zona tmp = new Zona(nome, type);
			tmp.load(customConfig);
		}
	}
	
	
	public static Zona getByID(int id) {
		for(Zona zon : ListZona) {
			if(zon.id == id) {
				return zon;
			}
		}
		return null;
		
	}
	
	
	
}
