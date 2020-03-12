package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.leeds.comp2913.api.Domain.Model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
