/**
 * 
 */
package it.TownyGDR.Towny;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import it.TownyGDR.TownyGDR;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.Task.TaskLocation;
import it.TownyGDR.Util.Exception.City.ExceptionCityImpossibleLoad;

/*********************************************************************
 * @author: Elsalamander
 * @data: 12 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Tutte le Aree come città/spawn/miniere/foreste/Parchi?/Altro
 * sono aree ben definite che sono generalizzate da questa classe
 * 
 *********************************************************************/
public abstract class Luogo {
	
	private static BukkitTask taskLocation;
	private static TaskLocation taskLocationObj;

	/**
	 * Ritorna che tipo di luogo è
	 * @return
	 */
	public abstract LuoghiType getType();

	/**
	 * Ogni luogo ha un id, che è gestito nella classe specifica del luogo.
	 * @return
	 */
	public abstract int getId();
	
	/**
	 * Ritorna tremite type e id il luogo specifico
	 * @param type
	 * @param id
	 * @return
	 * @throws ExceptionCityImpossibleLoad 
	 */
	public static Luogo getById(LuoghiType type, int id) throws ExceptionCityImpossibleLoad {
		switch(type){
			case City:{
				return City.getByID(id);
			}
			
			default:
				break;
			
		}
		
		return null;
	}

	/**
	 * 
	 */
	public static void saveAll() {
		for(City city : City.ListCity){
			try{
				city.save(null);
			}catch(IOException e){
				//Error
			}
		}
		
	}

	/**
	 * @param townyGDR
	 */
	public static void startTask(TownyGDR townyGDR) {
		taskLocationObj = new TaskLocation();
		
		taskLocation = Bukkit.getScheduler().runTaskTimerAsynchronously(townyGDR, taskLocationObj, 0, 20);
	}

	/**
	 * 
	 */
	public static void stopTask() {
		taskLocationObj.stop();
		taskLocation.cancel();
	}
	
}
