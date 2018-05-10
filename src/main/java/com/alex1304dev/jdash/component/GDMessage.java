package com.alex1304dev.jdash.component;

/**
 * Represents a private message in Geometry Dash
 * 
 * @author Alex1304
 */
public class GDMessage implements GDComponent {
	
	private long messageID;
	private long senderID;
	private String senderName;
	private String subject;
	private String body;
	private boolean isRead;
	private String timestamp;
	
	/**
	 * @param messageID
	 *            - the unique ID of the message
	 * @param senderID
	 *            - the ID of the user who sent the message
	 * @param senderName
	 *            - the name of the user who sent the message
	 * @param subject
	 *            - the subject of the message
	 * @param body
	 *            - the subject of the message
	 * @param isRead
	 *            - whether the message is marked as read
	 * @param timestamp
	 *            - the timestamp of the message
	 */
	public GDMessage(long messageID, long senderID, String senderName, String subject, String body, boolean isRead,
			String timestamp) {
		this.messageID = messageID;
		this.senderID = senderID;
		this.senderName = senderName;
		this.subject = subject;
		this.body = body;
		this.isRead = isRead;
		this.timestamp = timestamp;
	}
	
	/**
	 * Gets the unique ID of the message
	 * 
	 * @return long
	 */
	public long getMessageID() {
		return messageID;
	}

	/**
	 * Gets the ID of the user who sent the message
	 * 
	 * @return long
	 */
	public long getSenderID() {
		return senderID;
	}

	/**
	 * Gets the ID of the user who sent the message
	 * 
	 * @return long
	 */
	public String getSenderName() {
		return senderName;
	}
	
	/**
	 * Gets the subject of the message
	 * 
	 * @return String
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * Gets the body of the message
	 * 
	 * @return String
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Gets whether the message is marked as read
	 * 
	 * @return boolean
	 */
	public boolean isRead() {
		return isRead;
	}
	
	/**
	 * Gets the timestamp of the message
	 * 
	 * @return String
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GDMessage [messageID=" + messageID + ", senderID=" + senderID + ", senderName=" + senderName
				+ ", subject=" + subject + ", body=" + body + ", isRead=" + isRead + ", timestamp=" + timestamp + "]";
	}
	
	
}