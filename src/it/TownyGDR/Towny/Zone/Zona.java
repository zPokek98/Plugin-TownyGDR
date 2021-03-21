/**
 * 
 */
package it.TownyGDR.Towny.Zone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.TownyGDR;
import it.TownyGDR.Towny.LuoghiType;
import it.TownyGDR.Towny.Luogo;
import it.TownyGDR.Util.Util;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;
import it.TownyGDR.Util.Exception.Zona.ExceptionSectorInvalidStringLoad;
import it.TownyGDR.Util.Exception.Zona.ExceptionZonaImpossibleToLoad;
import it.TownyGDR.Util.Exception.Zona.ExceptionZonaNameNotAvaiable;
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
 * Per il loro salvataggio e poi accesso sarebbe buono avere:
 * - Per ogni area un file .yml in cui si segnano le coordinate
 *   dei vari elementi di area
 * - Natura area
 * - Altro...
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
	 * @throws ExceptionZonaNameNotAvaiable 
	 */
	public Zona(String nome, ZonaType type) throws ExceptionZonaNameNotAvaiable {
		if(!Zona.checkNameIsFree(nome)) throw new ExceptionZonaNameNotAvaiable("Nome già usato!!!");
		this.name = nome;
		this.id   = Zona.getMaxID() + 1;
		this.area = new TreeMap<Sector, ArrayList<ElementoArea>>();
		this.type = type;
		
		//Aggiungi alla cache
		ListZona.add(this);
	}
	
	
	/**
	 * Controlla in tutte le zone esistenti se una ha questo nome!
	 * @param nome
	 * @return
	 */
	private static boolean checkNameIsFree(String nome) {
		//Dato che tutte le zone sono in RAM la cerco solo la
		for(Zona zon : ListZona) {
			if(zon.name.equals(nome)) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Ritorna l'id massimo presente fra tutte le zone
	 * @return
	 */
	private static int getMaxID() {
		int id = 0;
		String[] list = Util.getListNameFile("Zone"); //Lista dei nomi dei file dentro la cartella "Zone"
		for(String str : list) {
			str = str.substring(0,str.length() - 4); //Cancella ".yml" alla fine
			CustomConfig customConfig = new CustomConfig("Zone" + File.separatorChar + str , TownyGDR.getInstance());
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
		//Controlla se appartiene già alla zona prima di aggungerlo
		if(this.contain(elementoArea)) {
			return false;
		}
		
		//prendi il settore per quell'area da aggiungere
		Sector sec = Sector.getSectorByLocation(elementoArea.getX(), elementoArea.getZ());
		
		//Caso particolare per il caso che il settore è stato generato per questa area
		if(sec.getZone().size() == 0) {
			sec.addZona(this);
		}
		
		//la zona ha questo settore?
		ArrayList<ElementoArea> sezione = this.area.get(sec);
		
		//Controlla se è Null, se lo è significa che la zona non aveva questo settore, crea e alloca
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
		//Per tutti i settori dell'area:
		for(Sector sec : this.area.keySet()) {
			//Prendi gli elemento d'area all'interno al settore della zona
			ArrayList<ElementoArea> list = this.area.get(sec);
			for(ElementoArea el : list) {
				//confronta i songoli elementi d'area
				if(el.getX() == ele.getX() && el.getZ() == ele.getZ()) {
					//trovato, finisci.
					return true;
				}
			}
			//return list.contains(ele);
		}
		return false;
	}
	
	/**
	 * Rimuovi l'elemento d'area
	 * @param elementoArea
	 * @return
	 */
	public boolean removeElementoArea(ElementoArea elementoArea) {
		//prendi il settore per quell'area dato
		Sector sec = Sector.getSectorByLocation(elementoArea.getX(), elementoArea.getZ());
		
		//la zona ha questo settore?
		ArrayList<ElementoArea> sezione = this.area.get(sec);
		
		//Controlla se è Null, non posso rimuover quello che non c'è, dato che la zona non può non essere
		//contenuta da questo settore
		if(sezione == null) {
			//non posso imuovere quello che non c'è
			return false;
		}else{
			//Controlla se l'elemento d'area c'è dentro la zona
			if(sezione.contains(elementoArea)) {
				//l'elemento d'area è dentro.
				//lo rimuovo
				boolean tmp = sezione.remove(elementoArea);
				
				//il settore è vuoto dopo aver rimosso l'elemto d'area?
				if(sezione.size() == 0) {
					//si elimina il settore dalla zona
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
			customConfig = new CustomConfig("Zone" + File.separatorChar + this.name + "(" + this.id + ")", TownyGDR.getInstance());
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
		if(config.getConfigurationSection("Area") == null) {
			config.set("Area.tmp", "tmp");
		}
		ConfigurationSection section = config.getConfigurationSection("Area");
		for(Sector tmp : this.area.keySet()) {
			ElementoArea.save(section, tmp, this.area.get(tmp));
		}
		//elimina il tmp
		config.set("Area.tmp", null);
		
		customConfig.save();
	}

	@Override
	public void load(CustomConfig database) throws IOException, ExceptionZonaImpossibleToLoad {
		CustomConfig customConfig;
		if(database == null) {
			customConfig = new CustomConfig("Zone" + File.separatorChar + this.name + "(" + this.id + ")", TownyGDR.getInstance());
		}else{
			customConfig = database;
		}
		FileConfiguration config = customConfig.getConfig();
		
		this.id    = config.getInt("ID");
		this.name  = config.getString("Nome");
		this.type  = ZonaType.valueOf(config.getString("Type"));
		
		String tmp = config.getString("Luogo", null);
		if(tmp != null) {
			this.luogo = LuoghiType.valueOf(tmp);
			try{
				this.luogoCache = Luogo.getById(luogo, config.getInt("LuogoID"));
			}catch(ExceptionCityImpossibleLoad e){
				this.type = null;
				this.luogoCache = null;
			}
		}
		if(config.contains("Area")) {
			String[] list = config.getConfigurationSection("Area").getKeys(false).stream().toArray(String[] :: new);
			for(String str : list) {
				Sector sec = null;
				try{
					sec = Sector.valueOf(str.replaceFirst("Settore", ""));
				}catch(ExceptionSectorInvalidStringLoad e){
					//Impossibile leggere il valore
					throw new ExceptionZonaImpossibleToLoad("Impossibile caricare la zona data");
				}
				if(sec != null) {
					ArrayList<ElementoArea> ele = ElementoArea.loadData(config.getConfigurationSection("Area." + str));
					this.area.put(sec, ele);
					sec.addZona(this);					
				}
			}
		}
	}


	/**
	 * Ritorna le arree della zona
	 * @return
	 */
	public Map<Sector, ArrayList<ElementoArea>> getArea() {
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
		this.luogo = luogo != null ? luogo.getType() : null;
	}

	/**
	 * Carica tutte le zone per l'avvio del server
	 * @throws IOException 
	 * @throws ExceptionZonaImpossibleToLoad 
	 */
	public static void initZona() throws IOException, ExceptionZonaImpossibleToLoad {
		String[] files = Util.getListNameFile("Zone");
		for(String str : files) {
			//rimuovi l'estenzione
			str = str.substring(0,str.length() - 4); //togli ".yml"
			CustomConfig customConfig = new CustomConfig("Zone" + File.separatorChar +str, TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			
			//prendi il nome della zona e il tipo
			String nome = config.getString("Nome");
			ZonaType type = ZonaType.valueOf(config.getString("Type"));
			
			Zona tmp = null;
			try {
				tmp = new Zona(nome, type);
				
				//prova a caricare la zona.
				tmp.load(customConfig);
			} catch (ExceptionZonaNameNotAvaiable e) {
				//Errore
				throw new ExceptionZonaImpossibleToLoad("Impossibile caricare la zona di file: " + str);
			}
		}
	}
	
	/**
	 * Ritorna la zona in base al suo id
	 * @param id
	 * @return
	 */
	public static Zona getByID(int id) {
		for(Zona zon : ListZona) {
			if(zon.id == id) {
				return zon;
			}
		}
		return null;
	}
	
	/**
	 * Ritorna la zona in base al suo nome
	 * @param name
	 * @return
	 */
	public static Zona getZona(String name) {
		for(Zona zon : ListZona) {
			if(zon.name.equals(name)) {
				return zon;
			}
		}
		return null;
	}


	/**
	 * Salva tutte le zone, per la chiususra del server
	 */
	public static void saveAll() {
		for(Zona zon : ListZona) {
			try{
				zon.save(null);
			}catch (IOException e){
				//Errore
			}
		}
	}
	
}
