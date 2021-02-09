package obradaSlika;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

//podeli na klijent i serverr funkcije

public class SlikeIO {


    public static void slanjeSlike(String putanja,String imeDatoteke,ServerSocket ss){
        ServerSocket serverSocket = ss;
        Socket socket= null;
        try{
            socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write((imeDatoteke + "\n").getBytes());
            BufferedImage image = ImageIO.read(new File(putanja));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
            outputStream.write(size);
            outputStream.write(baos.toByteArray());
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void slanjeBrojaTransfera(ServerSocket ss,String s){
        Socket socket = null;
        try{
            socket = ss.accept();
            int brojTransfera = 0;
            for(String imeDatoteke:new File("src\\server\\" + s).list()) {
                brojTransfera++;
            }
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write( (brojTransfera+"\n").getBytes() );
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int primanjeBrojaTransfera(){
        Socket socket = null;
        int broj = 0;
        try{
            socket = new Socket("127.0.0.1", 7777);
            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            broj = Integer.parseInt(bf.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return broj;
    }

    public static Object[] primanjeSlike(){
        Socket socket = null;
        Object[] o = new Object[2];
        try {
            socket = new Socket("127.0.0.1", 7777);
            InputStream inputStream = socket.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String imeDatoteke = bf.readLine();
            System.out.println(imeDatoteke);
            byte[] sizeAr = new byte[4];
            inputStream.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
            byte[] imageAr = new byte[size];
            inputStream.read(imageAr);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
            Image slika = SwingFXUtils.toFXImage(image,null);
            socket.close();
            bf.close();
            inputStream.close();
            o[0] = imeDatoteke;
            o[1] = slika;
        }catch (
                UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return o;
    }

    public static Image primanjeSlikeGalerija(){
        Socket socket = null;
        Image slika = null;
        try {
            socket = new Socket("127.0.0.1", 7777);
            InputStream inputStream = socket.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String imeDatoteke = bf.readLine();
            System.out.println(imeDatoteke);

            byte[] sizeAr = new byte[4];
            inputStream.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size];
            inputStream.read(imageAr);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

//            ImageIO.write(image, "jpg", new File("src\\sample\\slikeKlijent\\" + imeDatoteke));

            slika = SwingFXUtils.toFXImage(image,null);
            socket.close();
            bf.close();
            inputStream.close();
        }catch (
                UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return slika;
    }

}
