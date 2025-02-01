package guru.springframework.sfgjms.service;

import com.typesafe.config.Config;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.entity.Child;
import guru.springframework.sfgjms.entity.StateMachineWrapper;
import guru.springframework.sfgjms.util.ConfigProvider;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Service
public class SendMessengerService {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ChildService childService;


    public boolean sendMsg(long childId, List<StateMachineWrapper> list) {
        Config config = ConfigProvider.createChildDayConf(childId, list);
        String msg = config.toString();
        int startIndex = msg.indexOf('{');
        int endIndex = msg.lastIndexOf('}');
        if (isChild(childId)) {
            jmsTemplate.convertAndSend(JmsConfig.CHILD_QUEUE + childId,
                    msg.substring(startIndex, endIndex + 1));
            return true;
        } else {
            return false;
        }

    }


    public Config receive(long id) {
        if (!isChild(id)) return null;
        String message = (String) jmsTemplate.receiveAndConvert(JmsConfig.CHILD_QUEUE + id);
        log.info(message);
        return ConfigProvider.readConfString(message);

    }

    private Boolean isChild(long id) {
        Child child = childService.findById(id);
        if (child != null) {
            return true;
        }
        return false;
    }

}

