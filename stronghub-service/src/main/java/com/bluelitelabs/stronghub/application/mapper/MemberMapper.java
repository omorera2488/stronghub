package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Member;
import com.bluelitelabs.stronghub.web.dto.MemberDto;

@Component
public class MemberMapper {
	public MemberDto toDto(Member m) {
		return new MemberDto(m.getId(), m.getFirstName(), m.getLastName(), m.getEmail(), m.getPhone(),
				m.getDeletedAt() == null);
	}
}