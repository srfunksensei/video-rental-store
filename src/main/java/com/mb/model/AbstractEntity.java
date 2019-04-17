package com.mb.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue
	protected Long id;

	@Column(updatable = false, nullable = false)
	@CreatedDate
	protected LocalDate createdDate;

	@Column(nullable = false)
	@LastModifiedDate
	protected LocalDate updatedDate;

	protected AbstractEntity(LocalDate createdDate, LocalDate updatedDate) {
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
}
