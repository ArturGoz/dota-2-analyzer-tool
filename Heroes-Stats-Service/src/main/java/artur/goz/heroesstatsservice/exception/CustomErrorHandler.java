package artur.goz.heroesstatsservice.exception;


import artur.goz.heroesstatsservice.dto.RabbitResponse;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component("myCustomErrorHandler")
public class CustomErrorHandler implements RabbitListenerErrorHandler {
    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1,
                              ListenerExecutionFailedException e) throws Exception {
        return RabbitResponse.create(null, e.getMessage(), false);
    }
}
