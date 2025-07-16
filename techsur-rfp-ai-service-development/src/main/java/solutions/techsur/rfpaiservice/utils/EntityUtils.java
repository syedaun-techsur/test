package solutions.techsur.rfpaiservice.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class EntityUtils {

    /**
     * Recursively fetches all collections obtained by applying the given collectionSupplier function
     * on each element of the provided iterable collection.
     * 
     * @param <C>            the type of the iterable collection
     * @param <E>            the element type within the iterable collection
     * @param entities       the iterable collection of entities to process
     * @param collectionExtractor a function that extracts a collection from an entity
     * @return the original entities collection, unmodified
     */
    public static <C extends Iterable<E>, E> C fetchCollections(C entities, Function<E, Collection<?>> collectionExtractor) {
        if (entities == null || collectionExtractor == null) {
            return entities;
        }
        for (E entity : entities) {
            fetchCollection(collectionExtractor.apply(entity));
        }
        return entities;
    }

    /**
     * Forces initialization/fetching of the given collection by invoking its size() method.
     * This method relies on the side-effect of fetching data in lazy loading scenarios.
     * 
     * @param collection the collection to fetch or initialize
     */
    public static void fetchCollection(Collection<?> collection) {
        if (collection != null) {
            collection.size(); // NOSONAR: Side effect to trigger collection fetching
        }
    }
}