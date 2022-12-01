
package rebotesv1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class UsoRebotesV1 {
    
    public static void main(String[] args) {
       JFrame marco = new MarcoRebote();
       marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       marco.setVisible(true);
    }
}

class Pelota{
    private static final int TAMX = 15;
    private static final int TAMY = 15;
    private double x = 0;
    private double y = 0;
    private double dx = 1;
    private double dy = 1;

    //Animar la pelota
    public void muevePelota(Rectangle2D limites){
        x += dx;
        y += dy;
        if(x < limites.getMinX()){
            x = limites.getMinX();
            dx = -dx;
        }
        if(x+TAMX >= limites.getMaxX() ){
            x = limites.getMaxX()-TAMX;
            dx = -dx;
        }
        if(y < limites.getMinY()){
            y = limites.getMinY();
            dy = -dy;
        }
        if(y+TAMY >= limites.getMaxY() ){
            y = limites.getMaxY()-TAMY;
            dy = -dy;
        }
    }
    
    public Ellipse2D getShape(){
        return new Ellipse2D.Double(x,y,TAMX,TAMY);
    }
}
//Contendor donde se animan las pelotas
class ContenedorPelota extends JPanel{
    private ArrayList<Pelota> pelotas= new ArrayList<Pelota>();
    //Añadimos la pelota al contenedor
    public void add(Pelota b){
        pelotas.add(b);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(Pelota b: pelotas){
            g2.fill(b.getShape());
        }
        
        //Toolkit.getDefaultToolkit().sync();
    }    
}

class HiloRunnable implements Runnable{
    private Pelota pelota = new Pelota();
    private Component contenedor;

    public HiloRunnable(Pelota pelota, Component contenedor) {
        this.pelota = pelota;
        this.contenedor = contenedor;
    }
       
    
    @Override
    public void run() {
        System.out.println("Estado hilo " + Thread.currentThread().isInterrupted() );
        while(!Thread.currentThread().isInterrupted()){
            pelota.muevePelota(contenedor.getBounds());
//            contenedor.paint(contenedor.getGraphics());
            contenedor.repaint();  
        
//        while(!Thread.interrupted()){
//            pelota.muevePelota(contenedor.getBounds());
////            contenedor.paint(contenedor.getGraphics());
//            contenedor.repaint();
            


//        for(int i=1; i <=3000; i++){
//            pelota.muevePelota(contenedor.getBounds());
//            contenedor.paint(contenedor.getGraphics());
//            
//            try{
//                Thread.sleep(4);
//            }
//            catch(InterruptedException e){
//                System.out.println(e);
//            }
        }
        System.out.println("Estado hilo " + Thread.currentThread().isInterrupted() );
        
    }
    
}

class MarcoRebote extends JFrame{
    private ContenedorPelota contenedor;
    Thread h1;
    public MarcoRebote(){
        setBounds(600,300,400,350);
        setTitle("Rebotes");
        contenedor = new ContenedorPelota();
        add(contenedor, BorderLayout.CENTER);
        JPanel contenedorBotones = new JPanel();
        ponerBoton(contenedorBotones, "Iniciar", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento) {
                comienza_el_juego();
            }
        });
        ponerBoton(contenedorBotones, "Salir", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento) {
                System.exit(0);
            }
        });
        ponerBoton(contenedorBotones, "Interrumpir Hilo", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento) {
                InterrumpirHilo();
            }
        });        
        ponerBoton(contenedorBotones, "Continuar Hilo", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento) {
                ContinuarHilo();
            }
        });
        add(contenedorBotones, BorderLayout.SOUTH);               
    }
    public void ponerBoton(Container c, String titulo, ActionListener oyente){
        JButton boton = new JButton(titulo);
        c.add(boton);
        boton.addActionListener(oyente);
    }
    public void comienza_el_juego(){
        Pelota pelota = new Pelota();
        contenedor.add(pelota);
        Runnable r = new HiloRunnable(pelota, contenedor);
        h1 = new Thread(r);
        h1.start();
    }    
    
    public void InterrumpirHilo(){
//        h1.stop();
        h1.suspend();
    }  
    
        public void ContinuarHilo(){
//        h1.stop(); 
          h1.resume();
    }
}
