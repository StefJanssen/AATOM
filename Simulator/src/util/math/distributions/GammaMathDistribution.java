package util.math.distributions;

/**
 * Gamma distribution.
 * 
 * @author S.A.M. Janssen
 */
public class GammaMathDistribution extends MathDistribution {

	/**
	 * The shape parameter k.
	 */
	private double shape;
	/**
	 * The scale parameter gamma.
	 */
	private double scale;

	/**
	 * The gamma distribution.
	 * 
	 * @param k
	 *            Shape parameter k.
	 * @param gamma
	 *            The scale parameter gamma.
	 */
	public GammaMathDistribution(double k, double gamma) {
		this.shape = k;
		this.scale = gamma;
	}

	/**
	 * We use the code of apache commons math library:
	 * http://commons.apache.org/proper/commons-math/download_math.cgi
	 */
	@Override
	public double getValue() {
		if (shape < 1) {
			// [1]: p. 228, Algorithm GS

			while (true) {
				// Step 1:
				final double u = random.nextDouble();
				final double bGS = 1 + shape / Math.E;
				final double p = bGS * u;

				if (p <= 1) {
					// Step 2:

					final double x = Math.pow(p, 1 / shape);
					final double u2 = random.nextDouble();

					if (u2 > Math.exp(-x)) {
						// Reject
						continue;
					} else {
						return scale * x;
					}
				} else {
					// Step 3:

					final double x = -1 * Math.log((bGS - p) / shape);
					final double u2 = random.nextDouble();

					if (u2 > Math.pow(x, shape - 1)) {
						// Reject
						continue;
					} else {
						return scale * x;
					}
				}
			}
		}
		// Now shape >= 1
		final double d = shape - 0.333333333333333333;
		final double c = 1 / (3 * Math.sqrt(d));

		while (true) {
			final double x = random.nextGaussian();
			final double v = (1 + c * x) * (1 + c * x) * (1 + c * x);

			if (v <= 0) {
				continue;
			}

			final double x2 = x * x;
			final double u = random.nextDouble();

			// Squeeze
			if (u < 1 - 0.0331 * x2 * x2) {
				return scale * d * v;
			}

			if (Math.log(u) < 0.5 * x2 + d * (1 - v + Math.log(v))) {
				return scale * d * v;
			}
		}
	}
}