package com.amazing.api.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * The JPA entity representation for the OrganizationalData table.
 * This entity contains an entry about an OrganizationalData and also the ancestor of it.
 * 
 * @author kkailasa
 *
 */
@Entity
@Table(name = "organizationaldata")
@NamedEntityGraphs({
    	@NamedEntityGraph(name="OrganizationalData.hierarchy", 
    		attributeNodes = {
    				@NamedAttributeNode(value = "hierarchy", subgraph = "hierarchy.descendants"),
    				@NamedAttributeNode("ancestor")
    		},
    		subgraphs = {
    				@NamedSubgraph(name = "hierarchy.descendants", attributeNodes = {
    	    				@NamedAttributeNode(value = "descendant"),
    	    				@NamedAttributeNode(value = "ancestor")
    	    		})
    		}
    	),
	    @NamedEntityGraph(name="OrganizationalData.ancestor", 
			attributeNodes = {
					@NamedAttributeNode(value = "ancestor")
			}
		)

})
public class OrganizationalData extends AuditModel{

	@Id
    @GeneratedValue(generator = "organizationaldata_generator")
    @SequenceGenerator(
            name = "organizationaldata_generator",
            sequenceName = "organizationaldata_id_seq",
            initialValue = 1000,
            allocationSize = 1
    )
    private Long id;
	
	@NotBlank
	@Column(unique = true)
	private String identifier;
	
	@NotBlank
	private String name;
	
	@ManyToOne(optional = true)
	OrganizationalData ancestor;
	
	
	@OneToMany(mappedBy = "ancestor", fetch = FetchType.LAZY)
	@OrderBy("depth ASC, id ASC")
	Set<OrganizationalDataHierarchy> hierarchy = new HashSet<>();


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}


	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	/**
	 * @return the ancestor
	 */
	public OrganizationalData getAncestor() {
		return ancestor;
	}


	/**
	 * @param ancestor the ancestor to set
	 */
	public void setAncestor(OrganizationalData ancestor) {
		this.ancestor = ancestor;
	}


	/**
	 * @return the hierarchy
	 */
	public Set<OrganizationalDataHierarchy> getHierarchy() {
		return hierarchy;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param hierarchy the hierarchy to set
	 */
	public void setHierarchy(Set<OrganizationalDataHierarchy> hierarchy) {
		this.hierarchy = hierarchy;
	}
	
}
