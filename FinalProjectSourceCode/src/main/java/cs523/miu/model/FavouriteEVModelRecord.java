package cs523.miu.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteEVModelRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String make;
	private String model;
	private String evType;
	private long count;
	
}
