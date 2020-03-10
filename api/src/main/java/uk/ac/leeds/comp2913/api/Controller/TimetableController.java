package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    private final ResourceRepository resourceRepository;

    public TimetableController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    //Get all resources
    @GetMapping("")
    public Collection<Resource> getResources() {
        return resourceRepository.findAllWithBookings();
    }
}

