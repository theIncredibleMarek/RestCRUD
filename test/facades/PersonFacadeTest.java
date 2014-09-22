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
import model.Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek FURAK
 */
public class PersonFacadeTest
{

    PersonFacade facade;
    Gson gson = new Gson();

    public PersonFacadeTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {

    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
        facade = new PersonFacade();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of addPerson method, of class PersonFacade.
     */
    @Test
    public void testAddPerson() throws NotFoundException
    {
        Person p = new Person("aaa", "bbb", "ccc");
        String personAsJson = gson.toJson(p);
        Person person = facade.addPerson(personAsJson);

        String actual = facade.getPerson(person.getId());
        assertEquals(personAsJson, actual);
    }

    /**
     * Test of deletePerson method, of class PersonFacade.
     */
    @Test
    public void testDeletePerson()
    {
        System.out.println("deletePerson");
        int id = 0;
        PersonFacade instance = new PersonFacade();
        Person expResult = null;
        Person result = instance.deletePerson(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPerson method, of class PersonFacade.
     */
    @Test
    public void testGetPerson() throws Exception
    {
        testAddPerson();
    }

    /**
     * Test of getPersons method, of class PersonFacade.
     */
    @Test
    public void testGetPersons()
    {
       Person p1 = new Person("aaa", "bbb", "ccc");
        Person person1 = facade.addPerson(gson.toJson(p1));
        
        Person p2 = new Person("aaa", "bbb", "ccc");
        Person person2 = facade.addPerson(gson.toJson(p2));
        
        Map<Integer,Person> test = new HashMap();
        test.put(person1.getId(),p1);
        test.put(person2.getId(),p2);
        String expected = gson.toJson(test.values());
        
        
        String result = facade.getPersons();
        assertEquals(expected, result);
    }

    /**
     * Test of editPerson method, of class PersonFacade.
     */
    @Test
    public void testEditPerson()
    {
        System.out.println("editPerson");
        String json = "";
        PersonFacade instance = new PersonFacade();
        Person expResult = null;
        Person result = instance.editPerson(json);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
