package is.gestionevideo.database;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import is.gestionevideo.entity.Sport;
import is.gestionevideo.entity.Video;
import is.gestionevideo.entity.VideoApprofondimento;
import is.gestionevideo.entity.VideoEvento;

public class VideoDAO {
		public static Video createVideo(String nome, LocalDate data, Sport sport) throws SQLException {
			
			Video video = new Video(nome, data, sport);
			
			create(video);
			
			return video;
		}
		public static VideoEvento createVideoEvento(String nome, LocalDate data, Sport sport) throws SQLException {
			
			VideoEvento video = new VideoEvento(nome, data, sport);
			
			create(video);
			
			return video;
		}
		
		public static VideoApprofondimento createVideoApprofondimento(String nome, LocalDate data, Sport sport) throws SQLException {
			
			VideoApprofondimento video = new VideoApprofondimento(nome, data, sport);
			
			create(video);
			
			return video;
		}
		
		public static int create(Video v) throws SQLException {
			
			Connection conn = DBManager.getConnection();
			
			int id_video = -1;
			
			String query = "INSERT INTO VIDEO VALUES(NULL,?,?,?,?);";
			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.setString(1, v.getNome());
				stmt.setDate(2, Date.valueOf(v.getData()));
				stmt.setString(3, v.getSport().toString());
				
				if(v instanceof VideoEvento) {
					stmt.setString(4, "EVENTO");
				}
				else if(v instanceof VideoApprofondimento) {
					stmt.setString(4, "APPROFONDIMENTO");
				}
				else {
					stmt.setString(4, "");
				}
				
				
				stmt.executeUpdate();
				
				try(ResultSet result = stmt.getGeneratedKeys()) {
					
					if(result.next()) {
						id_video = result.getInt(1);
					}
				}
			}
			
			v.setId(id_video);
			
			return id_video;
		}
		
		public static void delete(Video v) throws SQLException {
			
			Connection conn = DBManager.getConnection();
			
			int id_video = v.getId();		
			String query = "DELETE FROM VIDEO WHERE ID=?;";	
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.setInt(1, id_video);
				
				stmt.executeUpdate();
			}
			
			
			if(v instanceof VideoApprofondimento) {
			
				String query2 = "DELETE FROM GIORNALISTI WHERE ID_VIDEO=?;";
				
				try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
					
					stmt2.setInt(1, id_video);
					
					stmt2.executeUpdate();
				}
			}
		}
		
}