var tmp = Handlebars.compile(template);

var context = {message:message};

return tmp(message);