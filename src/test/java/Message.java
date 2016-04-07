import java.util.Date;

public class Message
{

	private String message;
	private Date date;
	private String to;
	private String from;


	public Message(String message, Date date, String to, String from)
	{
		this.message = message;
		this.date = date;
		this.to = to;
		this.from = from;
	}


	public String getMessage()
	{
		return message;
	}


	public void setMessage(String message)
	{
		this.message = message;
	}


	public Date getDate()
	{
		return date;
	}


	public void setDate(Date date)
	{
		this.date = date;
	}


	public String getTo()
	{
		return to;
	}


	public void setTo(String to)
	{
		this.to = to;
	}


	public String getFrom()
	{
		return from;
	}


	public void setFrom(String from)
	{
		this.from = from;
	}
}
