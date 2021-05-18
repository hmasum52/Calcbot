package com.hmjk.calcbot.service;

import com.hmjk.calcbot.model.LastAnswer;
import com.hmjk.calcbot.repository.LastAnswerRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hasan Masum
 */
@Service
public class LastAnswerService {
    public static final String TAG = "LastAnswerService->";

    @Autowired
    LastAnswerRepository lastAnswerRepository;

    public LastAnswer getLastAnswer(String userId){
        return lastAnswerRepository.getLastAnswerByUserId(userId);
    }

    @Transactional
    public void save(LastAnswer lastAnswer){
        if(lastAnswer!=null){
            lastAnswerRepository.flush();
            LastAnswer previousSavedAnswer = getLastAnswer(lastAnswer.getUserId());
            if(previousSavedAnswer!=null){
                previousSavedAnswer.setAnswer(lastAnswer.getAnswer());
                lastAnswerRepository.save(previousSavedAnswer);
            }else
                lastAnswerRepository.save(lastAnswer);
        }
        else{
            System.out.println(TAG+"save(): lastAnswer is null");
        }
    }

}
