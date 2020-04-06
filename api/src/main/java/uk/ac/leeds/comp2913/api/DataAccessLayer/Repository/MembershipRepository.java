package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import uk.ac.leeds.comp2913.api.Domain.Model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByMembershipTypeId(Long membershipTypeId);
    List<Membership> findAll();
    Optional<Membership> findById(Long membership_id);
    }
