package it.TownyGDR.Util;

import java.io.File;

import it.TownyGDR.TownyGDR;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/

public class Util {
	
	public static String[] getListNameFile(String path) {
		File cartella = new File(TownyGDR.getInstance().getDataFolder(), path);
		
		//se non esiste la cartella
		if(!cartella.exists()){	
			//non esiste la cartella!
			cartella.mkdirs();
		}
		return cartella.list() == null ? new String[0] : cartella.list();
	}

}
