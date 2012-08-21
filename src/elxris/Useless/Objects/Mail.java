package elxris.Useless.Objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import elxris.Useless.Utils.Chat;

public class Mail {
    File file;
    FileConfiguration cache;
    Configuration fc;
    Plugin plugin;
    Chat chat;
    
    public Mail(Plugin p){
        plugin = p;
        file = new File(p.getDataFolder(), "mail.yml");
        cache = YamlConfiguration.loadConfiguration(file);
        chat = new Chat(p.getServer());
        fc = p.getConfig();
        load();
    }
    
    public void load(){
        cache = YamlConfiguration.loadConfiguration(file);
        interpreta();
    }
    public void save(){
        try {
            cache.save(file);
        } catch (IOException e) {
            plugin.getLogger().info("ERROR: No se pudo guardar mail.yml");
        }
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
                List<Long> lista = cache.getLongList("usuarios."+k2+"mensajes");
                lista.add(k);
                cache.set("usuarios."+k2+".mensajes", lista);
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
    public void noEliminar(String jugador, Long mail){
        if(!cache.isSet("correos."+mail)){
            return;
        }
        List<String> usuarios = cache.getStringList("correos."+mail+".usuarios");
        usuarios.add(jugador);
        cache.set("correos."+mail+".usuarios", usuarios);
    }
    public String getMail(Long id){
        String mail;
        Date fecha = new Date(id);
        mail = String.format(fc.getString("mbox.mail"), cache.getString("correos."+id+".remitente"),
                fecha.toString(), cache.getString("correos."+id+".mensaje"));
        return mail;
    }
    public void getMailList(String jugador){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        chat.mensaje(jugador, fc.getString("mbox.list"), mensajes.size());
    }
    public void getNextMail(String jugador){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        if(mensajes.size() == 0){
            chat.mensaje(jugador, fc.getString("mbox.listEnd"));
            return;
        }
        cache.set("usuarios."+jugador+".ultimo", cache.getString("correos."+mensajes.get(0)+".remitente"));
        String mensaje = getMail(mensajes.get(0));
        chat.mensaje(jugador, mensaje);
        eliminar(jugador, mensajes.get(0));
    }
    // TODO Crear sistema anti spam
    public void createBorrador(String jugador, String args[]){
        clearBorrador(jugador);
        List<String> destinatarios = new ArrayList<String>();
        for(String k: args){
            if(plugin.getServer().matchPlayer(k).size() >= 1){
                destinatarios.add(k);
            }else{
                chat.mensaje(jugador, fc.getString("mboxc.playerNotExist"), k);
            }
        }
        if(destinatarios.size() >= 1){
            cache.set("usuarios."+jugador+".borrador.destinatarios", destinatarios);
        }else{
            chat.mensaje(jugador, fc.getString("mboxc.noPlayerAdded"));
        }
    }
    public void createReply(String jugador){
        String[] remitente = {cache.getString("usuarios."+jugador+".ultimo")};
        if(remitente[0] == null){
            return;
        }
        createBorrador(jugador, remitente);
    }
    public void setMensaje(String jugador, String mensaje){
        cache.set("usuarios."+jugador+".borrador.mensaje", mensaje);
    }
    public void addMensaje(String jugador, String mensaje){
        String mensajeAnterior;
        if(cache.isSet("usuarios."+jugador+".borrador.mensaje")){
            mensajeAnterior = cache.getString("usuarios."+jugador+".borrador.mensaje");
        }else{
            mensajeAnterior = "";
        }
        setMensaje(jugador, mensajeAnterior+mensaje);
    }
    public void clearMensaje(String jugador){
        setMensaje(jugador, "");
    }
    public void clearBorrador(String jugador){
        cache.set("usuarios."+jugador+".borrador", null);
    }
    public void sendMensaje(String jugador, List<String> destinatarios, String mensaje){
        long fecha = System.currentTimeMillis();
        List<Long> mensajes = cache.getLongList("correos.mensajes");
        mensajes.add(fecha);
        cache.set("correos.mensajes", mensajes);
        String path = "correos."+fecha+".";
        cache.set(path+"remitente", jugador);
        cache.set(path+"mensaje", mensaje);
        cache.set(path+"usuarios", destinatarios);
        for(String k: destinatarios){
            chat.mensaje(k, fc.getString("mboxc.catched"));
        }
        chat.mensaje(jugador, fc.getString("mboxc.sended"));
        clearBorrador(jugador);
    }
    public void sendMensaje(String jugador){
        List<String> destinatarios = cache.getStringList("usuarios."+jugador+".borrador.destinatarios");
        String mensaje = cache.getString("usuarios."+jugador+".borrador.mensaje");
        sendMensaje(jugador, destinatarios, mensaje);
    }
    public void sendMensajeATodos(String jugador){
        List<String> destinatarios = new ArrayList<>();
        for(OfflinePlayer p: plugin.getServer().getOfflinePlayers()){
            destinatarios.add(p.getName());
        }
        if(!plugin.getServer().getPlayer(jugador).hasPermission("useless.mail.massive")){
            return;
        }
        sendMensaje("Servidor", destinatarios, cache.getString("usuarios."+jugador+".borrador.mensaje"));
    }
}
