package fr.seb.base;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Session;

@Import(library = { "context:js/lib/highcharts/prototype-adapter.js", "context:js/lib/highcharts/highcharts.js" }, stylesheet = "context:css/chart.css")
public class HighchartsComponent {

	@Environmental
	protected JavaScriptSupport javaScriptSupport;

	@Inject
	protected Session session;

}
