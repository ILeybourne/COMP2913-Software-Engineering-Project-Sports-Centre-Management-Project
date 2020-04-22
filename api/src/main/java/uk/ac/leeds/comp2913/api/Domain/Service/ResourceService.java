package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.Exception.UnableToUploadFileException;

@Service
public interface ResourceService {
    @Transactional
    Resource create(Resource resource);

    @Transactional
    Resource findById(Long resource_id);

    @Transactional
    Resource update(Long resource_id, Resource resourceRequest);

    @Transactional
    void deleteById(Long resource_id) throws ResourceNotFoundException;

    Page<Resource> findAll(Pageable pageable);

    Resource uploadImage(Long resource_id, File image) throws IOException, UnableToUploadFileException;

    File downloadImage(Long resource_id) throws IOException;
}
