package guru.springframework.sfgjms.controller;

import com.typesafe.config.Config;
import guru.springframework.sfgjms.dto.SMRequest;
import guru.springframework.sfgjms.entity.StateMachineWrapper;
import guru.springframework.sfgjms.entity.state.ChildDay;
import guru.springframework.sfgjms.entity.state.ChildEvent;
import guru.springframework.sfgjms.service.SendMessengerService;
import guru.springframework.sfgjms.service.TypeSafeSMService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log
public class SendTestController {
    private final ResponseEntity<?> NOT_FOUND =  ResponseEntity.notFound().build();
    private final int FIRST = 0;
    @Autowired
    private TypeSafeSMService typeSafeSMService;
    @Autowired
    private SendMessengerService service;


    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody SMRequest request) {
        List<StateMachineWrapper> confs = new ArrayList<>();
        List<ChildEvent> events = request.getEvents();
        ChildEvent first = events.get(FIRST);
        confs.add(new StateMachineWrapper(ChildDay.NEW, request.getChildDay(), first));
        for (ChildEvent e : events) {
            if (e != first)
                confs.add(new StateMachineWrapper(request.getChildDay(), request.getChildDay(), e));
        }
        confs.add(new StateMachineWrapper(request.getChildDay(), ChildDay.END, ChildEvent.COMPLETE));

        return service.sendMsg(request.getChildId(), confs)
                ? ResponseEntity.ok("Send daily routine to child") : NOT_FOUND;
    }


    @PostMapping("/receive")
    public ResponseEntity<?> receive(@RequestParam Long childId) throws Exception {
        Config config = service.receive(childId);
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.receiveSM(config);

        if (sm == null) {
            return NOT_FOUND;
        }

        return ResponseEntity.ok("message received");
    }

    @PostMapping("/go-to-school")
    public ResponseEntity<?> goToSchool(@RequestParam Long childId) {
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.getMachine(childId);
        if (sm.sendEvent(ChildEvent.GOING_TO_SCHOOL)) {
            return ResponseEntity.ok("Send Going to school");
        }
        return NOT_FOUND;
    }

    @PostMapping("/do-homework")
    public ResponseEntity<?> doHomework(@RequestParam Long childId) {
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.getMachine(childId);
        if (sm.sendEvent(ChildEvent.DOING_HOMEWORK)) {
            return ResponseEntity.ok("Doing homework");
        }
        return NOT_FOUND;
    }

    @PostMapping("/leisureActivity")
    public ResponseEntity<?> leisureActivity(@RequestParam Long childId) {
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.getMachine(childId);
        if (sm.sendEvent(ChildEvent.LEISURE_ACTIVITY)) {
            return ResponseEntity.ok("Leisure activity");
        }
        return NOT_FOUND;
    }

    @PostMapping("/play-videogame")
    public ResponseEntity<?> playVideoGame(@RequestParam Long childId) {
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.getMachine(childId);
        if (sm.sendEvent(ChildEvent.PLAYING_VIDEO_GAMES)) {
            return ResponseEntity.ok("Playing video games");
        }
        return NOT_FOUND;
    }

    @PostMapping("/sport-activity")
    public ResponseEntity<?> sportActivity(@RequestParam Long childId) {
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.getMachine(childId);
        if (sm.sendEvent(ChildEvent.SPORT_ACTIVITY)) {
            return ResponseEntity.ok("Some sport activity");
        }
        return NOT_FOUND;
    }

    @PostMapping("/complete")
    public ResponseEntity<?> complete(@RequestParam Long childId) {
        StateMachine<ChildDay, ChildEvent> sm = typeSafeSMService.getMachine(childId);
        if (sm.sendEvent(ChildEvent.COMPLETE)){
            return ResponseEntity.ok("Complete");
        }

        return NOT_FOUND;
    }
}
