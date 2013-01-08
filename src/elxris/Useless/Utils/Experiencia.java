package elxris.Useless.Utils;

import org.bukkit.entity.Player;

public class Experiencia {
    public static boolean cobrarExperiencia(Player p, int exp){
        int xp = p.getTotalExperience();
        if(xp < exp){
            return false;
        }
        p.setTotalExperience(xp-exp);
        return true;
    }
    public static boolean pagarExperiencia(Player p, int exp){
        int xp = p.getTotalExperience();
        p.setTotalExperience(xp+exp);
        return true;
    }
}
