package br.com.nlw.events.service;

import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exceptions.SubscriptionConflictException;
import br.com.nlw.events.exceptions.EventNotFoundException;
import br.com.nlw.events.exceptions.UserIndicadorNotFoundException;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository evtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId){

        //RECUPERAR O EVENTO PELO NOME
        Event evt = evtRepository.findByPrettyName(eventName);
        if (evt == null){ //CASO ALTERNATIVO 2
            throw new EventNotFoundException("Evento "+eventName+" não existe");
        }
        User userRec = userRepository.findByEmail(user.getEmail());
        if (userRec == null) { // CASO ALTERNATIVO 1
            userRec = userRepository.save(user);
        }

        User indicador = userRepository.findById(userId).orElse(null);
        if (indicador == null){
            throw new UserIndicadorNotFoundException("Usuario "+ userId +" indicador não existe");
        }

        Subscription subs = new Subscription();
        subs.setEvent(evt);
        subs.setSubscriber(userRec);
        subs.setIndication(indicador);

        Subscription tmpSub = subRepository.findByEventAndSubscriber(evt, userRec);
        if (tmpSub != null){ // CASO ALTERNATIVO 3
            throw new SubscriptionConflictException("Ja existe inscrição para o usuário " + userRec.getName() + " no evento " + evt.getTitle());
        }

        Subscription res = subRepository.save(subs);
        return new SubscriptionResponse(res.getSubscriptionNumber(), "http//codecraft.com/subscription/"+ res.getEvent().getPrettyName()+"/"+res.getSubscriber().getId());
    }
}
