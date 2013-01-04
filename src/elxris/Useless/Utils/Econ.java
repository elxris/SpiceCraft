package elxris.Useless.Utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import elxris.Useless.Useless;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Econ {
    private static Economy econ = null;
    private static boolean setup = false;
    private Player jugador;
    public Econ() {
        if (!setupEconomy() && !setup) {
            Useless.log(Strings.getString("alert.noEconomy"));
        }
    }
    public void setJugador(Player jugador) {
        this.jugador = jugador;
    }
    //Economía.
    private boolean setupEconomy() {
        setup = true;
        if (Useless.plugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Useless.plugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public boolean cobrar(double precio) {
        if(precio > 0){
            if(econ != null){
                if(econ.getBalance(getPlayerName(jugador)) >= precio){
                    EconomyResponse r = econ.withdrawPlayer(getPlayerName(jugador), precio);                
                    if(!r.transactionSuccess()){
                        mensaje(jugador, "alert.error");
                        return false;
                    }
                }else{
                    mensaje(jugador, "tw.s.noMoney");
                    return false;
                }
            }else{
                if(!Experiencia.cobrarEsperiencia(jugador, (int)precio)){
                    mensaje(jugador, "tw.s.noMoney");
                    return false;
                }
            }
            Chat.mensaje(jugador, "econ.cobrar", getPrecio(precio));
        }
        return true;
    }
    public String getPrecio(double precio){
        if(econ != null){
            if(precio < 1){
                return String.format("%s %s", econ.format(precio), econ.currencyNameSingular());                
            }else{
                return String.format("%s %s", econ.format(precio), econ.currencyNamePlural());
            }
        }
        // Si no
        return String.format(Strings.getString("exp.format"), (int)precio);
    }
    private String getPlayerName(Player jugador){
        return jugador.getName();
    }
    private void mensaje(Player p, String mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
}
