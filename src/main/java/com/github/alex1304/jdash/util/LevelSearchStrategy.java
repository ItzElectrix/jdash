package com.github.alex1304.jdash.util;

public enum LevelSearchStrategy {
	REGULAR(0),
	MOST_DOWNLOADED(1),
	MOST_LIKED(2),
	TRENDING(3),
	RECENT(4),
	BY_USER(5),
	FEATURED(6),
	MAGIC(7),
	AWARDED(11),
	FOLLOWED(12),
	HALL_OF_FAME(16);
	
	private final int val;
	
	LevelSearchStrategy(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}
}
