package elxris.Useless.Utils;

import java.util.Calendar;

public class Fecha {
    public static String formatoFecha(long time){
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
        fecha += unidadFecha(semanas, h, primero);
        h += 2;
        fecha += unidadFecha(dias, h, primero);
        h += 2;
        fecha += unidadFecha(horas, h, primero);
        h += 2;
        fecha += unidadFecha(minutos, h, primero);
        h += 2;
        fecha += unidadFecha(segundos, h, primero);
        //Fecha
        fecha += ") ";
        fecha += Calendar.DAY_OF_MONTH+"/"+Strings.getStringList("f.months").get(Calendar.MONTH);
        return fecha;
    }
    public static String unidadFecha(int tiempo, int h, boolean[] primero){
        String fecha = "";
        if(tiempo > 0){
            if(primero[0]){
                fecha = " ";
            }
            primero[0] = true;
            if(tiempo == 1){
                fecha += tiempo+" "+Strings.getStringList("f.units").get(h);
            }else{
                fecha += tiempo+" "+Strings.getStringList("f.units").get(h+1);
            }
        }
        return fecha;
    }
}
