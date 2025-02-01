package guru.springframework.sfgjms.builder;

import guru.springframework.sfgjms.entity.Child;
import guru.springframework.sfgjms.entity.state.ChildDay;
import guru.springframework.sfgjms.entity.state.ChildEvent;
import guru.springframework.sfgjms.service.ChildService;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import static guru.springframework.sfgjms.entity.state.ChildEvent.*;

@Log
@Data
@Component
public class ActionChildEvent {
    @Autowired
    private ChildService service;
    private Child child;

    public Action<ChildDay, ChildEvent> action(ChildEvent event) {
        switch (event) {
            case GOING_TO_SCHOOL:
                return goToSchool();
            case DOING_HOMEWORK:
                return doHomework();
            case SPORT_ACTIVITY:
                return sportActivity();
            case PLAYING_VIDEO_GAMES:
                return playVideoGame();
            case LEISURE_ACTIVITY:
                return leisureActivity();
            default:
                return complete();
        }
    }

    private Action<ChildDay, ChildEvent> goToSchool() {
        return new Action<ChildDay, ChildEvent>() {
            @Override
            public void execute(StateContext<ChildDay, ChildEvent> context) {
                child.setDay(context.getTarget().getId());
                service.save(child);
                log.info("Method go to school executed: " + child.toString());
            }
        };

    }

    private Action<ChildDay, ChildEvent> playVideoGame() {

        return new Action<ChildDay, ChildEvent>() {
            @Override
            public void execute(StateContext<ChildDay, ChildEvent> context) {
                child.setDay(context.getTarget().getId());
                log.info("Method play video games executed");
            }
        };
    }

    private Action<ChildDay, ChildEvent> doHomework() {

        return new Action<ChildDay, ChildEvent>() {
            @Override
            public void execute(StateContext<ChildDay, ChildEvent> context) {
                child.setDay(context.getTarget().getId());
                log.info("Method do homework executed");
            }
        };
    }


    private Action<ChildDay, ChildEvent> leisureActivity() {

        return new Action<ChildDay, ChildEvent>() {
            @Override
            public void execute(StateContext<ChildDay, ChildEvent> context) {
                child.setDay(context.getTarget().getId());
                log.info("Method leisureActivity executed");
            }
        };
    }

    private Action<ChildDay, ChildEvent> sportActivity() {

        return new Action<ChildDay, ChildEvent>() {
            @Override
            public void execute(StateContext<ChildDay, ChildEvent> context) {
                child.setDay(context.getTarget().getId());
                service.save(child);
                log.info("Method sport activity executed");
            }
        };
    }

    private Action<ChildDay, ChildEvent> complete() {

        return new Action<ChildDay, ChildEvent>() {
            @Override
            public void execute(StateContext<ChildDay, ChildEvent> context) {
                child.setDay(ChildDay.END);
                service.save(child);
                log.info("Method complete executed");
            }
        };
    }
}
