package solutions.techsur.common.microservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class PageUtils {
	public static Pageable pagingAndSorting(Integer page, Integer size, List<String> sort) {
		int pageNumber = page == null ? 0 : page;
		int pageSize = size == null ? 10 : size;

		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
		if (!CollectionUtils.isEmpty(sort)) {
			List<Sort.Order> sortOrder = sort.stream().map(s -> {
				String[] parts = s.split(":");
				String property = parts[0];
				Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC
						: Sort.Direction.ASC;
				return new Sort.Order(direction, property);
			}).toList();
			Sort sortParam = Sort.by(sortOrder);
			pageRequest = pageRequest.withSort(sortParam);
		}
		return pageRequest;
	}
}
