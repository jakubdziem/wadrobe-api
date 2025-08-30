package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByName(String name);
}
