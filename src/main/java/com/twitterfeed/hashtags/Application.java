package com.twitterfeed.hashtags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
/**
 * 
 *
 */
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import twitter4j.DirectMessage;
import twitter4j.EntitySupport;
import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Application {

	public static Twitter getTwitterinstance() throws TwitterException {
		/**
		 * if not using properties file, we can set access token by following way
		 */
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("IsXNpBLxH4m2Wb1OadRvIXhcr")
				.setOAuthConsumerSecret("kfti1Ol4tKXw8vfqHewVg8MkwQjKDT9OQuCOA4e1dqnXzIf0UV")
				.setOAuthAccessToken("928605213539287040-OPx5JzKbohTigj9cDJi80q18khsrI3S")
				.setOAuthAccessTokenSecret("soDEs6h0JO6dbI4pQOnMDt9xVHCtpbR2KJ3IDBIQYOtZb");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getSingleton();
		return twitter;

	}

	public static String createTweet(String tweet) throws TwitterException {
		Twitter twitter = getTwitterinstance();
		Status status = twitter.updateStatus("creating anushree API");
		return status.getText();
	}

	public static Trends getTrendingTopics() throws TwitterException {
		Twitter twitter = getTwitterinstance();
		Trends trends = twitter.getPlaceTrends(2282863);
		// India 2282863
		// world 1
		// Hyderabad 2295414
		int count = 0;
		for (Trend trend : trends.getTrends()) {
			if (count < 10) {
				System.out.println(trend.getName());
				count++;
			}
		}
		return trends;
	}

	public static List<Object> getTimeLine() throws TwitterException {
		Twitter twitter = getTwitterinstance();
		List<Status> statuses = twitter.getHomeTimeline();
		Map<String, Integer> mapHashTags = new HashMap<String, Integer>();

		System.out.println("Hash Tags: ");
		for (Status status : statuses) {
			HashtagEntity[] hashtagsEntities = status.getHashtagEntities();

			for (HashtagEntity hashtag : hashtagsEntities) {
				if (mapHashTags.containsKey(hashtag.getText())) {
					int count = mapHashTags.get(hashtag.getText());
					mapHashTags.put("#" + hashtag.getText(), ++count);
				} else {
					mapHashTags.put("#" + hashtag.getText(), 1);
				}
			}
		}

		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(mapHashTags.entrySet());

		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		if (list.size() <= 10) {
			System.out.println(list);
		} else {
			System.out.println("Trending #tags are more than 10");
		}

		return statuses.stream().map(item -> item.getText()).collect(Collectors.toList());
	}

	public static List<Object> getUserTimeLine() throws TwitterException {
		Twitter twitter = getTwitterinstance();
		List<Status> statuses = twitter.getUserTimeline();
		for (Status each : statuses) {

			System.out.println("Sent by: @" + each.getUser().getScreenName() + " - " + each.getUser().getName() + "\n"
					+ each.getText() + "\n");
		}
		return statuses.stream().map(item -> item.getText()).collect(Collectors.toList());
	}

	public static String sendDirectMessage(String recipientName, String msg) throws TwitterException {
		Twitter twitter = getTwitterinstance();
		DirectMessage message = twitter.sendDirectMessage(recipientName, msg);
		return message.getText();
	}

	public static List<String> searchtweets() throws TwitterException {
		Twitter twitter = getTwitterinstance();
		Query query = new Query("source:twitter4j anushrishinde6");
		System.out.println(query);
		QueryResult result = twitter.search(query);
		List<Status> statuses = result.getTweets();
		for (Status each : statuses) {

			System.out.println("Sent by: @" + each.getUser().getScreenName() + " - " + each.getUser().getName() + "\n"
					+ each.getText() + "\n");
		}
		return statuses.stream().map(item -> item.getText()).collect(Collectors.toList());
	}
	
	
	
	public static void main(String[] args) throws TwitterException {

		// getUserTimeLine();
		getTimeLine();
		// getTrendingTopics();
	}


	

}