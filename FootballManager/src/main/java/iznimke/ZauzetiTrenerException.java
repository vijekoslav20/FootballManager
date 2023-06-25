package iznimke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZauzetiTrenerException extends Exception{

    private static final Logger logger = LoggerFactory.getLogger(BazaPodatakaException.class);

    public ZauzetiTrenerException() {
    }

    public ZauzetiTrenerException(String message) {
        super(message);

        logger.error(message);
    }

    public ZauzetiTrenerException(String message, Throwable cause) {
        super(message, cause);

        logger.error(message);
    }

    public ZauzetiTrenerException(Throwable cause) {
        super(cause);
    }
}
