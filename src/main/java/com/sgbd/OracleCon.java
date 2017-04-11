package com.sgbd;

import com.sgbd.model.CitiesDTO;
import com.sgbd.model.Estate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleCon {

    private static OracleCon oracleCon;

    public static OracleCon getOracleCon() {
        if (oracleCon == null) {
            oracleCon = new OracleCon();
        }
        return oracleCon;
    }

    private OracleCon() {
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
        }
        query += "'" + username + "','user')";
        ResultSet rs=stmt.executeQuery(query);
    }

    public CitiesDTO getEstates(int startPosition, int draw,String[] filters) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe","raluca","raluca");
        Statement stmt=con.createStatement();
        String queryFilters = " ";
        filters[0] = filters[0].trim();
        filters[1] = filters[1].trim();
        filters[2] = filters[2].trim();
        filters[3] = filters[3].trim();
        filters[4] = filters[4].trim();
        filters[5] = filters[5].trim();
        filters[6] = filters[6].trim();
        filters[7] = filters[7].trim();
        filters[8] = filters[8].trim();
        filters[9] = filters[9].trim();
        filters[10] = filters[10].trim();
        filters[11] = filters[11].trim();
        filters[12] = filters[12].trim();
        if (filters[0].length() > 0) {
            queryFilters += "id = " + filters[0] + " and ";
        }
        if (filters[1].length() > 0) {
            queryFilters += "upper(real_estate_type) like '%" + filters[1].toUpperCase() + "%' and ";
        }
        if (filters[2].length() > 0) {
            queryFilters += "upper(address) like '%" + filters[2].toUpperCase() + "%' and ";
        }
        if (filters[3].length() > 0) {
            queryFilters += "surface = " + filters[3] + " and ";
        }
        if (filters[4].length() > 0) {
            queryFilters += "rooms_number = " + filters[4] + " and ";
        }
        if (filters[5].length() > 0) {
            queryFilters += "rent_price = " + filters[5] + " and ";
        }
        if (filters[6].length() > 0) {
            queryFilters += "buy_price = " + filters[6] + " and ";
        }
        if (filters[7].length() > 0) {
            queryFilters += "upper(division) like '%" + filters[7].toUpperCase() + "%' and ";
        }
        if (filters[8].length() > 0) {
            queryFilters += "construction_year = " + filters[8] + " and ";
        }
        if (filters[9].length() > 0) {
            queryFilters += "upper(description) like '%" + filters[9].toUpperCase() + "%' and ";
        }
        if (filters[10].length() > 0) {
            queryFilters += "rent_price = " + filters[10] + " and ";
        }
        if (filters[11].length() > 0) {
            queryFilters += "buy_price = " + filters[11] + " and ";
        }
        if (filters[12].length() > 0) {
            queryFilters += "upper(city) like '%" + filters[12].toUpperCase() + "%' and ";
        }
        System.out.println("Query filters: " + queryFilters + "!!!");

        ResultSet rs=stmt.executeQuery("select * from real_estate where "+ queryFilters + "id >= " + startPosition + " and id <= " + (startPosition + 10) + "order by id");
        List<Estate> estates = new ArrayList<>();
        String [][] estatesString = new String[12][14];
        int index = 0;
        while(rs.next()){
            String currentEstateString[] = new String[13];
            currentEstateString[0] = String.valueOf(rs.getInt(1));
            currentEstateString[1] = rs.getString(2);
            currentEstateString[2] = rs.getString(3);
            currentEstateString[3] = String.valueOf(rs.getString(4));
            currentEstateString[4] = String.valueOf(rs.getString(5));
            currentEstateString[5] = String.valueOf(rs.getString(6));
            currentEstateString[6] = String.valueOf(rs.getString(7));
            currentEstateString[7] = rs.getString(8);
            currentEstateString[8] = String.valueOf(rs.getString(9));
            currentEstateString[9] = rs.getString(10);
            currentEstateString[10] = rs.getDate(11).toString();
            currentEstateString[11] = rs.getDate(12).toString();
            currentEstateString[12] = rs.getString(13);
            estatesString[index] = currentEstateString;
            index ++;
        }
        rs=stmt.executeQuery("select count(*) from real_estate");
        int totalCount = 0;
        while (rs.next()){
           totalCount = rs.getInt(1);
        }

        CitiesDTO citiesDTO = new CitiesDTO(draw,totalCount,totalCount, estatesString);
        con.close();
        return citiesDTO;
    }

    public List<Estate> getEstatesWithFilters(String [] filters) throws SQLException, ClassNotFoundException  {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe","raluca","raluca");
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from real_estate");
        List<Estate> estates = new ArrayList<>();
        while(rs.next()){
            Estate currentEstate = new Estate();
            currentEstate.setID(rs.getInt(1));
            currentEstate.setType(rs.getString(2));
            currentEstate.setAddress(rs.getString(3));
            currentEstate.setSurface(rs.getInt(4));
            currentEstate.setRooms(rs.getInt(5));
            currentEstate.setRentPrice(rs.getInt(6));
            currentEstate.setBuyPrice(rs.getInt(7));
            currentEstate.setDivision(rs.getString(8));
            currentEstate.setConstructionYear(rs.getInt(9));
            currentEstate.setDescription(rs.getString(10));
            currentEstate.setCreationDate(rs.getDate(11).toString());
            currentEstate.setLastUpdate(rs.getDate(12).toString());
            currentEstate.setCity(rs.getString(13));
            estates.add(currentEstate);
        }
        con.close();
        return estates;

    }

    public void addProperty(String[] fields) throws SQLException,ClassNotFoundException {

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe","raluca","raluca");
        Statement stmt=con.createStatement();

        String query = "insert into real_estate(id, real_estate_type,surface,rooms_number, rent_price,buy_price, construction_year, description,address,division,city,create_date,last_update) values( (select MAX(ID)+ 1 from real_estate), ";
        String estate_type ="";
        int surface;
        int roomsNumber = 0;
        int rentPrice = 0;
        int buyPrice = 0;
        int constructionYear = 0;
        String description = "";
        String address = "";
        String division = "";
        String city="";
        for (String field: fields) {
            String postDataCopy = "";
            for (int index = 1; index < field.length() -1; index++) {
                postDataCopy += field.charAt(index);
            }

            String nameAndValue[] = postDataCopy.split("\":\"");
            if(nameAndValue[0].equals("real-estate-type")){
                estate_type = nameAndValue[1].toLowerCase();
                query += "'"+ estate_type + "' ,";
            }
            if(nameAndValue[0].equals("surface")){
                surface = Integer.parseInt(nameAndValue[1]);
                query += surface + ", ";
            }
            if(nameAndValue[0].equals("rooms_number")){
                roomsNumber = Integer.parseInt(nameAndValue[1]);
                query += roomsNumber + ", ";
            }

            if(nameAndValue[0].equals("rent_price")){
                rentPrice = Integer.parseInt(nameAndValue[1]);
                query += rentPrice + ", ";
            }
            if(nameAndValue[0].equals("buy_price")){
                buyPrice = Integer.parseInt(nameAndValue[1]);
                query += buyPrice + ", ";
            }
            if(nameAndValue[0].equals("construction-year")){
                constructionYear = Integer.parseInt(nameAndValue[1]);
                query += constructionYear + ", ";
            }
            if(nameAndValue[0].equals("division")){
                division = nameAndValue[1];
                query += "'" + division + "', ";
            }
            if(nameAndValue[0].equals("description")){
                description = nameAndValue[1];
                query += "'" + description + "', ";
            }

            if(nameAndValue[0].equals("addressLat")){
                address = nameAndValue[1] + ",";

            }
            if(nameAndValue[0].equals("addressLng")){
                address += nameAndValue[1];
                query += "'" + address + "', ";
            }

            if(nameAndValue[0].equals("city")){
                city += nameAndValue[1];
                query += "'" + city + "', ";
            }

        }
        query += "(select sysdate from dual),(select sysdate from dual))";

        System.out.println(query);
        ResultSet rs=stmt.executeQuery(query);
        System.out.println("here");
        con.close();
    }

    public void validateUser(String[] fields)  throws SQLException,ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe","raluca","raluca");
        Statement stmt=con.createStatement();
        String passwordWithCommas  = fields[0].split(":")[1];
        String mailWithCommas = fields[1].split(":")[1];
        String password = "";
        for (int index = 1; index < passwordWithCommas.length() - 1; index ++){
            password += passwordWithCommas.charAt(index);
        }
        String email = "";
        for (int index = 1; index < mailWithCommas.length(); index ++){
            email += mailWithCommas.charAt(index);
        }
        System.out.println("Pass: " + password + ", mail: " + email);
        String query = "Select count(*) as total from users where email = '"+ email +"' and user_password ='" + password + "'";
        //System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        int existsUser = -1;
        System.out.println(query);
        rs.next();
        existsUser = Integer.parseInt(rs.getString(1));
        System.out.println(existsUser);

        con.close();
        if(existsUser == 0 ) {
          //  throw  new SQLException();
        }
    }
}