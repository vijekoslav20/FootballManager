package iznimke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BazaPodatakaException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(BazaPodatakaException.class);

    public BazaPodatakaException() {
    }

    public BazaPodatakaException(String message) {
        super(message);

        logger.error(message);
    }

    public BazaPodatakaException(String message, Throwable cause) {
        super(message, cause);

        logger.error(message);
    }

    public BazaPodatakaException(Throwable cause) {
        super(cause);
    }
}
