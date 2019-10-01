**The basic EARS webservices : Module ears.**

------

**Project description :** To do.

------

**Installation**

1. Install Java (jre-jdk) 8 or later.
2. Install one database : 

- Oracle database XE  18c or later (connector :ojdbc8-12.2.0.1)

Create User XY:

-- USER SQL
ALTER USER "XY"
DEFAULT TABLESPACE "USERS"
TEMPORARY TABLESPACE "TEMP"
ACCOUNT UNLOCK ;

-- QUOTAS
ALTER USER "XY" QUOTA UNLIMITED ON "USERS";

-- ROLES
ALTER USER "XY" DEFAULT ROLE "CONNECT","RESOURCE";

-- SYSTEM PRIVILEGES

- MySQL Server 8.0.16 or later (connector :mysql-connector-java -8.0.11 )
- MariaDB 10.4.8 or later (connector: mariadb243 -2.4.3)
- Sql Server 2017 Express  or later (connector: mssql-jdbc - 7.4.1.jre11 )
- PostgreSQL 12 or later (connector: postgresql-42.2.8 )

