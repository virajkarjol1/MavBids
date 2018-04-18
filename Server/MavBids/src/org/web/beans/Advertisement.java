package org.web.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Advertisement {

	private int advertisementId;
	private String itemName;
	private Double currentBidPrice;
	private Double startingPrice; // Price for Bid or Buy
	private Date expiryDate;
	private Date startDate;
	private String expiryDateStr;
	private String description;
	private String status;
	private Double sellerId;
	private Double buyer;
	private String auctionType;
	private Double categoryID;
	private String advImgB64Str;

	public String getExpiryDateStr() {
		return expiryDateStr;
	}

	public void setExpiryDateStr(String expiryDateStr) {
		this.expiryDateStr = expiryDateStr;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.expiryDate = formatter.parse(expiryDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getAdvertisementId() {
		return advertisementId;
	}
	public void setAdvertisementId(int advertisementId) {
		this.advertisementId = advertisementId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getCurrentBidPrice() {
		return currentBidPrice;
	}
	public void setCurrentBidPrice(Double currentBidPrice) {
		this.currentBidPrice = currentBidPrice;
	}
	public Double getStartingPrice() {
		return startingPrice;
	}
	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuctionType() {
		return auctionType;
	}
	public void setAuctionType(String auctionType) {
		this.auctionType = auctionType;
	}
	public Double getSellerId() {
		return sellerId;
	}
	public void setSellerId(Double sellerId) {
		this.sellerId = sellerId;
	}
	public Double getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(Double categoryID) {
		this.categoryID = categoryID;
	}

	public String getAdvImgB64Str() {
		return advImgB64Str;
	}

	public void setAdvImgB64Str(String advImgB64Str) {
		this.advImgB64Str = advImgB64Str;
	}

	public Double getBuyer() {
		return buyer;
	}

	public void setBuyer(Double buyer) {
		this.buyer = buyer;
	}
}
