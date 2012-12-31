package elxris.Useless.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import elxris.Useless.Useless;
import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Fecha;

public class Mail {
    FileConfiguration cache;
    Configuration fc;
    Useless plugin;
    Chat chat;
    Archivo archivo;
    
    public Mail(Useless plugin){
        setPlugin(plugin);
        archivo = new Archivo(plugin, "mail.yml");
        chat = plugin.getChat();
        fc = plugin.getConfig();
        load();
    }
    
    public void load(){
        cache = archivo.load();
        interpreta();
    }
    public void save(){
        archivo.save(cache);
    }
    public void interpreta(){
        List<Long> listacorreos = cache.getLongList("correos.mensajes");
        List<Long> remover = cache.getLongList("correos.mensajes");
        //Quitar los mensajes anteriores.
        for(OfflinePlayer p: plugin.getServer().getOfflinePlayers()){
            cache.set("usuarios."+p.getName()+".mensajes", null);
        }
        for(Long k: listacorreos){
            List<String> usuarios = cache.getStringList("correos."+k+".usuarios");
            if(usuarios.size() == 0){
                cache.set("correos."+k, null);
                remover.remove(k);
            }
            for(String k2: usuarios){
                List<Long> listaAnterior = new ArrayList<Long>();
                if(cache.isSet("usuarios."+k2+".mensajes")){
                    listaAnterior = cache.getLongList("usuarios."+k2+".mensajes");
                    listaAnterior.add(k);
                }else{
                    listaAnterior.add(k);
                }
                cache.set("usuarios."+k2+".mensajes", listaAnterior);
            }
        }
        cache.set("correos.mensajes", remover);
        save();
    }
    public void eliminar(String jugador, Long mail){
        List<String> usuarios = cache.getStringList("correos."+mail+".usuarios");
        usuarios.remove(jugador);
        cache.set("correos."+mail+".usuarios", usuarios);
    }
    public void eliminarAll(String jugador){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        for(Long lng: mensajes){
            List<String> usuarios = cache.getStringList("correos."+lng+".usuarios");
            usuarios.remove(jugador);
            cache.set("correos."+lng+".usuarios", usuarios);
        }
    }
    /*public void noEliminar(String jugador, Long mail){
        if(!cache.isSet("correos."+mail)){
            return;
        }
        List<String> usuarios = cache.getStringList("correos."+mail+".usuarios");
        usuarios.add(jugador);
        cache.set("correos."+mail+".usuarios", usuarios);
    }*/
    public String[] getMail(Long id){
        String remitente = cache.getString("correos."+id+".remitente");
        if(cache.getBoolean("correos."+id+".servidor") == true){
            remitente = "Servidor";
        }
        String[] mail = {remitente,
                Fecha.formatoFecha(fc, id, fc.getString("f.units").split(" "), fc.getString("f.months").split(" ")),
                cache.getString("correos."+id+".mensaje"), remitente};
        return mail;
    }
    public void getMailList(String jugador){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        chat.mensaje(jugador, "mbox.list", mensajes.size());
    }
    public void getNextMail(String jugador, Boolean eliminar){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        if(mensajes.size() == 0){
            chat.mensaje(jugador, "mbox.listEnd");
            return;
        }
        for(long lng: mensajes){
            String[] mensaje = getMail(lng);
            chat.mensaje(jugador, "mbox.mail", mensaje);
            if(eliminar){
                eliminar(jugador, lng);
            }
        }
    }
    public void createBorrador(String jugador, String args[]){
        clearBorrador(jugador);
        List<String> destinatarios = new ArrayList<String>();
        for(String k: args){
            if(plugin.getServer().matchPlayer(k).size() >= 1){
                destinatarios.add(k);
            }else{
                chat.mensaje(jugador, "mboxc.playerNotExist", k);
            }
        }
        if(destinatarios.size() >= 1){
            cache.set("usuarios."+jugador+".borrador.destinatarios", destinatarios);
            chat.mensaje(jugador, "mboxc.created");
        }else{
            chat.mensaje(jugador, "mboxc.noPlayerAdded");
        }
    }
    public void setMensaje(String jugador, String mensaje){
        cache.set("usuarios."+jugador+".borrador.mensaje", mensaje);
    }
    public void addMensaje(String jugador, String mensaje){
        if(cache.getStringList("usuarios."+jugador+".borrador.destinatarios").size() < 1){
            chat.mensaje(jugador, "string", "§cError.");
            chat.mensaje(jugador, "mboxc.info");
            return;
        }
        String mensajeAnterior;
        if(cache.isSet("usuarios."+jugador+".borrador.mensaje")){
            mensajeAnterior = cache.getString("usuarios."+jugador+".borrador.mensaje");
        }else{
            mensajeAnterior = "";
        }
        if(mensajeAnterior.length() > 300){
            chat.mensaje(jugador, "mboxc.limit");
        }
        setMensaje(jugador, mensajeAnterior+" "+mensaje);
        chat.mensaje(jugador, "mboxc.add");
    }
    public void clearMensaje(String jugador){
        setMensaje(jugador, "");
    }
    public void clearBorrador(String jugador){
        cache.set("usuarios."+jugador+".borrador", null);
        save();
    }
    public void sendMensaje(String jugador, List<String> destinatarios, String mensaje, Boolean servidor){
        if(!cache.isSet("usuarios."+jugador+".borrador.mensaje")){
            chat.mensaje(jugador, "string", "§cError.");
            chat.mensaje(jugador, "mboxc.info");
            return;
        }
        long fecha = System.currentTimeMillis();
        List<Long> mensajes = cache.getLongList("correos.mensajes");
        mensajes.add(fecha);
        cache.set("correos.mensajes", mensajes);
        String path = "correos."+fecha+".";
        cache.set(path+"remitente", jugador);
        cache.set(path+"servidor", servidor);
        cache.set(path+"mensaje", mensaje);
        cache.set(path+"usuarios", destinatarios);
        for(String k: destinatarios){
            chat.mensaje(k, "mboxc.catched");
        }
        chat.mensaje(jugador, "mboxc.sended");
        clearBorrador(jugador);
    }
    public void sendMensaje(String jugador){
        List<String> destinatarios = cache.getStringList("usuarios."+jugador+".borrador.destinatarios");
        String mensaje = cache.getString("usuarios."+jugador+".borrador.mensaje");
        sendMensaje(jugador, destinatarios, mensaje, false);
    }
    public void sendMensajeATodos(String jugador){
        if(!plugin.getServer().getPlayer(jugador).hasPermission("useless.mail.massive")){
            return;
        }
        List<String> destinatarios = new ArrayList<>();
        for(OfflinePlayer p: plugin.getServer().getOfflinePlayers()){
            destinatarios.add(p.getName());
        }
        sendMensaje(jugador, destinatarios, cache.getString("usuarios."+jugador+".borrador.mensaje"), true);
    }
    
    public void setPlugin(Useless plugin) {
        this.plugin = plugin;
    }
    public Plugin getPlugin() {
        return plugin;
    }
}
