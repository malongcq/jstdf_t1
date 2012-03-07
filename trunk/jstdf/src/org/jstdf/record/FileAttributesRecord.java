package org.jstdf.record;

import java.nio.ByteBuffer;

import org.jstdf.util.StdfRecordUtils;


/**
 * File Attributes Record (FAR)
 * 
 * @author malong
 *
 */
public class FileAttributesRecord extends AbstractSTDFRecord
{
	public static final byte Type = 0;
	public static final byte SubType = 10;
	
	/**
	 * CPU type that wrote this file
	 */
	public int CPU_TYPE;
	/**
	 * STDF version number
	 */
	public int STDF_VER;
	
	@Override
	public void readContent(ByteBuffer bb)
	{
		CPU_TYPE = StdfRecordUtils.readU1Int(bb);
		STDF_VER = StdfRecordUtils.readU1Int(bb);
	}

	public static final FileAttributesRecord getInstance(int seq, int len, int typ, int sub, ByteBuffer bb)
	{
		FileAttributesRecord rec = new FileAttributesRecord();
		rec.setHeadInfo(seq, len, typ, sub);
		rec.readContent(bb);
		return rec;
	}
	
	@Override
	public STDFRecordType getRecordType()
	{
		return STDFRecordType.FAR;
	}
}
