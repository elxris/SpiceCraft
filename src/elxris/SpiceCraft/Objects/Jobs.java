package elxris.SpiceCraft.Objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import elxris.SpiceCraft.Utils.Archivo;

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
            List<Job> jobs = new ArrayList<Jobs.Job>();
            Job j = new Job(this, "elxris", 1, (byte)0, 1, 100);
            jobs.add(j);
            // TODO
            this.jobsCache = jobs;
        }
        return this.jobsCache;
    }
    public void check(Player p, Block b){
        for(Job j : getJobs()){
            if(j.check(p, b)){
                break;
            }
        }
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
        Long id;
        String autor;
        List<JobItem> block;
        Double costo;
        Integer cantidad;
        Integer restante;
        Long date;
        Boolean terminado;
        public Job(Jobs nucleo, String id){
            // TODO
        }
        public Job(Jobs nucleo, String autor, int materialID, byte data, 
                double costo, int cantidad){
            setKernel(nucleo);
            setID(System.currentTimeMillis());
            setAutor(autor);
            addBlock(materialID, data);
            setCosto(costo);
            setCantidad(cantidad);
            setRestante(cantidad);
            setDate(System.currentTimeMillis());
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
            // Si no pertenece el usuario al trabajo.
            if(!isUser(p)){
                // TODO quitar
                addUser(p);
                //return false;
            }
            // Si no es el bloque.
            if(!isBlock(block)){
                //return false; 
            }
            // TODO Destruir bloque y hacer el conteo.
            block.setTypeId(0);
            p.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, null);
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
            if(getPath(path) != o){
                getConfig().set(getPath()+path, o);
                save();
            }
        }
        public Object getPath(String path){
            if(!isSet(path)){
                return null;
            }
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
            if(this.autor == null){
                this.autor = (String) getPath(".autor");
            }
            return this.autor;
        }
        public boolean isAutor(Player p){
            return p.getName().contentEquals(getAutor());
        }
        public boolean isServidor(){
            return getAutor() == null ? true : false;
        }
        public void setID(long id){
            this.id = id;
        }
        public long getID(){
            return this.id;
        }
        public List<JobItem> getBlocks(){
            if(this.block == null){
                // Obtener los bloques de la lista.
                List<JobItem> block = new ArrayList<JobItem>();
                ConfigurationSection cf = getCache().getConfigurationSection(getPath()+".blocks");
                if(cf != null){
                    for(String k : cf.getKeys(false)){
                        Integer.parseInt(k);
                        block.add(new JobItem(Integer.parseInt(k), (byte)cf.getInt(k)));
                    }
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
                setPath(".costo", costo);
                this.costo = costo;
            }else{
                this.costo = 0.0d;
            }
        }
        public double getCosto() {
            if(this.costo == null){
                this.costo = (Double) getPath(".costo");
            }
            return this.costo;
        }
        public void setCantidad(int cantidad) {
            setPath(".cantidad", cantidad);
            this.cantidad = cantidad;
        }
        public int getCantidad() {
            if(this.cantidad == null){
                this.cantidad = (Integer) getPath(".cantidad");
            }
            return this.cantidad;
        }
        public void setRestante(int restante){
            setPath(".restante", restante);
            this.restante = restante;
        }
        public int getRestante(){
            if(this.restante == null){
                this.restante = (Integer) getPath(".restante");
            }
            return this.restante;
        }
        public void addRestante(int n){
            setRestante(getRestante()-n);
        }
        public void setDate(long date) {
            setPath(".date", date);
            this.date = date;
        }
        public long getDate(){
            if(this.date == null){
                this.date = (Long) getPath(".terminado");
            }
            return this.date;
        }
        public void setTerminado(boolean i){
            setPath(".terminado", i);
            this.terminado = i;
        }
        public boolean getTerminado(){
            if(this.terminado == null){
                this.terminado = (Boolean) getPath(".terminado");
            }
            return this.terminado;
        }
    }
    protected class JobItem{
        private Integer block;
        private byte data;
        private String path;
        private Integer cuenta;
        private Integer restante;
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
