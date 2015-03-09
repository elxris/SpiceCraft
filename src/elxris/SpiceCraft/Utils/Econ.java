package elxris.SpiceCraft.Utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import elxris.SpiceCraft.SpiceCraft;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Econ {
    private static Economy econ = null;
    private static boolean setup = false;
    private static EconLogger logg;
    public Econ() {
        if (!setup && !setupEconomy()) {
            SpiceCraft.log(Strings.getString("alert.noEconomy"));
        }
    }
    //Economía.
    private boolean setupEconomy() {
        setup = true;
        if (SpiceCraft.plugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = SpiceCraft.plugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public boolean cobrar(Player jugador, double cantidad) {
        if(cantidad < 0){
            return false;
        }else if(cantidad == 0){
            return true;
        }
        if(econ != null){
            if(econ.getBalance(jugador) >= cantidad){
                EconomyResponse r = econ.withdrawPlayer(jugador, cantidad);                
                if(!r.transactionSuccess()){
                    mensaje(jugador, "alert.error");
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if(!Experiencia.cobrarExperiencia(jugador, (int)cantidad)){
                return false;
            }
        }
        Chat.mensaje(jugador, "econ.cobrar", getPrecio(cantidad));
        return true;
    }
    public boolean pagar(Player jugador, double cantidad){
        if(cantidad <= 0){
            return false;
        }
        if(econ != null){
            EconomyResponse r = econ.depositPlayer(jugador, cantidad);
            if(!r.transactionSuccess()){
                mensaje(jugador, "alert.error");
                return false;
            }
        }else{
            Experiencia.pagarExperiencia(jugador, (int)cantidad);
        }
        Chat.mensaje(jugador, "econ.pagar", getPrecio(cantidad));
        return true;
    }
    public String getPrecio(double precio){
        if(econ != null){
            if(precio == 1){
                return String.format("%s %s", econ.format(precio), econ.currencyNameSingular());                
            }else{
                return String.format("%s %s", econ.format(precio), econ.currencyNamePlural());
            }
        }
        // Si no
        return String.format(Strings.getString("exp.format"), (int)precio);
    }
    private void mensaje(Player p, String mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
    public EconLogger getLogg(){
        if(logg == null){
            logg = new EconLogger();
        }
        return logg;
    }
}
