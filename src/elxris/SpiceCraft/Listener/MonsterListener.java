package elxris.SpiceCraft.Listener;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        if(!p.hasPermission("spicecraft.monster")){
            return;
        }
        if(!(entity instanceof LivingEntity)){
            return;
        }
        if(check(entity, p)){
            if(!cobrar(entity, p)){
                return;
            }
        }
    }
    public boolean check(Entity e, Player p){
        if(p.getItemInHand().getTypeId() == 0){
            String name = e.getType().getName();
            if(!getCache().isSet("mobs."+name)){
                return false;
            }
            Chat.mensaje(p, "mobs.infoHead", name);
            for(String s: getCache().getConfigurationSection("mobs."+name).getKeys(false)){
                Chat.mensaje(p, "mobs.infoItem", getAmountIn(e, s), s, getAmountOut(e, s), getItemName(e, s));
            }
            return false;
        }
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
    public int getAmountIn(Entity e, String itemName){
        return getCache().getInt(getPath(e, itemName)+".amountIN");
    }
    public int getAmountIn(Entity e, Player p){
        return getAmountIn(e, getItemHandName(p));
    }
    public int getAmountOut(Entity e, String itemName){
        return getCache().getInt(getPath(e, itemName)+".amountOUT");
    }
    public int getAmountOut(Entity e, Player p){
        return getAmountOut(e, getItemHandName(p));
    }
    public boolean isData(Entity e, String itemName){
        return (getCache().isSet(getPath(e, itemName)+".data") && getCache().isInt(getPath(e, itemName)+".data"));
    }
    public boolean isData(Entity e, Player p){
        return isData(e, getItemHandName(p));
    }
    public short getData(Entity e, String itemName){
        return (short)getCache().getInt(getPath(e, itemName)+".data");
    }
    public short getData(Entity e, Player p){
        return getData(e, getItemHandName(p));
    }
    public String getPath(Entity e, String item){
        return "mobs."+e.getType().getName()+"."+item;
    }
    public String getPath(Entity e, Player p){
        return getPath(e, getItemHandName(p));
    }
    public String getItemHandName(ItemStack item){
        return item.getType().name();
    }
    public String getItemHandName(Player p){
        return getItemHandName(p.getItemInHand());
    }
    public String getItemName(Entity e, String item){
        return getCache().getString(getPath(e, item)+".item");
    }
    public String getItemName(Entity e, Player p){
        return getItemName(e, getItemHandName(p));
    }
    public ItemStack getItem(Entity e, Player p){
        Material m = Material.getMaterial(getItemName(e, p));
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
            int data;
            do{
                data = PotionEffectType.values()[rndm.nextInt(PotionEffectType.values().length-1)+1].getId();
            }while(data == 7||data == 20 || data == 19);
            float n = rndm.nextFloat()/8f;
            for(float i = 1; i < 32; i*=2f){
                if(rndm.nextBoolean()){
                    p.getWorld().playSound(e.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, n*i);
                }
            }
            ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.getById(data), 100, 0, true));
        }
    }
    public boolean cobrar(Entity e, Player p){
        if(!new Econ().cobrar(p, getCache().getDouble("config.price"))){
            // Mensaje de que no tiene dinero suficiente.
            Chat.mensaje(p, "mobs.noMoney");
            return false;
        }
        // Calma al mob.
        if(e instanceof Monster){
            ((Monster) e).setTarget(null);
            ((Monster) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 250, 10000));
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
