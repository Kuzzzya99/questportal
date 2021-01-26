package com.softarex.kuzmich.questportal.repository;

import com.softarex.kuzmich.questportal.entity.Member;
import com.softarex.kuzmich.questportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findById(Integer id);

    Optional<Member> findByUser (User userId);
}
