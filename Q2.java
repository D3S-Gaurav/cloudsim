package org.cloudbus.cloudsim.examples.exam;

public class Q2 {

	static int decode(String s) {
		return Integer.parseInt(s, 2);
	}

	static int fitness(String s) {
		int x = decode(s);
		return x * x;
	}

	static void show(String name, String s) {
		System.out.println(name + " = " + s + "  x = " + decode(s) + "  f(x) = " + fitness(s));
	}

	public static void main(String[] args) {
		String[] names = { "P1", "P2", "P3", "P4" };
		String[] pop = { "00101", "01001", "00111", "01100" };

		// (a) decode and fitness
		System.out.println("(a)");
		int best = 0;
		for (int i = 0; i < pop.length; i++) {
			show(names[i], pop[i]);
			if (fitness(pop[i]) > fitness(pop[best]))
				best = i;
		}
		System.out.println("Best individual: " + names[best]);

		// (b) tournament selection: (P1, P2) and (P3, P4)
		System.out.println("\n(b)");
		String parent1 = fitness(pop[0]) >= fitness(pop[1]) ? pop[0] : pop[1];
		String parent2 = fitness(pop[2]) >= fitness(pop[3]) ? pop[2] : pop[3];
		show("Parent 1", parent1);
		show("Parent 2", parent2);

		// one-point crossover after third bit
		String child1 = parent1.substring(0, 3) + parent2.substring(3);
		String child2 = parent2.substring(0, 3) + parent1.substring(3);
		show("Offspring 1", child1);
		show("Offspring 2", child2);

		// (c) flip fourth bit from left of offspring 1
		System.out.println("\n(c)");
		char[] bits = child1.toCharArray();
		bits[3] = bits[3] == '0' ? '1' : '0';
		show("Mutated", new String(bits));
	}
}
