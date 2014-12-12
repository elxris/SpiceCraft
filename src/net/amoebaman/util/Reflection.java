package net.amoebaman.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

/**
 * Contains static methods useful for utilizing reflection as a workaround
 * and alternative for directly importing NMS (net.minecraft.server) and
 * OBC (org.bukkit.craftbukkit) classes, as plugins importing those classes
 * will break with each update of Bukkit/CraftBukkit.
 * 
 * @author AmoebaMan
 */

public class Reflection {
	/**
	 * Gets the version string used in the current version of
	 * Bukkit/CraftBukkit's package tree, to be used to access classes via
	 * reflection.
	 * 
	 * @return the package version
	 */
	public static String getVersion(){
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1);
        return version;
	}

	/**
	 * Gets a NMS (net.minecraft.server) class by its name.
	 * 
	 * @param className the name of the class to grab
	 * @return the class, or null if none was found
	 */
    public static Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + getVersion() + "." + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        }
        catch (Exception e) { e.printStackTrace(); }
        return clazz;
    }
    
    /**
     * Gets an OBC (org.bukkit.craftbukkit) class by its name, including any
     * package and subpackage names.
     * 
     * @param className the name of the class to grab
     * @return the class, or null if none was found
     */
    public static Class<?> getOBCClass(String className){
        String fullName = "org.bukkit.craftbukkit." + getVersion() + "." + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        }
        catch (Exception e) { e.printStackTrace(); }
        return clazz;
    }

    /**
	 * Invokes the getHandle() method (used by many CraftBukkit wrapper classes)
	 * to get the NMS instance behind an object.
	 * 
	 * @param obj the object in question
	 * @return the object's NMS handle, or null if none was found
	 */
    public static Object getHandle(Object obj) {
        try {
            return getMethod(obj.getClass(), "getHandle").invoke(obj);
        }
        catch (Exception e){
            e.printStackTrace(); 
            return null;
        }
    }

    /**
     * Gets a field belonging to a class by its name.
     * 
     * @param clazz a class
     * @param name the name of a field within the class
     * @return the field in question, or null if none was found
     */
    public static Field getField(Class<?> clazz, String name) {
        try {
        	Field field = clazz.getDeclaredField(name);
        	field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a method belonging to a class by its name, and optionally
     * by its parameter types if precision is required.
     * 
     * @param clazz a class
     * @param name the name of the desired method
     * @param args the parameter types of the desired method, in order
     * @return the method in question, or null if none was found
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods()) 
            if (m.getName().equals(name) && (args.length == 0 || classListEqual(args, m.getParameterTypes()))){
            	m.setAccessible(true);
                return m;
            }
        return null;
    }

    /**
     * Compares two class arrays to see if they are the same
     * @param l1 a class array
     * @param l2 another class array
     * @return true if the two arrays contain the same classes in the same order, false otherwise
     */
    public static boolean classListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }
}
