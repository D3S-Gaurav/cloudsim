package org.cloudbus.cloudsim.examples.exam;

/**
 * Q2: Genetic Algorithm to maximize f(x) = x^2, 0 <= x <= 31,
 * x encoded as a 5-bit binary chromosome.
 */
public class Q2 {

	// Decode a binary chromosome into its decimal value
	static int decode(String chromosome) {
		return Integer.parseInt(chromosome, 2);
	}

	// Fitness function f(x) = x^2
	static int fitness(String chromosome) {
		int x = decode(chromosome);
		return x * x;
	}

	// Tournament selection: the fitter of the two individuals wins
	static String tournament(String a, String b) {
		return fitness(a) >= fitness(b) ? a : b;
	}

	// One-point crossover after the given bit position
	static String[] crossover(String p1, String p2, int point) {
		String c1 = p1.substring(0, point) + p2.substring(point);
		String c2 = p2.substring(0, point) + p1.substring(point);
		return new String[] { c1, c2 };
	}

	// Bit-flip mutation at the given position (1-based from the left)
	static String mutate(String chromosome, int position) {
		char[] bits = chromosome.toCharArray();
		bits[position - 1] = bits[position - 1] == '0' ? '1' : '0';
		return new String(bits);
	}

	static void print(String name, String chromosome) {
		System.out.println(name + " = " + chromosome + "  x = " + decode(chromosome) + "  f(x) = " + fitness(chromosome));
	}

	public static void main(String[] args) {
		String[] names = { "P1", "P2", "P3", "P4" };
		String[] population = { "00101", "01001", "00111", "01100" };

		// (a) Decode each chromosome and calculate its fitness
		System.out.println("(a) Decoding and fitness");
		int best = 0;
		for (int i = 0; i < population.length; i++) {
			print(names[i], population[i]);
			if (fitness(population[i]) > fitness(population[best])) {
				best = i;
			}
		}
		System.out.println("Best individual: " + names[best]);

		// (b) Tournament selection on (P1, P2) and (P3, P4), then crossover after the third bit
		System.out.println();
		System.out.println("(b) Tournament selection and crossover");
		String parent1 = tournament(population[0], population[1]);
		String parent2 = tournament(population[2], population[3]);
		print("Parent 1", parent1);
		print("Parent 2", parent2);

		String[] offspring = crossover(parent1, parent2, 3);
		print("Offspring 1", offspring[0]);
		print("Offspring 2", offspring[1]);

		// (c) Bit-flip mutation on the fourth bit of the first offspring
		System.out.println();
		System.out.println("(c) Mutation");
		print("Mutated offspring 1", mutate(offspring[0], 4));
	}
}
