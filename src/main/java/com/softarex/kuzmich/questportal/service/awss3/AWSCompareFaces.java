package com.softarex.kuzmich.questportal.service.awss3;


import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;
import com.softarex.kuzmich.questportal.dto.RecognitionDTO;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

@Service
public class AWSCompareFaces {
    AmazonRekognition amazonRekognition;
    Float similarityThreshold = 70F;
    ByteBuffer sourceImageBytes=null;
    ByteBuffer targetImageBytes=null;

    @Autowired
    public AWSCompareFaces(AmazonRekognition amazonRekognition) throws IOException {
        this.amazonRekognition = amazonRekognition;
    }

    public CompareFacesResult getRequest(Image source, Image target) {
        CompareFacesRequest request = new CompareFacesRequest()
                .withSourceImage(source)
                .withTargetImage(target)
                .withSimilarityThreshold(similarityThreshold);
        return amazonRekognition.compareFaces(request);
    }

    public Image getSourceImage(MultipartFile sourceImage) {
        try (InputStream inputStream = new FileInputStream(convertMultiPartToFile(sourceImage))) {
            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
            throw new NoEntityException();
        }
        return new Image()
                .withBytes(sourceImageBytes);
    }

    public Image getTargetImage(MultipartFile targetImage) {
        try (InputStream inputStream = new FileInputStream(convertMultiPartToFile(targetImage))) {
            targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
            throw new NoEntityException();
        }
        return new Image()
                .withBytes(targetImageBytes);
    }


    public String compareFaces(RecognitionDTO recognitionDTO) {

        try {
            List<CompareFacesMatch> faceDetails = getRequest(getSourceImage(recognitionDTO.getImage()), getTargetImage(recognitionDTO.getTargetImage())).getFaceMatches();
            for (
                    CompareFacesMatch match : faceDetails) {
                ComparedFace face = match.getFace();
                BoundingBox position = face.getBoundingBox();
                return ("Faces matches with " + match.getSimilarity().toString()
                        + "% confidence.");

            }
        } catch (Exception ex) {
            List<ComparedFace> uncompared = getRequest(getSourceImage(recognitionDTO.getImage()), getTargetImage(recognitionDTO.getTargetImage())).getUnmatchedFaces();
            return ("There was " + uncompared.size()
                    + " face(s) that did not match");
        }
        return "success";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
