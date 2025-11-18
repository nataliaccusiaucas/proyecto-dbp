package com.hirehub.backend.jobrequest.event;

import com.hirehub.backend.jobrequest.domain.JobRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JobRequestCreatedEvent extends ApplicationEvent {

    private final JobRequest jobRequest;

    public JobRequestCreatedEvent(Object source, JobRequest jobRequest) {
        super(source);
        this.jobRequest = jobRequest;
    }

}