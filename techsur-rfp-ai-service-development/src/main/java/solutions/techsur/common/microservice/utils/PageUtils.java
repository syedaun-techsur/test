package solutions.techsur.common.microservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class PageUtils {

    /**
     * Creates a Pageable object with pagination and optional sorting parameters.
     *
     * @param page the page number (0-based), defaults to 0 if null
     * @param size the size of the page, defaults to 10 if null
     * @param sort list of sort parameters in the form "property:direction" (direction optional, defaults to ASC)
     * @return Pageable instance configured with page, size, and sorting
     */
    public static Pageable pagingAndSorting(Integer page, Integer size, List<String> sort) {
        int pageNumber = (page == null || page < 0) ? 0 : page;
        int pageSize = (size == null || size <= 0) ? 10 : size;

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        if (!CollectionUtils.isEmpty(sort)) {
            List<Sort.Order> sortOrders = sort.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map((String s) -> {
                        String[] parts = s.trim().split(":");
                        String property = parts[0];
                        Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1]))
                                ? Sort.Direction.DESC : Sort.Direction.ASC;
                        return new Sort.Order(direction, property);
                    }).toList();

            if (!sortOrders.isEmpty()) {
                Sort sortParam = Sort.by(sortOrders);
                pageRequest = pageRequest.withSort(sortParam);
            }
        }
        return pageRequest;
    }
}