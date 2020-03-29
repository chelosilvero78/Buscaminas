
package buscaminas;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BuscaMinas {

    static int[][] tableroMinas = new int[10][20];//necesario para que las casillas marquen nº de minas alrededor.
    
    static char[][] tableroCopia = new char[10][20];//paso a char de la anterior matriz. no se muestra ni cambia una vez creada
    static char[][] tableroOculto = new char[10][20];//matriz visible en el juego con minas ocultas se muestras y se cambia comparandola con anterior
    
    static int vacio = 0;
    static char oculto ='-';
    static char mina ='X';
    static boolean sigueJugando =true;//condicion para continuar turnos en el juego
   
    static int numMinas;
    static int minasPorMarcar;

    static Scanner sc = new Scanner(System.in);//para introducir datos por consola

    public static void main(String[] args) {
        
        llenaMatriz(tableroMinas, vacio);
        colocaMinas(tableroMinas,25);
        for (int k = 0; k < tableroMinas.length; k++) {
            for (int l = 0; l < tableroMinas[0].length; l++) {
                cuentaMinas(tableroMinas,k,l);
            }
        }
        copiaMatriz(tableroMinas, tableroCopia);
        llenaMatriz (tableroOculto,oculto);
        System.out.println("");
        mostrarMatriz(tableroOculto);
        minasPorMarcar=numMinas;
        do{
            escogeAccion();
        }while(sigueJugando && minasPorMarcar>0);
        
        if (sigueJugando){
            System.out.println("");
            System.out.println("¡GANASTE! El tablero está despejado");
            mostrarMatriz(tableroCopia);
        }
    }
    //menu de acciones
    public static void escogeAccion(){
        
        boolean opcioncorrecta=false;
        
        try{
            do{
                System.out.println("Escoge una opción:");
                System.out.println("1. para descubrir una casilla");
                System.out.println("2. para marcar una mina");
                int opcion = sc.nextInt();

                switch(opcion){
                    case 1: 
                        descubrirCasilla(tableroCopia, tableroOculto);
                        opcioncorrecta=true;
                        break;
                    case 2:
                        marcarMina(tableroCopia, tableroOculto);
                        opcioncorrecta=true;
                        break;
                    default:
                        System.out.println("Debes escoger una de las opciones");
                        break;
                }
            }while(!opcioncorrecta);
        }catch(InputMismatchException e){
            System.out.println("Debes insertar un numero");
            sc.next();
        }  
    }
    //marcamos una casilla como mina
    public static void marcarMina (char[][] matriz1, char[][]matriz2){
        int fila,columna;
        try{
            do{
                System.out.println("Indica la fila en que está la casilla a marcar");
                fila = sc.nextInt();
                System.out.println("Indica la columna en que está la casilla a marcar");
                columna =sc.nextInt();
        
                matriz2[fila][columna]='M';
               
                if (matriz1[fila][columna]==mina){
                    minasPorMarcar--;//para controlar cuando se acaba el juego
                }
            }while(fila<0 || fila>matriz1.length-1 || columna<0 || columna>matriz1[0].length-1);
        
            System.out.println("Quedan "+minasPorMarcar+" por descubrir");
            System.out.println("");
            mostrarMatriz(matriz2);
            
        }catch(InputMismatchException e){
                System.out.println("Debes insertar un numero");
                sc.next();
        }   
    }
    //para descubrir una casilla. si no es bomba dira cuantas hay alrededor, pero si lo es estalla y se acaba el juego
    public static void descubrirCasilla (char[][] matriz1, char[][] matriz2){
        int fila,columna;
        boolean correcto;
        try{ 
            do{
                System.out.println("Indica la fila en que está la casilla a descubrir");
                fila = sc.nextInt();
                System.out.println("Indica la columna en que está la casilla a descubrir");
                columna =sc.nextInt();
            
                if (!(fila<0 || fila>matriz1.length-1 || columna<0 || columna>matriz1[0].length-1)) {
                    correcto=true;
                    if(matriz1[fila][columna]==mina){
                        System.out.println("¡BOOM!");
                        System.out.println("");
                        System.out.println("¡HAS PERDIDO!");
                        mostrarMatriz(matriz1);
                        sigueJugando = false;
                    }else{
                        matriz2[fila][columna]=matriz1[fila][columna];
                        System.out.println("¡POR POCO! Sigue jugando...");
                        sigueJugando = true;
                        mostrarMatriz(matriz2);
                    }
                }else{
                    System.out.println("Posición incorrecta, vuelve a probar");
                    correcto = false;
                }
            }while(!correcto);   
        }catch(InputMismatchException e){
            System.out.println("Debes insertar un numero");
            sc.next();
        }            
    }
   // pasar una matriz int a matriz char
    public static void copiaMatriz (int[][]matriz1, char[][]matriz2){
        for (int i = 0; i < matriz1.length; i++) {
            for (int j = 0; j < matriz1[0].length; j++) {
                if (matriz1[i][j]!=9){
                    matriz2[i][j]=(char)(matriz1[i][j]+48); //por codigo ASCII
                }else{
                    matriz2[i][j]=mina;
                } 
            }
        }
    }
    //cuenta las minas que hay alrededor de una casilla vacía
    public static void cuentaMinas(int[][]matriz,int fila, int columna){
        int minasAlrededor=0;
       
        //interior
        if (columna!=0 && columna!=matriz[0].length-1 && fila!=0 && fila!=matriz.length-1){
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if(matriz[fila+i][columna+j]==9){
                        minasAlrededor++;
                    }
                }
            }
        //borde superior sin esquinas  
        }else if (fila==0 && columna!=0 && columna!=matriz[0].length-1){
            for (int i = 0; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if(matriz[fila+i][columna+j]==9){
                        minasAlrededor++;
                    }
                }
            }
        //borde inferior sin esquinas
        }else if (fila==matriz.length-1 && columna!=0 && columna!=matriz[0].length-1){
            for (int i = 0; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (matriz[fila-i][columna+j]==9){
                        minasAlrededor++;
                    }
                }
            }
        //borde izquierdo sin esquinas
        }else if (columna==0 && fila!=0 && fila!=matriz.length-1){
            for (int i = -1; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if (matriz[fila+i][columna+j]==9){
                        minasAlrededor++;
                    }
                }
            }
        //borde derecho sin esquinas
        }else if (columna==matriz[0].length-1 && fila!=0 && fila!=matriz.length-1){
            for (int i = -1; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if(matriz[fila+i][columna-j]==9){
                        minasAlrededor++;
                    }
                }
            }
        }
        //esquina sup. izq.
        if (fila==0 && columna==0){
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if(matriz[fila+i][columna+i]==9){
                        minasAlrededor++;
                    }
                }
            }
        //esquina sup. dra.
        }else if (fila==0 && columna==matriz[0].length-1){
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if(matriz[fila+i][columna-i]==9){
                        minasAlrededor++;
                    }
                }
            }
        //esquina inf. izq
        }else if (fila==matriz.length-1 && columna==0){
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if(matriz[fila-i][columna+i]==9){
                        minasAlrededor++;
                    }
                }
            }
        //esquina inf dra.
        }else if (fila==matriz.length-1 && columna==matriz[0].length-1){
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    if(matriz[fila-i][columna-i]==9){
                        minasAlrededor++;
                    }
                }
            }
        }
        if (matriz[fila][columna]!=9){
            matriz[fila][columna]=minasAlrededor;
        }  
    }
    //coloca un numero de minas en posiciones aleatorias sin repetirse
    public static void colocaMinas(int[][]matriz, int num){
        numMinas=num;
        for (int i = 0; i < num; i++) {
            boolean minaColocada = false;
            do{
                int fila = generaAleatorio(0,matriz.length-1);
                int columna = generaAleatorio (0, matriz[0].length-1);
                
            
                if (matriz[fila][columna]==0){
                    matriz[fila][columna]=9;
                    minaColocada =true; 
                }
            }while(!minaColocada);  
        }  
    }
    //crea una matriz int
    public static void llenaMatriz(int[][] matriz, int num){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                matriz[i][j]=num;
            }
        }
    }
    //crea una matriz char
    public static void llenaMatriz(char[][] matriz, char car){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                matriz[i][j]=car;
            }
        }
    }
    //Muestra el contenido de una matriz int con tablero de posiciones
    public static void mostrarMatriz(int[][] matriz){
        System.out.print("     ");
        for (int j = 0; j < 10; j++) {
            System.out.print(j+"  ");
        }
        for (int j = 10; j < matriz[0].length; j++) {
            System.out.print(j+" ");
        }
        System.out.println("");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < matriz.length; i++) {
            System.out.print(i+" |  ");
            for (int j = 0; j <  matriz[0].length; j++) {
                System.out.print(matriz[i][j]+"  ");
            }
            System.out.println("");
        }
    }
    //muestra contenido de matriz char con tablero de posiciones
     public static void mostrarMatriz(char[][] matriz){
        System.out.print("     ");
        for (int j = 0; j < 10; j++) {
            System.out.print(j+"  ");
        }
        for (int j = 10; j < matriz[0].length; j++) {
            System.out.print(j+" ");
        }
        System.out.println("");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < matriz.length; i++) {
            System.out.print(i+" |  ");
            for (int j = 0; j <  matriz[0].length; j++) {
                System.out.print(matriz[i][j]+"  ");
            }
            System.out.println("| "+i);
        }
         System.out.println("----------------------------------------------------------------");
         System.out.print("     ");
         for (int j = 0; j < 10; j++) {
             System.out.print(j+"  ");
         }
         for (int j = 10; j < matriz[0].length; j++) {
             System.out.print(j+" ");
         }
         System.out.println("");
    }
    //nos da un numero aleatorio
    public static int generaAleatorio(int min, int max){
        if (min>max){ 
            int aux = min;
            min = max;
            max = aux;           
        }
        return (int)(Math.random()*(max-min+1)+min);
    }
}
