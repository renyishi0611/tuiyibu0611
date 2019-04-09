package com.none.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TTags entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_tags")
public class TTags implements java.io.Serializable {

	// Field

	/**
	 * 
	 */
	private static final long serialVersionUID = -1315242349082933359L;
	private String tagId;
	private String name;
//	private Set<TNews> TNewses = new HashSet<TNews>(0);
//	private Set<TActivity> activitys = new HashSet<TActivity>(0);
//	private Set<TSurvey> surveys=new HashSet<TSurvey>(0);
	// Constructors

	/** default constructor */
	public TTags() {
	}

	/** minimal constructor */
	public TTags(String tagId) {
		this.tagId = tagId;
	}

	/** full constructor */
//	public TTags(String tagId, String name, Set<TNews> TNewses) {
//		this.tagId = tagId;
//		this.name = name;
//		this.TNewses = TNewses;
//	}

	// Property accessors
	@Id
	@Column(name = "tag_id", unique = true, nullable = false, length = 32)
	public String getTagId() {
		return this.tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	@Column(name = "name", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//	@JoinTable(name = "t_new_tag", joinColumns = { @JoinColumn(name = "tag_id", updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "news_id", updatable = false) })
//	public Set<TNews> getTNewses() {
//		return this.TNewses;
//	}
//
//	public void setTNewses(Set<TNews> TNewses) {
//		this.TNewses = TNewses;
//	}

	

	
	//tag  and activity of mapping 
	
//	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//	@JoinTable(name = "t_activity_tag", joinColumns = { @JoinColumn(name = "tag_id", updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "activity_id", updatable = false) })
//	public Set<TActivity> getActivitys() {
//		return activitys;
//	}
//
//	public void setActivitys(Set<TActivity> activitys) {
//		this.activitys = activitys;
//	}

//	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//	@JoinTable(name = "t_survey_tag", joinColumns = { @JoinColumn(name = "tag_id", updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "survey_id", updatable = false) })
//	public Set<TSurvey> getSurveys() {
//		return surveys;
//	}
//
//	public void setSurveys(Set<TSurvey> surveys) {
//		this.surveys = surveys;
//	}
}