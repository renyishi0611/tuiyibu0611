package com.none.core.dao.hibernate4;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.none.core.common.utils.PageUtil;
import com.none.core.dao.IBaseDao;
import com.none.core.model.Pager;

public class BaseHibernateDao implements IBaseDao {
	
	private static Logger logger = Logger.getLogger(BaseHibernateDao.class);
	private NamedParameterJdbcTemplate namedJdbcTemplate =null;
	
	@Autowired()
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	@Autowired
	 public JdbcTemplate jdbcTemplate;
	/**
	 * 获取Hibernate Session
	 * 
	 * @return Hibernate Session
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	public NamedParameterJdbcTemplate getNamedJdbcTemplate(){
		if(namedJdbcTemplate==null){
			return new NamedParameterJdbcTemplate(jdbcTemplate); 
		}
		return namedJdbcTemplate;
	}
	
	
	
	/**
	 * 获取Hibernate Criteria
	 * 
	 * @param Class
	 * @return Criteria
	 */
	public Criteria getCriteria(Class<?> cls){
		Criteria criteria = getSession().createCriteria(cls);
		return criteria;
	}
	
    /**
     * 保存对象
     * 
     * @param Object
     * @return 主键Id
     */
    public Serializable saveEntity(Object object){
    	if(object != null){
    		return getSession().save(object);
    	}
    	return null;
    }
    
