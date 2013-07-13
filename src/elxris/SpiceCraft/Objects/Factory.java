package elxris.SpiceCraft.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Chat;
import elxris.SpiceCraft.Utils.Econ;
import elxris.SpiceCraft.Utils.Strings;

public class Factory extends Savable implements Listener {
    private static Archivo file;
    private static FileConfiguration fc, volatil;
    private static MemoryConfiguration paths;
    private int VEL, STACKFULL;
    private long FRECUENCY;
    private double MULTIPLIER, SELLRATE;
    private boolean VARIABLE;
    public Factory() {
        init();
    }
    private void init() {
        VEL = SpiceCraft.plugin().getConfig().getInt("shop.vel");
        if(VEL < 1){
            VEL = 1;
        }
        FRECUENCY = SpiceCraft.plugin().getConfig().getLong("shop.freq");
        if(FRECUENCY < 1){
            FRECUENCY = 1;
        }
        FRECUENCY *= 60*1000;
        STACKFULL = SpiceCraft.plugin().getConfig().getInt("shop.full");
        if(STACKFULL < 64){
            STACKFULL = 64;
        }
        MULTIPLIER = SpiceCraft.plugin().getConfig().getDouble("shop.multiplier");
        if(MULTIPLIER < 0){
            MULTIPLIER = 1;
        }
        SELLRATE = SpiceCraft.plugin().getConfig().getDouble("shop.sellRate");
        VARIABLE = SpiceCraft.plugin().getConfig().getBoolean("shop.variable");
    }
    private void update(String item){
        long time = getSystemTimeHour();
        for(;getTimeHour(item) < time; addTime(item, FRECUENCY)){
            produce(item);
        }
    }
    private void produce(String item){
        // Producir deacuerdo a un item y su velocidad, y luego cambiar su velocidad.
        addCount(item, getVel(item));
        if(getCount(item) < 0){
            addVel(item, 1);
        }else if(getCount(item) > 0 && getCount(item) < STACKFULL){
            if(getVel(item) > VEL){
                addVel(item, -1);
            }else if(getVel(item) < VEL){
                addVel(item, +1);
            }
        }else if(getCount(item) > STACKFULL){
            if(getVel(item) > 1){
                addVel(item, -1);
            }
        }
    }
    private void setTime(String item, long time) {
        getCache().set("item."+item+".time", time);
        save();
    }
    private long getTime(String item) {
        isSet("item."+item+".time", getSystemTimeHour());
        return getCache().getLong("item."+item+".time");
    }
    private long getTimeHour(String item) {
        return getTime(item) - (getTime(item) % FRECUENCY);
    }
    private void addTime(String item, long time){
        setTime(item, getTime(item)+time);
    }
    private long getSystemTime(){ // Obtiene el tiempo del sistema.
        return System.currentTimeMillis();
    }
    private long getSystemTimeHour(){ // Obtiene el tiempo del sistema menos el resto de la frecuencia.
        return getSystemTime() - (getSystemTime() % FRECUENCY);
    }
    private void setCount(String item, double count){
        getCache().set("item."+item+".count", count);
        getTime(item);
        save();
    }
    private double getCount(String item){
        isSet("item."+item+".count", VEL);
        return getCache().getDouble("item."+item+".count");
    }
    private void addCount(String item, double count){
        setCount(item, getCount(item)+count);
    }
    private void addCountRecursive(String item, double count){
        for(String s: getDepends(item)){
            addCount(s, count);
        }
    }
    private void setVel(String item, int vel){
        getCache().set("item."+item+".vel", vel);
        save();
    }
    private int getVel(String item){
        if(VARIABLE){
            isSet("item."+item+".vel", VEL);
            return getCache().getInt("item."+item+".vel");
        }else{
            return VEL;
        }
    }
    private void addVel(String item, int vel){
        setVel(item, getVel(item)+vel);
    }
    private double getPriceData(String item){
        return getCache().getDouble("item."+item+".price");
    }
    private double getPrice(String item){
        double price = 0;
        for(String s: getDepends(item)){
            price += getPriceData(s) * getRazonPrecio(s) * MULTIPLIER;
        }
        return price/getRecipieMultiplie(item);
    }
    private double getRazonPrecio(String item){
        return (double)getVel(item)/VEL;
    }
    private double getPrecio(String item, int cantidad){
        double r = getPrice(item)*(double)cantidad;
        if(r < 0){
            r = 0;
        }
        return r;
    }
    private int getId(String item){
        return getCache().getInt("item."+item+".id");
    }
    private int getData(String item){
        return getCache().getInt("item."+item+".data");
    }
    private boolean getUserBuy(String item){
        return getCache().getBoolean("item."+item+".userBuy", SpiceCraft.plugin().getConfig().getBoolean("shop.defaultUserBuy"));
    }
    private boolean getUserSell(String item){
        return getCache().getBoolean("item."+item+".userSell", SpiceCraft.plugin().getConfig().getBoolean("shop.defaultUserSell"));
    }
    private int getRecipieMultiplie(String item){
        return getCache().getInt("item."+item+".recipieMultiplie", 1);
    }
    private List<String> getDepends(String item){
        List<String> dependency = new ArrayList<String>();
        update(item);
        if(!getCache().isSet("item."+item+".depend")){ // Si no hay dependencias
            dependency.add(item);
            return dependency;
        }
        dependency.add(item);
        ConfigurationSection memory = getCache().getConfigurationSection("item."+item+".depend");
        for(String s: memory.getKeys(false)){
            for(int i = memory.getInt(s); i > 0; i--){ // Numero de dependencia para los objetos.
                dependency.addAll(getDepends(s));
            }
        }
        return dependency;
    }
    private String searchItem(String s){ // Busca el nomrbe real de un objeto.
        makePaths();
        if(paths.isSet(s)){
            String res = paths.getString(s);
            return res;
        }
        return null;
    }
    private void makePaths(){
        if(paths != null){
           return; 
        }
        paths = new MemoryConfiguration();
        Set<String> items = getCache().getConfigurationSection("item").getKeys(false);
        for(String s: items){// Items
            paths.set(s, s);
            if(getCache().isSet("item."+s+".alias")){// Alias
                List<String> alias = getCache().getStringList("item."+s+".alias");
                for(String a: alias){
                    paths.set(a, s);
                }
            }
        }
        for(String s: items){// IDs
            if(haveData(s)){
                if(getData(s) == 0){
                    paths.set(getId(s)+"", s);
                }
                paths.set(getId(s)+":"+getData(s), s);
            }else{
                paths.set(getId(s)+"", s);
            }
        }
    }
    private void isSet(String path, Object value){
        if(!getCache().isSet(path)){
            getCache().set(path, value);
            save();
        }
    }
    private ItemStack createItem(Player p, String item, int size){
        ItemStack stack = new ItemStack(getId(item));
        short data = (short)getData(item);
        if(haveData(item)){
            stack.setDurability(data);
        }
        stack.setAmount(size);
        // Da la cabeza del que la pide.
        if(stack.getType() == Material.SKULL_ITEM){
            if(data == 3){
                SkullMeta skull = (SkullMeta) stack.getItemMeta();
                skull.setOwner(p.getName());
                stack.setItemMeta(skull);
            }
        }
        // Da un libro con un encantamiento al azar.
        if(stack.getType() == Material.ENCHANTED_BOOK){
            java.util.Random rndm = new java.util.Random();;
            Enchantment enchant;
            do{
                enchant = Enchantment.values()[rndm.nextInt(Enchantment.values().length)];
            } while(enchant.canEnchantItem(stack));
            stack.addUnsafeEnchantment(enchant, 1+rndm.nextInt(enchant.getMaxLevel()));
        }
        return stack;
    }
    private List<ItemStack> createItems(Player p, String item, int num){
        List<ItemStack> items = new ArrayList<ItemStack>();
        int maxStack = Material.getMaterial(getId(item)).getMaxStackSize();
        if(num%maxStack > 0){
            items.add(createItem(p, item, num%maxStack));
            num -= num%maxStack;
        }
        for(int i = 0; i < num/maxStack; i++){
            items.add(createItem(p, item, maxStack));
        }
        return items;
    }
    private boolean haveData(String item){
        return getCache().isSet("item."+item+".data");
    }
    // Comandos de la tienda.
    public boolean shop(Player p, String item, int cantidad){ // Compra
        String item_real = searchItem(item);
        if(item_real == null){
            Chat.mensaje(p, "shop.notExist");
            return false;
        }
        if(!(p.hasPermission("spicecraft.shop.master")||(getUserBuy(item)))){
            Chat.mensaje(p, "shop.cantBuy");
            return false;
        }
        Econ econ = new Econ();
        if(!econ.cobrar(p, getPrecio(item_real, cantidad))){
            Chat.mensaje(p, "shop.noMoney");
            return false;
        }
        addItemsToInventory(p, createItems(p, item_real, cantidad));
        addCountRecursive(item_real, (double)cantidad/getRecipieMultiplie(item_real)*(-1d));
        return true;
    }
    private void addItemsToInventory(Player p, List<ItemStack> items){ // Añade el item al inventario del jugador.
        ItemStack[] itemsArray = items.toArray(new ItemStack[0]);
        addItemsToInventory(p, itemsArray);
    }
    private void addItemsToInventory(Player p, ItemStack[] itemsArray){
        for(ItemStack item: p.getInventory().addItem(itemsArray).values()){
            p.getWorld().dropItem(p.getEyeLocation(), item);
        }
    }
    private void addItemToInventory(Player p, ItemStack item){
        List<ItemStack> itemsArray = new ArrayList<ItemStack>();
        itemsArray.add(item);
        addItemsToInventory(p, itemsArray);
    }
    public List<String> lookItems(String item, boolean all){ // Busca items.
        List<String> items = new ArrayList<String>();
        makePaths();
        String n = "";
        for(int i = 0; i < item.length(); i++){
            n += "[";
            n += item.toLowerCase().toCharArray()[i];
            n += item.toUpperCase().toCharArray()[i];
            n += "]";
        }
        item = n;
        for(String s: paths.getKeys(false)){
            if(items.size() == 18){
                break;
            }
            // Si encuentra un matche completo.
            if(s.matches("^"+item+"$")){
                if(all){
                    items.add(s);
                }else{
                    items = new ArrayList<String>();
                    items.add(s);
                    break;
                }
            }else if(s.matches("(.*)("+item+")(.*)")){
                items.add(s);
            }
        }
        return items;
    }
    public List<String> lookItems(String item){
        return lookItems(item, false);
    }
    public void reset(String item){
        item = searchItem(item);
        update(item);
        for(String s: getDepends(item)){
            setCount(s, VEL);
            setVel(s, VEL);
        }
    }
    public void setPrice(String item, Double NewPrice){
        reset(item);
        item = searchItem(item);
        getCache().set("item."+item+".price", (NewPrice-getPrice(item))+getPriceData(item));
        save();
    }
    public void showItemInfo(Player p, String itemName){
        String item = searchItem(itemName);
        String id = getId(item)+"";
        if(haveData(item)){
            id = id.concat(":"+getData(item));
        }
        Chat.mensaje(p, "shop.itemInfo", itemName, new Econ().getPrecio(getPrecio(item, 1)),
                getProduction(item), id);
    }
    private int getProduction(String item){
        List<String> dep = getDepends(item);
        if(dep.size() > 1){
            dep.remove(0);
        }
        int vel = 1;
        for(String s: dep){
            int v = getVel(s);
            if(v > vel){
                vel = v;
            }
        }
        return vel;
    }
    public void sell(Player p){ // Vende
        if(p.getGameMode() == GameMode.CREATIVE){
            Chat.mensaje(p, "shop.creative");
            return;
        }
        Inventory inv = org.bukkit.Bukkit.createInventory(p, 54, getShopName());
        FactoryGui gui = new FactoryGui(this, p);
        gui.updateInventory(inv);
        p.openInventory(inv);
    }
    @EventHandler
    private void onCloseInventory(org.bukkit.event.inventory.InventoryCloseEvent event){
        if(event.getInventory().getTitle().contentEquals("SHOP")){
            Inventory inv = event.getInventory();
            Player p = (Player) event.getPlayer();
            double money = 0;
            for(ItemStack item: inv.getContents()){
                String name = getItemName(item);
                if(name == null){
                    Chat.mensaje(p, "shop.notExist");
                    addItemToInventory(p, item);
                    continue;
                }
                if(!(p.hasPermission("spicecraft.shop.master")||(getUserSell(name)))){
                    Chat.mensaje(p, "shop.cantSell");
                    addItemToInventory(p, item);
                    continue;
                }
                addCountRecursive(name, (double)item.getAmount()/getRecipieMultiplie(name));
                double maxDurab = item.getType().getMaxDurability();
                double durab = maxDurab - item.getDurability();
                if(durab > 0){
                    money += getPrecio(name, item.getAmount())*(durab/maxDurab);
                }else{
                    money += getPrecio(name, item.getAmount());
                }
            }
            new Econ().pagar(p, money*SELLRATE);
        }
        if(event.getInventory().getTitle().contentEquals(getShopName())){
            new FactoryGui(this, ((Player)event.getPlayer())).close();
        }
    }
    public String getItemName(ItemStack item){
        if(item == null){
            return null;
        }
        double maxDurab = item.getType().getMaxDurability();
        String id = ""+item.getTypeId();
        if(item.getDurability() > 0){
            if(maxDurab == 0){
                id += ":"+item.getDurability();
            }
        }
        String name = searchItem(id);
        if(name == null){
            name = searchItem(id+":0");
        }
        return name;
    }
    // Gestion de archivos.
    private static void setFile(String path){
        Factory.file = new Archivo(path);
    }
    private static Archivo getFile() {
        if(file == null){
            setFile("shop.yml");
        }
        return file;
    }
    private static void setCache(FileConfiguration fc){
        Factory.fc = fc;
    }
    private static FileConfiguration getCache(){
        if(fc == null){
            setCache(getFile().load());
            getCache().setDefaults(Archivo.getDefaultConfig("shop.yml"));
        }
        return fc;
    }
    @Override
    public void run() {
        super.run();
        getFile().save(getCache());
    }
    private static FileConfiguration getVolatilCache(){
        if(volatil == null){
            volatil = new YamlConfiguration();
        }
        return volatil;
    }
    @EventHandler
    private void onClickInventory(InventoryClickEvent event){
        if(!event.getInventory().getName().contentEquals(getShopName())){
            return;
        }
        FactoryGui gui = new FactoryGui(this, ((Player)event.getWhoClicked()));
        if(event.getCurrentItem() == null){
            return;
        }
        if(event.getRawSlot() < event.getInventory().getSize()){
            event.setCancelled(true);
            if(event.getView().getCursor().getTypeId() == 0){
                if(event.getClick() == ClickType.LEFT){
                    // TODO Comprar 1 si tiene un displayName o cambiar de path.
                    if(event.getCurrentItem().getItemMeta().hasDisplayName()){
                    // Tiene nombre es un menú.
                        gui.addPath(getItemName(event.getCurrentItem()));
                        gui.updateInventory(event.getInventory());
                    }
                    event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
                }else if(event.getClick() == ClickType.RIGHT){
                    // TODO Comprar stack
                    ItemStack item = new ItemStack(event.getCurrentItem().getTypeId(), event.getCurrentItem().getMaxStackSize());
                    event.getWhoClicked().getInventory().addItem(item);
                }
            }else{
                // TODO Vender objetos.
                event.getView().setCursor(null);
            }
        }else{
            // Cancela lo que no sea un clic derecho o izquierdo.
            if(!(event.getClick() == ClickType.RIGHT
                    || event.getClick() == ClickType.LEFT)){
                event.setCancelled(true);
            }
            // Si se hace shift click fuera del inventario de la tienda, se vende el stack.
            // TODO Vender.
            if(event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT){
                event.setCurrentItem(null);
                event.setCancelled(true);
            }
        }
    }
    public String getShopName(){
        return getCache().getString("shop.name");
    }
    public class FactoryGui
    {
        Factory f;
        Player p;
        // 0 - 53 slots.
        public FactoryGui(Factory f, Player p){
            this.f = f;
            this.p = p;
        }
        public void updateInventory(Inventory inv){
            inv.clear();
            List<ItemStack> items;
            if(isRelativeSet("list")){
            // Si existe una lista de objetos.
                items = getItemList(getPath("list"));
            }else if(isRelativeSet("sub")){
            // Si existe un submenu
                items = getItemMenu(getPath("sub"));
            }else{
                return;
            }
            int itemsPerPage = 54;
            if(!isRelativeSet("return")){
                inv.addItem(getReturn());
                itemsPerPage--;
            }
            if(items.size() > itemsPerPage){
                itemsPerPage -= 2;
                if(getPage() > 1){
                    inv.addItem(getPrev());
                }
                if(((items.size()/itemsPerPage)-getPage())>=1){
                    inv.addItem(getNext());
                }
            }
            int start = (getPage()-1)*itemsPerPage;
            int count = 0;
            for(int i = 54-itemsPerPage; i < 54; i++){
                inv.setItem(start+i, items.get(count));
                if(++count >= items.size()){
                    break;
                }
            }
        }
        public List<ItemStack> getItemList(String path){
            List<ItemStack> list = new ArrayList<ItemStack>();
            for(String item : getConfig().getStringList(path+".list")){
                ItemStack i = f.createItem(p, item, 4);
                ItemMeta meta = i.getItemMeta();
                String id = getId(item)+((haveData(item))?":"+getData(item):"");
                meta.setLore(Strings.getSringList("shop.itemLore", getPrice(item), getProduction(item), id));
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
        public ItemStack getNext(){
            return getItemMenu("shop.next").get(0);
        }
        public ItemStack getPrev(){
            return getItemMenu("shop.previous").get(0);
        }
        public ItemStack getReturn(){
            return getItemMenu("shop.return").get(0);
        }
        public String getPath(){
            if(!getCache().isSet(p.getName()+".set") || !getCache().getBoolean(p.getName()+".set")){
                getCache().set(p.getName()+".set", true);
                getCache().set(p.getName()+".path", "shop.");
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
        public void close(){
            getCache().set(p.getName(), null);
        }
        public FileConfiguration getCache(){
            return Factory.getVolatilCache();
        }
        public FileConfiguration getConfig(){
            return Factory.getCache();
        }
    }
}