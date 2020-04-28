package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import uk.ac.leeds.comp2913.api.Domain.Model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long>, CustomMembershipRepository {
    Page<Membership> findByMembershipTypeId(Pageable pageable, Long membershipTypeId);
    Page<Membership> findByAccountId(Pageable pageable, Long account_id);
    List<Membership> findAll();
    Optional<Membership> findById(Long membership_id);

    @Query("select m from Membership m " +
            "where m.repeatingPayment = true and m.endDate = (SELECT MAX(mm.endDate)" +
            "from Membership mm where mm.account.id = m.account.id)")
    List<Membership> findLastWithRepeatPayments();

    List<Membership> findAllByAccountIdOrderByEndDateAsc(Long accountId);
}


