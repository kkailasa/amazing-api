package com.amazing.api.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazing.api.entity.OrganizationalData;
import com.amazing.api.entity.OrganizationalDataHierarchy;
import com.amazing.api.exception.ResourceNotFoundException;
import com.amazing.api.model.ErrorDetails;
import com.amazing.api.model.OrganizationalDataDTO;
import com.amazing.api.model.OrganizationalDataWithDescendantsDTO;
import com.amazing.api.repository.OrganizationalDataHierarchyRepository;
import com.amazing.api.repository.OrganizationalDataRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.HttpMethod;

/**
 * This class implements the API's needed for maintaining the Organization data for the system.
 * 
 * @author kkailasa
 *
 */
@RestController()
@RequestMapping(value = "/api", produces = {"application/hal+json"} )
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@Api(value = "OrganizationData Management System", description = "Operations pertaining to data in OrganizationData Management System")
public class RestApiController {

	@Autowired
	private OrganizationalDataRepository orgDataRepository;

	@Autowired
	private OrganizationalDataHierarchyRepository organizationalDataHierarchyRepository;

	/**
	 * This method implements the functionality needed for creating a new entry for the OrganizationalData. 
	 * 
	 * @param request New request containing the OrganizationData according to {@code OrganizationalDataDTO}.
	 * @return Resource 
	 * @throws Exception When the specified ancestorIdentifier is not valid.
	 *                   When no ancestorIdentifier is specified and the root node is already present.
	 */
	@ApiOperation(value = "Add an OrganizationalData", code = 201, httpMethod = "POST", consumes = "application/json" , produces = "application/hal+json")
	@PostMapping("/orgdata")
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Successfully inserted the data"),
	        @ApiResponse(code = 400, message = "No ancestor present for the specified identifier",response = ErrorDetails.class ),
	        @ApiResponse(code = 400, message = "Tried to create a root node as ancestorIdentifier was not specified. We cannot have multiple root nodes",response = ErrorDetails.class )
	    })
	public ResponseEntity<Resource<OrganizationalDataDTO>> createOrgData(@ApiParam(value = "OrganizationalData object store in database table", required = true) @Valid @RequestBody OrganizationalDataDTO request) throws Exception {
		Optional<OrganizationalData> ancestor = Optional.ofNullable(null);
		if (request.getOptionalAncestorIdentifier().isPresent()) {
			ancestor = orgDataRepository.findOneByIdentifier(request.getOptionalAncestorIdentifier().get());
			if (ancestor.isEmpty()) {
				throw new IllegalArgumentException("No ancestor present for the specified identifier.");
			}
		}

		if (request.getOptionalAncestorIdentifier().isEmpty()
				&& orgDataRepository.findOneByAncestorIdentifier(request.getOptionalAncestorIdentifier()).isPresent()) {
			throw new IllegalArgumentException("Tried to create a root node as ancestorIdentifier was not specified. We cannot have multiple root nodes.");
		}

		OrganizationalData data = new OrganizationalData();
		data.setIdentifier(request.getIdentifier());
		data.setName(request.getName());
		data.setAncestor(ancestor.isPresent() ? ancestor.get() : null);
		orgDataRepository.save(data);
		
		final URI uri = MvcUriComponentsBuilder.fromController(getClass())
					  .path("/{identifier}")
			          .buildAndExpand(data.getIdentifier())
			          .toUri();
		
		Resource<OrganizationalDataDTO> resource = new Resource<OrganizationalDataDTO>(request);
		resource.add(new Link(uri.toString(), "self"));
		resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(data.getIdentifier(), true)).withRel("datawithdescendants"));
		return ResponseEntity.created(uri).body(resource);
	}

	/**
	 * This method implements the functionality needed for updating an existing entry specified by the OrganizationalData. 
	 * 
	 * @param identifier identifier of the OrganizationalData to be updated
	 * @param request update request containing the OrganizationData according to {@code OrganizationalDataDTO}.
	 * @return 
	 * @throws Exception When the specified identifier is not valid.
	 * 					 When the specified ancestorIdentifier is not valid.
	 *                   When no ancestorIdentifier is specified and the root node is already present.
	 */
	@ApiOperation(value = "Update an OrganizationalData including the anscestor")
	@PutMapping("/orgdata/{identifier}")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully updated the data"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	public ResponseEntity<Resource<OrganizationalDataDTO>> updateOrgData(@ApiParam(value = "Identifier of the organizational data to be updated", required = true) @PathVariable String identifier, @ApiParam(value = "OrganizationalData to be updated for the specified identifier", required = true) @Valid @RequestBody OrganizationalDataDTO request)
			throws Exception {

		Optional<OrganizationalData> existingData = orgDataRepository.findOneByIdentifier(identifier);
		if (existingData.isEmpty()) {
			throw new ResourceNotFoundException(
					String.format("No OrganizationalData found for the specified identifier : %s ", identifier));
		}

		Optional<OrganizationalData> ancestor = Optional.ofNullable(null);
		if (request.getOptionalAncestorIdentifier().isPresent()) {
			ancestor = orgDataRepository.findOneByIdentifier(request.getOptionalAncestorIdentifier().get());
			if (ancestor.isEmpty()) {
				throw new IllegalArgumentException("No data present for the specified ancestoridentifier.");
			}
		}

		if (request.getOptionalAncestorIdentifier().isEmpty()
				&& orgDataRepository.findOneByAncestorIdentifier(request.getOptionalAncestorIdentifier()).isPresent()) {
			throw new IllegalArgumentException("Tried to create a root node as ancestorIdentifier was not specified. We cannot have multiple root nodes.");
		}

		OrganizationalData data = existingData.get();
		data.setIdentifier(request.getIdentifier());
		data.setName(request.getName());
		data.setAncestor(ancestor.isPresent() ? ancestor.get() : null);
		orgDataRepository.save(data);
		
		final URI uri = MvcUriComponentsBuilder.fromController(getClass())
				  .path("/{identifier}")
		          .buildAndExpand(data.getIdentifier())
		          .toUri();
		
		Resource<OrganizationalDataDTO> resource = new Resource<OrganizationalDataDTO>(request);
		resource.add(new Link(uri.toString(), "self"));
		resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(data.getIdentifier(), true)).withRel("datawithdescendants"));
		
		return ResponseEntity.ok(resource);
	}
	
    /**
     * This method implements the functionality needed for retrieving all organizationaldata in its simple form ( without specifying any hierarchy ) 
     * 
     * @return List of OrganizationalData
     */
	@ApiOperation(value = "Get all the OrganizationalData ")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully retrieved data"),
	    })
	@GetMapping("/orgdata")
	public ResponseEntity<Resources<Resource<OrganizationalDataDTO>>> getOrgData() {

		final List<Resource<OrganizationalDataDTO>> collection = orgDataRepository.findAll().stream().map(a -> {
			return new OrganizationalDataDTO(a.getIdentifier(), a.getName(),
					Optional.ofNullable(a.getAncestor()).isPresent() ? a.getAncestor().getIdentifier() : null);
		}).map( a -> {
			Resource<OrganizationalDataDTO> resource = new Resource<>(a);
			try {
				resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(a.getIdentifier(), false)).withSelfRel());
				resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(a.getIdentifier(), true)).withRel("datawithdescendants"));
			} catch (Exception e) {
				
			}
			return resource;
		}).collect(Collectors.toList());

		final Resources<Resource<OrganizationalDataDTO>> resources = new Resources<>(collection);
		final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
		resources.add(new Link(uriString, "self"));
		return ResponseEntity.ok(resources);

	}

	/**
	 * This method implements the functionality needed for retrieving the information about the specified organizational identifier and their descendants( based on the parameter ).
	 * 
	 * @param identifier Identifier of the OrganizationalData to be retrieved
	 * @param includeDescendants Flag to determine if the descendants details have to be fetched
	 * @return The OrganizationalData pertaining to the identifier specified. 
	 * @throws ResourceNotFoundException When there are no OrganizationalData for the specified identifier.
	 */
	@ApiOperation(value = "Get Organization Data pertaining to the specified identifier ")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully retrieved data"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@GetMapping("/orgdata/{identifier}")
	public ResponseEntity<Resource<OrganizationalDataDTO>> getOrgDataByIdentifier(@ApiParam(value = "Identifier of the OrganizationData for which the details are required", required = true ) @PathVariable String identifier,
			@ApiParam(value = "Param to determine if the descendants and the whole chain need to be included ", required = false ) @RequestParam(name = "includeDescendants", required = false, defaultValue = "false" ) Boolean includeDescendants)
			throws ResourceNotFoundException {
		Optional<OrganizationalData> organizationalData = Optional.ofNullable(null);
		if (Boolean.TRUE.equals(includeDescendants)) {
			organizationalData = orgDataRepository.findOneWithDescendantsByIdentifier(identifier);
		} else {
			organizationalData = orgDataRepository.findOneByIdentifier(identifier);
		}

		if (organizationalData.isEmpty()) {
			throw new ResourceNotFoundException("OrganizationData not found for identifier :  " + identifier);
		}

		// Get the Root Node and the depth of the current node from the root
		OrganizationalDataHierarchy rootToDescendantPath = organizationalDataHierarchyRepository
				.findRootNodeByDescendantIdentifier(identifier);

		OrganizationalDataWithDescendantsDTO dto = new OrganizationalDataWithDescendantsDTO(
				organizationalData.get().getIdentifier(), organizationalData.get().getName(),
				(Optional.ofNullable(organizationalData.get().getAncestor()).isPresent()
						? organizationalData.get().getAncestor().getIdentifier()
						: null));
		dto.setRootIdentifier(rootToDescendantPath.getAncestor().getIdentifier());
		dto.setHeight(rootToDescendantPath.getDepth());
		if (Boolean.TRUE.equals(includeDescendants)) {
			for (OrganizationalDataHierarchy hierarchy : organizationalData.get().getHierarchy()) {
				if (!hierarchy.getDescendant().getIdentifier().equals(dto.getIdentifier())) {
					OrganizationalDataWithDescendantsDTO descendantDto = new OrganizationalDataWithDescendantsDTO(
							hierarchy.getDescendant().getIdentifier(), hierarchy.getDescendant().getName(),
							hierarchy.getDescendant().getAncestor().getIdentifier());
					descendantDto.setRootIdentifier(rootToDescendantPath.getAncestor().getIdentifier());
					descendantDto.setHeight(rootToDescendantPath.getDepth() + hierarchy.getDepth());
					dto.addDescendant(descendantDto);
				}
			}
		}
		Resource<OrganizationalDataDTO> resource = new Resource<OrganizationalDataDTO>(dto);
		resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(identifier, includeDescendants)).withSelfRel());
		resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(identifier, true)).withRel("datawithdescendants"));
		if (dto.getOptionalAncestorIdentifier().isPresent()) {
			resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(dto.getOptionalAncestorIdentifier().get(), true)).withRel("ancestor"));
		}
		resource.add(linkTo(methodOn(RestApiController.class).getOrgDataByIdentifier(rootToDescendantPath.getAncestor().getIdentifier(), false)).withRel("root"));
		return ResponseEntity.ok().body(resource);
	}
	
}
