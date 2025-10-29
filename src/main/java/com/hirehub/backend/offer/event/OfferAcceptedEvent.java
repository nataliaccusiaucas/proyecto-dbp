package com.hirehub.backend.offer.event;

import com.hirehub.backend.offer.domain.Offer;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OfferAcceptedEvent extends ApplicationEvent {

    private final Offer offer;

    public OfferAcceptedEvent(Object source, Offer offer) {
        super(source);
        this.offer = offer;
    }

}
