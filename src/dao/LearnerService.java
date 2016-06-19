package dao;

import java.util.*;
import javax.persistence.EntityTransaction;

import metier.Action;
import metier.Indicator;
import metier.Learner;
import metier.LearnerAction;

public class LearnerService extends EntityService {
	
	public void insertLearner(Learner learner)
	{
		try
		{
			EntityTransaction transaction = startTransaction();
			if(!entityManager.contains(learner))
			{
				transaction.begin();
				entityManager.persist(learner);
				entityManager.flush();
				transaction.commit();
			}
			entityManager.close();
		} catch (Exception e)
		{
			
		}
	}
	
	public List<Learner> search(String word)
	{
		List<Learner> learners = null;
		try 
		{
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			learners= (List<Learner>) entityManager.createQuery("SELECT l FROM Learner l WHERE (lower(l.surname) like :word OR lower(l.forename) like :word OR lower(l.email) like :word) ORDER BY l.id").setParameter("word", "%"+word+"%").getResultList();
			entityManager.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return learners;
	}
	
	public Learner find(int id)
	{
		Learner learner = null;
		try 
		{
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			learner=entityManager.find(Learner.class, id);
			entityManager.close();
			emf.close();
		} catch (Exception e)
		{
			
		}
		return learner;
	}
	
	public List<Learner> findAll()
	{
		List<Learner> learners = null;
		try 
		{
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			learners= (List<Learner>) entityManager.createQuery("SELECT l FROM Learner l ORDER BY l.id").getResultList();
			entityManager.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		
		return learners;
	}
	
	public void delete(int id) {
		delete(find(id));
	}

	public void delete(Learner i) {
		try {
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			
			LearnerActionService las = new LearnerActionService();
			
			//suppression des learnerAction
			for(LearnerAction la : i.getLearnerActions()){
				las.delete(la);
			}
			
			
			//suppression dans la table Learner
			if (!entityManager.contains(i)) {
				i = entityManager.merge(i);
			}
			entityManager.remove(i);
			transaction.commit();
			entityManager.close();
			emf.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
