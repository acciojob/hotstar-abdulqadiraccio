package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository

        userRepository.save(user);
        return user.getId();


    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        Integer count =0;
        User user1 = userRepository.findById(userId).get();
        Subscription subscription = user1.getSubscription();
        SubscriptionType subscriptionType = subscription.getSubscriptionType();

        if(subscriptionType.toString().equals("BASIC")){
            for (WebSeries wb : webSeriesList){
                if(wb.getSubscriptionType().toString().equals("BASIC"))
                    count++;
            }
        }
        else if (subscriptionType.toString().equals("PRO")) {
            for (WebSeries wb : webSeriesList){
                if(wb.getSubscriptionType().toString().equals("PRO") || wb.getSubscriptionType().toString().equals("BASIC")){
                    count++;
                }
            }
        }
        else    //(subscriptionType.toString().equals("ELITE")))
        {
            for (WebSeries wb : webSeriesList){
                count++;    // if you are Elite you can watch all webSeries
            }
        }

        return count;
    }


    }



