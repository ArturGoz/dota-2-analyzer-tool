package artur.goz.userservice.exception;


import artur.goz.userservice.dto.RabbitResponse;
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
        Throwable rootCause = e.getCause();
        if (rootCause instanceof MyException e1) {
            return RabbitResponse.create(null, e1.getMessage() + " " + e1.getErrorCode(), false);
        }
        return RabbitResponse.create(null, e.getMessage(), false);
    }
}
