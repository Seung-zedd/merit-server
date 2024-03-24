package com.merit.repository;

import com.merit.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Skill findByName(@Param("name") String name);

//    @Modifying(clearAutomatically = true)
//    @Query("update Skill s set s.name  = :name")
//    void bulkSkillName(@Param("name") String name);
}
