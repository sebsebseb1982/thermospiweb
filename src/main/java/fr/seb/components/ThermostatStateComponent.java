package fr.seb.components;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import fr.seb.base.HighchartsComponent;

public class ThermostatStateComponent extends HighchartsComponent {

	@Parameter(required = true)
	@Property
	private String id;

	@Parameter(required = true)
	@Property
	private String title;

	@AfterRender
	public void afterRender() {

		StringBuilder javascript = new StringBuilder();

		javascript.append("var ").append(id).append(" = new Highcharts.Chart({");
		javascript.append("		chart: {");
		javascript.append("			renderTo: '").append(id).append("',");
		javascript.append("			plotBackgroundColor: null,");
		javascript.append("			plotBorderWidth: null,");
		javascript.append("			plotShadow: false");
		javascript.append("		},");
		javascript.append("		title: {");
		javascript.append("			text: '").append(title).append("'");
		javascript.append("		},");
		javascript.append("		tooltip: {");
		javascript.append("			pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'");
		javascript.append("		},");
		javascript.append("		plotOptions: {");
		javascript.append("			pie: {");
		javascript.append("				allowPointSelect: true,");
		javascript.append("				cursor: 'pointer',");
		javascript.append("				dataLabels: {");
		javascript.append("					enabled: true,");
		javascript.append("					color: '#000000',");
		javascript.append("					connectorColor: '#000000',");
		javascript.append("					format: '<b>{point.name}</b>: {point.percentage:.1f} %'");
		javascript.append("				}");
		javascript.append("			}");
		javascript.append("		},");
		javascript.append("		series: [{");
		javascript.append("			type: 'pie',");
		javascript.append("			name: 'Browser share',");
		javascript.append("			data: [");
		javascript.append("				['Firefox',   45.0],");
		javascript.append("				['IE',       26.8],");
		javascript.append("				{");
		javascript.append("					name: 'Chrome',");
		javascript.append("					y: 12.8,");
		javascript.append("					sliced: true,");
		javascript.append("					selected: true");
		javascript.append("				},");
		javascript.append("				['Safari',    8.5],");
		javascript.append("				['Opera',     6.2],");
		javascript.append("				['Others',   0.7]");
		javascript.append("			]");
		javascript.append("		}]");
		javascript.append("});");

		javaScriptSupport.addScript(javascript.toString());
	}

}
