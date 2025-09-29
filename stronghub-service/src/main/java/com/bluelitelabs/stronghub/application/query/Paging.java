package com.bluelitelabs.stronghub.application.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

/** Pagination and sorting control */
public class Paging {
	public static final int DEFAULT_SIZE = 20;
	public static final int MAX_SIZE = 100;

	private Paging() {
	}

	public static Pageable page(@PositiveOrZero Integer page, @Min(1) @Max(MAX_SIZE) Integer size, String[] sort) {

		int p = page == null ? 0 : page;
		int s = size == null ? DEFAULT_SIZE : size;

		Sort spec = Sort.unsorted();
		if (sort != null) {
			for (String srt : sort) {
				String[] parts = srt.split(",", 2);
				String prop = parts[0];
				Sort.Direction dir = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) ? Sort.Direction.DESC
						: Sort.Direction.ASC;
				spec = spec.and(Sort.by(dir, prop));
			}
		}
		return PageRequest.of(p, s, spec);
	}
}
