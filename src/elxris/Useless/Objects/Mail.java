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
import org.bukkit.entity.Player;
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
        for(Long k: listacorreos){
            List<String> usuarios = cache.getStringList("correos."+k+".usuarios");
            int cuenta = 0;
            for(String k2: usuarios){
                cuenta++;
                List<Long> lista = cache.getLongList("usuarios."+k2+"mensajes");
                lista.add(k);
                cache.set("usuarios."+k2+".mensajes", k);
            }
            if(cuenta == 0){
                cache.set("correos."+k, null);
                listacorreos.remove(k);
            }
        }
        cache.set("correos.mensajes", listacorreos);
        save();
    }
    public void eliminar(String jugador, Long mail){
        List<String> usuarios = cache.getStringList("correos."+mail+".usuarios");
        usuarios.remove(jugador);
        cache.set("correos."+mail+".usuarios", usuarios);
        save();
    }
    public void noEliminar(String jugador, Long mail){
        List<String> usuarios = cache.getStringList("correos."+mail+".usuarios");
        usuarios.add(jugador);
        cache.set("correos."+mail+".usuarios", usuarios);
        save();
    }
    public String getMail(Long id){
        String mail = "";
        Date fecha = new Date(id);
        mail.concat(String.format(fc.getString("mabox.mail"), cache.getString("correos."+id+".remitente"),
                fecha.toString(), cache.getString("correos."+id+".mensaje")));
        return mail;
    }
    public void getMailList(String jugador){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        chat.mensaje(jugador, fc.getString("mbox.list"), mensajes.size());
    }
    public void getNextMail(String jugador){
        List<Long> mensajes = cache.getLongList("usuarios."+jugador+".mensajes");
        String mensaje = getMail(mensajes.get(0));
        chat.mensaje(jugador, mensaje);
        cache.set("usuarios."+jugador+".ultimo", mensajes.get(0));
        eliminar(jugador, mensajes.get(0));
    }
    public void keepMail(String jugador){
        noEliminar(jugador, cache.getLong("usuarios."+jugador+".ultimo"));
    }
    public void createBorrador(String jugador, String args[]){
        List<String> destinatarios = new ArrayList<String>();
        for(String k: args){
            if(plugin.getServer().matchPlayer(k).get(0) != null){
                destinatarios.add(k);
            }else{
                chat.mensaje(jugador, fc.getString("mbox.playerNotExist"), k);
            }
        }
        if(destinatarios.size() > 1){
            cache.set("usuarios."+jugador+".borrador.destinatarios", destinatarios);
        }else{
            chat.mensaje(jugador, fc.getString("mboxc.noPlayerAdded"));
        }
        clearMensaje(jugador);
    }
    public void createReply(String jugador){
        String[] remitente = {cache.getString("correos."+cache.getLong("usuarios."+jugador+".ultimo")+".remitente")};
        createBorrador(jugador, remitente);
        clearMensaje(jugador);
    }
    public void setMensaje(String jugador, String mensaje){
        cache.set("usuarios."+jugador+".borrador.mensaje", mensaje);
    }
    public void addMensaje(String jugador, String mensaje){
        String mensajeAnterior = cache.getString("usuarios."+jugador+".borrador.mensaje");
        setMensaje(jugador, mensajeAnterior+mensaje);
    }
    public void clearMensaje(String jugador){
        setMensaje(jugador, "");
    }
    public void sendMensaje(String jugador, List<String> destinatarios, String mensaje){
        long fecha = System.currentTimeMillis();
        // TODO Grabar el mensaje
        // TODO Borrar el contenido del borrador.
    }
    public void sendMensaje(String jugador){
        List<String> destinatarios = cache.getStringList("");
        String mensaje = cache.getString("");
        // TODO Terminar de poner el path
        sendMensaje(jugador, destinatarios, mensaje);
    }
    public void sendMensajeATodos(String jugador){
        List<String> destinatarios = new ArrayList<>();
        for(OfflinePlayer p: plugin.getServer().getOfflinePlayers()){
            destinatarios.add(p.getName());
        }
        // TODO Limitar a los que tienen permisos.
        sendMensaje("Servidor", destinatarios, cache.getString("usuarios."+jugador+".borrador.mensaje"));
    }
}
