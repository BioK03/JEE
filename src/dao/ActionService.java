package dao;

import java.util.*;
import javax.persistence.EntityTransaction;

import metier.Action;
import metier.Mission;

public class ActionService extends EntityService {
	
	public void insertAction(Action action)
	{
		try
		{
			EntityTransaction transaction = startTransaction();
			if(!entityManager.contains(action))
			{
				transaction.begin();
				entityManager.persist(action);
				entityManager.flush();
				transaction.commit();
			}
			entityManager.close();
		} catch (Exception e)
		{
			
		}
	}
	
	public Action find(int id)
	{
		Action action = null;
		try 
		{
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			action=entityManager.find(Action.class, id);
			entityManager.close();
			emf.close();
		} catch (Exception e)
		{
			
		}
		return action;
	}
	
	@SuppressWarnings("unchecked")
	public List<Action> search(String word)
	{
		List<Action> actions = null;
		try 
		{
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			actions= (List<Action>) entityManager.createQuery("SELECT a FROM Action a WHERE lower(a.wording) like :word ORDER BY a.id").setParameter("word", "%"+word+"%").getResultList();
			entityManager.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return actions;
	}
	
	public void delete(int id)
	{
		deleteObjects(getCascade(id));
	}
	
	@SuppressWarnings("unchecked")
	public List<Action> findAll()
	{
		List<Action> actions = null;
		try 
		{
			EntityTransaction transaction = startTransaction();
			transaction.begin();
			actions= (List<Action>) entityManager.createQuery("SELECT a FROM Action a ORDER BY a.id").getResultList();
			entityManager.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		
		return actions;
	}
	
	public List<Object> getCascade(int id) {
		Action a=find(id);
		ArrayList<Object> returns = new ArrayList<>();
		returns.addAll(a.getIndicators());
		returns.addAll(a.getInscriptionActions());
		returns.add(a);
		return returns;
	}
	
	public List<Action> getActionByMission(int id) {
		System.out.println(id);
		MissionService mService = new MissionService();
		Mission m = mService.find(id);
		System.out.println(m.getActions().size());
		return m.getActions();
	}
	
//	public List<Action> search(String word)
//	{
//		List<Action> actions = null;
//		try 
//		{
//			EntityTransaction transaction = startTransaction();
//			transaction.begin();
//			actions= (List<Action>) entityManager.createQuery("SELECT a FROM Action a WHERE lower(a.wording) like :word ORDER BY a.id").setParameter("word", "%"+word+"%").getResultList();
//			entityManager.close();
//		} catch (Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
//		return actions;
//	}
	
}
