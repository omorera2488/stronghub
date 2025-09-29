package com.bluelitelabs.stronghub.application.sort;

import java.util.Arrays;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class GymSortWhitelist implements SortWhitelist {

	private static final Set<String> ALLOWED = Set.of("id", "name", "country", "currency", "status", "createdAt",
			"updatedAt");

	@Override
	public String[] sanitize(String[] sort) {
		if (sort == null)
			return null;
		return Arrays.stream(sort).filter(s -> ALLOWED.contains(s.split(",", 2)[0])).toArray(String[]::new);
	}
}
