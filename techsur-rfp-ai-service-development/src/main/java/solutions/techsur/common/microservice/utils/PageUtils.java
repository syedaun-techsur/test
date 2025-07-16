package solutions.techsur.common.microservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for pagination and sorting.
 */
public class PageUtils {

    /**
     * Creates a Pageable object with pagination and optional sorting parameters.
     *
     * @param page the page number, zero-based. Defaults to 0 if null.
     * @param size the size of the page to be returned. Defaults to 10 if null.
     * @param sort a list of sorting criteria in the format "property:direction" (direction is optional, defaults to ASC).
     * @return a Pageable instance with the specified paging and sorting.
     */
    public static Pageable pagingAndSorting(Integer page, Integer size, List<String> sort) {
        int pageNumber = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(10);

        if (CollectionUtils.isEmpty(sort)) {
            return PageRequest.of(pageNumber, pageSize);
        }

        List<Sort.Order> sortOrders = sort.stream()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(s -> {
                String[] parts = s.split(":");
                String property = parts[0];
                Sort.Direction direction = Sort.Direction.ASC;
                if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
                    direction = Sort.Direction.DESC;
                }
                return new Sort.Order(direction, property);
            })
            .collect(Collectors.toList());

        Sort sortParam = Sort.by(sortOrders);
        return PageRequest.of(pageNumber, pageSize, sortParam);
    }
}