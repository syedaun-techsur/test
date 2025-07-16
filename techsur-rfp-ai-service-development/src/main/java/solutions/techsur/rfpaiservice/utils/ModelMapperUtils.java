package solutions.techsur.rfpaiservice.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

/**
 * Utility class for ModelMapper operations.
 */
public class ModelMapperUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    /**
     * Copies non-null properties from source to target, optionally ignoring specified properties.
     *
     * @param <T>              The type of the source and target objects
     * @param source           The source object from which to copy properties
     * @param target           The target object to which properties are copied
     * @param ignoreProperties Optional list of property names to ignore
     */
    public static <T> void copyNonNullProperties(T source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }

        PropertyMap<T, T> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                if (ignoreProperties != null) {
                    for (String property : ignoreProperties) {
                        skip(destination(property));
                    }
                }
            }
        };

        modelMapper.addMappings(propertyMap);
        try {
            modelMapper.map(source, target);
        } finally {
            // Remove mappings to prevent accumulation which can cause unexpected behaviors
            modelMapper.getTypeMap(source.getClass(), target.getClass()).ifPresent(map -> modelMapper.removeTypeMap(map));
        }
    }
}