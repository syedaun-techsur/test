package solutions.techsur.rfpaiservice.utils;

import java.util.Collection;
import java.util.function.Function;

/**
 * Utility class for working with entity collections to ensure they are properly fetched/initialized.
 */
public class EntityUtils {

    /**
     * Ensures that for each entity in the given iterable collection,
     * the collection returned by the collectionSupplier function is fetched
     * (e.g., lazy-loaded collections are initialized).
     *
     * @param <C> type of the collection of entities, must be Iterable of E
     * @param <E> entity type
     * @param entities collection of entities to process
     * @param collectionSupplier function to get a collection from each entity
     * @return the input collection after fetching corresponding collections from each entity
     * @throws NullPointerException if entities or collectionSupplier is null
     */
    public static <C extends Iterable<E>, E> C fetchCollections(final C entities, final Function<E, Collection<?>> collectionSupplier) {
        if (entities == null) {
            throw new NullPointerException("entities collection must not be null");
        }
        if (collectionSupplier == null) {
            throw new NullPointerException("collectionSupplier must not be null");
        }

        for (final E entity : entities) {
            final Collection<?> collection = collectionSupplier.apply(entity);
            fetchCollection(collection);
        }
        return entities;
    }

    /**
     * Forces fetching/initialization of a collection by calling its size() method.
     *
     * @param collection the collection to fetch, must not be null
     * @throws NullPointerException if collection is null
     */
    public static void fetchCollection(final Collection<?> collection) {
        if (collection == null) {
            throw new NullPointerException("collection must not be null");
        }
        collection.size(); // Force initialization/load of the collection (e.g., for lazy-loaded collections)
    }
}