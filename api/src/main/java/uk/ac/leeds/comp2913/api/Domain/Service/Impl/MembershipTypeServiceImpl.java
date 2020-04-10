package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.MembershipTypeRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.MembershipType;
import uk.ac.leeds.comp2913.api.Domain.Service.MembershipTypeService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

@Service
public class MembershipTypeServiceImpl implements MembershipTypeService {
    private final MembershipTypeRepository membershipTypeRepository;

    @Autowired
    public MembershipTypeServiceImpl(MembershipTypeRepository membershipTypeRepository){
        this.membershipTypeRepository = membershipTypeRepository;
    }

    @Override
    public Page<MembershipType> findAllMembershipTypes(Pageable pageable) {
        return membershipTypeRepository.findAll(pageable);
    }

    @Override
    public MembershipType findMembershipTypeById(Long membership_type_id) {
        return membershipTypeRepository.findById(membership_type_id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership type not found for ID" + membership_type_id));
    }

}
