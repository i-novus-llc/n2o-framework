package net.n2oapp.framework.sandbox.cases.persons;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "persons")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
    @Query("SELECT p FROM Person p " +
            "where :firstName is null or (p.firstName like %:firstName%)")
    Page<Person> findAll(Pageable pageable,
                         @Param("firstName") String firstName);
}
