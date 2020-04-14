package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;

public interface MembershipTypeService {


    Page<MembershipType> findAllMembershipTypes(Pageable pageable);

    MembershipType findMembershipTypeById(Long membership_type_id);
}
