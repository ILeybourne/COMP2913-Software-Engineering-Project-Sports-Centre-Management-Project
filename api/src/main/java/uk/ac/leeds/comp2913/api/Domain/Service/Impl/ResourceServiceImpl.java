package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.Util.S3Client;

@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final S3Client s3Client;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository, S3Client s3Client) {
        this.resourceRepository = resourceRepository;
        this.s3Client = s3Client;
    }

    @Override
    public void deleteById(Long resource_id) {
        /*TODO, check foreign keys*/
        this.resourceRepository.findById(resource_id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
        this.resourceRepository.deleteById(resource_id);
    }

    @Override
    public Page<Resource> findAll(Pageable pageable) {
        return this.resourceRepository.findAll(pageable);
    }

    @Override
    public Resource uploadImage(Long resource_id, File image) throws IOException {
        Resource r = resourceRepository.findById(resource_id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID " + resource_id + " could not be found"));
        String extension = "";
        final String path = image.getPath();

        if (path.contains(".")) {
            extension = path.substring(path.lastIndexOf("."));
        }
        String filename = r.getImageName() + extension;
        File namedFile = new File(filename);

        Files.copy(image.toPath(), namedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        s3Client.uploadFile(namedFile);
        r.setFilePath(filename);
        return this.resourceRepository.save(r);
    }

    @Override
    public File downloadImage(Long resource_id) throws IOException {
        Resource resource = resourceRepository.findById(resource_id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID " + resource_id + " could not be found"));
        final String filePath = resource.getFilePath();
        byte[] bytes = this.s3Client.getFile(filePath);
        FileOutputStream stream = new FileOutputStream(filePath);
        stream.write(bytes);
        return new File(filePath);
    }

    @Override
    public Resource findById(Long resource_id) {
        return this.resourceRepository.findById(resource_id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));

    }

    @Override
    public Resource update(Long resource_id, Resource resourceRequest) {
        Resource r = this.resourceRepository.findById(resource_id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + resource_id));
        r.setName(resourceRequest.getName());
        r.setActivities(resourceRequest.getActivities());
        r.setActivityTypes(resourceRequest.getActivityTypes());
        return r;
    }

    @Override
    public Resource create(Resource resource) {

        return resourceRepository.save(resource);
    }
}
