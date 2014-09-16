package com.example.xmlcdataparsing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class XMLCDATAParsingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmlcdataparsing);
		
		final TextView res=(TextView)findViewById(R.id.res);
		try 
		{
			HttpClient client=new DefaultHttpClient();
			HttpGet request=new HttpGet("http://www.cbc.ca/cmlink/rss-technology");
			HttpResponse response=client.execute(request);
			
			HttpEntity entity=response.getEntity();
			String xml=EntityUtils.toString(entity);
			
			System.out.println(xml);
			
			DocumentBuilder docBuilder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is=new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc=docBuilder.parse(is);
			
			/*<title>
			<![CDATA[ CBC | Technology News ]]>
			</title>*/
			
			Element channel=(Element)doc.getElementsByTagName("channel").item(0);
			Element title=(Element)channel.getElementsByTagName("title").item(0);
			
			//inside title-tag ..... there is another tag <![CDATA[   ]]>.... which typecast into CharacterData from Node
			CharacterData titleCDATA=(CharacterData)title.getChildNodes().item(0);
			
			res.setText(titleCDATA.getData());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmlcdataparsing, menu);
		return true;
	}

}
