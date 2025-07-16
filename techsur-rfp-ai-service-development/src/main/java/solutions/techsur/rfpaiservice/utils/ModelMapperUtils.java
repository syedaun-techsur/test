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

    /**
     * Copies non-null properties from source to target, optionally ignoring specified properties.
     *
     * @param source           the source object to copy from
     * @param target           the target object to copy to
     * @param ignoreProperties property names to ignore during copying
     * @param <T>              the type of the source and target objects
     */
    public static <T> void copyNonNullProperties(T source, T target, String... ignoreProperties) {
        if (ignoreProperties == null || ignoreProperties.length == 0) {
            modelMapper.map(source, target);
            return;
        }

        PropertyMap<T, T> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                for (String property : ignoreProperties) {
                    skip(destination(property));
                }
            }
        };

        // Add the mapping, map the objects, then remove the mapping to avoid unexpected side effects
        modelMapper.addMappings(propertyMap);
        modelMapper.map(source, target);
        modelMapper.typeMap((Class<T>) source.getClass(), (Class<T>) target.getClass()).addMappings(new PropertyMap<T, T>() {
            @Override
            protected void configure() {
                // no-op to reset the mappings after use
            }
        });
    }
}