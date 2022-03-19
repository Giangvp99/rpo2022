package ru.iu3.rpo.backen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.rpo.backen.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}