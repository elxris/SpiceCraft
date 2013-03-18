package elxris.Useless.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;

public class Jobs {
    private FileConfiguration cache;
    private Archivo archivo;
    private List<JobInterface> jobs;
    public void onBlockBreak(Player p, Block block){
        /*for(long l: getPlayerJobs(p)){
            p.getInventory().contains(getJobItem(l), 1);
        }
        for(long l: getPlayerRols(p)){
            
        }*/
    }
    public void showPlayerJobs(Player p){
        //  Rehacer esta clase.
        if(!getCache().isSet("users."+p.getName())){
            Chat.mensaje(p, "jobs.noJobs");
            return;
        }
        String trabajos = new String();
        /*for(long s: getPlayerRols(p)){
            trabajos += String.format(Strings.getString("jobs.jobFormat"), getRolName(s));
        }
        for(long s: getPlayerJobs(p)){
            trabajos += String.format(Strings.getString("jobs.jobFormat"), getJobName(s));
        }*/
        Chat.mensaje(p, trabajos);
    }
    private ItemStack getJobItem(long id){
        
        Material material = Material.getMaterial(getCache().getInt("job."+id+".id"));
        ItemStack item = new ItemStack(material);
        item.getData().setData((byte) getCache().getInt("job."+id+".data"));
        
        item.setAmount(item.getMaxStackSize());
        return item;
    }
    private List<JobInterface> getJobs(){
        // TODO Crear los trabajos si la lista no existe.
        return null;
    }
    public Archivo getArchivo(){
        if(archivo == null){
            archivo = new Archivo("jobs.yml");
        }
        if(!archivo.exist()){
            Archivo.blankFile("jobs.yml");
        }
        return archivo;
    }
    public FileConfiguration getCache(){
        if(cache == null){
            cache = getArchivo().load();
        }
        return cache;
    }
    
    
    // ## Clases Privadas ##
    
    private interface JobInterface{
        public void destroy(Player p);
        public void setPath(String path);
        public String getDisplay();
        public void setCantidadTotal(int cantidad);
        public int getCantidadTotal();
        public void setCantidadRestante(int cantidad);
        public void addCantidadRestante(int cantidad);
        public int getCantidadRestante();
        public void setServidor(boolean servidor);
        public boolean getServidor();
        public void setCosto(double costo);
        public double getCosto();
        public void setDate(long date);
        public long getDate();
        public void setItems(List<JobItem> objetos);
        public void addItem(JobItem objeto);
        public List<JobItem> getItems();
        public boolean check(Player p, int id, byte data);
        public boolean isUser(Player p);
        public void addUser(Player p);
        public void removeUser(Player p);
    }
    protected class Job implements JobInterface{
        public void destroy(Player p) {
        }
        public void setPath(String path) {
        }
        public String getDisplay() {
            return null;
        }
        public void setCantidadTotal(int cantidad) {
        }
        public int getCantidadTotal() {
            return 0;
        }
        public void setCantidadRestante(int cantidad) {
        }
        public void addCantidadRestante(int cantidad) {
        }
        public int getCantidadRestante() {
            return 0;
        }
        public void setServidor(boolean servidor) {
        }
        public boolean getServidor() {
            return false;
        }
        public void setCosto(double costo) {
        }
        public double getCosto() {
            return 0;
        }
        public void setDate(long date) {
        }
        public long getDate() {
            return 0;
        }
        public void setItems(List<JobItem> objetos) {
        }
        public void addItem(JobItem objeto) {
        }
        public List<JobItem> getItems() {
            return null;
        }
        public boolean check(Player p, int id, byte data) {
            return false;
        }
        public boolean isUser(Player p) {
            return false;
        }
        public void addUser(Player p) {
        }
        public void removeUser(Player p) {
        }
    }
    protected class JobItem{
        int id;
        byte data;
        public JobItem(int id, byte data) {
            setId(id);
            setData(data);
        }
        public JobItem(int id, MaterialData data){
            setId(id);
            setData(data);
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setData(byte data){
            this.data = data;
        }
        public void setData(int data){
            setData((byte)data);
        }
        public void setData(MaterialData data){
            byte itemData = data.getData();
            setData(itemData);
        }
        public byte getData(){
            return this.data;
        }
        public boolean isJobItem(int id, byte data){
            if(getId() == id){
                if(getData() == data){
                    return true;
                }
            }
            return false;
        }
        public boolean isJobItem(int id, MaterialData data){
            byte itemData = data.getData();
            return isJobItem(id, itemData);
        }
        public boolean isJobItem(Block block){
            return isJobItem(block.getTypeId(), block.getData());
        }
        public ItemStack toItemStack(){
            ItemStack item = new MaterialData(getId(), getData()).toItemStack();
            return item;
        }
        public ItemStack toItemStack(int cantidad){
            ItemStack item = toItemStack();
            if(item.getMaxStackSize() <= cantidad){
                item.setAmount(cantidad);
                return item;
            }
            item.setAmount(item.getMaxStackSize());
            return item;
        }
        public List<ItemStack> toItemStacks(int cantidad){
            List<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack item = toItemStack();
            for(int i = 0; i < (cantidad/item.getMaxStackSize()); i++){
                item.setAmount(item.getMaxStackSize());
                items.add(item);
            }
            item.setAmount(cantidad%item.getMaxStackSize());
            items.add(item);
            return items;
        }
    }
}
