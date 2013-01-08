#Posibles ideas a implementar
-----
##Idea #1
Que con un comando, puedas convertir los entities cercanas en lo que tu quieras.
##Idea #2
Que las bolas de nieve hagan daño.
##Idea #3
Que puedas vender tus items por experiencia. O tu experiencia por items.
##Idea #4
Que al matar un mob, tengas una oportunidad de tener un huevo de el. Y muy, muy raramente un spawn block.
#Librería
-----
##Comandos
###/lib
Da la ayuda del comando.
###/lib buy [id]
Compra el libro indicado.
###/lib info [id]
Muestra información sobre el libro. (Cómo el número de páginas, el costo, el autor, el título.);
###/lib list
Muestra los libros todos los libros en venta. (Muestra primero los libros que haya subido el usuario, y de otro color.)
###/lib top
Muestra los 5 libros más vendidos.
###/lib sell [precio]
Vende el libro que tengas en mano.
###/lib delete [id]
Borra de la tienda el libro indicado.
###/lib payme
Paga las retribuciones.
##Permisos
-Comprar
-Vender
##Configuraciones
###Valores
-Porcentaje de comisión
-Costo publicación
###Strings
-Ayuda
-Has comprado un libro. Gracias.
-No tienes suficiente dinero para comprar.
-No tienes ningún libro en la mano.
-No tienes suficiente dinero para publicar.
-No eres el autor del libro.
-No puedes borrar ese libro.
-Los libros [%s], te han generado %s ganancias.
-Lista de libros. %s
-Elemento de lista color verde. (Propio).
-Elemento de lista color azul.
-Top de libros. %s
-Info de libro. Título, numero de páginas, costo, autor.
##Estructura datos.
`libros:
    -id1
    -id2
libro:
    id:
        title:
        autor:
        text:
        cost:
        count:
        payed:`