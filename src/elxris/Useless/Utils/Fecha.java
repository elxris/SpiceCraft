package elxris.Useless.Utils;

import java.util.Calendar;

public class Fecha {
    public static String formatoFecha(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        long diff = (System.currentTimeMillis() - time);
        String fecha = "";
        fecha += c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
        fecha += tiempoAgo(diff);
        fecha += c.get(Calendar.DAY_OF_MONTH)+"/"+Strings.getStringList("f.months").get(c.get(Calendar.MONTH));
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
    public static String tiempoAgo(long diff){
        diff /= 1000;
        String fecha = "";
        // Calcular cuanto tiempo ha pasado.
        boolean[] primero = {false};
        int semanas, dias, horas, minutos, segundos, count, h = 0;
        count = 0;
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
        
        fecha += " (";
        fecha += Strings.getString("mail.timeago");
        if(semanas != 0){
            fecha += unidadFecha(semanas, h, primero);
            count++;
        }
        h += 2;
        if(dias != 0){
            fecha += unidadFecha(dias, h, primero);
            count++;
        }
        h += 2;
        if(horas != 0 && count < 2){
            fecha += unidadFecha(horas, h, primero);
            count++;
        }
        h += 2;
        if(minutos != 0 && count < 2){
            fecha += unidadFecha(minutos, h, primero);
            count++;
        }
        h += 2;
        if(segundos != 0 && count < 2){
            fecha += unidadFecha(segundos, h, primero);
            count++;
        }
        //Fecha
        fecha += ") ";
        return fecha;
    }
}
