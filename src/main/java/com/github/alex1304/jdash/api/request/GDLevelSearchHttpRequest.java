package com.github.alex1304.jdash.api.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.alex1304.jdash.api.GDHttpRequest;
import com.github.alex1304.jdash.api.GDHttpResponseBuilder;
import com.github.alex1304.jdash.component.GDComponentList;
import com.github.alex1304.jdash.component.GDLevelPreview;
import com.github.alex1304.jdash.component.GDSong;
import com.github.alex1304.jdash.component.property.GDLevelLength;
import com.github.alex1304.jdash.util.Constants;
import com.github.alex1304.jdash.util.Utils;

/**
 * HTTP request to search for levels
 * 
 * @author Alex1304
 */
public class GDLevelSearchHttpRequest extends GDHttpRequest<GDComponentList<GDLevelPreview>> {
	
	public GDLevelSearchHttpRequest(String keywords, int page) {
		this(
			Constants.LEVEL_SEARCH_TYPE_REGULAR,
			keywords,
			new HashSet<>(),
			new HashSet<>(),
			page,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			0
		);
	}

	public GDLevelSearchHttpRequest(int type, String keywords, Set<Integer> difficulties, Set<Integer> lengths,
			int page, boolean uncompleted, boolean onlyCompleted, boolean featured, boolean original,
			boolean twoPlayer, boolean coins, boolean epic, boolean star, int demonFilter) {
		super("/getGJLevels21.php", false);
		this.getParams().put("type", "" + type);
		try {
			this.getParams().put("str", URLEncoder.encode(keywords, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			this.getParams().put("str", keywords);
			System.err.println("Unable to URLEncode search terms '" + keywords + "'");
		}
		this.getParams().put("diff", difficulties.isEmpty() ? "-" : Utils.setOfIntToString(difficulties));
		this.getParams().put("len", lengths.isEmpty() ? "-" : Utils.setOfIntToString(lengths));
		this.getParams().put("page", "" + page);
		this.getParams().put("total", "0");
		this.getParams().put("uncompleted", uncompleted ? "1" : "0");
		this.getParams().put("onlyCompleted", onlyCompleted ? "1" : "0");
		this.getParams().put("featured", featured ? "1" : "0");
		this.getParams().put("original", original ? "1" : "0");
		this.getParams().put("twoPlayer", twoPlayer ? "1" : "0");
		this.getParams().put("coins", coins ? "1" : "0");
		this.getParams().put("epic", epic ? "1" : "0");
		this.getParams().put("star", star ? "1" : "0");
		this.getParams().put("demonFilter", "" + demonFilter);
	}

	@Override
	public GDHttpResponseBuilder<GDComponentList<GDLevelPreview>> responseBuilderInstance() {
		return response -> {
			if (response.equals("-1"))
				return new GDComponentList<>();
			
			GDComponentList<GDLevelPreview> lvlPrevList = new GDComponentList<>();

			String[] split1 = response.split("#");
			String levels = split1[0];
			String creators = split1[1];
			String songs = split1[2];

			Map<Long, String> structuredCreatorsInfo = structureCreatorsInfo(creators);
			Map<Long, GDSong> structuredSongsInfo = structureSongsInfo(songs);
			String[] levelArray = levels.split("\\|");

			for (int i = 0; i < levelArray.length; i++) {
				String l = levelArray[i];

				Map<Integer, String> lmap = Utils.splitToMap(l, ":");
				
				GDSong song = Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_AUDIO_TRACK)) > 0 ?
						Utils.getAudioTrack(Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_AUDIO_TRACK))):
						structuredSongsInfo.get(Long.parseLong(lmap.get(Constants.INDEX_LEVEL_SONG_ID)));
						
				String creatorName = structuredCreatorsInfo.get(Long.parseLong(
						lmap.get(Constants.INDEX_LEVEL_CREATOR_ID)));

				lvlPrevList.add(new GDLevelPreview(Long.parseLong(lmap.get(Constants.INDEX_LEVEL_ID)),
					lmap.get(Constants.INDEX_LEVEL_NAME),
					creatorName == null ? "-" : creatorName,
					Constants.VALUE_TO_DIFFICULTY
							.apply(Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_DIFFICULTY))),
					Constants.VALUE_TO_DEMON_DIFFICULTY
							.apply(Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_DEMON_DIFFICULTY))),
					Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_STARS)),
					song,
					Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_FEATURED_SCORE)),
					lmap.get(Constants.INDEX_LEVEL_IS_EPIC).equals("1"),
					Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_DOWNLOADS)),
					Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_LIKES)),
					GDLevelLength.values()[Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_LENGTH))],
					Integer.parseInt(lmap.get(Constants.INDEX_LEVEL_COIN_COUNT)),
					lmap.get(Constants.INDEX_LEVEL_COIN_VERIFIED).equals("1"),
					lmap.get(Constants.INDEX_LEVEL_IS_DEMON).equals("1"),
					lmap.get(Constants.INDEX_LEVEL_IS_AUTO).equals("1")));
			}

			return lvlPrevList;
		};
	}
	
	/**
	 * Parses the String representing level creators into a Map that associates
	 * the creator ID with their name
	 * 
	 * @param creatorsInfoRD
	 *            - the String representing the creators
	 * @return a Map of Long, String
	 */
	private static Map<Long, String> structureCreatorsInfo(String creatorsInfoRD) {
		if (creatorsInfoRD.isEmpty())
			return null;
		
		String[] arrayCreatorsRD = creatorsInfoRD.split("\\|");
		Map<Long, String> structuredCreatorsInfo = new HashMap<>();
		
		for (String creatorRD : arrayCreatorsRD) {
			structuredCreatorsInfo.put(Long.parseLong(creatorRD.split(":")[0]), creatorRD.split(":")[1]);
		}
		
		return structuredCreatorsInfo;
	}
	
	/**
	 * Parses the String representing level songs into a Map that associates
	 * the song ID with their title
	 * 
	 * @param songsInfoRD
	 *            - the String representing the songs
	 * @return a Map of Long, String
	 */
	private static Map<Long, GDSong> structureSongsInfo(String songsInfoRD) {
		if (songsInfoRD.isEmpty())
			return new HashMap<>();

		String[] arraySongsRD = songsInfoRD.split("~:~");
		Map<Long, GDSong> result = new HashMap<>();

		for (String songRD : arraySongsRD) {
			Map<Integer, String> songMap = Utils.splitToMap(songRD, "~\\|~");
			long songID = Long.parseLong(songMap.get(Constants.INDEX_SONG_ID));
			String songTitle = songMap.get(Constants.INDEX_SONG_TITLE);
			String songAuthor = songMap.get(Constants.INDEX_SONG_AUTHOR);
			String songSize = songMap.get(Constants.INDEX_SONG_SIZE);
			String songURL = songMap.get(Constants.INDEX_SONG_URL);
			try {
				songURL = URLDecoder.decode(songURL, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			result.put(songID, new GDSong(songID, songAuthor, songSize, songTitle, songURL, true));
		}

		return result;
	}

}
