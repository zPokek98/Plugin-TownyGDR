/**
 * 
 */
package it.TownyGDR.PlayerData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.TownyGDR;
import it.TownyGDR.PlayerData.EtniaList.Casate.Casata;
import it.TownyGDR.PlayerData.EtniaList.Casate.CasataType;
import it.TownyGDR.PlayerData.Statistiche.Stats.KDA;
import it.TownyGDR.Tag.Taggable;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Util.Util;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;
import it.TownyGDR.Util.Save.Salva;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.milkbowl.vault.economy.EconomyResponse;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che "Estende" la classe player anche se non esplicitamente
 * Verrano racchiuse tutte le informazioni riguardanti il player
 * e le sue statistiche di rilevanza e altri dati.
 * 
 * 
 *********************************************************************/
@SuppressWarnings("unused")
public class PlayerData implements Salva<CustomConfig>, Taggable{

	//Variabile di cache per il playerData per eventuali get, e ottimizzazione tempi
	private static CopyOnWriteArrayList<PlayerData> ListPlayerData = new CopyOnWriteArrayList<PlayerData>(); //mi obbliga a creare la
																					   //funzione "equals"
	
	private static CopyOnWriteArrayList<PlayerData> ListFirstJoin = new CopyOnWriteArrayList<PlayerData>(); 	//Lista di support
																						//per il primo join
	//Contenitore dei Tag per il player
	private static ArrayList<String> TagList = new ArrayList<String>();
	static{		//Immetti i tag nel contenitore dei Tag
		TagList.add("%Player.name%");
		TagList.add("%Player.uuid%");
		TagList.add("%Player.KDA.kill%");
		TagList.add("%Player.KDA.death%");
	}
	
	//Variabili oggetto giocatore**********
	private Player player;	//Puntatore cache all'oggetto player per otterene il player secondo il server
	private UUID   uuid;    //UUID del giocatore senza ricorrere a funzioni strane, usato poi per il 
							//salvataggio e localizzazione del suo file e righa nel database.
	
	private Etnia etnia;	//Etnia giocatore
	private Casata casata;	//Casata del giocatore
	
	private long   idCity;	//Id della città se ne fa parte, caso contrario -1.
	private City   city;	//Puntatore oggetto città se è in una, caso contrario null.
	
	//Statistiche
	private KDA    kda;		//Kill/Death/"Assist?" del player
	
	
	/**
	 * Costruttore per il PlayerData, senza informazioni apparte il suo UUID
	 * tutto il resto ha valore di default.
	 * @param player
	 */
	public PlayerData(Player player){
		if(player != null) {
			this.player = player;
			this.uuid   = player.getUniqueId();
		}else{
			this.player = null;
			this.uuid   = null;
		}
		this.idCity = -1;
		this.city   = null;
		this.kda    = new KDA();
		
		this.etnia = null;
		this.casata = null;
		
		//Aggiungilo alla cache.
		ListPlayerData.add(this);
	}
	

