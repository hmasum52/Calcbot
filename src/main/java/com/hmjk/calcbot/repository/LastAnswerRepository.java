package com.hmjk.calcbot.repository;

import com.hmjk.calcbot.model.LastAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LastAnswerRepository extends JpaRepository<LastAnswer, Integer> {
     LastAnswer getLastAnswerByUserId(String userId);

     @Modifying
     @Query("UPDATE LastAnswer l SET l.answer =: answer WHERE l.userId=: userID")
     void updateAnswer(@Param(value = "userID") String userID, @Param(value = "answer") double answer);
}
