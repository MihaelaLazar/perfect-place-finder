package com.sgbd;

import com.sgbd.dto.CitiesDTO;
import com.sgbd.dto.EstateDTO;
import com.sgbd.dto.LoginDTO;
import com.sgbd.dto.SignUpDTO;
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

    public void connectToDB() {
        try {
            //step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //step2 create  the connection object
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "raluca", "raluca");

            //step3 create the statement object
            Statement stmt = con.createStatement();

            //step4 execute query
            ResultSet rs = stmt.executeQuery("select * from userdfgs");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));

            //step5 close the connection object
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void addUser(SignUpDTO user) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "raluca", "raluca");
        Statement stmt = con.createStatement();

        String query = "insert into users(id, first_name, last_name, user_password,email, username, user_role) values( (select MAX(ID)+ 1 from users), ";
        user.setUsername(user.getFirstName() + "." + user.getLastName());
        query += "'" + user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getPassword() + "', '" + user.getEmail() + "', '" + user.getUsername() + "','user')";
        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
    }

    public CitiesDTO getEstates(int startPosition, int draw, String[] filters) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "raluca", "raluca");
        Statement stmt = con.createStatement();
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

        ResultSet rs = stmt.executeQuery("select * from real_estate where " + queryFilters + "id >= " + startPosition + " and id <= " + (startPosition + 10) + "order by id");
        List<Estate> estates = new ArrayList<>();
        String[][] estatesString = new String[12][14];
        int index = 0;
        while (rs.next()) {
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
            index++;
        }
        rs = stmt.executeQuery("select count(*) from real_estate");
        int totalCount = 0;
        while (rs.next()) {
            totalCount = rs.getInt(1);
        }

        CitiesDTO citiesDTO = new CitiesDTO(draw, totalCount, totalCount, estatesString);
        con.close();
        return citiesDTO;
    }

    public List<Estate> getEstatesWithFilters(String[] filters) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "raluca", "raluca");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from real_estate");
        List<Estate> estates = new ArrayList<>();
        while (rs.next()) {
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

    public void addProperty(EstateDTO estateDTO) throws SQLException, ClassNotFoundException {

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "raluca", "raluca");
        Statement stmt = con.createStatement();
        String query = "insert into real_estate(id, real_estate_type,surface,rooms_number, rent_price,buy_price, construction_year, description,address,division,city,create_date,last_update) values( (select MAX(ID)+ 1 from real_estate), ";
        query += "'" + estateDTO.getRealEstateType() +
                "', " + estateDTO.getSurface() + ", " + estateDTO.getRoomsNumber() +
                ", " + estateDTO.getRentPrice() + ", " + estateDTO.getBuyPrice() +
                ", " + estateDTO.getConstructionYear() + ", '" + estateDTO.getDescription() +
                "', '" + estateDTO.getAddressLat() + "," + estateDTO.getAddressLng() +
                "', '" + estateDTO.getDivision() + "', '" + estateDTO.getCity() +
                "', ";
        query += "(select sysdate from dual),(select sysdate from dual))";

        // System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("here");
        con.close();
    }

    public String[] validateUser(LoginDTO loginDTO) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "raluca", "raluca");
        Statement stmt = con.createStatement();

        String query = "Select count(*) as total from users where email = '" + loginDTO.getEmail() + "' and user_password ='" + loginDTO.getPassword() + "'";
        //System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        int existsUser;
        System.out.println(query);
        rs.next();
        existsUser = Integer.parseInt(rs.getString(1));
        System.out.println(existsUser);

        con.close();
        if (existsUser == 0) {
            throw new SQLException();
        } else {
            String[] emailAndPassword = {loginDTO.getEmail(), loginDTO.getPassword()};
            return emailAndPassword;
        }
    }
}