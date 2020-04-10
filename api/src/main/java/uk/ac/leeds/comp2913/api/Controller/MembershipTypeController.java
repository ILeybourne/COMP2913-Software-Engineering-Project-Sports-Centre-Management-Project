package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipTypeService;
import uk.ac.leeds.comp2913.api.ViewModel.Assembler.MembershipTypePagedResourcedAssembler;
import uk.ac.leeds.comp2913.api.ViewModel.MembershipTypeDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/membership/types")
public class MembershipTypeController {
    private final MembershipTypeService membershipTypeService;
    private final PagedResourcesAssembler<MembershipType> pagedResourcesAssembler;
    private final MembershipTypePagedResourcedAssembler membershipTypePagedResourcedAssembler;



    @Autowired
    public MembershipTypeController(MembershipTypeService membershipTypeService, PagedResourcesAssembler<MembershipType> pagedResourcesAssembler, MembershipTypePagedResourcedAssembler membershipTypePagedResourcedAssembler){
        this.membershipTypeService = membershipTypeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.membershipTypePagedResourcedAssembler = membershipTypePagedResourcedAssembler;
    }
    @GetMapping("")
    @Operation(summary = "Get all membership types",
          description = "Get list of all available membership types" +
                  "showing basic information")
    public PagedModel<MembershipTypeDTO> getMembershipTypes(Pageable pageable) {
          Page<MembershipType> allMembershipTypes = membershipTypeService.findAllMembershipTypes(pageable);
          return pagedResourcesAssembler.toModel(allMembershipTypes, membershipTypePagedResourcedAssembler);
      }

        @GetMapping("/{membership_type_id}")
  @Operation(summary = "Get a specific membership type",
          description = "Get particular membership type with a link to view all members with this type")
  public MembershipTypeDTO getMembershipTypeById( @Parameter(description = "The ID of the membership type", required = true)@PathVariable Long membership_type_id) {
      MembershipTypeDTO membershipType = membershipTypePagedResourcedAssembler.toModel(membershipTypeService.findMembershipTypeById(membership_type_id));
      membershipType.add(linkTo(MembershipController.class).slash(membership_type_id).withRel("add membership of this type"));
      membershipType.add(linkTo(MembershipController.class).slash("members").slash("type").slash(membership_type_id).withRel("Memberships with this type"));
      return membershipType;
  }
}
