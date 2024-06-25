-- appointedMember table create
DROP TABLE IF EXISTS appointedMember;
CREATE TABLE appointedMember (
    name varchar(255) not null,
    regNo varchar(255) not null
);
-- taxStandardInfo table create

DROP TABLE IF EXISTS taxStandardInfo;
CREATE TABLE taxStandardInfo(
    id bigint not null auto_increment PRIMARY KEY,
    taxBaseMin int not null,
    taxBaseMax int not null,
    standardAmount int not null,
    extraPercent int not null
);