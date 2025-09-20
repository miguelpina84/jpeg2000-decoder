package cu.datys.jpeg2000.image_processor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageDecoderTest {

    private ImageDecoder imageDecoder;

    @BeforeEach
    void setUp() {
        imageDecoder = new ImageDecoder();
    }

    @Test
    void testNullImageDataThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> imageDecoder.readAndProcessImage(null));
    }

    @Test
    void testEmptyImageDataThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> imageDecoder.readAndProcessImage(new byte[0]));
    }

    @Test
    void testNonJPEG2000ImageReturnsOriginal() throws IOException {
        byte[] dummyImage = "not-a-jpeg2000".getBytes();
        byte[] result = imageDecoder.readAndProcessImage(dummyImage);
        assertArrayEquals(dummyImage, result);
    }
}
