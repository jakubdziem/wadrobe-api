package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeRepository  extends JpaRepository<Size, Long> {
    List<Size> findAllByName(String name);
}
