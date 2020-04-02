package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class ResourceServiceImpl implements ResourceService {
    private ResourceRepository resourceRepository;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
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
