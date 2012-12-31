package elxris.Useless.Utils;

import java.util.Date;

import org.bukkit.configuration.Configuration;

public class Fecha {
    public static String formatoFecha(Configuration fc, long time, String[] unidades, String[] meses){
        Date oldFecha = new Date(time);
        long diff = (System.currentTimeMillis() - time);
        diff /= 1000;
        String fecha = "";
        boolean[] primero = {false};
        //Calcular cuanto tiempo ha pasado.
        int semanas, dias, horas, minutos, segundos, h = 0;
        semanas = (int) (diff/(7*24*60*60));
        diff %= (7*24*60*60);
        dias = (int) (diff/(24*60*60));
        diff %= (24*60*60);
        horas = (int) (diff/(60*60));
        diff %= (60*60);
        minutos = (int) (diff/(60));
        diff %= (60);
        segundos = (int) (diff);
        //Guardar cuanto tiempo ha pasado
        fecha += "(";
        fecha += unidadFecha(semanas, unidades, h, primero);
        h += 2;
        fecha += unidadFecha(dias, unidades, h, primero);
        h += 2;
        fecha += unidadFecha(horas, unidades, h, primero);
        h += 2;
        fecha += unidadFecha(minutos, unidades, h, primero);
        h += 2;
        fecha += unidadFecha(segundos, unidades, h, primero);
        //Fecha
        fecha += ") ";
        fecha += oldFecha.getDate()+"/"+meses[oldFecha.getMonth()];
        return fecha;
    }
    public static String unidadFecha(int tiempo, String[] unidades, int h, boolean[] primero){
        String fecha = "";
        if(tiempo > 0){
            if(primero[0]){
                fecha = " ";
            }
            primero[0] = true;
            if(tiempo == 1){
                fecha += tiempo+" "+unidades[h];
            }else{
                fecha += tiempo+" "+unidades[h+1];
            }
        }
        return fecha;
    }
}
