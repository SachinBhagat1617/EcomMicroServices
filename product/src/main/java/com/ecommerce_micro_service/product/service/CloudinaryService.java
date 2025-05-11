package com.ecommerce_micro_service.product.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString(); // public image URL
    }
    public void deleteImage(String imageUrl){
        try {
            // Extract public_id from the Cloudinary URL
            String publicId = extractPublicIdFromUrl(imageUrl);

            // Delete the image using public_id
            Map<String, String> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Check if deletion was successful
            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("Failed to delete image from Cloudinary");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary", e);
        }


    }

    private String extractPublicIdFromUrl(String imageUrl) {
        // Example Cloudinary URL format:
        // https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/folder/image_name.jpg

        try {
            // Split the URL by '/'
            String[] urlParts = imageUrl.split("/");

            // Get the filename with extension
            String fileNameWithExtension = urlParts[urlParts.length - 1];

            // Remove the file extension
            int extensionIndex = fileNameWithExtension.lastIndexOf('.');
            if (extensionIndex > 0) {
                return fileNameWithExtension.substring(0, extensionIndex);
            }

            // If no extension, return the whole filename
            return fileNameWithExtension;

        } catch (Exception e) {
            throw new RuntimeException("Invalid Cloudinary URL format", e);
        }
    }


}
