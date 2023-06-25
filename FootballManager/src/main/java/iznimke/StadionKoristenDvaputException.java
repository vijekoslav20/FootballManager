package iznimke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StadionKoristenDvaputException extends Exception{

    private static final Logger logger = LoggerFactory.getLogger(BazaPodatakaException.class);

    public StadionKoristenDvaputException() {
    }

    public StadionKoristenDvaputException(String message) {
        super(message);

        logger.error(message);
    }

    public StadionKoristenDvaputException(String message, Throwable cause) {
        super(message, cause);

        logger.error(message);
    }

    public StadionKoristenDvaputException(Throwable cause) {
        super(cause);
    }
}
