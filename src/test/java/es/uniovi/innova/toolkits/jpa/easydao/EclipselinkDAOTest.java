package es.uniovi.innova.toolkits.jpa.easydao;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uniovi.innova.toolkits.jpa.easydao.test.Util;
import es.uniovi.innova.toolkits.jpa.easydao.test.entities.Address;
import es.uniovi.innova.toolkits.jpa.easydao.test.entities.Person;

public class EclipselinkDAOTest {
	
	private EntityManager em;
	

	private Person person1,person2,person3;
	private Address address1,address2;
	
	@Before
	public void createDataSource(){
		em=Util.createTestEntityManager();
		final DAO<Person> dao= new DAO<Person>(Person.class,em);
		person1=new Person("person1",1);
		person2=new Person("person2",1);
		person3=new Person("person3",3);
		address1=new Address("address1");
		address2=new Address("address2");
		
		new Transaction(em) {
			@Override
			protected void transactionalCode() {
				person1=dao.persist(person1);
				person2=dao.persist(person2);
				person3=dao.persist(person3);	
				person1.setAddress(address1);
				person2.setAddress(address2);
				person3.setAddress(address1);
			}
		}.execute();

	}
	
	@Test
	public void itReturnsEveryEntityWhenExampleHasNoPopulatedAttributes() {
		DAO<Person> dao=new EclipselinkDAO<Person>(Person.class,em);
		List<Person> people=dao.queryByExample(new Person());
		assertEquals("Not every person was retrieved",3,people.size());
		System.out.println(people);
	}
	
	@Test
	public void itReturnsEveryEntityWithACertainAddress() {
		DAO<Person> dao=new EclipselinkDAO<Person>(Person.class,em);
		Person example=new Person();
		example.setAddress(address1);
		List<Person> people=dao.queryByExample(example);
		assertEquals("Not every person was retrieved",2,people.size());
		System.out.println(people);
	}
	
//	@Test
//	public void itDoesntTakeIntoAccountPrimitiveDefaultValues() {
//		DAO<Person> dao=new EclipselinkDAO<Person>(Person.class,em);
//		Person example=new Person();
//		List<Person> people=dao.queryByExample(example);
//		assertEquals("Not every person was retrieved",3,people.size());
//		System.out.println(people);
//	}
	
	@After
	public void after(){
		new Transaction(em){
			@Override
			protected void transactionalCode() {
				em.createQuery("DELETE FROM Person p").executeUpdate();
			}
		}.execute();
		
		em.close();
	}
	
	
}