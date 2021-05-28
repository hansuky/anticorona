package anticorona;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="cancellations", path="cancellations")
public interface CancellationRepository extends PagingAndSortingRepository<Cancellation, Long>{

    Optional<Cancellation> findByBookingId(Long bookingId);
    
}
