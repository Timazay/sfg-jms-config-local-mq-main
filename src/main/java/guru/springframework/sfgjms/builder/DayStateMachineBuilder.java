package guru.springframework.sfgjms.builder;


import guru.springframework.sfgjms.entity.Child;
import guru.springframework.sfgjms.entity.state.ChildDay;
import guru.springframework.sfgjms.entity.state.ChildEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class DayStateMachineBuilder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private StateMachineBuilder.Builder<ChildDay, ChildEvent> builder;
    @Autowired
    private ActionChildEvent action;
    {
        this.builder = StateMachineBuilder.builder();
        try {
            builder.configureConfiguration()
                    .withConfiguration()
                    .listener(listener())
                    .autoStartup(true);

            builder.configureStates()
                    .withStates()
                    .initial(ChildDay.NEW)
                    .state(ChildDay.WEEKEND)
                    .state(ChildDay.WEEKDAY)
                    .end(ChildDay.END);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StateMachine<ChildDay, ChildEvent> buildMachine() throws Exception {
        return builder.build();
    }

    public DayStateMachineBuilder build(Child child, ChildDay source, ChildDay target, ChildEvent event) throws Exception {
        action.setChild(child);
        /*if (child != null) {*/
            builder.configureTransitions()
                    .withExternal()
                    .source(source)
                    .target(target)
                    .event(event)
                    .action(action.action(event));
        return this;
    }
    public StateMachineListener<ChildDay, ChildEvent> listener() {
        return new StateMachineListenerAdapter<ChildDay, ChildEvent>() {
            @Override
            public void stateChanged(State<ChildDay, ChildEvent> from, State<ChildDay, ChildEvent> to) {
                logger.info("State change from " + from.getId() + " to " + to.getId());
            }
        };
    }

}
