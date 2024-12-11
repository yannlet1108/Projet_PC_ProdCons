package prodCons.v4;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import prodCons.IProdConsBuffer;
import prodCons.Producer;
import prodCons.Consumer;

class TestProdCons4 {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(TestProdCons4.class.getClassLoader().getResourceAsStream("prodCons/options.xml"));
        } catch (InvalidPropertiesFormatException e) {
            System.out.println("Invalid properties format");
        } catch (IOException e) {
            System.out.println("Error reading properties file");
        }
        int nProd = Integer.parseInt(properties.getProperty("nProd"));
        int nCons = Integer.parseInt(properties.getProperty("nCons"));
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
        int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
        int consTime = Integer.parseInt(properties.getProperty("consTime"));
        int minProd = Integer.parseInt(properties.getProperty("minProd"));
        int maxProd = Integer.parseInt(properties.getProperty("maxProd"));

        IProdConsBuffer buffer = new ProdConsBuffer4(bufSz);
        Producer[] producers = new Producer[nProd];
        Consumer[] consumers = new Consumer[nCons];

        for (int i = 0; i < nProd; i++) {
            producers[i] = new Producer(buffer, prodTime, minProd, maxProd);
            producers[i].start();
        }
        for (int i = 0; i < nCons; i++) {
            consumers[i] = new Consumer(buffer, consTime, 1);
            consumers[i].setDaemon(true);
            consumers[i].start();
        }

        for (int i = 0; i < nProd; i++) {
            try {
                producers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (buffer.nmsg() != 0){
            System.out.print(""); // pour ne pas avoir un corps de boucle vide
        }
        
        System.out.println("All producers have finished their work");
    }
}
