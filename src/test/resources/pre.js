//Declare a Java Type
var SimpleDateFormat =  Java.type("java.text.SimpleDateFormat");

//Create an instance.
var dateFormatter = new SimpleDateFormat(messageFormat);

var preResponse =  'Processed:' + new Date().getTime() + ' \n-----------------------\n';