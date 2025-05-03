# jpeg2000-decoder

## Overview

`jpeg2000-decoder` is a library designed to decode JPEG 2000 image files. It provides a simple and efficient way to process and extract image data from files encoded in the JPEG 2000 format, widely used for high-quality image compression.

## Features

- Decode JPEG 2000 image files.
- Remove alpha channels and convert images to JPG format.
- Easy integration into Spring Boot applications.

## Installation

To use `jpeg2000-decoder` in your Spring Boot project, follow these steps:

1. Clone this repository and build the project:

   ```bash
   git clone https://github.com/your-repo/jpeg2000-decoder.git
   cd jpeg2000-decoder
   mvn clean install
   ```

2. Add the generated dependency to your project's `pom.xml`:

   ```xml
   <dependency>
       <groupId>cu.datys</groupId>
       <artifactId>jpeg2000-decoder</artifactId>
       <version>0.0.1-SNAPSHOT</version>
   </dependency>
   ```

## Usage

### Injecting `ImageDecoder` in a Spring Boot Service

You can use the [`ImageDecoder`](src/main/java/cu/datys/jpeg2000/image_processor/ImageDecoder.java) class to process JPEG 2000 images in your Spring Boot application. Here's an example:

1. Create a service that uses `ImageDecoder`:

   ```java
   // filepath: src/main/java/com/example/service/ImageProcessingService.java
   package com.example.service;

   import cu.datys.jpeg2000.image_processor.ImageDecoder;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;

   import java.io.IOException;

   @Service
   public class ImageProcessingService {

       private final ImageDecoder imageDecoder;

       @Autowired
       public ImageProcessingService(ImageDecoder imageDecoder) {
           this.imageDecoder = imageDecoder;
       }

       public byte[] processImage(byte[] imageData) throws IOException {
           return imageDecoder.readAndProcessImage(imageData);
       }
   }
   ```

2. Create a REST controller to expose an endpoint for image processing:

   ```java
   // filepath: src/main/java/com/example/controller/ImageController.java
   package com.example.controller;

   import com.example.service.ImageProcessingService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.http.MediaType;
   import org.springframework.web.bind.annotation.*;
   import org.springframework.web.multipart.MultipartFile;

   import java.io.IOException;

   @RestController
   @RequestMapping("/api/images")
   public class ImageController {

       private final ImageProcessingService imageProcessingService;

       @Autowired
       public ImageController(ImageProcessingService imageProcessingService) {
           this.imageProcessingService = imageProcessingService;
       }

       @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
       public byte[] processImage(@RequestParam("file") MultipartFile file) throws IOException {
           return imageProcessingService.processImage(file.getBytes());
       }
   }
   ```

3. Test the endpoint by sending a JPEG 2000 image to `/api/images/process` using tools like `curl` or Postman:

   ```bash
   curl -X POST -F "file=@input.jp2" http://localhost:8080/api/images/process --output output.jpg
   ```

### Example Workflow

1. Upload a JPEG 2000 image to the `/api/images/process` endpoint.
2. The service will decode the image, remove the alpha channel (if present), and convert it to JPG format.
3. The processed image will be returned as the response.

## Requirements

- Java 21 or higher.
- Spring Boot 3.4.5 or higher.
- Maven 3.9.9 or higher.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with a detailed description of your changes.

## License

This project is licensed under the GNU General Public License v2. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

Special thanks to the contributors and the open-source community for their support and inspiration.