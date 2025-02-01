package guru.springframework.sfgjms.util;

import com.typesafe.config.*;
import guru.springframework.sfgjms.entity.StateMachineWrapper;

import java.util.ArrayList;
import java.util.List;

public interface ConfigProvider {

    static Config readConfString(String config){
        return ConfigFactory.parseString(config);
    }

    static Config createChildDayConf(long childId, List<StateMachineWrapper> conf){
        Config config = ConfigFactory.empty();

        List<ConfigObject> childConfigs = new ArrayList<>();
        for (StateMachineWrapper child : conf) {
            ConfigObject childConfig = ConfigFactory.empty()
                    .withValue("source", ConfigValueFactory.fromAnyRef(child.getSource().toString()))
                    .withValue("target", ConfigValueFactory.fromAnyRef(child.getTarget().toString()))
                    .withValue("event", ConfigValueFactory.fromAnyRef(child.getEvent().toString()))
                    .root();

            childConfigs.add(childConfig);
        }

        return config
                .withValue("id", ConfigValueFactory.fromAnyRef(childId))
                .withValue("children", ConfigValueFactory.fromIterable(childConfigs));
    }
}
