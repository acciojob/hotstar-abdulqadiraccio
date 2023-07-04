package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto) {

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription = new Subscription();
        SubscriptionType subscriptionType = subscriptionEntryDto.getSubscriptionType();
        int totalAmount = 0;
        int noOfScreens = subscriptionEntryDto.getNoOfScreensRequired();
        if (subscriptionType.toString().toUpperCase().equals("BASIC")) {
            totalAmount = 500 + 200 * noOfScreens;
        } else if (subscriptionType.toString().toUpperCase().equals("PRO")) {
            totalAmount = 800 + 250 * noOfScreens;
        } else if (subscriptionType.toString().toUpperCase().equals("ELITE")) {
            totalAmount = 1000 + 350 * noOfScreens;
        }
        subscription.setSubscriptionType(subscriptionType);
        subscription.setNoOfScreensSubscribed(noOfScreens);
        subscription.setTotalAmountPaid(totalAmount);
        subscription.setUser(user);

        user.setSubscription(subscription);


        userRepository.save(user);
        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId) throws Exception {

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        SubscriptionType subscriptionType = user.getSubscription().getSubscriptionType();
        Subscription userSubscription = user.getSubscription();
        int prevPaid = user.getSubscription().getTotalAmountPaid();
        int currPaid = 0;
        if (subscriptionType.toString().equals("ELITE")) throw new Exception("Already the best Subscription");
        if (userSubscription.getSubscriptionType().toString().equals("BASIC")) {
            currPaid = 800 + (250 * user.getSubscription().getNoOfScreensSubscribed());
            userSubscription.setSubscriptionType(SubscriptionType.PRO);
        } else {
            currPaid = 1000 + (350 * user.getSubscription().getNoOfScreensSubscribed());
            userSubscription.setSubscriptionType(SubscriptionType.ELITE);
        }

        subscriptionRepository.save(userSubscription);

        return currPaid - prevPaid;
    }

    public Integer calculateTotalRevenueOfHotstar() {

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList = subscriptionRepository.findAll();

        int revenue = 0;

        for (Subscription subscription : subscriptionList) {
            revenue += subscription.getTotalAmountPaid();
        }

        return revenue;

    }
}
