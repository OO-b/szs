-- appointedMember table insert
INSERT INTO appointedMember (name, regNo) VALUES ('동탁', '921108-1582816');
INSERT INTO appointedMember (name, regNo) VALUES ('관우', '681108-1582816');
INSERT INTO appointedMember (name, regNo) VALUES ('손권', '890601-2455116');
INSERT INTO appointedMember (name, regNo) VALUES ('유비', '790411-1656116');
INSERT INTO appointedMember (name, regNo) VALUES ('조조', '810326-2715702');

-- appointedMember table insert
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (0, 14000000, 0, 6);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (14000000, 50000000, 840000, 15);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (50000000, 88000000, 6240000, 24);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (88000000, 150000000, 1536000, 35);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (150000000, 300000000, 3706000, 38);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (300000000, 500000000, 9406000, 40);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (500000000, 1000000000, 1740600, 42);
INSERT INTO taxStandardInfo (taxBaseMin, taxBaseMax, standardAmount, extraPercent) VALUES (1000000000, 999999999, 3840600, 45);
