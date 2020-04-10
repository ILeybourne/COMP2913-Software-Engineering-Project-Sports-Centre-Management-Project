package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import javax.validation.Valid;

import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;

public interface MembershipService {
    Page<Membership> findAllMembers(Pageable pageable);

    Membership findMembershipById(Long membership_id);

    Page<Membership> findMembershipsByMembershipType(Pageable pageable, Long membership_type_id);

    Membership addMember(Long account_id, Long membership_type_id, Membership membership);

    Membership updateMembership(Long membership_id, Membership membershipRequest);

    ResponseEntity<?> deleteMembership(Long membership_id);

    Membership stopRepeatPayment(Long membership_id);

    void automatedMembershipRenewals();

}
