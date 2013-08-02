package elxris.SpiceCraft.Utils;

import elxris.SpiceCraft.Objects.Savable;

public class Logger extends Savable{
    private Archivo file;
    private String datos;
    private String fileName;
    public Logger(String fileName){
        this.fileName = fileName;
    }
    @Override
    public void run(){
        super.run();
        saveNow();
    }
    private Archivo getFile(){
        if(file == null){
            file = new Archivo(fileName);
        }
        return file;
    }
    private void saveNow(){
        getFile().saveString(datos);
        datos = null;
    }
    public void log(String s){
        if(datos == null){
            datos = new String();
        }
        datos = datos.concat(s);
        // Nueva linea.
        datos = datos.concat("\n");
        save();
    }
}
