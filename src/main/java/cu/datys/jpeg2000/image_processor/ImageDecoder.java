package cu.datys.jpeg2000.image_processor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import cu.datys.jpeg2000.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;


@Component
@Slf4j
public class ImageDecoder {

    /**
     * Reads and processes an image from the provided byte array. If the image is in
     * JPEG2000 format,
     * it processes the image by removing the alpha channel (if present) and
     * converts it to a JPG format.
     * If the image is not in JPEG2000 format, it returns the original byte array
     * without modification.
     *
     * @param imageData The byte array containing the image data. Must not be null
     *                  or empty.
     * @return A byte array containing the processed image data. If the image is not
     *         in JPEG2000 format,
     *         the original byte array is returned.
     * @throws IllegalArgumentException If the provided imageData is null or empty.
     * @throws IOException              If an error occurs while reading or
     *                                  processing the image.
     */
    public byte[] readAndProcessImage(byte[] imageData) throws IOException {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Image data must not be null or empty");
        }

        // Detectar el MIME type de la imagen con Apache Tika
        Tika tika = new Tika();
        String mimeType = tika.detect(imageData);

        // Si la imagen no es JPEG2000, devolverla tal cual
        if (!Constants.JPEG2000_MIME_TYPE.equals(mimeType) && !Constants.JPEG2000_SCND_TYPE.equals(mimeType)) {
            log.info("Image is not JPEG2000, returning original data...");
            return imageData;
        }

        log.info("Processed image JPEG2000...");
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                ImageInputStream iis = ImageIO.createImageInputStream(bais)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                throw new IOException("Image format not supported or no reader found for JPEG2000");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(iis);
                BufferedImage image = reader.read(0);

                // Remove alfa channel
                BufferedImage processedImage = removeAlphaChannel(image);

                // Convert to JPG byte array
                return convertToByteArray(processedImage, "JPG");

            } finally {
                reader.dispose();
            }
        }
    }

    /**
     * Removes the alpha channel from the given BufferedImage. If the image does not have
     * an alpha channel, it is returned as is. Otherwise, a new image is created with the
     * alpha channel removed, and the original image is drawn onto it with a white background.
     *
     * @param image the BufferedImage from which the alpha channel should be removed
     * @return a new BufferedImage without an alpha channel, or the original image if it
     *         does not have an alpha channel
     */
    private BufferedImage removeAlphaChannel(BufferedImage image) {
        if (!image.getColorModel().hasAlpha()) {
            return image;
        }

        BufferedImage newImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = newImage.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            g.drawImage(image, 0, 0, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }

    /**
     * Converts a {@link BufferedImage} to a byte array in the specified format.
     *
     * @param image the {@link BufferedImage} to be converted.
     * @param format the format in which the image should be encoded (e.g., "png", "jpg").
     * @return a byte array representing the encoded image.
     * @throws IOException if an error occurs during writing or if no writer is found for the specified format.
     */
    private byte[] convertToByteArray(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (!ImageIO.write(image, format, baos)) {
                throw new IOException("No se encontró writer para formato: " + format);
            }
            return baos.toByteArray();
        }
    }
}
