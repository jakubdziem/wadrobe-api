package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.model.WornDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WornDateRepository extends JpaRepository<WornDate, Long> {
}
