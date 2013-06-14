package elxris.SpiceCraft.Listener;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Chat;
import elxris.SpiceCraft.Utils.Econ;

public class MonsterListener implements Listener {
    public static Archivo file;
    public static FileConfiguration cache;
    @EventHandler
    public void interactListener(PlayerInteractEntityEvent event){
        Player p = event.getPlayer();
        Entity entity = event.getRightClicked();
        if(!event.getPlayer().hasPermission("spicecraft.monster")){
            return;
        }
        if(!(event.getRightClicked() instanceof LivingEntity)){
            return;
        }
        if(check(entity, p)){
            if(!cobrar(entity, p)){
                return;
            }
        }
    }
    public boolean check(Entity e, Player p){
        if(getCache().contains(getPath(e, p))){
            if(getAmountIn(e, p) <= p.getItemInHand().getAmount()){
                return true;
            }else{
                // Mensaje de que no son suficientes.
                Chat.mensaje(p, "mobs.notEnough");
            }
        }
        return false;
    }
    public int getAmountIn(Entity e, Player p){
        return getCache().getInt(getPath(e, p)+".amountIN");
    }
    public int getAmountOut(Entity e, Player p){
        return getCache().getInt(getPath(e, p)+".amountOUT");
    }
    public boolean isData(Entity e, Player p){
        return (getCache().isSet(getPath(e, p)+".data") && getCache().isInt(getPath(e, p)+".data"));
    }
    public short getData(Entity e, Player p){
        return (short)getCache().getInt(getPath(e, p)+".data");
    }
    public String getPath(Entity e, Player p){
        return "mobs."+e.getType().getName()+"."+p.getItemInHand().getType().name();
    }
    public ItemStack getItem(Entity e, Player p){
        Material m = Material.getMaterial(getCache().getString(getPath(e, p)+".item"));
        ItemStack item = new ItemStack(m, getAmountOut(e, p));
        if(isData(e, p)){
            item.setDurability(getData(e, p));
        }
        return item;
    }
    public void dropItem(Entity e, Player p){
        p.getWorld().dropItemNaturally(e.getLocation(), getItem(e, p));
        playEffect(e, p);
    }
    public void playEffect(Entity e, Player p){
        Random rndm = new Random();
        if(getCache().getBoolean("config.visual")){
            int data = PotionType.values()[rndm.nextInt(PotionType.values().length)].getDamageValue();
            p.getWorld().playEffect(e.getLocation(), Effect.POTION_BREAK, data);
        }
    }
    public boolean cobrar(Entity e, Player p){
        if(!new Econ().cobrar(p, getCache().getDouble("config.price"))){
            // Mensaje de que no tiene dinero suficiente.
            Chat.mensaje(p, "mobs.noMoney");
            return false;
        }
        // Calma al mob.
        if(e instanceof Creature){
            ((Creature) e).setTarget(null);
        }
        dropItem(e, p);
        ItemStack item = p.getItemInHand();
        item.setAmount(item.getAmount()-getAmountIn(e, p));
        if(item.getAmount() == 0){
            item = null;
        }
        p.setItemInHand(item);
        return true;
    }
    public static FileConfiguration getCache(){
        if(cache == null){
            cache = getArchivo().load();
        }
        return cache;
    }
    public static Archivo getArchivo(){
        if(file == null){
            file = new Archivo("mobs.yml");
        }
        return file;
    }
}
