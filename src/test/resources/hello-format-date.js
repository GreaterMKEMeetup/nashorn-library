//Declare a Java Type
var SimpleDateFormat =  Java.type("java.text.SimpleDateFormat");

//Create an instance.
var dateFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss:SSS");

var formattedDate = dateFormatter.format(message.getDate());

return message.getMessage() + ": Hey " + message.to + " get back to me by " + formattedDate + '.';