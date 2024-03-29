
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import service.PaperService;
/**
 * This class represents a paper an author may submit for review.
 * @author Danielle Tucker
 */
public class Paper extends Observable
{
	/**
	 * The unique identifier for the paper.
	 */
	private int my_paper_ID;
		
	/**
	 * The author of this paper.
	 */
	private User my_author;
	
	/**
	 * The title of this paper.
	 */
	private String my_title =  "No Title Set";
	
	/**
	 * Keywords used for searching for this paper.
	 */
	private String my_keywords = "No Keywords Set";
	
	/**
	 * The abstract.
	 */
	private String my_abstract = "No Abstract Set";
	
	/**
	 * The conference category this paper belongs to.
	 */
	private String my_category = "No category set";
	
	/**
	 * Stores the contents of the paper.
	 */
	private String content = "";
	
	/**
	 * Stores the revised content of the paper;
	 */
	private String revised_content = "";
	
	/**
	 * The status of the paper in the review process.
	 */
	private Status my_status = Status.SUBPROGRAM_CHAIR_NEEDED;
	
	private Recommendation my_recommendation;
	
	private List<Review> my_reviews = new ArrayList<Review>();
	
	private Status acceptanceStatus;
	
	/**
	 * TEST CONSTRUCTOR.
	 */
	public Paper()
	{
		my_author = new Author(new ArrayList<Paper>());
		my_title = "Title";
		my_keywords = "keywords";
		my_abstract = "abstract";
		my_category = "category";
		content = "content";
		acceptanceStatus = Status.UNDECIDED;
	}
	
	/**
	 * Create a paper with no associated reviews or decisions.
	 * @param the_author the author of this paper.
	 * @param the_title the title of this paper.
	 * @param the_keywords the keywords to describe this paper.
	 * @param the_abstract the abstract for this paper.
	 * @param the_category the conference category associated with this paper.
	 * @param the_document_path the user path for this document.
	 */
	public Paper(final User the_author, final String the_title, final String the_keywords,
			final String the_abstract, final String the_category, final String content)
	{
		my_author = the_author;
		my_title = the_title;
		my_keywords = the_keywords;
		my_abstract = the_abstract;
		my_category = the_category;
		this.content = content;
		acceptanceStatus = Status.UNDECIDED;
	}
	
