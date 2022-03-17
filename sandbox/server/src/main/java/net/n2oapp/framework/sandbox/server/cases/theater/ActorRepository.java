package net.n2oapp.framework.sandbox.server.cases.theater;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "actors")
public interface ActorRepository extends PagingAndSortingRepository<Actor, Long> {
    @Query("SELECT p FROM Actor p " +
            "where :firstName is null or (p.firstName like %:firstName%)")
    Page<Actor> findAll(Pageable pageable,
                         @Param("firstName") String firstName);
}
