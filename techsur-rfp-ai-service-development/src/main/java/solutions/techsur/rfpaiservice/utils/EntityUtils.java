package solutions.techsur.rfpaiservice.utils;

import java.util.Collection;
import java.util.function.Function;

public class EntityUtils {
    public static <C extends Iterable<E>, E> C fetchCollection(C entityPage, Function<E, Collection<?>> collectionSupplier) {
        entityPage.forEach(entity -> fetchCollection(collectionSupplier.apply(entity)));
        return entityPage;
    }

    public static void fetchCollection(Collection<?> collection) {
        collection.size(); //NOSONAR as it's just a trick to fetch the collection
    }

}
