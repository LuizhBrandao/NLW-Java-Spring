package br.com.nlw.events.service;

import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionService {

    @Autowired
    private EventRepository evtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subRepository;

    public Subscription createNewSubscription(String eventName, User user){

        //Recuperar o evento pelo nome
        Event evt = evtRepository.findByPrettyName(eventName);
        user = userRepository.save(user);

        Subscription subs = new Subscription();
        subs.setEvent(evt);
        subs.setSubscriber(user);

        Subscription res = subRepository.save(subs);
        return res;
    }
}
