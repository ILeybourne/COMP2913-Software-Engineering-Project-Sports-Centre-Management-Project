package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.Domain.Service.ResourceService;
import uk.ac.leeds.comp2913.api.Exception.FileUploadFailedException;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.FacilityDTOAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.ResourceDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TODO: @CHORE, annotate with Swagger API documentation
 * TODO: @CHORE, move domain logic into a service
 * TODO: @CHORE, add HAL to all endpoints, with links to where the client can find
 * *          the associated resource, account and activity  for the booking
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;
    private final PagedResourcesAssembler<Resource> pagedResourcesAssembler;
    private final FacilityDTOAssembler facilityDTOAssembler;


    public ResourceController(ResourceService resourceService, PagedResourcesAssembler<Resource> pagedResourcesAssembler, FacilityDTOAssembler facilityDTOAssembler) {
        this.resourceService = resourceService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.facilityDTOAssembler = facilityDTOAssembler;
    }

    //Get Resources
    @GetMapping(value = "", produces = "application/hal+json")
    @Operation(summary = "Get all facilities",
            description = "Get a list of all facilities with basic information")
    public PagedModel<ResourceDTO> getResources(Pageable pageable) {
        Page<Resource> allResources = resourceService.findAll(pageable);
        return pagedResourcesAssembler.toModel(allResources, facilityDTOAssembler);
    }

    @GetMapping("/{resource_id}")
    @Operation(summary = "Get a specific facility",
            description = "Get a specific facility with more information and links")
    public ResourceDTO getResourceById(@Parameter(description = "The ID of the facility/resource", required = true) @PathVariable Long resource_id) {
        final Resource resource = resourceService.findById(resource_id);
        return facilityDTOAssembler.toModel(resource);
    }

    //Post new resource
    @PostMapping("")
    @Operation(summary = "Create a new facility",
            description = "Create a new facility")
    @PreAuthorize("hasAuthority('SCOPE_create:resources')")
    public ResourceDTO createResource(@Parameter(description = "The ID of the facility/resource", required = true) @Valid @RequestBody Resource resource) {
        return facilityDTOAssembler.toModel(resourceService.create(resource));
    }

    //update resource
    @PutMapping("/{resource_id}")
    @Operation(summary = "Update a facility",
            description = "Update the details of a facility")
    @PreAuthorize("hasAuthority('SCOPE_update:resources')")
    public Resource updateResource(@Parameter(description = "The ID of the facility/resource", required = true) @PathVariable Long resource_id,
                                   @Parameter(description = "A Resource object", required = true) @Valid @RequestBody Resource resourceRequest) {
        return resourceService.update(resource_id, resourceRequest);
    }

    //delete resource
    @DeleteMapping("/{resource_id}")
    @Operation(summary = "delete a facility",
            description = "Delete a facility")
    @PreAuthorize("hasAuthority('SCOPE_delete:resources')")
    public ResponseEntity<?> deleteResource(@Parameter(description = "The ID of the facility/resource", required = true) @PathVariable Long resource_id) {
        resourceService.deleteById(resource_id);
        return ResponseEntity.noContent()
                .build();
    }

    //    @PreAuthorize("hasAuthority('SCOPE_update:resource')")
    @Operation(
            summary = "Upload an image of the facility to the server",
            description = "Upload an image of the facility to the server to be displayed on the facilities page"
    )
    @PostMapping("upload/{resource_id}")
    @PreAuthorize("hasAuthority('SCOPE_update:resources')")
    public ResourceDTO uploadImage(@Parameter(description = "The ID of the facility", required = true) @PathVariable Long resource_id, @RequestParam("image") MultipartFile image) {
        // Save image to S3
        // Check image size
        // return the url to the image using a href
        try {
            String TEMP_FILE_PREFIX = "resource";
            String TEMP_FILE_POSTFIX = image.getOriginalFilename();
            File tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_POSTFIX);
            tempFile.deleteOnExit();
            image.transferTo(tempFile);

            if (image.isEmpty()) {
                throw new FileUploadFailedException("The file provided was empty, please try again");
            }

            String mimetype = new MimetypesFileTypeMap().getContentType(tempFile);
            String type = mimetype.split("/")[0];

            if (!type.equals("image")) {
                throw new FileUploadFailedException("The file provided was not an image, please try again with an image");
            }

            Resource r = this.resourceService.uploadImage(resource_id, tempFile);
            tempFile.delete();
            return facilityDTOAssembler.toModel(r);
        } catch (IOException e) {
            throw new FileUploadFailedException("The file upload failed, please try again");
        }
    }

    @GetMapping("image/{resource_id}")
    public ResponseEntity<byte[]> downloadImage(@Parameter(description = "The ID of the facility", required = true) @PathVariable Long resource_id) {
        try {
            File image = resourceService.downloadImage(resource_id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(Files.probeContentType(image.toPath())));
            return new ResponseEntity<>(Files.readAllBytes(image.toPath()), headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Image not found");
        }
    }
}

