package entitet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serijalizacija<T> {
    private final Logger logger = LoggerFactory.getLogger(Serijalizacija.class);

    public void serijaliziraj(List<T> listaObjekata, String putanja) throws IOException {

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(putanja));

        try (objectOutputStream)
        {
            objectOutputStream.writeObject(listaObjekata);
        } catch (IOException e) {
            logger.error("Nije uspjelo serijaliziranje objekta");
            throw new RuntimeException(e);
        }
    }

    public List<T> deserijaliziraj(String putanja) throws IOException {
        List<T> objekti = new ArrayList<>();
        File file = new File(putanja);

        if(file.length() == 0){
            return objekti;
        }
        else {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));

            try(objectInputStream){
                objekti = (List<T>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Nije uspjelo deserijaliziranje objekta");
                throw new RuntimeException(e);
            }
        }

        return objekti;
    }
}
