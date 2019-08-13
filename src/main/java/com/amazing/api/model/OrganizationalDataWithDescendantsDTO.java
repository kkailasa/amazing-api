package com.amazing.api.model;

import java.util.LinkedList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details about the OrganizationalData including ")
public class OrganizationalDataWithDescendantsDTO extends OrganizationalDataDTO{

	@ApiModelProperty(notes = "The root identifier of the OrganizationalData")
	private String rootIdentifier;
	
	@ApiModelProperty(notes = "The height of the OrganizationalData")
	private Integer height;
	
	@ApiModelProperty(notes = "The descendants of the OrganizationalData")
	private List<OrganizationalDataWithDescendantsDTO> descendants;
	
	public OrganizationalDataWithDescendantsDTO(String identifier, String name, String ancestorIdentifier) {
		super(identifier, name, ancestorIdentifier);
	}

	/**
	 * @return the rootIdentifier
	 */
	public String getRootIdentifier() {
		return rootIdentifier;
	}

	/**
	 * @param rootIdentifier the rootIdentifier to set
	 */
	public void setRootIdentifier(String rootIdentifier) {
		this.rootIdentifier = rootIdentifier;
	}

	/**
	 * @return the depth
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setHeight(Integer depth) {
		this.height = depth;
	}

	/**
	 * @return the descendants
	 */
	public List<OrganizationalDataWithDescendantsDTO> getDescendants() {
		return descendants;
	}

	/**
	 * @param descendants the descendants to set
	 */
	public void setDescendants(List<OrganizationalDataWithDescendantsDTO> descendants) {
		this.descendants = descendants;
	}
	
	/**
	 * 
	 * @param dto
	 */
	public void addDescendant(OrganizationalDataWithDescendantsDTO dto) {
		if (descendants == null ) {
			descendants = new LinkedList<>();
		}
		if (dto.getOptionalAncestorIdentifier().isPresent() && getIdentifier().equals(dto.getOptionalAncestorIdentifier().get())) {
			descendants.add(dto);
		} else {
			for( OrganizationalDataWithDescendantsDTO  dtoinner : descendants) {
				dtoinner.addDescendant(dto);
			}
		}
	}
}
