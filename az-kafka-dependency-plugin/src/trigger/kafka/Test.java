package trigger.kafka;
import azkaban.flowtrigger.DependencyInstanceCallback;
import azkaban.flowtrigger.DependencyInstanceConfig;
import azkaban.flowtrigger.DependencyInstanceConfigImpl;
import azkaban.flowtrigger.DependencyInstanceContext;
import azkaban.flowtrigger.DependencyInstanceRuntimeProps;
import azkaban.flowtrigger.DependencyInstanceRuntimePropsImpl;
import azkaban.flowtrigger.DependencyPluginConfig;
import azkaban.flowtrigger.DependencyPluginConfigImpl;
import java.util.HashMap;
import java.util.Map;
import trigger.kafka.Constants.DependencyInstanceConfigKey;
import trigger.kafka.Constants.DependencyPluginConfigKey;


public class Test {
  public static void main(final String[] args) throws InterruptedException {
    final KafkaDependencyCheck check = new KafkaDependencyCheck();
    final Map<String, String> pluginConfigMap = new HashMap<>();
    final String brokerURL = "localhost:9092";
    final String schemaRegistryRestfulClient = "http://localhost:8000";


    pluginConfigMap.put(DependencyPluginConfigKey.KAKFA_BROKER_URL, brokerURL);
    pluginConfigMap.put(DependencyPluginConfigKey.SCHEMA_REGISTRY_URL, schemaRegistryRestfulClient);

    final DependencyPluginConfig pluginConfig = new DependencyPluginConfigImpl(pluginConfigMap);
    check.init(pluginConfig);
    DependencyInstanceContext Di1 = createContext(check, "AzEvent_Topic4", "name","chia.*","1");
    DependencyInstanceContext Di2 = createContext(check, "AzEvent_Topic4", "name","^\\w*","1");
    DependencyInstanceContext Di3 = createContext(check, "AzEvent_Topic4", "username","chiawei_start1","1");
    DependencyInstanceContext Di4 = createContext(check, "AzEvent_Topic4", "username",".*","1");


    while(true){
      KafkaProducerTest Pt = new KafkaProducerTest();
      Thread.sleep(40000);
    }
  }

  private static DependencyInstanceContext createContext(final KafkaDependencyCheck check,
      final String topic, final String field,final String match,final String counter) {
    final Map<String, String> props = new HashMap<>();

    props.put(DependencyInstanceConfigKey.FIELD, field);
    props.put(DependencyInstanceConfigKey.TOPIC, topic);
    props.put(DependencyInstanceConfigKey.COUNTER, counter);
    props.put(DependencyInstanceConfigKey.MATCH,match);

    final DependencyInstanceConfig instConfig = (DependencyInstanceConfig)new DependencyInstanceConfigImpl(props);
    final Map<String, String> runtimePropsMap = new HashMap<>();
    runtimePropsMap.put("startTime", String.valueOf(System.currentTimeMillis()));
    final DependencyInstanceRuntimeProps runtimeProps =(DependencyInstanceRuntimeProps) new DependencyInstanceRuntimePropsImpl(runtimePropsMap);

    final DependencyInstanceCallback callback = (DependencyInstanceCallback)new DependencyInstanceCallbackI();
    final DependencyInstanceContext context = check.run(instConfig, runtimeProps, callback);
    return context;
  }

}
