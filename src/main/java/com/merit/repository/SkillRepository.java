package com.merit.repository;

import com.merit.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("select s from Skill s where s.name = :name")
    Skill findByName(String name);

}
