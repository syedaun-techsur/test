package solutions.techsur.rfpaiservice.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    public static <T> void copyNonNullProperties(T source, T target, String... ignoreProperties) {
        PropertyMap<T, T> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                for (String property : ignoreProperties) {
                    skip(destination(property));
                }
            }
        };

        modelMapper.addMappings(propertyMap);
        modelMapper.map(source, target);
    }
}
