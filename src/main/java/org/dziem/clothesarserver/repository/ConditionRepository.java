package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.model.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionRepository  extends JpaRepository<Condition, Long> {
    List<Condition> findAllByName(String name);
}
