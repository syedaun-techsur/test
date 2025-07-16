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
     * Copies non-null properties from source to target.
     * Note: Ignoring specific properties by name is not supported in this implementation.
     *
     * @param <T>    The type of the source and target objects
     * @param source The source object from which to copy properties
     * @param target The target object to which properties are copied
     * @param ignoreProperties Optional list of property names to ignore (currently ignored)
     */
    public static <T> void copyNonNullProperties(T source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }

        modelMapper.map(source, target);

        // Remove the TypeMap to prevent accumulation which can cause unexpected behaviors
        var typeMap = modelMapper.getTypeMap(source.getClass(), target.getClass());
        if (typeMap != null) {
            modelMapper.removeTypeMap(typeMap);
        }
    }
}