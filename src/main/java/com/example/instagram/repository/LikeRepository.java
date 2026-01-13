package com.example.instagram.repository;

import com.example.instagram.model.Like;
import com.example.instagram.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

    boolean existsById(LikeId id);

    long countByPostId(String postId);
}
