function timestampHMTickFormatter(inputTimeStamp) {
	mydate = new Date();
	mydate.setTime(inputTimeStamp);
	var curr_date = mydate.getDate();
    var curr_month = mydate.getMonth() + 1; //Months are zero based
    var curr_year = mydate.getFullYear();
	return curr_date + "/" + curr_month + "/" + curr_year + " " + mydate.getHours() +":" + mydate.getMinutes() + ":" + mydate.getSeconds();
}