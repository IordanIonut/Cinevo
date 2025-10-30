package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService
{
    @Autowired
    private PersonRepository personRepository;

    public Person findPersonById(final Integer id)
    {
        return this.personRepository.findPersonById(id).orElseGet(Person::new);
    }

}
