package prodCons.v1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import prodCons.IProdConsBuffer;
import prodCons.Producer;
import prodCons.Consumer;

class TestProdCons {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("prodCons/options.xml"));
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

        IProdConsBuffer buffer = new prodCons.v1.ProdConsBuffer(bufSz);
        Producer[] producers = new Producer[nProd];
        Consumer[] consumers = new Consumer[nCons];
        for (int i = 0; i < nProd; i++) {
            producers[i] = new Producer(buffer, prodTime, minProd, maxProd);
            producers[i].start();
        }
        for (int i = 0; i < nCons; i++) {
            consumers[i] = new Consumer(buffer, consTime);
            consumers[i].start();
        }


    }
}
