package com.mb.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(updatable = false, nullable = false)
	@CreatedDate
	private LocalDate createdDate;

	@Column(nullable = false)
	@LastModifiedDate
	private LocalDate updatedDate;

}