	/**
	 * Set the unique id of this paper.
	 * @param the_id the unique id of this paper.
	 */
	public void setID(final int the_id)
	{
		my_paper_ID = the_id;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The id of this paper
	 * @return the id of this paper
	 */
	public int getID()
	{
		return my_paper_ID;
	}
	
	/**
	 * Set the author of this paper.
	 * @param the_author
	 */
	public void setAuthor(final User the_author)
	{
		my_author = the_author;
		setChanged();
    notifyObservers();
	}
	
	/**
	 * The author of this paper.
	 * @return the author of this paper.
	 */
	public User getAuthor()
	{
		return my_author;
	}
	
	/**
	 * The title of the paper
	 * @param the_title the title of the paper.
	 */
	public void setTitle(final String the_title)
	{
		my_title = the_title;
		setChanged();
    notifyObservers();
	}
	
	/**
	 * Get the title of the paper.
	 * @return the title of the paper.
	 */
	public String getTitle()
	{
		return my_title;
	}
	
	/**
	 * Set the keywords for the search of this paper.
	 * @param the_keywords the string of keywords used to search the paper.
	 */
	public void setKeywords(final String the_keywords)
	{
		my_keywords = the_keywords;
		setChanged();
    notifyObservers();
	}
	
	/**
	 * The keywords for searching this paper.
	 * @return the keywords for searching this paper.
	 */
	public String getKeywords()
	{
		return my_keywords;
	}
	
	/**
	 * View the abstract for this paper.
	 * @return the abstract for this paper.
	 */
	public String getAbstract()
	{
		return my_abstract;
	}
	
	/**
	 * Set the abstract for this paper.
	 * @param the_abstract the abstract
	 */
	public void setAbstract(final String the_abstract)
	{
		my_abstract = the_abstract;
		setChanged();
    notifyObservers();
	}
	
	/**
	 * The conference category that this paper belongs to.
	 * @return the conference category associated with this paper
	 */
	public String getCategory()
	{
		return my_category;
	}
	
	/**
	 * Set the conference category that this paper belongs to.
	 * @param the_category the conference category
	 */
	public void setCategory(final String the_category)
	{
		my_category = the_category;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the text of the paper.
	 * @return the text (content) of the paper.
	 * @author Roshun
	 */
	public String getContent() {
	  return content;
	}
	
	/**
	 * Set the content(text) of the paper.
	 * @param content the text of the paper.
	 * @author Roshun
	 */
	public void setContent(final String content) {
	  this.content = content;
	  setChanged();
    notifyObservers();
	}
	
	/**
	 * Set the revised content of this paper.
	 * @param content the revised content.
	 */
	public void setRevisedContent(final String content)
	{
		revised_content = content;
		setChanged();
    notifyObservers();
	}
	
	/**
	 * Get the revised content.
	 * @return the revised paper's content.
	 */
	public String getRevisedContent()
	{
		return revised_content;
	}
	
	
	/**
	 * Determine the status of the Paper in the review process.
	 * @return the status of the paper.
	 */
	public Status getStatus()
	{
		Status result = my_status;
		if(my_status == Status.ACCEPT || my_status == Status.DECLINE)
		{
			//Decision has been made
		}
		else if(PaperService.getInstance().getAssignedSubprogramChair(my_paper_ID).getID() == 0)
		{
			result = Status.SUBPROGRAM_CHAIR_NEEDED;
		}
		else if(my_reviews.size() < 3)
		{
			result = Status.REVIEWS_NEEDED;
		}
		else if(my_recommendation == null || my_recommendation.getRecommender().getID()==0)
		{
			result = Status.RECOMMENDATION_NEEDED;
		}
		else
		{
			result = Status.DECISION_NEEDED;
		}
		return result;
	}
	
	/**
	 * Set the status of this paper.
	 * @param the_status the status of the paper in the workflow.
	 */
	public void setStatus(final Status the_status)
	{
		if(the_status != null)
		{
			my_status = the_status;
		}
	}
	
	/**
	 * Gets paper's acceptance status.
	 */
	public Status getAcceptanceStatus() {
	  return acceptanceStatus;
	}
	
	public void setAcceptanceStatus(Status aStatus) {
	  acceptanceStatus = aStatus;
	}
	
	/**
	 * Set the SubProgramChair's recommendation.
	 * @param the_recommendation the recommendation to associate with this paper.
	 */
	public void setRecommendation(final Recommendation the_recommendation)
	{
		my_recommendation = the_recommendation;
		setChanged();
    notifyObservers();
	}

	
	/**
	 * Get the SubProgram Chair's recommendation associated with this paper.
	 * @return the SubProgram Chair's recommendation.  May be null.
	 */
	public Recommendation getRecommendation()
	{
		return my_recommendation;
	}
	
	/**
	 * Add a review to this paper.
	 * @param the_review the review to add to this paper.
	 */
	public void addReview(final Review the_review)
	{
		my_reviews.add(the_review);
	}
	
	/**
	 * Get all reviews associated with this paper
	 * @return a list of all reviews.
	 */
	public List<Review> getReviews()
	{
		if(my_reviews.isEmpty()) {
		  List<Review> temp = new ArrayList<Review>();
		  temp.add(new Review() {
		    @Override 
		    public String toString() {
		      return "No reviews available";
		    }
		  });
		  return temp;
		}else 
		  return my_reviews;
	}
	
	/**
	 * Determines if the object is equal to this paper.
	 * @param the_object the object to compare to this paper.
	 * @return if this paper is equal to the_object
	 */
	public boolean equals(Object the_object)
	{
		boolean result = false;
		if(this == the_object)
		{
			result = true;
		}
		
		else if(the_object == null || (the_object.getClass() != getClass())) {
		  result = false;
		}
		
		else if( ((Paper) the_object).getID() == getID() )
		  result = true;
		
		return result;
	}

	/**
	 * Set all reviews in this paper with the list.  Will replace any reviews with the list provided.
	 * @param reviews the list of reviews for this paper.
	 */
	public void setReview(List<Review> the_reviews) 
	{
		my_reviews = the_reviews;
	}
}
