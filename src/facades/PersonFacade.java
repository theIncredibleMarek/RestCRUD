/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import exceptions.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Person;

/**
 *
 * @author Marek FURAK
 */
public class PersonFacade implements IPersonFacade
{

    Map<Integer, Person> persons = new HashMap();
    private  int nextId = 0;
    private Gson gson = new Gson();
 
    @Override
    public Person addPerson(String json)
    {
        Person p = gson.fromJson(json, Person.class);
        System.out.println(p);
        p.setId(nextId);
        persons.put(nextId, p);
        nextId++;
        return p;
    }

    @Override
    public Person deletePerson(int id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPerson(int id) throws NotFoundException
    {
        Person p = persons.get(id);
        if(p==null)
        {
                throw new NotFoundException("Shit man, that person with the ID "+id+" ain't even there.");   
        }
      return gson.toJson(p);
    }

    @Override
    public String getPersons()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Person editPerson(String json)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
