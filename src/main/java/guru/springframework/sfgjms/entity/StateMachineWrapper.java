package guru.springframework.sfgjms.entity;

import guru.springframework.sfgjms.entity.state.ChildDay;
import guru.springframework.sfgjms.entity.state.ChildEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateMachineWrapper {
    private ChildDay source;
    private ChildDay target;
    private ChildEvent event;
}
