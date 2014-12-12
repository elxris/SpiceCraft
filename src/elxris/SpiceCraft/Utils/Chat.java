package elxris.SpiceCraft.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.MissingFormatArgumentException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.amoebaman.util.Reflection;
import elxris.SpiceCraft.SpiceCraft;

public class Chat {
    private static void enviar(Player p, String mensaje, Object... i){
        if(mensaje == null || mensaje == ""){
            return;
        }
        if(p == null){
            return;
        }
        String m;
        try{
            m = String.format(mensaje, i);
            Object handle = Reflection.getHandle(p);
			Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
			Reflection.getMethod(connection.getClass(), "sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, createChatPacket(m));
        }catch(IllegalFormatException e){
            p.sendMessage(mensaje);
            SpiceCraft.log("MISSING FORMAT ARGUMENT EXCEPTION \n" +
                    "Caused by: "+mensaje);
        } catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			m = String.format(mensaje, i);
			p.sendMessage(m);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /* This part is thank to Fanciful
     * 
     * */
    
    private static Constructor<?> nmsPacketPlayOutChatConstructor;
    // The ChatSerializer's instance of Gson
 	private static Object nmsChatSerializerGsonInstance;
 	private static Method fromJsonMethod;

 	private static Object createChatPacket(String json) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
 		if(nmsChatSerializerGsonInstance == null){
 			// Find the field and its value, completely bypassing obfuscation
 			for(Field declaredField : Reflection.getNMSClass("ChatSerializer").getDeclaredFields()){
 				if(Modifier.isFinal(declaredField.getModifiers()) && Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType().getName().endsWith("Gson")){
 					// We've found our field
 					declaredField.setAccessible(true);
 					nmsChatSerializerGsonInstance = declaredField.get(null);
 					fromJsonMethod = nmsChatSerializerGsonInstance.getClass().getMethod("fromJson", String.class, Class.class);
 					break;
 				}
 			}
 		}

 		// Since the method is so simple, and all the obfuscated methods have the same name, it's easier to reimplement 'IChatBaseComponent a(String)' than to reflectively call it
 		// Of course, the implementation may change, but fuzzy matches might break with signature changes
 		Object serializedChatComponent = fromJsonMethod.invoke(nmsChatSerializerGsonInstance, json, Reflection.getNMSClass("IChatBaseComponent"));
 		
 		if(nmsPacketPlayOutChatConstructor == null){
			try {
				nmsPacketPlayOutChatConstructor = Reflection.getNMSClass("PacketPlayOutChat").getDeclaredConstructor(Reflection.getNMSClass("IChatBaseComponent"));
				nmsPacketPlayOutChatConstructor.setAccessible(true);
			} catch (NoSuchMethodException e) {
				SpiceCraft.log("Could not find Minecraft method or constructor.");
			} catch (SecurityException e) {
				SpiceCraft.log("Could not access constructor.");
			}
		}
 		
 		return nmsPacketPlayOutChatConstructor.newInstance(serializedChatComponent);
 	}
 	
    public static void mensaje(Player p, List<String> list, Object... i){
        enviar(p, Strings.parseList(list), i);
    }
    public static void mensaje(Player p, String path, Object... i){
        if(path == null){
            return;
        }
        if(Strings.getStringList(path) == null){
            enviar(p, path);
            return;
        }
        mensaje(p, Strings.getStringList(path), i);
    }
    public static void mensaje(String p, String m, Object... i){
        Player jugador = SpiceCraft.getOnlinePlayer(p);
        if(jugador == null){
            return;
        }
        mensaje(jugador, m, i);
    }
}
