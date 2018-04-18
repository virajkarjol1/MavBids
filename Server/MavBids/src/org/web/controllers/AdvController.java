package org.web.controllers;

import java.io.IOException;
//import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.web.beans.Advertisement;
import org.web.beans.Bid;
import org.web.beans.DirectBuy;
import org.web.beans.Image;
import org.web.beans.Response;
import org.web.beans.Review;
import org.web.dao.DBMgr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AdvController {

	final static Logger logger = Logger.getLogger(AdvController.class);

	@Autowired
	DBMgr dbMgr;

	public @ResponseBody String getAdvertisement(){

		return null;
	}

	@RequestMapping(value = "/postAdvertisement", method = RequestMethod.POST)
	public @ResponseBody String saveAdvertisement(Advertisement adv){
		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		try {
			int advId = dbMgr.saveAdvertisement(adv);

			byte[] imgData = Base64.decodeBase64(adv.getAdvImgB64Str());
			Image advImage = new Image();
			advImage.setAdId(advId);
			advImage.setImage(imgData);

			Boolean rs = dbMgr.saveImage(advImage);

			r.setResult(rs);
			return mapper.writeValueAsString(r);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/getAdvByName", method = RequestMethod.GET)
	public @ResponseBody String getAdvertisementByName(@RequestParam("itemName") String itemName){

		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		List<Advertisement> advList = dbMgr.getAdvertisementByName(itemName);

		try {
			r.setResult(advList);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/getAdvertisements", method = RequestMethod.GET)
	public @ResponseBody String getAdvertisements(@RequestParam(value="limit", required=false) String limit,
			@RequestParam(value="byUserId", required=false) String byUserId,
			@RequestParam(value="forUserId", required=false) String forUserId,
			@RequestParam(value="status", required=false) String status){

		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		int rlimit = -1;

		if(!(limit == null)) {
			if(!limit.equalsIgnoreCase("")){
				try{
					rlimit = Integer.parseInt(limit);
				} catch(Exception e){
					logger.error("/getAdvertisements : Error in converting limit to int value" + e.getMessage());
				}
			}
		}

		List<Advertisement> advList = dbMgr.getAdvertisements(rlimit, byUserId, status,forUserId);

		try {
			r.setResult(advList);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			logger.error("/getAdvertisements : Error in returning advertisements list" + e.getMessage());
			return "{}";
		}
	}

	@RequestMapping(value = "/bidAdvertisement", method = RequestMethod.POST)
	public @ResponseBody String bidAdvertisement(Bid bid){

		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		Boolean status = dbMgr.saveBid(bid);

		try {
			r.setResult(status);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}


	@RequestMapping(value = "/directBuy", method = RequestMethod.POST)
	public @ResponseBody String directBuy(DirectBuy directBuy){

		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		Boolean status = dbMgr.directBuy(directBuy);

		try {
			r.setResult(status);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/getMyOrders", method = RequestMethod.POST)
	public @ResponseBody String getMyOrders(@RequestParam(value="userId", required=false) String userId){

		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		List<Advertisement> advList = dbMgr.getMyOrders(userId);

		try {
			r.setResult(advList);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/addReview", method = RequestMethod.POST)
	public @ResponseBody String addReview(Review review){

		Response r = new Response();
		ObjectMapper mapper = new ObjectMapper();

		Boolean status = dbMgr.addReview(review);

		try {
			r.setResult(status);
			return mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@RequestMapping(value = "/uploadAdvImage", method = RequestMethod.POST)
	public @ResponseBody String uploadAdvImage(@RequestParam("file") MultipartFile file){

		if(!file.isEmpty()){
			logger.info("Content type : " + file.getContentType());
			byte[] imgData = null;
			try {
				imgData = file.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
				return "false";
			}

			if(imgData != null){
				Image img = new Image();
				img.setImage(imgData);

				dbMgr.saveImage(img);
			}
			return "true";
		} else
			return "false";
	}

	@RequestMapping(value = "/getAdvImage", method = RequestMethod.GET)
	public @ResponseBody HttpEntity<byte[]> getAdvImage(@RequestParam("advId") String advId){
		int advImageId = Integer.parseInt(advId);

		Image img = dbMgr.getImage(advImageId);

		if (img != null){
			HttpHeaders headers = new HttpHeaders();
			// TODO Save the content type during upload ?
	        headers.setContentType(MediaType.IMAGE_JPEG);
	        headers.setContentLength(img.getImage().length);
	        return new HttpEntity<byte[]>(img.getImage(), headers);
		} else {
			//TODO How about sending some default image ?
			return null;
		}
	}
}
