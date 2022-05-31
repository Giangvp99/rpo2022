package ru.iu3.rpo.backen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.rpo.backen.models.Painting;

@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long> {
}
