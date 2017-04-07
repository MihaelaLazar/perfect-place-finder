package com.sgbd;

import java.sql.*;

public class OracleCon {

    public OracleCon() {
    }

    public void  connectToDB(){
        try{
            //step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //step2 create  the connection object
            Connection con=DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe","raluca","raluca");

            //step3 create the statement object
            Statement stmt=con.createStatement();

            //step4 execute query
            ResultSet rs=stmt.executeQuery("select * from userdfgs");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

            //step5 close the connection object
            con.close();

        }catch(Exception e){ System.out.println(e);}

    }

    public void addUser(String[] fields) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe","raluca","raluca");
        Statement stmt=con.createStatement();

        String query = "insert into users(id, first_name, last_name, user_password,email, username, user_role) values( (select MAX(ID)+ 1 from users), ";
        String username ="";
        for (String field: fields) {
            String postDataCopy = "";
            for (int index = 1; index < field.length() -1; index++) {
                postDataCopy += field.charAt(index);
            }

            String nameAndValue[] = postDataCopy.split("\":\"");
            if(nameAndValue[0].equals("first-name")){
                username = nameAndValue[1].toLowerCase();
            }
            query += "'"+ nameAndValue[1] + "' ,";
           // System.out.println(nameAndValue[0] + " " + nameAndValue[1]);
        }
        query += "'" + username + "','user')";
        ResultSet rs=stmt.executeQuery(query);
    }
}