package cu.datys.jpeg2000.utils;


/**
 * A utility class that holds constant values related to JPEG2000 file handling.
 * This class is not meant to be instantiated.
 */
public class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final String JPEG2000_MIME_TYPE = "image/jp2";
    public static final String JPEG2000_EXTENSION = ".jp2";
    public static final String JPEG2000_MAGIC_NUMBER = "00000000"; // Placeholder for actual magic number
    public static final String JPEG2000_FILE_NAME = "image" + JPEG2000_EXTENSION;
    public static final int JPEG2000_HEADER_SIZE = 12; // Placeholder for actual header size
    public static final String JPEG2000_SCND_TYPE = "image/jpeg2000"; // Placeholder for actual MIME type
}
