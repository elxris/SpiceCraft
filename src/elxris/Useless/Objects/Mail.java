package elxris.Useless.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Fecha;
import elxris.Useless.Utils.Strings;

public class Mail {
    FileConfiguration cache;
    MemoryConfiguration draft;
    Archivo archivo;
    
    public Mail(){
        archivo = new Archivo("mail.yml");
        draft = new MemoryConfiguration();
        load();
    }
    public void load(){
        cache = archivo.load();
        interpreta();
    }
    public void save(){
        archivo.save(cache);
        load();
    }
    public void interpreta(){
        if(!cache.isSet("msg")){
            sendMensajeATodos("Server", Strings.getString("mail.first"));
            return;
        }
        Set<String> listacorreos = cache.getConfigurationSection("msg").getKeys(false);
        for(String k: listacorreos){
            List<String> usuarios = cache.getStringList("msg."+k+".usuarios");
            if(usuarios.size() == 0 || System.currentTimeMillis()-Long.parseLong(k) >= 1296000000L ){
                // Los correos mayores a 15 d�as (15*24*60*60*1000) milisegundos, se eliminan.
                cache.set("msg."+k, null);
            }
        }
    }
    public void eliminar(String jugador, Long mail){
        List<String> usuarios = cache.getStringList("msg."+mail+".usuarios");
        usuarios.remove(jugador);
        cache.set("msg."+mail+".usuarios", usuarios);
    }
    public void eliminarAll(String jugador){
        if(!cache.isSet("msg")){
            return;
        }
        Set<String> mensajes = cache.getConfigurationSection("msg").getKeys(false);
        for(String lng: mensajes){
            cache.getStringList(lng+".usuarios").remove(jugador);
        }
        save();
    }
    public String[] getMail(Long id){
        String remitente = cache.getString("msg."+id+".remitente");
        if(cache.getBoolean("msg."+id+".servidor") == true){
            remitente = "Servidor";
        }
        String[] mail = {remitente,
                Fecha.formatoFecha(id),
                cache.getString("msg."+id+".mensaje"), remitente};
        return mail;
    }
    public void getMailList(String jugador){
        int mensajes = 0;
        Set<String> mail = cache.getConfigurationSection("msg").getKeys(false);
        for(String id: mail){
            for(String s: cache.getStringList("msg."+id+".usuarios")){
                if(s.contentEquals(jugador)){
                    mensajes++;
                }
            }
        }
        Chat.mensaje(jugador, "mail.list", mensajes);
    }
    public void getNextMail(String jugador, Boolean eliminar){ //Obtiene todos los correos.
        List<String> mensajes = new ArrayList<String>();
        Set<String> mail = cache.getConfigurationSection("msg").getKeys(false);
        for(String id: mail){
            for(String s: cache.getStringList("msg."+id+".usuarios")){
                if(s.contentEquals(jugador)){
                    mensajes.add(id);
                }
            }
        }
        if(mensajes.size() == 0){
            Chat.mensaje(jugador, "mail.listEnd");
            return;
        }
        Chat.mensaje(jugador, "mail.readStart");
        // Enviando cada uno de los mensajes.
        for(String lng: mensajes){
            String[] mensaje = getMail(Long.parseLong(lng));
            Chat.mensaje(jugador, "mail.mail", mensaje);
            if(eliminar){
                eliminar(jugador, Long.parseLong(lng));
            }
        }
        Chat.mensaje(jugador, "mail.readFinish");
    }
    public void createBorrador(String jugador, String args[]){ //Inicia el borrador.
        clearBorrador(jugador);
        List<String> destinatarios = checkDestinatarios(jugador, args);
        if(destinatarios.size() >= 1){
            draft.set(jugador+".destinatarios", destinatarios);
            Chat.mensaje(jugador, "mail.created");
        }else{
            Chat.mensaje(jugador, "mail.noPlayerAdded");
        }
    }
    public void setMensaje(String jugador, String mensaje){
        draft.set(jugador+".mensaje", mensaje);
    }
    public void addMensaje(String jugador, String mensaje){
        if(draft.getStringList(jugador+".destinatarios").size() < 1){
            Chat.mensaje(jugador, "mail.noMessage");
            return;
        }
        String mensajeAnterior = "";
        if(draft.isSet(jugador+".mensaje")){
            mensajeAnterior = draft.getString(jugador+".mensaje");
        }
        if(mensajeAnterior.length() > Strings.getInt("mail.v.maxChar")){
            if(!Useless.getPlayer(jugador).hasPermission("useless.mail.noCharLimit")){
                Chat.mensaje(jugador, "mail.limit");
                return;
            }
        }
        setMensaje(jugador, mensajeAnterior+" "+mensaje);
        Chat.mensaje(jugador, "mail.add");
    }
    public void clearMensaje(String jugador){
        setMensaje(jugador, "");
    }
    public void clearBorrador(String jugador){
        draft.set(jugador, null);
    }
    public void sendMensaje(String jugador, List<String> destinatarios, String mensaje, Boolean servidor){
        destinatarios = checkDestinatarios(jugador, destinatarios.toArray(new String[0]));
        if(destinatarios.size() < 1){
            return;
        }
        long fecha = System.currentTimeMillis();
        String path = "msg."+fecha+".";
        cache.set(path+"remitente", jugador);
        cache.set(path+"servidor", servidor);
        cache.set(path+"mensaje", mensaje);
        cache.set(path+"usuarios", destinatarios);
        for(String k: destinatarios){
            Chat.mensaje(k, "mail.catched");
        }
        Chat.mensaje(jugador, "mail.sended");
        clearBorrador(jugador);
        save();
    }
    public void sendMensaje(String jugador){
        if(!hasMensaje(jugador)){
            return;
        }
        List<String> destinatarios = draft.getStringList(jugador+".destinatarios");
        String mensaje = draft.getString(jugador+".mensaje");
        sendMensaje(jugador, destinatarios, mensaje, false);
    }
    public void sendMensajeATodos(String jugador){
        if(!hasMensaje(jugador)){
            return;
        }
        sendMensajeATodos(jugador, draft.getString(jugador+".mensaje"));
    }
    public void sendMensajeATodos(String jugador, String mensaje){
        if(!jugador.contentEquals("Server") && !Useless.getPlayer(jugador).hasPermission("useless.mail.massive")){
            return;
        }
        List<String> destinatarios = new ArrayList<>();
        for(OfflinePlayer p: Useless.plugin().getServer().getOfflinePlayers()){
            destinatarios.add(p.getName());
        }
        sendMensaje(jugador, destinatarios, mensaje, true);
    }
    public String isDestinatario(String player){
        List<Player> l = Useless.plugin().getServer().matchPlayer(player);
        if(l.size() == 1){
            return l.get(0).getName();
        }else{
            if(Useless.plugin().getServer().getOfflinePlayer(player).hasPlayedBefore()){
                return player;
            }
        }
        return null;
    }
    public boolean hasMensaje(String jugador){
        if(!draft.isSet(jugador+".mensaje")){
            Chat.mensaje(jugador, "mail.noMessage");
            return false;
        }
        return true;
    }
    public List<String> checkDestinatarios(String jugador, String[] destinatarios){
        List<String> checked = new ArrayList<String>();
        for(String s: destinatarios){
            String destinatario = isDestinatario(s);
            if(destinatario != null){
                checked.add(destinatario);
            }else{
                Chat.mensaje(jugador, "mail.playerNotExist", s);
            }
        }
        return checked;
    }
}
