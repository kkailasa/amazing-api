package com.amazing.api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

/**
 * The JPA entity representation for the OrganizationalDataHierarchy table.
 * This entity contains the  hierarchical relationship between a OrganizationalData to other data that are direct or indirect descendant of it. 
 * This is an implementation of the Closure table representation for storing hierarchical data.
 * 
 * @author kkailasa
 *
 */
@Entity
@Table(name = "organizationaldatahierarchy")
public class OrganizationalDataHierarchy extends AuditModel {

	@Id
	@GeneratedValue(generator = "organizationaldatahierarchy_generator")
	@SequenceGenerator(name = "organizationaldatahierarchy_generator", sequenceName = "organizationaldatahierarchy_id_seq", initialValue = 1000, allocationSize = 1)
	private Long id;

	@ManyToOne(optional = false)
	private OrganizationalData ancestor;

	@ManyToOne(optional = false)
	private OrganizationalData descendant;

	@ColumnDefault(value = "0")
	private Integer depth;
	

	/**
	 * 
	 */
	public OrganizationalDataHierarchy() {
		super();
	}

	/**
	 * @param ancestor
	 * @param descendant
	 * @param depth
	 */
	public OrganizationalDataHierarchy(OrganizationalData ancestor, OrganizationalData descendant, Integer depth) {
		super();
		this.ancestor = ancestor;
		this.descendant = descendant;
		this.depth = depth;
	}

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
	 * @return the descendant
	 */
	public OrganizationalData getDescendant() {
		return descendant;
	}

	/**
	 * @param descendant the descendant to set
	 */
	public void setDescendant(OrganizationalData descendant) {
		this.descendant = descendant;
	}

	/**
	 * @return the depth
	 */
	public Integer getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

}
