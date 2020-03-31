package ru.cetelem.cassiope.supplier.io.icfl;

import java.util.Date;

public class ICflHeader implements ICFLEntry {

	/*
	Record Id		Header record identifier	Alphabet(02)
	H-BATCH-DATE	File generation date		DDMMYYYY
	Sequence Number	Sequence number				Numeric(05)
	Creator System	Generator system identifier	Alphabet(07)
	Filler			Spaces						Alphabet(116)
	Creator Company	Creator company identifier	Alphabet(02)
	*/

	public String recordId = "AA";
	public Date batchDate;
	public long sequnceNumber;
	public String createrSystem;
	public String filler;
	public String createrCompany;
	public String filler2;

}
