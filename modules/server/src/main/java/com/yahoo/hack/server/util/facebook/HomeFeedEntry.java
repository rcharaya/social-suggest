package com.yahoo.hack.server.util.facebook;

/**
 * Created by IntelliJ IDEA.
 * User: vikashk
 * Date: 10/9/11
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
import java.util.List;

public class HomeFeedEntry {

	public final String TYPE_LINK = "link";
	public final String TYPE_PHOTO = "photo";
	public final String TYPE_STATUS = "status";
	public final String TYPE_VIDEO = "video";

	public String getTYPE_VIDEO() {
		return TYPE_VIDEO;
	}

	private String description;		//contains text for keywords
	private String caption;			//contains text for keywords
	private String id;
	private String icon;
	private String name;			//contains text for keywords
	private String message;			//contains text for keywords
	private String picture;
	private String attribution;
	private String updated_time;
	private String created_time;
	private String link;
	private String type; // link, photo, status, video
	private String story;			//contains text for keyword

	// Nested classes for From entries
	private HomeFeedEntryFrom from;

	//Nested classes for Likes entries
	private HomeFeedEntryLikes likes;

	// Nested classes for Application entries
	private HomeFeedApplication application;

	public HomeFeedApplication getApplication() {
		return application;
	}

	// No Arg constructor
	public HomeFeedEntry() {
	}

	public String getTYPE_LINK() {

		return TYPE_LINK;
	}

	public String getTYPE_PHOTO() {
		return TYPE_PHOTO;
	}

	public String getTYPE_STATUS() {
		return TYPE_STATUS;
	}

	public String getCaption() {
		return caption;
	}

	public String getId() {
		return id;
	}

	public String getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public String getPicture() {
		return picture;
	}

	public String getAttribution() {
		return attribution;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public String getCreated_time() {
		return created_time;
	}

	public String getType() {
		return type;
	}

	public HomeFeedEntryFrom getFrom() {
		return from;
	}

	public String getDescription() {
		return description;
	}

	public String getStory() {
		return story;
	}

	public HomeFeedEntryLikes getLikes() {
		return likes;
	}

	public String getLink() {
		return link;
	}
	// Getter methods should be declared. Setter methods are optional
}
