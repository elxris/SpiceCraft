package elxris.Useless;

public class CheckConfiguration {
    private boolean changed = false;
    public CheckConfiguration() {
        update();
        //Comandos tw
        setPath("comm.tw.new", "nuevo", "n");
        setPath("comm.tw.destroy", "destruir", "d", "eliminar", "e", "borrar", "b");
        setPath("comm.tw.list", "listar", "lista", "list", "li", "l");
        // Comandos mbox
        setPath("comm.mbox.count", ".");
        setPath("comm.mbox.read", "leer", "l", "read", "r");
        setPath("comm.mbox.clear", "borrar", "b", "clear", "c");
        // Comandos mboxc
        setPath("comm.mboxc.new", "nuevo", "n", "crear", "c", "new");
        setPath("comm.mboxc.add", "agregar", "a�adir", "a", "add");
        setPath("comm.mboxc.send", "enviar", "e", "send", "s");
        setPath("comm.mboxc.sendall", "atodos", "t", "sendall", "sa");
        setPath("comm.mboxc.clear", "limpiar", "l", "clear", "cl");
        // Comandos upin
        setPath("comm.upin.del", "delete", "del", "delete", "borrar", "b");
        setPath("comm.upin.list", "listar", "l", "list");
        // Comandos lib
        setPath("comm.lib.del", "delete", "del", "delete", "borrar", "b");
        setPath("comm.lib.buy", "compra", "buy", "c");
        setPath("comm.lib.info", "info", "i");
        setPath("comm.lib.top", "top", "t");
        setPath("comm.lib.list", "list", "listar", "l");
        setPath("comm.lib.sell", "vender", "vende", "sell", "v", "s");
        setPath("comm.lib.pay", "paga", "pay", "p");
        // Warps
        setPath("tw.info", "�aAyuda /tw",
                "�e/tw �cn�euevo [minutos]�r Para crear un warp temporal personal.",
                "Recuerda que por cada minuto te cobrar� %s.",
                "�e/tw �cn�euevo [nombre] [minutos]�r Hacer un warp temporal que cualquiera pueda usar.",
                "�e/tw [nombre]�r Para hacer uso de un warp temporal.",
                "�e/tw �cb�eorrar�r o �e/tw �cb�eorrar [nombre]�r Borra un warp personal o p�blico si eres el due�o.",
                "�e/tw list�r Lista los warps p�blicos.",
                "�e/tw�r Muestra esta ayuda, o puede ser un comando r�pido para un warp temporal personal.");
        setPath("tw.s.created", "Warp Temporal de %d minutos creado.",
        "Usa �e/tw %s�r para usarlo.");
        setPath("tw.s.remain", "Quedan �4%d�r segundos para la destrucci�n de el warp temporal.");
        setPath("tw.s.teleported", "Teleportando..");
        setPath("tw.s.destroyed", "Warp Destruido.");
        setPath("tw.s.noMoney", "No puedes pagarte un warp temporal en este momento.");
        setPath("tw.s.noExist", "No existe el warp. �e/tw�r Para m�s info.");
        setPath("tw.s.exist", "Ya existe ese warp.");
        setPath("tw.s.timeLimit", "No debe ser menor a %d minutos ni mayor a %d minutos.");
        setPath("tw.s.warpLimit", "No puedes hacer otro warp, ya tienes muchos.");
        setPath("tw.s.noOwner", "No puedes borrar este warp, no es tuyo.");
        setPath("tw.s.listHeader", "### Lista de warps p�blicos ###", "%s");
        setPath("tw.s.listItem", "<%s:%s %s segundos restantes>");
        setPath("tw.v.maxTime", 120);
        setPath("tw.v.minTime", 1);
        setPath("tw.v.maxPerUser", 2);
        setPath("tw.v.price", 10.0);
        // Mail
        setPath("mbox.info", "�aAyuda /mbox",
                "�e/mbox . �rNumerar los correos que tienes.",
                "�e/mbox �cl�eeer �rLeer el correo.",
                "�e/mbox �cb�eorrar �rBorrar la bandeja de entrada.",
                "�e/mboxc �rAyuda para crear correos.");
        setPath("mbox.list", "Tienes �e%d�r mensajes. �e/mbox�r Para m�s info.");
        setPath("mbox.mail", "De: �a%s�r Fecha: %s",
                "�b�o%s�r",
                "�e/mboxc �ce�enviar %s [Mensaje]�r para responder");
        setPath("mbox.timeago", "Hace ");
        setPath("mbox.readStart", "�c### Inicio Bandeja de Correo ###");
        setPath("mbox.readFinish", "�c### Fin Bandeja de Correo ###",
                "Escribe �e/mbox �cb�eorrar�r para borrar la bandeja de entrada.");
        setPath("mbox.listEnd", "Ya no tienes correos.");
        setPath("mbox.deleted", "Mensajes eliminados.");
        // Create mail
        setPath("mboxc.info", "�aAyuda /mboxc",
                "Para crear un correo debes primero hacer un borrador para despu�s agregar el mensaje.",
                "�e/mboxc �cn�eevo [Usuario] ... �rCrea un borrador con destinatario o destinatarios. Uno m�nimo.",
                "�e/mboxc �ca�egregar [Mensaje] �rAgrega el mensaje a el correo.",
                "�e/mboxc �cb�eorrar �rBorra el mensaje.",
                "�e/mboxc �ce�enviar �rYa que tienes listo el mensaje, este comando lo env�a.",
                "�e/mboxc �ce�enviar [Usuario] [Mensaje] �rComando r�pido para enviar un mensaje.",
                "�e/mboxc a�ct�eodos�r (Solo Admins)Env�a el mensaje a todos los usuarios.");
        setPath("mboxc.noPlayerAdded", "Ning�n destinatario, por lo tanto mensaje no creado.");
        setPath("mboxc.playerNotExist", "El jugador %s no encontrado. No ha sido agregado.");
        setPath("mboxc.catched", "Mensaje recibido.");
        setPath("mboxc.sended", "Mensaje enviado.");
        setPath("mboxc.limit", "Lo siento, ya has superado el l�mite de 300 caracteres.");
        setPath("mboxc.created", "Borrador creado. �e/mboxc �ca�egregar <Mensaje>�r para a�adir mensaje.");
        setPath("mboxc.add", "Mensaje a�adido a�ade m�s con �e/mboxc a [mensaje] �e/mboxc �ce�enviar�r para enviar.");
        setPath("mboxc.noMessage", "�cPrimero debes crear un borrador. �e/mboxc para m�s ayuda.");
        setPath("mboxc.v.maxChar", 200);
        // Compass
        setPath("upin.info", "�aAyuda /upin", 
                "�e/upin [nombre] [x] [z]�r Para crear una posici�n que puede ser localizada con una br�jula.",
                "�e/upin [nombre]�r Para usarla.",
                "�e/upin �cb�eorrar [nombre]�r Para borrarla si eres el due�o.",
                "�e/upin �cl�eistar�r Para ver todas las posiciones.");
        setPath("upin.noPin", "No existe la posicion.");
        setPath("upin.exist", "Ya existe esa posicion. Elige otra.");
        setPath("upin.list", "�a### Lista de las posiciones ###",
                "�b%s");
        setPath("upin.item", "<%s: %s [%s][%s]> ");
        setPath("upin.noList", "�cNo hay posiciones. A�n.");
        setPath("upin.created", "Posici�n creada, �sala con �e/upin %s");
        setPath("upin.set", "La posici�n ahora se mostrar� en un comp�s.");
        setPath("upin.nether", "�cDe nada sirve crear una posici�n aqu�. No se mostrar�.");
        setPath("upin.del", "Borrada la posici�n.");
        setPath("upin.v.maxPerUser", 3);
        setPath("upin.limitPin", "Ya tienes demasiadas posiciones, ya no puedes hacer m�s.");
        // Librer�a
        setPath("lib.info", "�aAyuda /lib",
                "�e/lib �cc�eomprar [id]�r Compra el libro indicado.",
                "�e/lib �ci�enfo [id]�r Muestra la informaci�n del libro indicado.",
                "�e/lib �cl�eistar�r Muestra una lista de los libros disponibles.",
                "�e/lib �ct�eop�r Muestra una lista de los 3 libros m�s vendidos.",
                "�e/lib �cv�eender [precio]�r Vende el libro que tienes en mano.",
                "�e/lib �cb�eorrar [id]�r Borra el libro indicado",
                "�e/lib �cp�eaga�r Cobra las retribuciones.");
        setPath("lib.sell", "Has vendido un libro.");
        setPath("lib.buy", "Has comprado un libro. Gracias.");
        setPath("lib.send", "Has enviado satisfactoriamente tu libro. Pronto ganar�s dinero con tus habilidades ret�ricas.");
        setPath("lib.noBook", "No existe el libro.");
        setPath("lib.bookInfo", "�aFICHA DEL LIBRO",
                "�bAutor: �c%s",
                "�bT�tulo: �c%S",
                "�bP�ginas: �c%d",
                "�bPrecio: �c%s",
                "�bVentas: �c%d");
        setPath("lib.noMoney", "No tienes suficiente dinero para comprar un libro.");
        setPath("lib.noHand", "No tienes un libro en la mano para vender.");
        setPath("lib.wrongAuthor", "No eres el autor del libro.");
        setPath("lib.pay", "�eCon los libros [%s�e], has ganado �a%s.");
        setPath("lib.noPay", "No tienes retribuciones.");
        setPath("lib.list", "###Lista de libros###");
        setPath("lib.itemMe", "�d[%d] ::%s:: <%s>{%s}");
        setPath("lib.item", "�b[%d] ::%s:: <%s>{%s}");
        setPath("lib.top", "###Top Libros###");
        setPath("lib.topItem", "%d.- %s");
        setPath("lib.del", "Has borrado el libro satisfactoriamente.");
        setPath("lib.repeated", "Est� repetido. �Acaso quieres enga�ar a tus lectores?");
        setPath("lib.isNotABook", "Lo que tienes en tu mano no es un libro.");
        setPath("lib.topSize", 3);
        setPath("lib.percent", 50);
        // Comandos
        setPath("cmd.reload", "Reload de comandos y configuraciones completo!");
        // Errores y alertas.
        setPath("alert.notsaved", "Error, no se ha podido guardar: ");
        setPath("alert.noEconomy", "No hay plugin de econom�a. Se cobrar� con puntos de experiencia.");
        setPath("alert.error", "�cError al usar el comando. Revisa la ayuda.");
        setPath("alert.noInteger", "Tienes que ingresar valor/es enteros.");
        setPath("alert.permission", "No tienes permiso de usar este comando.");
        setPath("alert.positive", "S�lo son admitidos valores positivos.");
        // Formatos
        setPath("f.units", "mes", "meses", "dia", "dias", "hora", "horas", "minuto", "minutos", "segundo", "segundos");
        setPath("f.months", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", 
                "Octubre", "Noviembre", "Diciembre");
        // Esperiencia
        setPath("exp.format", "%d puntos de experiencia");
        // Econom�a.
        setPath("econ.cobrar", "Cobrando %s.");
        setPath("econ.pagar", "Pagando %s.");
        // Guarda el archivo de configuraci�n.
        isChanged();
    }
    private void update() {
        String prev;
        if(!p().getConfig().isSet("v")){
            prev = "0.7.6";
        }else{
            prev = p().getConfig().getString("v");
        }
        if(prev.contains("0.7.6") || prev.contains("0.7.7") || prev.contains("0.7.7b")){
            delPath("lib.list");
            delPath("lib.item");
            delPath("lib.itemMe");
            prev = "0.7.7b";
        }
        delPath("v");
        setPath("v", Useless.getVersion());
    }
    private void setPath(String path, Object... v){
        if(!p().getConfig().isSet(path)){
            p().getConfig().set(path, v);
            changed();
        }
    }
    private void delPath(String path){
        p().getConfig().set(path, null);
    }
    private Useless p(){
        return Useless.plugin();
    }
    private void isChanged(){
        if(!changed){
            return;
        }
        p().saveConfig();
        p().reloadConfig();
    }
    
    public void changed() {
        changed = true;
    }
}
