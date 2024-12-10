package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByTagName(String tagName);

    List<Tag> findByTagNameIn(Set<String> tagNames);

}