	/**
	 * Ritorna l'oggetto player per il server.
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Ritorna l'uuid del playerData.
	 * @return
	 */
	public UUID getUUID() {
		return this.uuid;
	}
	
	
	/**
	 * Ritorna se è lo stesso player con il confronto degli uuid
	 * non serve loggetto player.
	 * @param pd
	 * @return
	 */
	public boolean equals(PlayerData pd) {
		return this.uuid.equals(pd.getUUID());
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
		PlayerData other = (PlayerData) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	/**
	 * Salva i dati del player
	 * @throws IOException 
	 */
	public void save() throws IOException {
		this.save(null);
	}

	@Override
	/**
	 * Salva i dati del giocatore del file/database specificato
	 * Se è null, vengono salvati i dati su file, nel modo 
	 * standard.
	 */
	public void save(CustomConfig database) throws IOException{
		//prendi
		CustomConfig customConfig;
		FileConfiguration config;
		if(database == null) {
			customConfig = new CustomConfig("PlayerData" + File.separatorChar + this.uuid , TownyGDR.getInstance());
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		//salva i dati player
		config.set("UUID", this.uuid.toString());
		config.set("IdCity", this.idCity);
		
		
		//Carica le statistiche
		if(!config.contains("Statistiche")) {
			config.createSection("Statistiche");
		}
		
		//salva il KDA
		if(!config.contains("Statistiche.KDA")) config.set("Statistiche.KDA.tmp", "tmp");
		this.kda.save(config.getConfigurationSection("Statistiche.KDA"));
		if(config.contains("Statistiche.KDA.tmp")) config.set("Statistiche.KDA.tmp", null);
		
		if(this.etnia != null) {
			this.etnia.save(config);
		}
		
		if(this.casata != null) {
			this.casata.save(config);
		}
		
		//Salva i dati nel file.
		if(!customConfig.save()) {
			throw new IOException(); //Errore nel salvataggio!
		}
	}

	@Override
	public void load(CustomConfig database) {
		//prendi
		CustomConfig customConfig;
		FileConfiguration config;
		if(database == null) {
			customConfig = new CustomConfig("PlayerData/" + this.uuid , true, TownyGDR.getInstance());
			config = customConfig.getConfig();
		}else{
			customConfig = database;
			config = customConfig.getConfig();
		}
		
		//Leggi l'id della città, prendi il suo puntatore, e verifica se esiste in caso contrario metti
		//id città a -1
		this.idCity = config.getInt("IdCity", -1);
		if(this.idCity != -1) {
			try{
				this.city = City.getByID(idCity);
			}catch(ExceptionCityImpossibleLoad e){
				this.idCity = -1;
				this.city = null;
			}
			if(this.city == null){
				this.idCity = -1;
			}
		}
		
		this.etnia = Etnia.load(config);
		this.casata = Casata.load(config);
		
		//Carica le statistiche
		if(!config.contains("Statistiche")) {
			config.createSection("Statistiche");
		}
		
		//salva il KDA
		if(!config.contains("Statistiche.KDA")) config.set("Statistiche.KDA.tmp", "tmp");
		this.kda.load(config.getConfigurationSection("Statistiche.KDA"));
		if(config.contains("Statistiche.KDA.tmp")) config.set("Statistiche.KDA.tmp", null);
		//...
		
	}

	/**
	 * Carica il player tramite un uuid
	 * @param uuid
	 * @return
	 */
	public static PlayerData loadPlayerData(UUID uuid) {
		PlayerData pd = new PlayerData(null);
		pd.uuid = uuid;
		pd.load(new CustomConfig("PlayerData/" + pd.uuid , true, TownyGDR.getInstance()));
		return pd;
	}
	
	/**
	 * Ritorna il playerData con questo uuid se esiste
	 * @param playerUuid
	 * @return
	 */
	public static PlayerData getFromUUID(UUID playerUuid) {
		//Controlla fra quelli già caricati
		for(PlayerData pd : ListPlayerData) {
			if(pd.getUUID().equals(playerUuid)) {
				return pd;
			}
		}
		
		//Controlla se esiste il suo file
		if(PlayerData.checkExistThisPlayer(playerUuid)) {
			//Esiste il file, lo carico
			return PlayerData.loadPlayerData(playerUuid);
		}
		
		//Non è mai esistito questo player
		return null;
	}
	
	/**
	 * @param target
	 * @return
	 */
	public static PlayerData getPlayerData(Player p) {
		PlayerData tmp = PlayerData.getFromUUID(p.getUniqueId());
		if(tmp == null) {
			tmp = new PlayerData(p);
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + String.format("[%s]Nuovo giocatore! Nome: %s ", TownyGDR.getInstance().getDescription().getName(), p.getName()));
			//salvo per la prima volta il player
			
			tmp.firstJoin();
			try{
				tmp.save();
			}catch (IOException e){
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + String.format("[%s] Impossibile salvare i dati di : %s", TownyGDR.getInstance().getDescription().getName(), p.getName()));
			}
		}
		return tmp;
	}


	/**
	 * Controlla se questo player con l'uuid esiste un suo salvataggio
	 * @param uuid
	 * @return
	 */
	public static boolean checkExistThisPlayer(UUID uuid) {
		String[] paths = Util.getListNameFile("PlayerData");
		for(String str : paths) {
			str = str.substring(0,str.length() - 4);
			if(str.equals(uuid.toString())) {
				return true;
			}
		}
		return false;
	}
	
