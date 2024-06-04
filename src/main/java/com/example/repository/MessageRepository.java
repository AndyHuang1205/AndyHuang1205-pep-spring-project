package com.example.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
        Optional<Message> findByMessageId(int messageId);
        Optional<Integer> deleteById(int id);
        Optional<List<Message>> findAllByPostedBy(int id);
}
