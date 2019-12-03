package util.math.distributions;

/**
 * Generalized extreme value distribution.
 * 
 * @author S.A.M. Janssen
 */
public class GeneralizedExtremeValueMathDistribution extends MathDistribution {

	private double loc;
	private double scale;
	private double shape;

	/**
	 * Generalized extreme value distribution with three parameters.
	 * 
	 * @param loc
	 * @param scale
	 * @param shape
	 */
	public GeneralizedExtremeValueMathDistribution(double loc, double scale, double shape) {
		this.loc = loc;
		this.scale = scale;
		this.shape = shape;
	}

	/**
	 * Method from http://jdistlib.sourceforge.net/
	 * 
	 * @return the value.
	 */
	@Override
	public double getValue() {
		if (scale < 0)
			return Double.NaN;
		return shape == 0 ? (loc - scale * Math.log(getRandomExponential()))
				: (loc + scale * ((Math.pow(getRandomExponential(), -shape) - 1) / shape));
	}

	/**
	 * 
	 * @return
	 */
	private double getRandomExponential() {
		/* q[k-1] = sum(log(2)^k / k!) k=1,..,n, */
		/* The highest n (here 16) is determined by q[n-1] = 1.0 */
		/* within standard precision */
		final double q[] = new double[] { 0.6931471805599453, 0.9333736875190459, 0.9888777961838675,
				0.9984959252914960, 0.9998292811061389, 0.9999833164100727, 0.9999985691438767, 0.9999998906925558,
				0.9999999924734159, 0.9999999995283275, 0.9999999999728814, 0.9999999999985598, 0.9999999999999289,
				0.9999999999999968, 0.9999999999999999, 1.0000000000000000 };

		double a = 0.;
		double u = random
				.nextDouble(); /* precaution if u = 0 is ever returned */
		while (u <= 0. || u >= 1.)
			u = random.nextDouble();
		for (;;) {
			u += u;
			if (u > 1.)
				break;
			a += q[0];
		}
		u -= 1.;

		if (u <= q[0])
			return a + u;

		int i = 0;
		double ustar = random.nextDouble(), umin = ustar;
		do {
			ustar = random.nextDouble();
			if (umin > ustar)
				umin = ustar;
			i++;
		} while (u > q[i]);
		return a + umin * q[0];
	}

	public String toString() {
		return "GEV," + loc + "," + scale + "," + shape;
	}
}
