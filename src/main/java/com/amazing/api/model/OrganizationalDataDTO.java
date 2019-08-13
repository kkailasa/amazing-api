package com.amazing.api.model;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(value = Include.NON_NULL)
@JsonRootName("organizationaldata")
@ApiModel(description = "All basic details about the OrganizationalData. ")
public class OrganizationalDataDTO {

	
	@NotBlank
	@ApiModelProperty(notes = "The identifier of the OrganizationalData")
	private String identifier;
	
	@NotBlank
	@ApiModelProperty(notes = "The name of the OrganizationalData")
	private String name;
	
	@ApiModelProperty(notes = "The identifier of the ancestor of the OrganizationalData")
	private String ancestorIdentifier;

	
	/**
	 * @param identifier
	 * @param name
	 * @param ancestorIdentifier
	 */
	public OrganizationalDataDTO(@NotBlank String identifier, @NotBlank String name, String ancestorIdentifier) {
		super();
		this.identifier = identifier;
		this.name = name;
		this.ancestorIdentifier = ancestorIdentifier;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}


	/**
	 * @return the ancestor
	 */
	public String getAncestorIdentifier() {
		return ancestorIdentifier;
	}


	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param ancestorIdentifier the ancestorIdentifier to set
	 */
	public void setAncestorIdentifier(String ancestorIdentifier) {
		this.ancestorIdentifier = ancestorIdentifier;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Optional<String> getOptionalAncestorIdentifier(){
		return Optional.ofNullable(ancestorIdentifier);
	}
	
}