    /**
     * 更新对象
     * 
     * @param Object
     */
    public void updateEntity(Object object){
    	if(object != null){
    		getSession().update(object);
    		
    	}
    }
    /**
     * 更新对象2
     * 
     * @param Object
     */
    public void updateObj(Object object){
    	if(object != null){
    		getSession().merge(object);
    	}
    }
    /**
     * 根据过滤字段更新对象
     * 
     * @param Object
     */
    public boolean updateEntityIgnore(Object target, String[] ignoreProperties){
    	Serializable idValue = null;
    	if(target != null){
    		// 获取要修改对象的主键值
    		for(Method m : target.getClass().getMethods()){
				Id id = m.getAnnotation(Id.class);		// 单主键
				EmbeddedId embeddedId = m.getAnnotation(EmbeddedId.class);		// 嵌入式主键
				if(id != null || embeddedId != null){
					try {
						Method idMethod = target.getClass().getDeclaredMethod(m.getName());
						idValue = (Serializable) idMethod.invoke(target);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
    	}
    	if(idValue != null){
    		if(idValue.hashCode() == 0){
    			logger.debug("系统错误：要修改的对象主键不能为空！-" + target.getClass().getName());
			}else{
	    		// 先查询要修改的对象
	        	Object object = findEntity(target.getClass(), idValue);
	        	if(object != null){
	        		// 设置更新时要忽略的字段
	        		BeanUtils.copyProperties(target, object, ignoreProperties);
	        		// 执行更新操作
	        		updateEntity(object);
	        		return true;
	        	}
			}
    	}
    	return false;
    }
    
    /**
     * 保存/修改对象
     * 
     * @param Object
     */
    public void saveOrUpdateEntity(Object object){
    	if(object != null){
    		getSession().saveOrUpdate(object);
    	}
    }
    
    /**
     * 根据对象主键删除对象
     * 
     * @param cls
     * @param id
     * 
     * @return boolean
     */
    public boolean deleteEntity(Class<?> cls, Serializable id){
		if(id.hashCode() == 0){
			logger.debug("系统错误：要删除的对象主键不能为空！" + cls.getName());
		}
    	if(cls != null && id.hashCode() != 0){
        	Object object = findEntity(cls, id);
        	if(object != null){
        		getSession().delete(object);
        		return true;
        	}
    	}
    	return false;
    }
    
    /**
     * 直接删除对象
     * 
     * @param Object
     * 
     * @return boolean
     */
    public boolean deleteEntity(Object object){
    	if(object != null){
    		getSession().delete(object);
    		return true;
    	}
    	return false;
    }
    
    /**
     * 查询对象
     * 
     * @param Object
     * @return boolean
     */
    @SuppressWarnings("unchecked")
	public <T> T findEntity(Class<?> cls, Serializable id){
    	if(id.hashCode() == 0){
    		logger.debug("系统错误：要查询的对象主键不能为空！" + cls.getName());
    	}
    	Object object = null;
    	if(cls != null && id.hashCode() != 0){
    		object = getSession().get(cls, id);
    	}
    	return (T)object;
    }
    
    /**
     * 查询所有对象
     * 
     * @param Object
     * @return boolean
     */
    public List<?> findAllEntity(Class<?> cls){
    	if(cls != null){
    		return getCriteria(cls).list();
    	}
    	return null;
    }
    
	/**
	 *QBC查询所有
	 * 
	 * @param criteria
	 * @return List
	 */
	public List<?> listQBC(Criteria criteria) {
		return criteria.list();		
		
	}
	
	/**
	 * QBC分页查询
	 * 
	 * @param criteria
	 * @param pager 分页参数
	 * @param queryCache 是否开启查询缓存
	 * 
	 * @return 分页数据Map
	 */
	public Map<?,?> listPageQBC(Criteria criteria, Pager pager, Boolean...queryCache) {
		if(queryCache.length > 0 ? queryCache[0] : false){
			criteria.setCacheable(true);
		}
		// 查询当前页数的分页数据
		criteria.setFirstResult((pager.getPageNo() - 1) * pager.getPageSize());
		criteria.setMaxResults(pager.getPageSize());
		// 根据页面参数进行排序
		if(StringUtils.isNotBlank(pager.getDirection()) && StringUtils.isNotBlank(pager.getSort())){
			if(pager.getDirection().equals("asc")){
				criteria.addOrder(Order.asc(pager.getSort()));
			}else if(pager.getDirection().equals("desc")){
				criteria.addOrder(Order.desc(pager.getSort()));
			}
		}
		List<?> list = criteria.list();
		// 清除分页相关参数
		criteria.setFirstResult(0);
		criteria.setMaxResults(0);
		// 获取总条数
		Object totalRows = criteria.setProjection(Projections.rowCount()).uniqueResult();
		if(totalRows != null){
			pager.setTotalRows(Integer.valueOf(totalRows.toString()));
		}
		// 获取页面用于产生分页数据的Map集合
		Map<?,?> gridDataMap = PageUtil.getPageDataJSON(pager, list);
		return gridDataMap;
	}
	
	/**
	 * QBCL分页查询
	 * 
	 * @param criteria
	 * @param pager 分页参数
	 * @param queryCache 是否开启查询缓存
	 * 
	 * @return 分页数据List
	 */
	public List<?> listPageQBCL(Criteria criteria, Pager pager, Boolean...queryCache) {
		if(queryCache.length > 0 ? queryCache[0] : false){
			criteria.setCacheable(true);
		}
		// 查询当前页数的分页数据
		criteria.setFirstResult((pager.getPageNo() - 1) * pager.getPageSize());
		criteria.setMaxResults(pager.getPageSize());
		// 根据页面参数进行排序
		if(StringUtils.isNotBlank(pager.getDirection()) && StringUtils.isNotBlank(pager.getSort())){
			if(pager.getDirection().equals("asc")){
				criteria.addOrder(Order.asc(pager.getSort()));
			}else if(pager.getDirection().equals("desc")){
				criteria.addOrder(Order.desc(pager.getSort()));
			}
		}
		List<?> list = criteria.list();
		// 清除分页相关参数
		criteria.setFirstResult(0);
		criteria.setMaxResults(0);
		// 获取总条数
		Object totalRows = criteria.setProjection(Projections.rowCount()).uniqueResult();
		if(totalRows != null){
			pager.setTotalRows(Integer.valueOf(totalRows.toString()));
			//获取总页数
			Integer pages=pager.getTotalRows()%pager.getPageSize()>0?pager.getTotalRows()/pager.getPageSize()+1:pager.getTotalRows()/pager.getPageSize();
			pager.setTotalPages(pages);
		}
		return list;
	}
	
	/**
	 * HQL占位符，分页查询
	 * 
	 * @param hql 查询语句(select,from等查询关键字必须小写)
	 * @param params 查询参数
	 * @param pager	分页参数
	 * @param queryCache 是否开启查询缓存
	 * 
	 * @return 分页数据Map
	 */
	public Map<?,?> listPageHQL(StringBuffer hql, Map<String, Object> params, Pager pager, Boolean...queryCache) {
		String sortName = pager.getSort();
		// 对HQL语句进行处理获取要排序的字段
		String[] sqlSplit = null;
		try {
			sqlSplit = hql.substring(hql.indexOf("(") + 1, hql.indexOf(")")).replaceAll(" ", "").toString().split(",");
		} catch (Exception e) {
		}
		if(sortName != null){
			if(sqlSplit != null && hql.indexOf("left join fetch") == -1){
				for(String s : sqlSplit){
					if(s.indexOf(sortName) != -1){
						sortName = s;
						break;
					}
				}
			}
			// 根据页面参数进行排序
			if(StringUtils.isNotBlank(pager.getDirection()) && StringUtils.isNotBlank(pager.getSort())){
				if(pager.getDirection().equals("asc")){
					hql.append(" order by " + sortName + " asc");
				}else if(pager.getDirection().equals("desc")){
					hql.append(" order by " + sortName + " desc");
				}
			}
		}

		// 分页查询Query
		Query query = this.getSession().createQuery(hql.toString());
		
		// 设置查询缓存
		if(queryCache.length > 0 ? queryCache[0] : true){
			query.setCacheable(true);
		}
		
		// 用于统计数据总条数的SQL
		String hqlCount = hql.toString();
		
		// 用于处理自己的返回结果的情况
		if(hqlCount.indexOf("select") != -1 && hqlCount.indexOf("from") != -1){
			hqlCount = hqlCount.substring(hqlCount.indexOf("from"), hqlCount.length());
		}
		// count(*)不能和order一起使用
		if(hqlCount.indexOf("order") != -1){
			hqlCount = hqlCount.substring(0, hqlCount.indexOf("order"));
		}
		hqlCount = hqlCount.replace("fetch", "");
		
		hqlCount = "select count(*) " + hqlCount;
		
		// 用于统计数据总条数的Query
		Query queryNum = this.getSession().createQuery(hqlCount);
		
		// 设置查询缓存
		if(queryCache.length > 0 ? queryCache[0] : true){
			queryNum.setCacheable(true);
		}
				
		// 设置查询参数
		if(params !=null && params.size() > 0){
			for(Object o:params.keySet()){
				// 设置分页Query参数
				query.setParameter((String)o, params.get(o));
				// 设置统计数据总条数Query参数
				queryNum.setParameter((String)o, params.get(o));
			}
		}
		// 总条数
		Integer totalCount = Integer.valueOf(queryNum.uniqueResult().toString());
		pager.setTotalRows(totalCount);

		// 查询当前页数的分页数据
		query.setFirstResult((pager.getPageNo() - 1) * pager.getPageSize());
		query.setMaxResults(pager.getPageSize());
		
		List<?> list = query.list();
				
		// 获取页面用于产生分页数据的Map集合
		Map<?,?> gridDataMap = PageUtil.getPageDataJSON(pager, list);
		return gridDataMap;
	}
	

	/**
	 * HQL占位符，不分页查询
	 * 
	 * @param hql
	 * @param values
	 * @return HQL占位符，分页数据Map
	 */
	public List<?> listByHQL(String hql, Object[] values) {
		Query query = this.getSession().createQuery(hql);
		 if (values != null) {
	         for (int i = 0; i < values.length; i++) {
	             if (values[i] instanceof Date) {
	                 query.setDate(i, (Date) values[i]);
	             }else if (values[i] instanceof Timestamp) {
	                 query.setTimestamp(i, (Timestamp) values[i]);
	             }else {
	                 query.setParameter(i, values[i]);
	             }
	         }
	     }
		return query.list();
	}
	
	/**
	 * HQL占位符，执行操作
	 * 
	 * @param hql
	 * @param values
	 * @return 
	 */
	public int exeByHql(String hql, Object[] values) {
		Query query = this.getSession().createQuery(hql);
		 if (values != null) {
	         for (int i = 0; i < values.length; i++) {
	             if (values[i] instanceof Date) {
	                 query.setDate(i, (Date) values[i]);
	             } 
	             if (values[i] instanceof Timestamp) {
	                 query.setTimestamp(i, (Timestamp) values[i]);
	             }
	             else {
	                 query.setParameter(i, values[i]);
	             }
	         }
	     }
		return query.executeUpdate();
	}

	/**
	 * 刷新缓存
	 */
	public void flush() {
		this.getSession().flush();
	}
	
	/**
	 * 清空缓存
	 */
	public void clear() {
		this.getSession().clear();
	}
	/**
	 * 根据通用sql语句查询结果，带分页
	 * 注意：1.使用于mysql数据库。2.传过来的sql不能有limit 分页语句
	 * 返回一个list<Map>
	 */
	@SuppressWarnings("rawtypes")
	public List<?> getListBySql(Pager pager,String sql,Class<?> cls){
		SQLQuery query=null;
		int totalRows=0;
		if(sql!=null&&!"".equals(sql)){
			if(pager==null){
				pager=new Pager();
				pager.setPageNo(1);
				pager.setPageSize(10);
			}
			
			String sqlSele=sql;//分页查询sql
			String sqlCount="";//查询总条数的语句
			
			//处理sql
			sqlCount=" select count(*) as totalRows  from ("+sql+" )  as count_cxb_sql_index_numzongshu ";
			int index=sql.lastIndexOf(" order ");
		    if(index!=-1){
		    	sqlSele="select * from ( "+sql+" ) as count_cxb_sql_index  limit "+(pager.getPageNo()-1)*pager.getPageSize()+", "+pager.getPageSize()+" ";
		    }
		    query=this.getSession().createSQLQuery(sqlSele);
		    query.addEntity(cls);
		    //select  totle counts
		    SQLQuery queryCount=this.getSession().createSQLQuery(sqlCount);
		    if(queryCount!=null){
		    	if(queryCount.list()!=null){
		    		if(queryCount.list().size()>0){
		    			if(queryCount.list().get(0)!=null){
		    				totalRows=Integer.valueOf(queryCount.list().get(0).toString());
		    			}
		    		}
		    	}
		    }
		    pager.setTotalRows(totalRows);
			//获取总页数
			Integer pages=totalRows%pager.getPageSize()>0?pager.getTotalRows()/pager.getPageSize()+1:pager.getTotalRows()/pager.getPageSize();
			pager.setTotalPages(pages);
		}else{
			return new ArrayList();
		}
		return query.list();
	}
	
	/**
	 * 根据通用sql语句查询结果，带分页
	 * 注意：1.使用于mysql数据库。2.传过来的sql不能有limit 分页语句
	 * 返回一个list<Map>
	 */
	public List<Map<String , Object>> getListBySql(Pager pager,String sql){
		List<Map<String , Object>> listmap=new ArrayList<Map<String , Object>>();
		int totalRows=0;
		if(sql!=null&&!"".equals(sql)){
			if(pager==null){
				pager=new Pager();
				pager.setPageNo(1);
				pager.setPageSize(10);
			}
			
			String sqlSele=sql;//分页查询sql
			String sqlCount="";//查询总条数的语句
			
			//处理sql
			sqlCount=" select count(*) as totalRows  from ("+sql+" )  as count_cxb_sql_index_numzongshu ";
			int index=sql.lastIndexOf(" order ");
		    if(index!=-1){
		    	sqlSele="select * from ( "+sql+" ) as count_cxb_sql_index  limit "+(pager.getPageNo()-1)*pager.getPageSize()+", "+pager.getPageSize()+" ";
		    }
		    listmap=jdbcTemplate.queryForList(sqlSele);
		    //select  totle counts
		    SQLQuery queryCount=this.getSession().createSQLQuery(sqlCount);
		    if(queryCount!=null){
		    	if(queryCount.list()!=null){
		    		if(queryCount.list().size()>0){
		    			if(queryCount.list().get(0)!=null){
		    				totalRows=Integer.valueOf(queryCount.list().get(0).toString());
		    			}
		    		}
		    	}
		    }
		    pager.setTotalRows(totalRows);
			//获取总页数
			Integer pages=totalRows%pager.getPageSize()>0?pager.getTotalRows()/pager.getPageSize()+1:pager.getTotalRows()/pager.getPageSize();
			pager.setTotalPages(pages);
		}else{
			return listmap;
		}
		return listmap;
	}
	
	
	
	/**
	 * 根据通用sql语句查询结果，不分页
	 * 注意：1.使用于mysql数据库。
	 * 返回一个list<Map>
	 */
	@SuppressWarnings("rawtypes")
	public List<?> getListBySql(String sql,Class<?> cls){
		SQLQuery query=null;
		if(sql!=null&&!"".equals(sql)){
		    query=this.getSession().createSQLQuery(sql);
		    query.addEntity(cls);
		}else{
			return new ArrayList();
		}
		return query.list();
	}

}
