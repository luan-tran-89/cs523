package cs523.miu.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiclesEachYearRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String year;
	private long count;
	
}
