package org.web.beans;

public class Review {

	private int id;
	private int reviewer;
	private int adId;
	private String review;
	private int rating;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReviewer() {
		return reviewer;
	}
	public void setReviewer(int reviewer) {
		this.reviewer = reviewer;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}
