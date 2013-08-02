package elxris.SpiceCraft.Utils;

import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;

public class EconLogger extends Logger{
    private static Boolean on;
    public EconLogger() {
        super("economy.logg");
    }
    public void logg(String comando, Player p, String concepto, String datos, int cantidad, double dinero){
        logg(comando, p, concepto, datos, cantidad, new Econ().getPrecio(dinero));
    }
    public void logg(String comando, Player p, String concepto, String datos, int cantidad, String dinero){
        logg(comando, p.getName(), concepto, datos, cantidad, dinero);
    }
    public void logg(String comando, String jugador, String concepto, String datos, int cantidad, String dinero){
        if(!isOn()){
            return;
        }
        super.log(Fecha.formatoFecha(System.currentTimeMillis())+" ["+comando+"] "+jugador+
                " "+concepto+
                " "+datos+
                ((cantidad != 0)?(" x "+cantidad):"")+
                ": "+dinero);
        // [Shop] elxris buy carrots x 1 = 50 coins.
    }
    public Boolean isOn(){
        if(on == null){
            on = SpiceCraft.plugin().getConfig().getBoolean("econ.logg", true);
        }
        return on;
    }
}
