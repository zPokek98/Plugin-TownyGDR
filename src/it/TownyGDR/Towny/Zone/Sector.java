/**
 * 
 */
package it.TownyGDR.Towny.Zone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import it.CustomConfig.CustomConfig;

/*********************************************************************
 * @author: Elsalamander
 * @data: 11 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Suddivisione del mondo in MegaBlocchi per facilitare il search di
 * un dato.
 * 
 * Un settore è sopra alla zona, infatti essi contengono come cache le
 * zone che hanno anche una piccola area dentro il settore.
 * 
 * Un settore non è un oggetto che va definito in quanto generto per
 * necessità per contenere le zone(se non esistono zone -> non esistono settori,
 * se un zona viene creata e questa fuori esce dal settore senza rientrare
 * in un altro viene generato un settore che contiene la parte)
 * 
 * 
 * 
 * 
 * Manca il salvataggio e il load per i settori anche nelle zone ******************
 * 
 *********************************************************************/
public class Sector implements Comparable<Sector>{
	//Vaariabile di cache per i settori
	private static ArrayList<Sector> ListSector_pos_pos = new ArrayList<Sector>();
	private static ArrayList<Sector> ListSector_pos_neg = new ArrayList<Sector>();
	private static ArrayList<Sector> ListSector_neg_pos = new ArrayList<Sector>();
	private static ArrayList<Sector> ListSector_neg_neg = new ArrayList<Sector>();
	
	private static ArrayList<ArrayList<Sector>> ListSector = new ArrayList<ArrayList<Sector>>();
	static {
		ListSector.add(ListSector_pos_pos);
		ListSector.add(ListSector_pos_neg);
		ListSector.add(ListSector_neg_pos);
		ListSector.add(ListSector_neg_neg);
	}
	
	//Size Settore, deve essere un numero DISPARI dato che useremo un chunk come centro
	//e andando per discreti bisogna avere un numero dispari
	private static final int SizeSector = 21; 
	
	//Variabili oggetto
	private int coord_x;
	private int coord_z;
	private ArrayList<Zona> zoneInSector;
	
	/**
	 * Costruttore privato, serve unicamente aggiungere un settore
	 * che è neccessario ai fini di coprire le zone esistenti
	 * @param x
	 * @param z
	 */
	private Sector(int x, int z) {
		this.coord_x = x;
		this.coord_z = z;
		this.zoneInSector = new ArrayList<Zona>();

		Sector.getList(x, z).add(this);
	}
	
	/**
	 * I due settori sono uguali se le coordinate che lo identificano sono le stesse
	 * @param sec
	 * @return
	 */
	public boolean equals(Sector sec) {
		return (this.coord_x == sec.coord_x && this.coord_z == sec.coord_z);
	}
	
	/**
	 * Ritorna il settore che contiene la posizione data
	 * @param loc
	 * @return
	 */
	public static Sector getSectorByLocation(int x, int z) {		
		//Scansiona su tutti i settori
		ArrayList<Sector> List = Sector.getList(x,z);
		for(Sector sec : List) {
			if(sec.contain(x, z)) {
				return sec;
			}
		}
		return Sector.createSector(x, z); //crea un settore senza città
	}

	/**
	 * Ritorna la lista a cui appartengono queste coordinate
	 * @param x
	 * @param z
	 * @return
	 */
	private static ArrayList<Sector> getList(int x, int z) {
		if( x >= 0 && z >= 0) {
			return ListSector_pos_pos;
		}else if( x >= 0 && z < 0 ) {
			return ListSector_pos_neg;
		}else if( x < 0 && z >= 0 ) {
			return ListSector_neg_pos;
		}else if( x < 0 && z < 0 ) {
			return ListSector_neg_neg;
		}
		return null;
	}

	/**
	 * Ritorna il settore che contiene la posizione data
	 * @param loc
	 * @return
	 */
	public static Sector getSectorByLocation(Chunk loc) {
		return getSectorByLocation(loc.getX(), loc.getZ());
	}
	
	/**
	 * Ritorna il settore che contiene la posizione data
	 * @param loc
	 * @return
	 */
	public static Sector getSectorByLocation(Location loc) {
		return getSectorByLocation(loc.getChunk());
	}
	
	/**
	 * Ritorna TRUE se contiene la posizione data
	 * @param loc
	 * @return
	 */
	public boolean contain(Location loc) {
		return this.contain(loc.getChunk());
	}

