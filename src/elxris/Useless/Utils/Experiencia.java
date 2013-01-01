package elxris.Useless.Utils;

import org.bukkit.entity.Player;

public class Experiencia {
    
    public static int getExperiencia(int lvl, float nextLvl){
        float exp = 0;
        float expLvl = 17;
        for(int i = 0; i < lvl; i++){
            if(i > 16){
                expLvl += 3;
            }
            exp += expLvl;
        }
        exp += (expLvl*nextLvl);
        return (int) exp;
    }
    
    public static float setExperiencia(int exp){
        float expLvl = 17;
        int i, e = 0;
        for(i = 0; (i + expLvl) < exp; i += expLvl){
            e++;
            if(i >= (17*16)){
                expLvl += 3;
            }
        }
        return (e+((exp - i)/expLvl)); //Regla de tres más los niveles de experiencia.
    }
    
    public static boolean cobrarEsperiencia(Player p, int exp){
        float nextLvl = getExperiencia(p.getLevel(), p.getExp());
        if(nextLvl < exp){
            return false;
        }
        nextLvl = setExperiencia((int) nextLvl - exp);
        int lvl = (int) (nextLvl-(nextLvl%1));
        nextLvl = nextLvl%1;
        p.setExp(nextLvl);
        p.setLevel(lvl);
        Chat.mensaje(p, "exp.cobrar", exp);
        return true;
    }
}
