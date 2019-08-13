package com.amazing.api;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.amazing.api.controller.RestApiController;
import com.amazing.api.entity.OrganizationalData;
import com.amazing.api.entity.OrganizationalDataHierarchy;
import com.amazing.api.repository.OrganizationalDataHierarchyRepository;
import com.amazing.api.repository.OrganizationalDataRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RestApiController.class, secure = false)
public class RestApiControllerTests {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private OrganizationalDataRepository oganizationalDataRepository;
	
	@MockBean
    private OrganizationalDataHierarchyRepository organizationalDataHierarchyRepository;

	OrganizationalData root = new OrganizationalData();
	
	OrganizationalData child1 = new OrganizationalData();
	
	OrganizationalData child2 = new OrganizationalData();
	
	OrganizationalData child11 = new OrganizationalData();
	
	OrganizationalData child12 = new OrganizationalData();
	
	OrganizationalData child21 = new OrganizationalData();
	
	@Before
	public void setUp() {
	    
	    root.setIdentifier("0");
	    root.setName("0");
	    root.setAncestor(null);
	    
	    
	    child1.setIdentifier("1");
	    child1.setName("child1");
	    child1.setAncestor(root);
	    
	    
	    child2.setIdentifier("2");
	    child2.setName("child2");
	    child2.setAncestor(root);
	    
	    
	    child11.setIdentifier("3");
	    child11.setName("child11");
	    child11.setAncestor(child1);
	    
	    
	    child12.setIdentifier("4");
	    child12.setName("child12");
	    child12.setAncestor(child1);
	    
	    
	    child21.setIdentifier("5");
	    child21.setName("child21");
	    child21.setAncestor(child2);
	    
	    OrganizationalData child22 = new OrganizationalData();
	    child22.setIdentifier("6");
	    child22.setName("child2");
	    child22.setAncestor(child2);
	    
	    Set<OrganizationalDataHierarchy> hierarchy = new LinkedHashSet<>();
	    hierarchy.add(new OrganizationalDataHierarchy(root,root, 0));
	    hierarchy.add(new OrganizationalDataHierarchy(root,child1, 1));
	    hierarchy.add(new OrganizationalDataHierarchy(root,child2, 1));
	    hierarchy.add(new OrganizationalDataHierarchy(root,child11, 2));
	    hierarchy.add(new OrganizationalDataHierarchy(root,child12, 2));
	    hierarchy.add(new OrganizationalDataHierarchy(root,child21, 2));
	    hierarchy.add(new OrganizationalDataHierarchy(root,child22, 2));
	    root.setHierarchy(hierarchy);
	    
	    Set<OrganizationalDataHierarchy> hierarchy1 = new LinkedHashSet<>();
	    hierarchy1.add(new OrganizationalDataHierarchy(child1,child1, 0));
	    hierarchy1.add(new OrganizationalDataHierarchy(child1,child11, 1));
	    hierarchy1.add(new OrganizationalDataHierarchy(child1,child12, 1));
	    child1.setHierarchy(hierarchy1);
	    
	    
	    Set<OrganizationalDataHierarchy> hierarchy2 = new LinkedHashSet<>();
	    hierarchy2.add(new OrganizationalDataHierarchy(child2,child2, 0));
	    hierarchy2.add(new OrganizationalDataHierarchy(child2,child21, 1));
	    hierarchy2.add(new OrganizationalDataHierarchy(child2,child22, 1));
	    child2.setHierarchy(hierarchy2);
	    
	 
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(root.getIdentifier()))
	      .thenReturn(Optional.of(root));
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(child1.getIdentifier()))
	      .thenReturn(Optional.of(child1));
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(child2.getIdentifier()))
	      .thenReturn(Optional.of(child2));
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(child11.getIdentifier()))
	      .thenReturn(Optional.of(child11));
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(child12.getIdentifier()))
	      .thenReturn(Optional.of(child12));
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(child21.getIdentifier()))
	      .thenReturn(Optional.of(child21));
	    Mockito.when(oganizationalDataRepository.findOneByIdentifier(child22.getIdentifier()))
	      .thenReturn(Optional.of(child22));
	   // Mockito.when(oganizationalDataRepository.findOneByIdentifier(anyString())).thenReturn(Optional.ofNullable(null));
	    
	    Mockito.when(oganizationalDataRepository.findAll()).thenReturn(Arrays.asList(root, child1, child2, child11, child12, child21, child22));
	    
	    Mockito.when(oganizationalDataRepository.findOneWithDescendantsByIdentifier(root.getIdentifier()))
	      .thenReturn(Optional.of(root));
	    
	    OrganizationalData nullableObject = null;
	    Mockito.when(oganizationalDataRepository.findOneByAncestorIdentifier(anyObject())).thenReturn(null);
	    
	    Mockito.when(organizationalDataHierarchyRepository.findRootNodeByDescendantIdentifier(root.getIdentifier())).thenReturn(new OrganizationalDataHierarchy(root,root, 0));
	}
	
	@Test
	public void givenOrganizationData_whenGetOrgData_thenReturnJsonArray()
			  throws Exception {
		mvc.perform(get("/api/orgdata")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("{\"_embedded\":{\"organizationalDataDTOList\":[{\"identifier\":\"0\",\"name\":\"0\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/0?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/0?includeDescendants=true\"}}},{\"identifier\":\"1\",\"name\":\"child1\",\"ancestorIdentifier\":\"0\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/1?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/1?includeDescendants=true\"}}},{\"identifier\":\"2\",\"name\":\"child2\",\"ancestorIdentifier\":\"0\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/2?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/2?includeDescendants=true\"}}},{\"identifier\":\"3\",\"name\":\"child11\",\"ancestorIdentifier\":\"1\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/3?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/3?includeDescendants=true\"}}},{\"identifier\":\"4\",\"name\":\"child12\",\"ancestorIdentifier\":\"1\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/4?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/4?includeDescendants=true\"}}},{\"identifier\":\"5\",\"name\":\"child21\",\"ancestorIdentifier\":\"2\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/5?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/5?includeDescendants=true\"}}},{\"identifier\":\"6\",\"name\":\"child2\",\"ancestorIdentifier\":\"2\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/6?includeDescendants=false\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/6?includeDescendants=true\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata\"}}}"));
	}
	
	@Test
	public void givenValidOrganizationData_whenCreateOrgData_thenReturnSuccess()
			  throws Exception {
		Mockito.when(oganizationalDataRepository.save(anyObject())).thenAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		      Object[] args = invocation.getArguments();
		      return (Object) args[0];
		    }
		  });
		mvc.perform(post("/api/orgdata")
		     .content("{\n" + 
		     		"	\"identifier\" : \"7\",\n" + 
		     		"	\"name\" : \"child3\",\n" + 
		     		"	\"ancestorIdentifier\" : \"0\"\n" + 
		     		"}")
		     .contentType(MediaType.APPLICATION_JSON))
			 .andExpect(status().isCreated())
			 .andExpect(content().string("{\"identifier\":\"7\",\"name\":\"child3\",\"ancestorIdentifier\":\"0\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/7\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/7?includeDescendants=true\"}}}"));
	}
	
	@Test
	public void givenValidOrganizationData_whenUpdateOrgData_thenReturnSuccess()
			  throws Exception {
		Mockito.when(oganizationalDataRepository.save(anyObject())).thenAnswer(new Answer<Object>() {
		    @Override
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		      Object[] args = invocation.getArguments();
		      return (Object) args[0];
		    }
		  });
		mvc.perform(post("/api/orgdata")
		     .content("{\n" + 
		     		"	\"identifier\" : \"5\",\n" + 
		     		"	\"name\" : \"child21\",\n" + 
		     		"	\"ancestorIdentifier\" : \"0\"\n" + 
		     		"}")
		     .contentType(MediaType.APPLICATION_JSON))
			 .andExpect(status().isCreated())
			 .andExpect(content().string("{\"identifier\":\"5\",\"name\":\"child21\",\"ancestorIdentifier\":\"0\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/5\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/5?includeDescendants=true\"}}}"));
	}
	

	@Test
	public void givenOrganizationData_whenGetOrgDataByIdentifierWithDescendants_thenReturnOrgdata()
			  throws Exception {
		mvc.perform(get("/api/orgdata/0?includeDescendants=true")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("{\"identifier\":\"0\",\"name\":\"0\",\"rootIdentifier\":\"0\",\"height\":0,\"descendants\":[{\"identifier\":\"1\",\"name\":\"child1\",\"ancestorIdentifier\":\"0\",\"rootIdentifier\":\"0\",\"height\":1,\"descendants\":[{\"identifier\":\"3\",\"name\":\"child11\",\"ancestorIdentifier\":\"1\",\"rootIdentifier\":\"0\",\"height\":2,\"descendants\":[]},{\"identifier\":\"4\",\"name\":\"child12\",\"ancestorIdentifier\":\"1\",\"rootIdentifier\":\"0\",\"height\":2,\"descendants\":[]}]},{\"identifier\":\"2\",\"name\":\"child2\",\"ancestorIdentifier\":\"0\",\"rootIdentifier\":\"0\",\"height\":1,\"descendants\":[{\"identifier\":\"5\",\"name\":\"child21\",\"ancestorIdentifier\":\"2\",\"rootIdentifier\":\"0\",\"height\":2},{\"identifier\":\"6\",\"name\":\"child2\",\"ancestorIdentifier\":\"2\",\"rootIdentifier\":\"0\",\"height\":2}]}],\"_links\":{\"self\":{\"href\":\"http://localhost/api/orgdata/0?includeDescendants=true\"},\"datawithdescendants\":{\"href\":\"http://localhost/api/orgdata/0?includeDescendants=true\"},\"root\":{\"href\":\"http://localhost/api/orgdata/0?includeDescendants=false\"}}}"));
	}
	
	
	@Configuration
    @ComponentScan(basePackageClasses = { RestApiController.class })
    public static class TestConf {}
}
