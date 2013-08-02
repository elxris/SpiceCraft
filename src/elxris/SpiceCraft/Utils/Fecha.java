package elxris.SpiceCraft.Utils;

import java.util.Calendar;

public class Fecha {
    public static String formatoFecha(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        String fecha = new String();
        fecha += c.get(Calendar.DAY_OF_MONTH)+"/"+Strings.getStringList("f.months").get(c.get(Calendar.MONTH));
        fecha += " ";
        fecha += c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
        return fecha;
    }
    public static String formatoFechaDiff(long time){
        return formatoFecha(time)+" "+formatoDiff(time);
    }
    public static String formatoDiff(long time){
        long diff = (System.currentTimeMillis() - time);
        String fecha = new String();
        fecha += "(";
        fecha += Strings.getString("mbox.timeago");
        fecha += parseTiempo(diff/1000);
        fecha += ")";
        return fecha;
    }
    private static String unidadFecha(int tiempo, int h, boolean[] primero){
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
    public static String parseTiempo(long seconds){
        if(seconds < 0){
            seconds = -seconds;
        }
        long diff = seconds;
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
        return fecha;
    }
}
