package com.amazing.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amazing.api.entity.OrganizationalData;

/**
 * This class acts as the Spring Data Respository for the {@ OrganizationalDataHierarchy } entity.
 * 
 * @author kkailasa
 *
 */
@Repository
public interface OrganizationalDataRepository extends JpaRepository<OrganizationalData, Long>  {

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	Optional<OrganizationalData> findOneByIdentifier(String identifier);
	
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	Optional<OrganizationalData> findOneByAncestorIdentifier(Optional<String> identifier);
	
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	@EntityGraph(value = "OrganizationalData.hierarchy")
	Optional<OrganizationalData> findOneWithDescendantsByIdentifier(String identifier);
	
}
