package anticorona;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="injections", path="injections")
public interface InjectionRepository extends PagingAndSortingRepository<Injection, Long>{

    Optional<Injection> findByBookingId(Long bookingId);
    
}