	//***************************************************************************** Economy player
	/** 
	 * Ritorna i soldi del player.
	 */
	public double getBalance() {
		return 10000000;
		//return TownyGDR.econ.getBalance(player);
	}
	
	/**
	 * Aggiungi i soldi al player
	 * @param val
	 * @return
	 */
	public boolean addMoney(double val) {
		if(val > 0) {
			/*EconomyResponse result = *///TownyGDR.econ.depositPlayer(player, val);
			return true;
		}
		return false;
	}
	
	/**
	 * Togli soldi al player
	 * @param val
	 * @return
	 */
	public boolean withdrawMoney(double val) {
		if(val > 0) {
			//TownyGDR.econ.withdrawPlayer(player, val);
			return true;
		}
		return false;
	}
	
	
	//...operare con la vault
	
	//************************************************************************************* City interaction
	/**
	 * Imposta il valore della citta al player
	 * @param i
	 */
	public void setCity(City city) {
		if(city != null) {
			this.city = city;
			this.idCity = this.city.getId();
		}else {
			this.idCity = -1;
			this.city = null;
		}
	}

	/**
	 * Ritorna la città a cui appartiene, null se non appartiene a nessuna città.
	 * @return
	 */
	public City getCity() {
		if(this.city == null) {
			if(this.idCity != -1) {
				try{
					return City.getByID(idCity);
				}catch(ExceptionCityImpossibleLoad e){
					this.idCity = -1;
					this.city = null;
				}
			}
		}else{
			return this.city;
		}
		return null;
	}
	
	//*************************************************************************************** Etnie/Casate
	/**
	 * @return the etnia
	 */
	public Etnia getEtnia() {
		return etnia;
	}

	/**
	 * @return the casata
	 */
	public Casata getCasata() {
		return casata;
	}
	
