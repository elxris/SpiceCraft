package elxris.SpiceCraft.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import elxris.SpiceCraft.Utils.Econ;
import elxris.SpiceCraft.Utils.Strings;

public class FactoryGui
{
    private static Factory f;
    private static ItemStack itemNext;
    private static ItemStack itemPrevious;
    private static ItemStack itemReturn;
    public static final int SIZE = 27;
    private Player p;
    // 0 - 53 slots.
    public FactoryGui(Factory f){
        FactoryGui.f = f;
    }
    public FactoryGui(Player p){
        this.p = p;
    }
    public void updateInventory(Inventory inv){
        inv.clear();
        List<ItemStack> items;
        int itemsSize;
        if(isRelativeSet("list")){
            // Si existe una lista de objetos.
            itemsSize = getItemListSize(getPath());
        }else if(isRelativeSet("sub")){
            // Si existe un submenu
            itemsSize = getItemMenuSize(getPath("sub"));
        }else if(isUserShop()){
            itemsSize = getItemUserSize(getPath("items"));
        }else{
            return;
        }
        int itemsPerPage = SIZE;
        // Si es la tienda del servidor, y no es una tienda particular, incluye un return;
        if(!isRelativeSet("return") && !isUserShop()){
            inv.addItem(getReturn());
            itemsPerPage--;
        }
        if(itemsSize > itemsPerPage){
            itemsPerPage -= 2;
            if(getPage() > 1){
                inv.setItem((SIZE-2)-itemsPerPage, getPrev());
            }
            if(itemsSize-(getPage()*itemsPerPage)>=1){
                inv.setItem((SIZE-1)-itemsPerPage, getNext());
            }
        }
        if(isRelativeSet("list")){
            // Si existe una lista de objetos.
            items = getItemList(getPath(), itemsPerPage, getPage());
        }else if(isRelativeSet("sub")){
            // Si existe un submenu
            items = getItemMenu(getPath("sub"));
        }else if(isUserShop()){
            items = getItemUser(getPath("items"), itemsPerPage, getPage());
        }else{
            return;
        }
        
        int count = 0;
        for(int i = SIZE-itemsPerPage; i < SIZE; i++){
            inv.setItem(i, items.get(count));
            if(++count >= items.size()){
                break;
            }
        }
    }
    public List<ItemStack> getItemList(String path, int itemsPerPage, int page){
        List<ItemStack> list = new ArrayList<ItemStack>();
        Econ econ = new Econ();
        // Inicia algunas variables que se usarán en el loop.
        ItemStack i;
        ItemMeta meta;
        String id;
        double precio;
        int limitUP = (page)*itemsPerPage;
        int limitDOWN = (--page)*itemsPerPage;
        int count = 0;
        for(String item : getConfig().getStringList(path+".list")){
            if(count < limitDOWN){
                count++;
                continue;
            }else if(count >= limitUP){
                break;
            }
            count++;
            i = f.createItem(p, item, 1);
            meta = i.getItemMeta();
            id = i.getTypeId()+((i.getDurability() > 0)?":"+i.getDurability():"");
            precio = f.getPrice(item);
            meta.setLore(Strings.getStringList("shop.itemLore",
                    econ.getPrecio(precio), econ.getPrecio(precio*f.SELLRATE),
                    f.getProduction(item), id));
            i.setItemMeta(meta);
            list.add(i);
        }
        return list;
    }
    public List<ItemStack> getItemMenu(String path){
        List<ItemStack> list = new ArrayList<ItemStack>();
        for(String item : getConfig().getConfigurationSection(path).getKeys(false)){
            String name = getConfig().getString(path+"."+item+".name");
            ItemStack i = f.createItem(p, item, 1);
            ItemMeta m = i.getItemMeta();
            m.setDisplayName(name);
            i.setItemMeta(m);
            list.add(i);
        }
        return list;
    }
    public List<ItemStack> getItemUser(String path, int itemsPerPage, int page){
        List<ItemStack> list = new ArrayList<ItemStack>();
        ItemStack i;
        ItemMeta meta;
        String id;
        double precio;
        int limitUP = (page)*itemsPerPage;
        int limitDOWN = (--page)*itemsPerPage;
        int count = 0;
        ConfigurationSection config = getConfig().getConfigurationSection(path);
        for(String item : config.getKeys(false)){
            if(count < limitDOWN){
                count++;
                continue;
            }else if(count >= limitUP){
                break;
            }
            i = f.createItem(p, item, 1);
            meta = i.getItemMeta();
            id = i.getTypeId()+((i.getDurability() > 0)?":"+i.getDurability():"");
            precio = (f.getPrice(item)/f.MULTIPLIER)*f.USERMULTIPLIER;
            meta.setLore(Strings.getStringList("shop.userItemLore", precio,
                    config.get(item), id));
            i.setItemMeta(meta);
            list.add(i);
        }
        return list;
    }
    public int getItemListSize(String path){
        return getConfig().getStringList(path+".list").size();
    }
    public int getItemMenuSize(String path){
        return getConfig().getConfigurationSection(path).getKeys(false).size();
    }
    public int getItemUserSize(String path){
        return getConfig().getConfigurationSection(path).getKeys(false).size();
    }
    public ItemStack getNext(){
        if(itemNext == null){
            itemNext = getItemMenu("shop.next").get(0);
        }
        return itemNext;
    }
    public ItemStack getPrev(){
        if(itemPrevious == null){
            itemPrevious = getItemMenu("shop.previous").get(0); 
        }
        return itemPrevious;
    }
    public ItemStack getReturn(){
        if(itemReturn == null){
            itemReturn = getItemMenu("shop.return").get(0);
        }
        return itemReturn;
    }
    /*
    click[Top/Bot][Cursor][Slot]
    */
    public boolean click(final InventoryClickEvent e){
        boolean cancelled = false;
        ItemStack cursor = e.getCursor();
        int currentItem = e.getRawSlot();
        InventoryView view = e.getView();
        ClickType click = e.getClick();
        // Si el click es dentro del inventario de la tienda.
        if(e.getRawSlot() < e.getInventory().getSize()){
            cancelled = clickTopCursorSlot(view, click, cursor, currentItem);
        // Si el click es fuera del inventario de la tienda.
        }else{
            // Cancela lo que no sea un clic derecho, izquierdo o doble click.
            cancelled = clickBotCursorSlot(view, click, cursor, currentItem);
        }
        return cancelled;
    }
    // Click en la tienda con algo en mano y algo en el slot.
    public boolean clickTopCursorSlot(InventoryView view, ClickType click, ItemStack cursor, int currentItem){
        boolean cancelled = false;
        // Si tiene el cursor está vacio.
        if(cursor.getTypeId() == 0){
            clickTopSlot(view, click, currentItem);
        // Si el cursor tiene objeto.
        }else{
            int amount = cursor.getAmount();
            // Si es izquierdo, vende el stack.
            if(click == ClickType.LEFT){
                f.sellItem(p, cursor);
                cursor.setAmount(0);
            // Si es derecho, vende uno.
            }else if(click == ClickType.RIGHT){
                cursor.setAmount(1);
                f.sellItem(p, cursor);
                cursor.setAmount(--amount);
            }
            view.setCursor(cursor);
        }
        cancelled = true;
        return cancelled;
    }
    // Click en la tienda con algo en mano.
    public void clickTopCursor(InventoryView view, ClickType click, ItemStack cursor){
        
    }
    // Click en la tienda con algo en el slot.
    public void clickTopSlot(InventoryView view, ClickType click, int currentItem){
        ItemStack current = view.getItem(currentItem);
        // Cancela si el slot clickado no tiene un objeto.
        if(current.getTypeId() == 0){
            return;
        }
        // Si tiene nombre seguro es un menú.
        if(current.getItemMeta().hasDisplayName()){
            String itemName = current.getItemMeta().getDisplayName();
            if(itemName.contentEquals(getReturn().getItemMeta().getDisplayName())){
                close();
            }else if(itemName.contentEquals(getNext().getItemMeta().getDisplayName())){
                addPage(1);
            }else if(itemName.contentEquals(getPrev().getItemMeta().getDisplayName())){
                addPage(-1);
            }else{
                // Si es un menú.
                addPath(f.getItemName(current));
                setPage(1);
            }
            updateInventory(view.getTopInventory());
        // Si no es un objeto-menú, es un item de la tienda.
        }else{
            // Se compra uno.
            if(click == ClickType.LEFT){
                f.shop(p, f.getItemName(current), 1);
            // Se compra un stack.
            }else if(click == ClickType.RIGHT){
                f.shop(p, f.getItemName(current), current.getMaxStackSize());
            }
        }
    }
    // Click fuera de la tienda con algo en el cursor y en el slot.
    public boolean clickBotCursorSlot(InventoryView view, ClickType click, ItemStack cursor, int currentItem){
        boolean cancelled = false;
        ItemStack current = view.getItem(currentItem);
        if(!(click == ClickType.RIGHT
                || click == ClickType.LEFT
                || click == ClickType.DOUBLE_CLICK)){
            cancelled = true;
        }
        // Si se hace shift click fuera del inventario de la tienda, se vende el stack.
        if(click.isShiftClick()){
            if(current.getTypeId() == 0){
                return cancelled;
            }
            view.setItem(currentItem, null);
            f.sellItem(p, current);
            cancelled = true;
        }
        return cancelled;
    }
    // Click fuera de la tienda con algo en el cursor.
    public void clickBotCursor(InventoryView view, ClickType click, ItemStack cursor){
        
    }
    // Click fuera de la tienda con algo en el slot.
    public void clickBotSlot(InventoryView view, ClickType click, ItemStack currentItem){
        
    }
    public String getPath(){
        if(!getCache().isSet(p.getName()+".set") || !getCache().getBoolean(p.getName()+".set")){
            getCache().set(p.getName()+".set", true);
            setPath("shop.");
        }
        return getCache().getString(p.getName()+".path");
    }
    public boolean isRelativeSet(String s){
        return getConfig().isSet(getPath(s));
    }
    public String getPath(String s){
        return getPath()+s;
    }
    public void setPath(String s){
        getCache().set(p.getName()+".path", s);
    }
    public void addPath(String s){
        setPath(getPath()+"sub."+s+".");
    }
    public int getPage(){
        return getCache().getInt(p.getName()+".page", 1);
    }
    public void setPage(int i){
        getCache().set(p.getName()+".page", i);
    }
    public void addPage(int i){
        setPage(getPage()+i);
    }
    public void close(){
        getCache().set(p.getName(), null);
    }
    public void playSound(){
        p.getWorld().playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
    }
    public boolean isUserShop(){
        return isRelativeSet("items");
    }
    public FileConfiguration getCache(){
        return Factory.getVolatilCache();
    }
    public FileConfiguration getConfig(){
        return Factory.getCache();
    }
}