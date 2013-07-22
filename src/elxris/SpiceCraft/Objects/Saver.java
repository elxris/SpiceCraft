package elxris.SpiceCraft.Objects;

import org.bukkit.configuration.file.FileConfiguration;

import elxris.SpiceCraft.Utils.Archivo;

public class Saver extends Savable{
    private Archivo archivo;
    private FileConfiguration data;
    
    public Saver(Archivo file){
        setArchivo(file);
        setData(getArchivo().load());
    }
    public void save(){
        super.save();
    }
    public void saveNow(){
        getArchivo().save(getData());
    }
    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }
    public Archivo getArchivo() {
        return archivo;
    }
    public void setData(FileConfiguration data) {
        this.data = data;
    }
    public FileConfiguration getData() {
        return data;
    }
    @Override
    public void run() {
        super.run();
        saveNow();
    }
}
