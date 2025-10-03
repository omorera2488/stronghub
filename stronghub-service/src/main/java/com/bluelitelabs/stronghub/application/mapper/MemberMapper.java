package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Member;
import com.bluelitelabs.stronghub.web.dto.MemberCreateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberDto;
import com.bluelitelabs.stronghub.web.dto.MemberUpdateRequest;

@Component
public class MemberMapper {
	public MemberDto toDto(Member m) {
		MemberDto dto = new MemberDto();
		dto.setId(m.getId());
		dto.setFirstName(m.getFirstName());
		dto.setLastName(m.getLastName());
		dto.setEmail(m.getEmail());
		dto.setPhone(m.getPhone());
		dto.setDateOfBirth(m.getDateOfBirth());
		dto.setGender(m.getGender());
		return dto;
	}

	public Member toEntity(MemberCreateRequest r) {
		Member m = new Member();
		m.setFirstName(r.getFirstName());
		m.setLastName(r.getLastName());
		m.setEmail(r.getEmail());
		m.setPhone(r.getPhone());
		m.setDateOfBirth(r.getDateOfBirth());
		m.setGender(r.getGender());
		return m;
	}

	public void apply(Member m, MemberUpdateRequest r) {
		m.setFirstName(r.getFirstName());
		m.setLastName(r.getLastName());
		m.setEmail(r.getEmail());
		m.setPhone(r.getPhone());
		m.setDateOfBirth(r.getDateOfBirth());
		m.setGender(r.getGender());
	}
}