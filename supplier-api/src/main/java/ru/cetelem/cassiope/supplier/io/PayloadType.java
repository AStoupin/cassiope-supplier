package ru.cetelem.cassiope.supplier.io;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public enum PayloadType {

	// @formatter:off
	CFL ( Direction.OUT, "CFL desc", "cfl-mapping.xml" , "cfl-stream" ),
	F120( Direction.OUT, "F120 desc", "f120-mapping.xml" , "f120-stream" ),
	F150( Direction.OUT, "F150 desc", "f150-mapping.xml" , "f150-stream" ),
	F910( Direction.IN , "F910 desc", "f910-mapping.xml" , "f910-stream" ),
	F920( Direction.IN , "F920 desc", "f920-mapping.xml" , "f920-stream" ),
	F940( Direction.IN , "F940 desc", "f940-mapping.xml" , "f940-stream" ),
	F950( Direction.IN , "F950 desc", "f950-mapping.xml" , "f950-stream" ),
	ICFL( Direction.IN , "ICFL desc", "icfl-mapping.xml" , "icfl-stream" ),
	/* to be continued */
	;
	// @formatter:on
	
	public final Direction direction;
	public final String description;
	public final String mapping;
	public final String streamName;

	private PayloadType(
	    Direction direction,
	    String description,
	    String mapping,
	    String streamName
	) {
		this.direction = direction;
		this.description = description;
		this.mapping = mapping;
		this.streamName = streamName;
	}

	public enum Direction {
		IN, OUT;
	}
	
	public static HashSet<PayloadType> getHashSet(Direction direction) {
		HashSet<PayloadType> hashSet = new HashSet<PayloadType>();
		List<PayloadType> list = Arrays.asList(PayloadType.values());		
		list.stream()
		    .filter(item -> direction.equals(item.direction))
		    .forEach(item -> hashSet.add(item));
		return hashSet;		
	}

	public static List<PayloadType> getList(Direction direction) {
		return Arrays.asList(
				PayloadType.values()).stream()
					.filter(item -> direction.equals(item.direction))
					.collect(Collectors.toList());		
	}

}
