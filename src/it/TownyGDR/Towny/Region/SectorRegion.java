/**
 * 
 */
package it.TownyGDR.Towny.Region;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;

import org.bukkit.Chunk;
import org.bukkit.Location;

import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Util.Exception.Zona.ExceptionSectorInvalidStringLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 2 mag 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class SectorRegion implements Comparable<SectorRegion>{
	//Vaariabile di cache per i settori
	private static TreeSet<SectorRegion> ListSector_pos_pos = new TreeSet<SectorRegion>();
	private static TreeSet<SectorRegion> ListSector_pos_neg = new TreeSet<SectorRegion>();
	private static TreeSet<SectorRegion> ListSector_neg_pos = new TreeSet<SectorRegion>();
	private static TreeSet<SectorRegion> ListSector_neg_neg = new TreeSet<SectorRegion>();
	
	private static ArrayList<TreeSet<SectorRegion>> ListSector = new ArrayList<TreeSet<SectorRegion>>();
	static {
		ListSector.add(ListSector_pos_pos);
		ListSector.add(ListSector_pos_neg);
		ListSector.add(ListSector_neg_pos);
		ListSector.add(ListSector_neg_neg);
	}
	
	//Size Settore, deve essere un numero DISPARI dato che useremo un chunk come centro
	//e andando per discreti bisogna avere un numero dispari
	private static final int SizeSector = 20; 
	
	//Variabili oggetto
	private int coord_x;
	private int coord_z;
	private ArrayList<Region> zoneInSector;
	
	/**
	 * Costruttore privato, serve unicamente aggiungere un settore
	 * che ? neccessario ai fini di coprire le zone esistenti
	 * @param x
	 * @param z
	 */
	private SectorRegion(int x, int z) {
		this.coord_x = x;
		this.coord_z = z;
		this.zoneInSector = new ArrayList<Region>();
		
		SectorRegion.getList(x, z).add(this);
	}
	
	/**
	 * I due settori sono uguali se le coordinate che lo identificano sono le stesse
	 * @param sec
	 * @return
	 */
	public boolean equals(SectorRegion sec) {
		return (this.coord_x == sec.coord_x && this.coord_z == sec.coord_z);
	}
	
	/**
	 * Ritorna il settore che contiene la posizione data, se non esisteva viene creato
	 * e inserito.
	 * @param loc
	 * @return
	 */
	public static SectorRegion getSectorByLocation(int x, int z) {		
		//Scansiona su tutti i settori
		TreeSet<SectorRegion> List = SectorRegion.getList(x,z);
		for(SectorRegion sec : List) {
			if(sec.contain(x, z)) {
				return sec;
			}
		}
		return SectorRegion.createSector(x, z); //crea un settore senza citt?
	}

	/**
	 * Ritorna la lista a cui appartengono queste coordinate
	 * I gruppi dei settori ? suddiviso in 4, l'algoritmo non tiene in considerazione
	 * il valore della coppia (x,z) ma solo del loto segno.
	 * @param x
	 * @param z
	 * @return
	 */
	private static TreeSet<SectorRegion> getList(int x, int z) {
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
	 * Ritorna il settore che contiene la posizione data dal chuck
	 * @param loc
	 * @return
	 */
	public static SectorRegion getSectorByLocation(Chunk loc) {
		return getSectorByLocation(loc.getX(), loc.getZ());
	}
	
	/**
	 * Ritorna il settore che contiene la posizione data dalla location
	 * @param loc
	 * @return
	 */
	public static SectorRegion getSectorByLocation(Location loc) {
		return getSectorByLocation(loc.getChunk());
	}
	
	/**
	 * Ritorna TRUE se contiene la posizione data dalla location
	 * @param loc
	 * @return
	 */
	public boolean contain(Location loc) {
		return this.contain(loc.getChunk());
	}

	/**
	 * Ritorna TRUE se contiene la posizione data dalle coordinate del chunk/Elemento Area
	 * @param loc
	 * @return
	 */
	public boolean contain(int x, int z) {		
		int x_ = x >= 0 ? x : x - SizeSector;
		int z_ = z >= 0 ? z : z - SizeSector;
		
		//divisione intera sulla x
		int coord_x = x_ / SizeSector;
		int coord_z = z_ / SizeSector;
		
		return this.coord_x == coord_x && this.coord_z == coord_z;
		
		/*
		int l = SizeSector/2;
		//Controllo asse X
		if(  (this.coord_x + 0.5)* SizeSector - l <= tmp_x && (this.coord_x + 0.5) * SizeSector + l >= tmp_x  ) {
			//Controllo asse Z
			if(  (this.coord_z + 0.5) * SizeSector - l <= tmp_z && (this.coord_z + 0.5) * SizeSector + l >= tmp_z  ) {
				//rientra!
				return true;
			}
		}
		return false;
		*/
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
	 * Ritorna la zona che ha un elemento d'area/chuck con le coordinate date
	 * le coordinate sono quelle che si rieriscono al chunk/elemento d'area
	 * del chunk x e z.
	 * @param x
	 * @param z
	 * @return
	 */
	public Region getZonaByArea(int x, int z) {
		//Controlla se le coordinate appartengono a questo settore
		if(!this.contain(x, z)) return null;
		
		for(Region tmp : this.zoneInSector) {
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
	 * Crea un settore per l'elemento d'areaa dato
	 * @param elementoArea
	 * @return
	 */
	protected static SectorRegion createSectorFor(ElementoArea area) {
		return createSector(area.getX(), area.getZ());
	}
	
	/**
	 * Crea un settore per le coordinate date che identificano un chunk/elemento d'area
	 * @param x
	 * @param z
	 * @return
	 */
	protected static SectorRegion createSector(int x_, int z_) {
		int x = x_ >= 0 ? x_ : x_ - SizeSector;
		int z = z_ >= 0 ? z_ : z_ - SizeSector;
		
		//divisione intera sulla x
		int coord_x = x / SizeSector;
		int coord_z = z / SizeSector;
		
		return new SectorRegion(coord_x, coord_z);
	}
	
	/**
	 * Ritorna la coordinata X del settore
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

	/*
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
	*/
	
	/*
	private CustomConfig getCustomConfig() {
		return new CustomConfig("Settori" + File.pathSeparator + this.coord_x + ";" + this.coord_z);
	}
	*/

	@Override
	/**
	 * Comparazione di 2 settori
	 */
	public int compareTo(SectorRegion sector) {
		//scelto 47 e 53 perch? sono primi e abbastanza alti si azzera in caso di coordinate abbastanza grandi
		//47*53=2491 improbabile raggiungere questa coordinata di settore
		return (this.coord_x - sector.coord_x)*47 + (this.coord_z - sector.coord_z)*53;
	}
	
	/**
	 * To string combinato al VauleOf
	 * Crea una stringa che contiene le informazioni minime per identificae un settore
	 */
	public String toString() {
		return this.coord_x + ";" + this.coord_z;
	}

	/**
	 * Ritorna un settore tramite una stringa data dal toString
	 * @param str
	 * @return
	 * @throws ExceptionSectorInvalidStringLoad 
	 */
	public static SectorRegion valueOf(String str) throws ExceptionSectorInvalidStringLoad {
		if(str == null) return null;
		Scanner scan = new Scanner(str);
		scan.useDelimiter(";");
		
		int x = 0;
		int z = 0;
		
		try{
			x = scan.nextInt();
			z = scan.nextInt();
		}catch(InputMismatchException e){
			//Dati non vaalidi creo una eccezione
			scan.close();
			//return null; //return stupido.
			String mes = "Stringa data per ottenere il settore non valida!\nValore in input: " + str ;
			throw new ExceptionSectorInvalidStringLoad(mes);
		}
		scan.close();
		return SectorRegion.getSector(x, z);
	}

	/**
	 * Ritorna il settore in base alle coordinate deil settore!
	 * @param x
	 * @param z
	 * @return
	 */
	public static SectorRegion getSector(int x, int z) {
		TreeSet<SectorRegion> list = SectorRegion.getList(x, z);
		for(SectorRegion sec : list) {
			if(sec.getX() == x && sec.getZ() == z) {
				return sec;
			}
		}
		return new SectorRegion(x, z);
	}
	
	/**
	 * Inserisci nella variabile di cache la zona
	 * @param zona
	 */
	public void addRegion(Region zona) {
		//per precauzione controllo se ? null
		if(zona != null) {
			if(!this.zoneInSector.contains(zona)) {
				this.zoneInSector.add(zona);
			}
		}
	}

	/**
	 * @param zona
	 */
	public void removeRegion(Region zona) {
		//per precauzione controllo se ? null
		if(zona != null) {
			if(this.zoneInSector.contains(zona)) {
				this.zoneInSector.remove(zona);
			}
		}
	}
	
	/**
	 * Ritorna tutte le zone che sono dentro al settore
	 * @return
	 */
	public ArrayList<Region> getZone() {
		return this.zoneInSector;
	}

}

