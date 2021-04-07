/**
 * 
 */
package it.TownyGDR.Util.Title;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;


/*********************************************************************
 * @author: Elsalamander
 * @data: 1 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class Title extends Reflection{

	public void send(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
	    try {
	        Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
	        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),int.class, int.class, int.class);
	        Object packet = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,fadeInTime, showTime, fadeOutTime);

	        Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
	        Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor( getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),int.class, int.class, int.class);
	        Object timingPacket = timingTitleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,fadeInTime, showTime, fadeOutTime);

	        sendPacket(player, packet);
	        sendPacket(player, timingPacket);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
