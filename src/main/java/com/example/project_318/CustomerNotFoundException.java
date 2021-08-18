package com.example.project_318;

class CustomerNotFoundException extends RuntimeException {

  CustomerNotFoundException(Long id) {
    super("Could not find Customer " + id);
  }
}