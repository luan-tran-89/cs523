package cs523.miu.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTypeReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private String evType;
	
	private long count;
	
}
