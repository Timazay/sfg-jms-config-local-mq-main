package guru.springframework.sfgjms.service;

import com.typesafe.config.Config;
import guru.springframework.sfgjms.builder.DayStateMachineBuilder;
import guru.springframework.sfgjms.entity.Child;
import guru.springframework.sfgjms.entity.StateMachineWrapper;
import guru.springframework.sfgjms.entity.state.ChildDay;
import guru.springframework.sfgjms.entity.state.ChildEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TypeSafeSMService {
    @Autowired
    private DayStateMachineBuilder builder;
    @Autowired
    private ChildService childService;
    private final Map<Long, StateMachine<ChildDay, ChildEvent>> stateMachineMap;
    {
        stateMachineMap = new HashMap<>();
    }

    public StateMachine<ChildDay, ChildEvent> receiveSM(Config config) throws Exception {
        Child child = childService.findById(config.getLong("id"));

        if (child == null) return null;
        setNewDay(child);
        List<? extends Config> childConfigs = config.getConfigList("children");

        for (Config childConfig : childConfigs) {
            ChildDay source = ChildDay.valueOf(childConfig.getString("source"));
            ChildDay target = ChildDay.valueOf(childConfig.getString("target"));
            ChildEvent event = ChildEvent.valueOf(childConfig.getString("event"));

            builder.build(child, source, target, event);
        }

        StateMachine<ChildDay, ChildEvent> stateMachine = builder.buildMachine();
        stateMachineMap.put(child.getId(),stateMachine);
        return stateMachine;
    }

    public StateMachine<ChildDay, ChildEvent> getMachine(long id){
        Child child = childService.findById(id);
        if (child == null){
            return null;
        }
       return stateMachineMap.get(id);
    }
    public void setNewDay(Child child) {
        child.setDay(ChildDay.NEW);
        childService.save(child);
    }


}
