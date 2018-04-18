package org.web.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.web.beans.Advertisement;
import org.web.beans.Bid;
import org.web.beans.Category;
import org.web.beans.DirectBuy;
import org.web.beans.Image;
import org.web.beans.Review;
import org.web.beans.User;

@Component
public class DBMgr {

	final static Logger logger = Logger.getLogger(DBMgr.class);
	private static SessionFactory factory;

	public DBMgr() {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public boolean saveUser(User user){
		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();

		session.save(user);
		tx.commit();

		session.close();
		return true;
	}

	public User getUser(String emailId){
		Session session = factory.openSession();
		Query q = session.createQuery("from User where email = :email");

		@SuppressWarnings("unchecked")
		List<User> userList = q.setParameter("email", emailId).list();

		if(userList.size() >= 1)
			return userList.get(0);
		else
			return null;
	}

	public int saveAdvertisement(Advertisement adv){
		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();

		int advId = (Integer) session.save(adv);
		tx.commit();

		session.close();
		return advId;
	}

	@SuppressWarnings("unchecked")
	public List<Advertisement> getAdvertisements(int rlimit, String byUserId, String status, String forUserId){
		Session session = factory.openSession();
		List<Advertisement> advList;

		Criteria criteria = session.createCriteria(Advertisement.class);
		criteria.addOrder(Order.desc("startDate"));

		if(status != null && !status.isEmpty())
			criteria.add(Restrictions.eq("status", status));

		if(byUserId != null && !byUserId.isEmpty())
			criteria.add(Restrictions.eq("sellerId", Double.parseDouble(byUserId)));

		if(forUserId != null && !forUserId.isEmpty())
			criteria.add(Restrictions.ne("sellerId", Double.parseDouble(forUserId)));

		if(rlimit > -1)
			criteria.setMaxResults(rlimit);

		advList = criteria.list();
		session.close();
		return advList;
	}


	public List<Advertisement> getAdvertisementByName(String itemName){
		Session session = factory.openSession();

		@SuppressWarnings("unchecked")
		List<Advertisement> advList = session.createCriteria(Advertisement.class)
										.add(Restrictions.like("itemName", "%" + itemName  + "%"))
										.list();

		return advList;
	}

	public List<Advertisement> getMyOrders(String userId){
		Session session = factory.openSession();

		@SuppressWarnings("unchecked")
		List<Advertisement> advList = session.createCriteria(Advertisement.class)
							.add(Restrictions.eq("buyer", Double.parseDouble(userId)))
							.add(Restrictions.eq("status", "SOLD"))
							.list();

		return advList;
	}

	public boolean saveBid(Bid bid){
		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();

		session.save(bid);
		tx.commit();

		session.close();
		return true;
	}

	public boolean directBuy(DirectBuy directBuy){
		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();

		session.save(directBuy);
		tx.commit();

		session.close();
		return true;
	}

	public boolean addReview(Review review){
		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();

		session.save(review);
		tx.commit();

		session.close();
		return true;
	}


	public String addCategory(Category c){
		return null;
	}

	public String removeCategory(Category c){
		return null;
	}

	public boolean saveImage(Image img){

		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();

		session.save(img);
		tx.commit();

		session.close();
		return true;
	}

	public Image getImage(int advImageId){
		Session session = factory.openSession();

		@SuppressWarnings("unchecked")
		List<Image> imgList = session.createCriteria(Image.class)
								.add(Restrictions.eq("adId", advImageId))
								.list();
		session.close();

		if(imgList.size() > 0)
			return imgList.get(0);
		else
			return null;
	}
}
