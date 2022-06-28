package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CittaDao {
	
	
	public List<String> getAllCitta(){
		
		final String sql = "SELECT Localita FROM situazione";
		
		List<String> allCitta = new ArrayList<String>();
		
		
			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);

				ResultSet rs = st.executeQuery();

				while (rs.next()) {

					String r = rs.getString("Localita");
					if (!allCitta.contains(r))
						allCitta.add(r);
				}

				conn.close();
				return allCitta;

			} catch (SQLException e) {

				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}

}
