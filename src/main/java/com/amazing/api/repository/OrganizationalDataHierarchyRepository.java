package com.amazing.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.amazing.api.entity.OrganizationalData;
import com.amazing.api.entity.OrganizationalDataHierarchy;

/**
 * This class acts as the Spring Data Respository for the {@ OrganizationalData} entity.
 * 
 * @author kkailasa
 *
 */
@Repository
public interface OrganizationalDataHierarchyRepository extends JpaRepository<OrganizationalData, Long>  {

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	@Query("select h from OrganizationalDataHierarchy h where h.descendant.identifier = ?1 and not exists ( select 1 from OrganizationalDataHierarchy ih where ih.descendant.id = h.ancestor.id and ih.descendant.id != ih.ancestor.id )")
	OrganizationalDataHierarchy findRootNodeByDescendantIdentifier(String identifier);
}
