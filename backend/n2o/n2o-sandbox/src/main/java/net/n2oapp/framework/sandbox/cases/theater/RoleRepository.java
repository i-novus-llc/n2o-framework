package net.n2oapp.framework.sandbox.cases.theater;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "roles")
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    @Query("SELECT p FROM Role p " +
            "where :actorId is null or (p.actorId = :actorId)")
    Page<Role> findAll(Pageable pageable,
                        @Param("actorId") Long actorId);
}
