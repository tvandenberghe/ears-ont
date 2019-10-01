**The basic EARS webservices : Module ears.**

------

**Project description :** To do.

------

**Installation**

1. Install Java : Oracle Corporation (jre-jdk) 1.8.0_172-b11 or later.
2. Install Tomcat 902 or later

In Tomcat installation directory \webapps\manager\WEB-INF\web.xml   file

Modified content  like 

```
   <multipart-config>

​      <!-- 50MB max -->

​      <max-file-size>52428800</max-file-size>

​      <max-request-size>52428800</max-request-size>

​      <file-size-threshold>0</file-size-threshold>

​    </multipart-config>
```

 

```
Replace by

​    <multipart-config>

​      <!-- 50MB max 

​      <max-file-size>52428800</max-file-size>

​      <max-request-size>52428800</max-request-size>

​      <file-size-threshold>0</file-size-threshold>-->

​              <max-file-size>92428800</max-file-size>

​      <max-request-size>92428800</max-request-size>

​      <file-size-threshold>0</file-size-threshold>

​              

​    </multipart-config>
```









In Tomcat installation directory \conf\tomcat-users.xml file

add 

```
<role rolename="manager-gui"/>

<user password="XYZ" roles="manager-gui" username="XYZ"/>
```



In Tomcat installation directory put folder with content

1. https://github.com/tvandenberghe/ears/tree/master/toTomcat/ears
2. https://github.com/tvandenberghe/ears/tree/master/toTomcat/var





1. Install one database : 

- Oracle database XE  18c or later (connector :ojdbc8-12.2.0.1)

Create User casino with password casino

-- USER SQL
ALTER USER casino
DEFAULT TABLESPACE "USERS"
TEMPORARY TABLESPACE "TEMP"
ACCOUNT UNLOCK ;

-- QUOTAS
ALTER USER casino QUOTA UNLIMITED ON "USERS";

-- ROLES
ALTER USER casino DEFAULT ROLE "CONNECT","RESOURCE";

-- SYSTEM PRIVILEGES



Process DDL script:

https://github.com/tvandenberghe/ears/blob/master/databaseScript/exportCasinoForOracleDatabase.sql

  



- MySQL Server 8.0.16 or later (connector :mysql-connector-java -8.0.11 )
- MariaDB 10.4.8 or later (connector: mariadb243 -2.4.3)
- Sql Server 2017 Express  or later (connector: mssql-jdbc - 7.4.1.jre11 )
- PostgreSQL 12 or later (connector: postgresql-42.2.8 )

