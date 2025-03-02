package guru.springframework.sfgjms;

import guru.springframework.sfgjms.controller.SendTestController;
import guru.springframework.sfgjms.dto.SMRequest;
import guru.springframework.sfgjms.entity.StateMachineWrapper;
import guru.springframework.sfgjms.entity.state.ChildDay;
import guru.springframework.sfgjms.entity.state.ChildEvent;
import guru.springframework.sfgjms.service.SendMessengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SentTestControllerTest {
    @InjectMocks
    private SendTestController sendTestController;
    private SMRequest request;
    private List<StateMachineWrapper> list;
    @Mock
    private SendMessengerService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        request = new SMRequest(1L, ChildDay.WEEKDAY, List.of(ChildEvent.DOING_HOMEWORK,
                ChildEvent.GOING_TO_SCHOOL));

        list = new ArrayList<>();
        list.add(new StateMachineWrapper(ChildDay.NEW, request.getChildDay(), request.getEvents().get(0)));
        for (ChildEvent e : request.getEvents()) {
            if (e != request.getEvents().get(0)) {
                list.add(new StateMachineWrapper(request.getChildDay(),
                        request.getChildDay(), e));
            }
        }
        list.add(new StateMachineWrapper(request.getChildDay(), ChildDay.END, ChildEvent.COMPLETE));
    }

    @Test
    public void testSubmit_success() {

        when(service.sendMsg(request.getChildId(), list)).thenReturn(true);

        // Act
        ResponseEntity<?> response = sendTestController.submit(request);

        // Assert
        assertEquals(ResponseEntity.ok("Send daily routine to child"), response);
    }

    @Test
    public void testSubmit_notFound() {
        request.setChildId(2);
        when(service.sendMsg(request.getChildId(), list)).thenReturn(false);
        // Act
        ResponseEntity<?> response = sendTestController.submit(request);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), response);
    }
}
