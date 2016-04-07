
var response = 'Hey ' + message.to + ', '  + managerName + ' is on my butt about the TODO list.\n';


response += '' + message.getMessage() + '\n';

Java.from(tasks).forEach(function(item){
	response += '\t' + item + '\n';
});

response += '\nDue Date: ' + dateFormatter.format(message.getDate());

