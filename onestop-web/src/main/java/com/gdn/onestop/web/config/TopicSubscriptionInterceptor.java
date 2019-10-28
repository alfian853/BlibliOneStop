package com.gdn.onestop.web.config;

import com.gdn.onestop.service.GroupService;
import com.gdn.onestop.service.impl.OsUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    @Autowired
    GroupService groupService;

    @Autowired
    OsUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            Principal userPrincipal = headerAccessor.getUser();
            if(!validateSubscription(userPrincipal, headerAccessor.getDestination())) {
                throw new IllegalArgumentException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscription(Principal principal, String topicDestination) {
        if (principal == null || !topicDestination.startsWith("/subscribe/chat")) {
            return false;
        }

        String groupId = topicDestination.substring(16);

        return groupService.isValidMember(
                userDetailsService.loadUserByUsername(principal.getName()),
                groupId
        );
    }
}
