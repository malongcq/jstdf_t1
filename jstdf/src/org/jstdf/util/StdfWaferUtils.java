package org.jstdf.util;

import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;

import org.jstdf.PartResultSet;
import org.jstdf.record.PartResultsRecord;
import org.jstdf.record.WaferConfigurationRecord;


/**
 * General utilities for wafer data generation.
 * 
 * @author malong
 *
 */
public class StdfWaferUtils 
{
	private StdfWaferUtils() {}
	
	/**
	 * Create Hard/Soft bin wafermap.
	 * 
	 * @param wafermap the wafer part test results. 
	 * @param softBin true to use soft bin, false to use hard bin.
	 * @param wcr WaferConfigurationRecord used to create wafermap, can be null.
	 * @return the bin wafermap in 2D array.
	 */
	public static final int[][] createWafermapBin2DArray(
			Map<Point, PartResultSet> wafermap,
			boolean softBin,
			WaferConfigurationRecord wcr)
	{
		int x_f = wcr==null ? 1 : wcr.getDirectionFactorX();
		int y_f = wcr==null ? 1 : wcr.getDirectionFactorY();
		
		int max_x = Integer.MIN_VALUE, min_x = Integer.MAX_VALUE;
		int max_y = Integer.MIN_VALUE, min_y = Integer.MAX_VALUE;
		for(Point p : wafermap.keySet())
		{
			int xx = p.x * x_f;
			int yy = p.y * y_f;
			
			max_x = xx > max_x ? xx : max_x;
			max_y = yy > max_y ? yy : max_y;
			min_x = xx < min_x ? xx : min_x;
			min_y = yy < min_y ? yy : min_y;
		}
		
		int[][] wmap = new int[max_y-min_y+1][max_x-min_x+1];
		for(int y=0;y<wmap.length;y++) for(int x=0;x<wmap[y].length;x++) wmap[y][x] = -1;
		
		for(PartResultSet part : wafermap.values())
		{
			PartResultsRecord prr = part.getPartResultsRecord();
			int xx = prr.X_COORD * x_f - min_x;
			int yy = prr.Y_COORD * y_f - min_y;
			wmap[yy][xx] = softBin ? prr.SOFT_BIN : prr.HARD_BIN;
		}
		
		return wmap;
	}
	
	/**
	 * Create Hard/Soft bin count summary.
	 * 
	 * @param wafermap the wafer part test results. 
	 * @param softBin true to use soft bin, false to use hard bin.
	 * @return a map stored bin-count pair of data.
	 */
	public Map<Integer, Integer> createWafermapBinCountSummary(
			Map<Point, PartResultSet> wafermap,
			boolean softBin)
	{
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		for(PartResultSet part : wafermap.values())
		{
			PartResultsRecord prr = part.getPartResultsRecord();
			int bin = softBin ? prr.SOFT_BIN : prr.HARD_BIN;
			Integer cnt = map.get(bin);
			if(cnt==null) cnt = 0;
			map.put(bin, ++cnt);
		}
		
		return map;
	}	
}