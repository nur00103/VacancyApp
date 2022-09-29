package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.dto.response.PhotoResponse;
import com.example.vacancyapp.dto.response.ResponsePhoto;
import com.example.vacancyapp.entity.Photo;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import com.example.vacancyapp.repository.PhotoRepository;
import com.example.vacancyapp.service.PhotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final PhotoRepository photoRepository;

    @Override
    public ResponsePhoto uploadPhoto(MultipartFile multipartFile) {
        try {
            String url = "https://vusalrehimov.000webhostapp.com/upload.php";
            File file = multipartFileToFile(multipartFile);
            MultiValueMap multiValueMap = getMultiValueMap(file);
            Object result = webClient
                    .post()
                    .uri(url)
                    .contentType(MediaType.parseMediaType(multipartFile.getContentType()))
                    .body(BodyInserters.fromMultipartData(multiValueMap))
                    .retrieve()
                    .bodyToMono(Object.class)
                            .block();
            file.delete();

            String response = objectMapper.writeValueAsString(result);

            ResponsePhoto responseModel =
                    objectMapper.readValue(response, ResponsePhoto.class);

            if (responseModel.getError()) {
                throw new MyException(ExceptionEnum.PHOTO);
            } else {
                String photoUrl=responseModel.getResult().getUrl();
                Photo photo=new Photo();
                photo.setUrl(photoUrl);
                photoRepository.save(photo);
                return responseModel;
            }
        }catch (Exception ex){
            log.error("Error", ex);
            ex.printStackTrace();
            return new ResponsePhoto(
                    new PhotoResponse(null),
                    true,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    public MultiValueMap<String, HttpEntity<?>> getMultiValueMap(File file){
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }

    public File multipartFileToFile(MultipartFile multipartFile) throws Exception{
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }
}
