package elxris.Useless.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import elxris.Useless.Utils.Archivo;

public class Jobs extends Savable{
    private Archivo archivo;
    private FileConfiguration cache;
    private List<Job> jobsCache;
    private FileConfiguration playerCache;
    
    public void resetPlayerCache(){
        this.playerCache = null;
    }
    public void resetJobsCache(){
        this.jobsCache = null;
        resetPlayerCache();
    }
    public List<Job> getJobs(){
        if(this.jobsCache == null){
            // TODO
        }
        return this.jobsCache;
    }
    public FileConfiguration getPlayers(){
        if(this.playerCache == null){
            // TODO
        }
        return this.playerCache;
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
    /* Guardar */
    @Override
    public void run(){
        super.run();
        archivo.save(getCache());
    }
    
    // ## Clases Privadas ##
    protected class Job{
        Jobs kernel;
        String autor;
        String id;
        List<JobItem> block;
        double costo;
        int cantidad;
        int restante;
        long date;
        boolean terminado;
        public Job(Jobs nucleo, String id){
        }
        public Job(Jobs nucleo, String autor, String id, int materialID, byte data, 
                double costo, int cantidad, int restante, long date, boolean terminado){
            setKernel(nucleo);
            setID(id);
            setAutor(autor);
            addBlock(materialID, data);
            setCosto(costo);
            setCantidad(cantidad);
            setRestante(restante);
            setDate(date);
            setTerminado(terminado);
        }
        public boolean finish(Player p){
            if(p.getName().contentEquals(getAutor())){
                // TODO Recolectar los objetos, y el dinero que resta.
                destroyJob();
                return true;
            }else if(p.hasPermission("useless.jobs.master")){
                destroyJob();
                return true;
            }
            return false;
        }
        public void destroyJob() {
            // TODO Borrar el trabajo del cache.
            getConfig().set(getPath(), null);
            kernel.resetJobsCache();
        }
        public String getPath() {
            return "jobs."+getID();
        }
        public FileConfiguration getConfig(){
            return kernel.getCache();
        }
        public String getDisplayName() {
            // TODO
            return null;
        }
        public boolean check(Player p, Block block) {
            if(!isUser(p)){
                return false;
            }
            if(!isBlock(block)){
                return false; 
            }
            // TODO Destruir bloque y hacer el conteo.
            return true;
        }
        public boolean isUser(Player p) {
            // Obtener si el usuario está en el trabajo.
            return getCache().getStringList(getPath()+".users").contains(p.getName());
        }
        public void addUser(Player p) {
            // Añadir un usuario a la lista.
            if(!isUser(p)){
                List<String> users = getCache().getStringList(getPath()+".users");
                users.add(p.getName());
                getCache().set(getPath()+".users", users);
                getKernel().resetPlayerCache();
            }
        }
        public void removeUser(Player p) {
            // Quitar un usuario
            List<String> users = getCache().getStringList(getPath()+".users");
            users.remove(p.getName());
            getCache().set(getPath()+".users", users);
            getKernel().resetPlayerCache();
        }
        
        /* GETTERS & SETTERS */
        
        public boolean isSet(String path){
            if(getConfig().isSet(getPath()+path)){
                return true;
            }
            return false;
        }
        public void setPath(String path, Object o){
            if(getPath() != o){
                save();
            }
            getConfig().set(getPath()+path, o);
        }
        public Object getPath(String path){
            return getConfig().get(getPath()+path);
        }
        public void setKernel(Jobs nucleo){
            this.kernel = nucleo;
        }
        public Jobs getKernel(){
            return this.kernel;
        }
        public void setAutor(String autor){
            setPath(".autor", autor);
            this.autor = autor;
        }
        public String getAutor(){
            return this.autor;
        }
        public boolean isAutor(Player p){
            return p.getName().contentEquals(getAutor());
        }
        public boolean isServidor(){
            return getAutor() == null ? true : false;
        }
        public void setID(String id){
            this.id = id;
        }
        public String getID(){
            return this.id;
        }
        public List<JobItem> getBlocks(){
            if(this.block == null){
                // Obtener los bloques de la lista.
                List<JobItem> block = new ArrayList<JobItem>();
                ConfigurationSection cf = getCache().getConfigurationSection(getPath()+".blocks");
                for(String k : cf.getKeys(false)){
                    Integer.parseInt(k);
                    block.add(new JobItem(Integer.parseInt(k), (byte)cf.getInt(k)));
                }
                this.block = block;
            }
            return this.block;
        }
        public void addBlock(int blockID, byte data){
            getBlocks().add(new JobItem(blockID, data));
        }
        public boolean isBlock(Block block){
            for(JobItem item : getBlocks()){
                if(item.isJobItem(block)){
                    return true;
                }
            }
            return false;
        }
        public int getBlockID(int i){
            if(i >= getBlocks().size()){
                return 0;
            }
            return getBlocks().get(i).getId();
        }
        public byte getData(int i){
            if(i >= getBlocks().size()){
                return 0;
            }
            return getBlocks().get(i).getData();
        }
        public void setCosto(double costo) {
            if(costo >= 0){
                this.costo = costo;
            }else{
                this.costo = 0;
            }
        }
        public double getCosto() {
            return this.costo;
        }
        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
        public int getCantidad() {
            return this.cantidad;
        }
        public void setRestante(int restante){
            this.restante = restante;
        }
        public int getRestante(){
            return this.restante;
        }
        public void addRestante(int n){
            setRestante(getRestante()+n);
        }
        public void setDate(long date) {
            this.date = date;
        }
        public long getDate() {
            return this.date;
        }
        public void setTerminado(boolean i){
            this.terminado = i;
        }
        public boolean getTerminado(){
            return this.terminado;
        }
    }
    protected class JobItem{
        private int block;
        private byte data;
        public JobItem(int block, byte data) {
            setId(block);
            setData(data);
        }
        public JobItem(int id, MaterialData data){
            setId(id);
            setData(data);
        }
        public void setId(int block){
            this.block = block;
        }
        public int getId(){
            return this.block;
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
