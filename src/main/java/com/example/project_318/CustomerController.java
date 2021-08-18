package com.example.project_318;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@RestController
class CustomerController {

  private final CustomerRepository repository;
  private final CustomerModelAssembler assembler;

  CustomerController(CustomerRepository repository,CustomerModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }


  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/customers")
  CollectionModel<EntityModel<Customer>> all() {
  
    List<EntityModel<Customer>> customers = repository.findAll().stream() //
    .map(customer -> EntityModel.of(customer,
                linkTo(methodOn(CustomerController.class).one(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).all()).withRel("customers"))).collect(Collectors.toList());
  
    return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class).all()).withSelfRel());
  }
  // end::get-aggregate-root[]

  @PostMapping("/customers")
	Customer newCustomer(@RequestBody Customer newCustomer) {
		return repository.save(newCustomer);
	}

  // Single item
  
  @GetMapping("/customers/{id}")
  EntityModel<Customer> one(@PathVariable Long id) {
  
    Customer customer = repository.findById(id) //
        .orElseThrow(() -> new CustomerNotFoundException(id));
  
    return EntityModel.of(customer, //
    linkTo(methodOn(CustomerController.class).one(id)).withSelfRel(),
    linkTo(methodOn(CustomerController.class).all()).withRel("customers"));
  }

  @PutMapping("/customers/{id}")
	Customer replaceCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {

		return repository.findById(id) //
				.map(customer -> {
					customer.setCompanyName(newCustomer.getCompanyName());
					customer.setAddress(newCustomer.getAddress());
          customer.setCountry(newCustomer.getCountry());
					return repository.save(customer);
				}) //
				.orElseGet(() -> {
					newCustomer.setId(id);
					return repository.save(newCustomer);
				});
	}

  @DeleteMapping("/customers/{id}")
  void deleteCustomer(@PathVariable Long id) {
		repository.deleteById(id);
	}

}