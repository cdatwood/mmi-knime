package com.mmiagency.knime.nodes.moz.api.example;


import com.mmiagency.knime.nodes.moz.api.authentication.Authenticator;
import com.mmiagency.knime.nodes.moz.api.constants.LinksConstants;
import com.mmiagency.knime.nodes.moz.api.response.LinksResponse;
import com.mmiagency.knime.nodes.moz.api.response.UrlResponse;
import com.mmiagency.knime.nodes.moz.api.service.LinksService;
import com.mmiagency.knime.nodes.moz.api.service.URLMetricsService;
import com.google.gson.Gson;

/**
 * A sample class to show how to invoke the various methods
 * 
 * @author Radeep Solutions
 *
 */
public class Sample 
{
	public static void main(String args[])
	{
		String objectURL = "www.seomoz.org";
		
		//Add your accessID here
		String accessID = "mozscape-8d460a7941";
		
		//Add your secretKey here
		String secretKey = "4fdd8625365ba3e89272da0027b91700";
		
		Authenticator authenticator = new Authenticator();
		authenticator.setAccessID(accessID);
		authenticator.setSecretKey(secretKey);

        if (false) {
            URLMetricsService urlMetricsService = new URLMetricsService(authenticator);
            String response = urlMetricsService.getUrlMetrics(objectURL);
            Gson gson = new Gson();
            UrlResponse res = gson.fromJson(response, UrlResponse.class);
            System.out.println(res);
            //{"ut":"SEO Blog | SEOmoz Blog Featuring Search Engine Marketing News &amp; Tips","uu":"www.seomoz.org/blog"}
        }

		LinksService linksService = new LinksService();
		linksService.setAuthenticator(authenticator);
		String response = linksService.getLinks(objectURL, LinksConstants.LINKS_SCOPE_PAGE_TO_PAGE, null, LinksConstants.LINKS_SORT_PAGE_AUTHORITY, LinksConstants.LINKS_COL_URL, 0, 10);
		Gson gson = new Gson();
		LinksResponse links[] = gson.fromJson(response, LinksResponse[].class);
		
		for(int i =0 ; i < links.length; i++) {
			System.out.println(links[i]);
		}

		/*
		AnchorTextService anchorTextService = new AnchorTextService(authenticator);
		String response = anchorTextService.getAnchorText(objectURL, 
				AnchorTextConstants.ANCHOR_SCOPE_TERM_TO_PAGE, AnchorTextConstants.ANCHOR_SORT_DOMAINS_LINKING_PAGE,
				AnchorTextConstants.ANCHOR_COL_TERM_OR_PHRASE + AnchorTextConstants.ANCHOR_COL_INTERNAL_PAGES_LINK);
		Gson gson = new Gson();
		AnchorTextResponse anchors[] = gson.fromJson(response, AnchorTextResponse[].class);
		
		for(int i =0 ; i < anchors.length; i++)
		{
			System.out.println(anchors[i]);
		}
		//[{"atut":"SEOmoz"},{"atut":"Blog"},{"atut":"SEO"}]
		*/
		
		/*
		TopPagesService topPagesService = new TopPagesService(authenticator);
		String response = topPagesService.getTopPages(objectURL, TopPagesConstants.TOPPAGES_COL_ALL, 0, 3);
		Gson gson = new Gson();
		TopPagesResponse topPages[] = gson.fromJson(response, TopPagesResponse[].class);
		
		for(int i =0 ; i < topPages.length; i++)
		{
			System.out.println(topPages[i]);
		}
		//[{"uu":"www.seomoz.org/"},{"uu":"www.seomoz.org/blog"},{"uu":"www.seomoz.org/tools"}]
		*/
	}
}
