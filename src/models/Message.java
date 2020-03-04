package models;

import java.io.Serializable;

public class Message implements Serializable
{
	public MessageType type;
	public Object extra;
	
	public Message (MessageType type)
	{
		this.type = type;
	}
	
	public Message(MessageType type, Object extra) 
	{
		this (type);
		
		this.extra = extra;
	}
	
	@Override
	public String toString() 
	{
		return type + " - " + extra;
	}
}