	/**
	 * Ritorna TRUE se contiene la posizione data
	 * @param loc
	 * @return
	 */
	public boolean contain(int x, int z) {
		int tmp_x   = x;
		int tmp_z   = z;
		
		int l   = (SizeSector - 1)/2;
		//Controllo asse X
		if(  this.coord_x - l > tmp_x && this.coord_x + l < tmp_x  ) {
			//Controllo asse Z
			if(  this.coord_z - l > tmp_z && this.coord_z + l < tmp_z  ) {
				//rientra!
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Ritorna TRUE se contiene la posizione data, coordinate dei chunk!!!!
	 * @param loc
	 * @return
	 */
	public boolean contain(Chunk chunk) {
		return this.contain(chunk.getX(), chunk.getZ());
	}
	
	/**
	 * Ritorna la zona che ha un elemento d'area con le coordinate
	 * del chunk x e z.
	 * @param x
	 * @param z
	 * @return
	 */
	public Zona getZonaByArea(int x, int z) {
		//Controlla se le coordinate appartengono a questo settore
		if(!this.contain(x, z)) return null;
		
		for(Zona tmp : this.zoneInSector) {
			ArrayList<ElementoArea> aree = tmp.getArea().get(this);
			for(ElementoArea ele : aree) {
				if(ele.getX() == x && ele.getZ() == z) {
					return tmp;
				}
			}
		}
		return null;
	}

	/**
	 * Crea un settore per l'area
	 * @param elementoArea
	 * @return
	 */
	protected static Sector createSectorFor(ElementoArea area) {
		return createSector(area.getX(), area.getZ());
	}
	
	/**
	 * Crea un settore per le coordinate date
	 * @param x
	 * @param z
	 * @return
	 */
	protected static Sector createSector(int x_, int z_) {
		int x = x_ > 0 ? x_ : x_ - SizeSector;
		int z = z_ > 0 ? z_ : z_ - SizeSector;
		
		//divisione intera sulla x
		int coord_x = x / SizeSector;
		int coord_z = z / SizeSector;
		
		return new Sector(coord_x, coord_z);
	}
	
	/**
	 * Ritorna la coordinata Z del settore
	 * @return
	 */
	public int getX() {
		return this.coord_x;
	}
	
	/**
	 * Ritorna la coordinata Z del settore
	 * @return
	 */
	public int getZ() {
		return this.coord_z;
	}

	
	public void save(CustomConfig database) throws IOException {
		FileConfiguration config;
		if(database == null) {
			database = this.getCustomConfig();
		}
		config = database.getConfig();
		config.set("X", this.coord_x);
		config.set("Z", this.coord_z);
		
		//le zone che ha all'interno
		//Riga
		String tmp = "";
		for(Zona zon : this.zoneInSector) {
			tmp += zon.getID() + ";";
		}
		tmp = tmp.substring(0, tmp.length() - 1);
		config.set("Zone", tmp);
		
		database.save();		
	}
	
	private CustomConfig getCustomConfig() {
		return new CustomConfig("Settori" + File.pathSeparator + this.coord_x + ";" + this.coord_z);
	}

	@Override
	public int compareTo(Sector sector) {
		return (this.coord_x - sector.coord_x)*11 + (this.coord_z - this.coord_z)*17;
	}
	
	/**
	 * To string combinato al VauleOf
	 */
	public String toString() {
		return this.coord_x + ";" + this.coord_z;
	}

	/**
	 * Ritorna un settore tramite una stringa
	 * @param str
	 * @return
	 */
	public static Sector valueOf(String str) {
		if(str == null) return null;
		Scanner scan = new Scanner(str);
		scan.useDelimiter(";");
		
		int x = 0;
		int z = 0;
		
		try{
			x = scan.nextInt();
			z = scan.nextInt();
		}catch(InputMismatchException e){
			scan.close();
			return null;
		}
		scan.close();
		return Sector.getSector(x, z);
	}

	/**
	 * Ritorna il settore in base alle coordinate deil settore!
	 * @param x
	 * @param z
	 * @return
	 */
	public static Sector getSector(int x, int z) {
		ArrayList<Sector> list = Sector.getList(x, z);
		for(Sector sec : list) {
			if(sec.getX() == x && sec.getZ() == z) {
				return sec;
			}
		}
		return new Sector(x, z);
	}
	
	/**
	 * @param zona
	 */
	public void addZona(Zona zona) {
		if(!this.zoneInSector.contains(zona)) {
			this.zoneInSector.add(zona);
		}
	}

	/**
	 * @return
	 */
	public ArrayList<Zona> getZone() {
		return this.zoneInSector;
	}

}