	/**
	 * Prima volta che joina il player, mostra il libro
	 * con le istruzioni e poi le gui di selezione per le etnie
	 * e casate
	 */
	private void firstJoin() {
		//Aggiungi alla lista del primo join
		ListFirstJoin.add(this);
		
		//apri il libro
		this.openBook();
	}
	/**
	 * Eseguire un comando esce dal libro
	 * Aprire un link non esce dal libro
	 */
	private void openBook() {        
        //Crea il libro
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		
        //meta del libro
        BookMeta meta = (BookMeta) book.getItemMeta();
        
        meta.setAuthor("Elsalamander");
        meta.setTitle("Istruzioni");
        
        //Testo delle pagine
        TextComponent lc = new TextComponent();
        
        //Prima pagina
        lc.addExtra(  "Ogni player deve "
        			+ "selezionare una Etnia "
        			+ "a cui fare parte e "
        			+ "poi una Casata, ogni "
        			+ "Etnia ha una al di "
        			+ "sotto una lista di "
        			+ "casate diverse\n"
        			+ "\n"
        			+ "La scelta verrà "
        			+ "\"avviata\" dopo "
        			+ "aver cliccato sul "
        			+ "testo indicato...");
        //Aggiungi la pagina
        meta.spigot().addPage(lc.getExtra().toArray(new BaseComponent[lc.getExtra().size()]));
        
        //Seconda pagina
        lc = new TextComponent();
        lc.addExtra(  "Dopo aver scelto la "
    				+ "Etnia e Casata "
    				+ "siete liberi di "
    				+ "giocare alla Towny\n"
    				+ "\n"
    				+ "Fine.\n\n");
        lc.addExtra(new ComponentBuilder("Selezione Etnia Casata").italic(true).bold(true).color(ChatColor.RED)
        			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/firstJoin " + this.uuid))
        			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("lore")))
        			.create()[0]);
        meta.spigot().addPage(lc.getExtra().toArray(new BaseComponent[lc.getExtra().size()]));
        
        //ClickEvent.Action.RUN_COMMAND
        //il comando che esegue e quello "scriverebbe il giocatore"
        
        //imposta il meta
        book.setItemMeta(meta);
        
        //apri il libro
        this.player.openBook(book);
	}
	
	public static CopyOnWriteArrayList<PlayerData> getListFirstJoin(){
		return ListFirstJoin;
	}
	
	public static void removeListFirstJoin(PlayerData pd){
		ListFirstJoin.remove(pd);
	}
	
	/**
	 * Gui 0
	 */
	public void openSelectionFirstJoin() {
		
		Map<String, ArrayList<CasataType>> et = Etnia.getMapEtnia();
		
		int slot = ((int)(et.keySet().size() / 8) + 1) * 9;
		
		Inventory gui = TownyGDR.getInstance().getServer().createInventory(this.player, slot, "Selezione Etnia");
		
		ItemStack item = null;
		List<String> lore = null;
		int i = 0;
		for(String str : et.keySet()) {
			item = new ItemStack(Material.TOTEM_OF_UNDYING);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(str);
			
			//lore
			lore = new ArrayList<String>();
			lore.add("Questa Etnia ha le seguenti casate:");
			for(CasataType ty : et.get(str)) {
				lore.add("- " + ty.toString());
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			gui.setItem(i, item);
			
			i++;
		}
		this.player.openInventory(gui);
	}
	
	/**
	 * Gui 1
	 * @param etnia
	 */
	public void openSelectionFirstJoin(Etnia etnia) {
		Map<String, ArrayList<CasataType>> et = Etnia.getMapEtnia();
		
		ArrayList<CasataType> ty = et.get(etnia.getName());
		this.etnia = etnia;
		
		int slot = ((int)(ty.size() / 8) + 1) * 9;
		
		Inventory gui = TownyGDR.getInstance().getServer().createInventory(this.player, slot, "Selezione Casata");
		
		ItemStack item = null;
		List<String> lore = null;
		int i = 0;
		for(CasataType t : ty) {
			item = new ItemStack(Material.REDSTONE_TORCH);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(t.toString());
			
			item.setItemMeta(meta);
			
			gui.setItem(i, item);
			
			i++;
		}
		this.player.openInventory(gui);
	}
	
	public void interactSelectionGui(InventoryClickEvent event, int nGui, Etnia etnia) {
		Map<String, ArrayList<CasataType>> et = Etnia.getMapEtnia();
		
		if(nGui == 0) {
			//prima gui			
			int slot = event.getSlot();
			
			if(et.keySet().size() >= slot + 1) {
				
				//int size = et.keySet().size();
				this.etnia = Etnia.getByName(et.keySet().stream().toArray(String[] :: new)[0]);
				this.player.closeInventory();
				openSelectionFirstJoin(this.etnia);
			}else{
				event.setCancelled(true);
			}
			
		}else if(nGui == 1) {
			//seconda gui			
			int slot = event.getSlot();
			
			ArrayList<CasataType> ty = et.get(etnia.getName());
			
			if(ty.size() >= slot + 1) {				
				this.casata = Casata.getByName(ty.get(slot).toString());
				
				PlayerData.removeListFirstJoin(this);
				
				this.player.closeInventory();
			}else{
				event.setCancelled(true);
			}
		}
	}
	
	
	//******************************************************************************************* Statistiche
	/**
	 * Ritorna la statistica KDA
	 * @return the kda
	 */
	public KDA getKda() {
		return kda;
	}
	
	//******************************************************************************************* TAG
	@Override
	public boolean hasTag(String tag) {
		return PlayerData.TagList.contains(tag);
	}

	@Override
	public ArrayList<String> getTagList() {
		return PlayerData.TagList;
	}

	@Override
	public String getValueFromTag(String str) {
		if(str == null || str.length() <= 2) return "(Error Invalid code: " + str + ")";
		
		//Si riferisce al KDA?
		if(str.contains("KDA")) {
			//mantieni solo la parte dal KDA in poi
			return this.kda.getValueFromTag(str.replaceFirst("Player.", ""));
		}
		//...
		
		
		//non ha che fare con le statistiche, cerca il valore...
		switch(str) {
			case "%Player.name%":{
				return this.player.getName();
			}
			case "%Player.uuid%":{
				return this.uuid.toString();
			}
			//...
			
		}
		
		return "(Error Invalid code: " + str + ")";
	}


	/**
	 * @param pd 
	 * 
	 */
	public static void cacheRemove(PlayerData pd) {
		PlayerData.ListPlayerData.remove(pd);
	}


	/**
	 * @return
	 */
	public static CopyOnWriteArrayList<PlayerData> getListPlayerData() {
		return PlayerData.ListPlayerData;
	}




	

}
