package org.cloudbus.cloudsim.examples.exam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

// Run "Q1" for part (b), "Q1 sjf" for part (c)
public class Q1 {

	public static void main(String[] args) throws Exception {
		boolean sjf = args.length > 0 && args[0].equals("sjf");
		long[] lengths = { 400, 200, 600, 300, 500 };

		CloudSim.init(1, Calendar.getInstance(), false);

		// Datacenter: one host with 3 PEs of 1000 MIPS
		List<Pe> peList = new ArrayList<>();
		for (int i = 0; i < 3; i++)
			peList.add(new Pe(i, new PeProvisionerSimple(1000)));
		List<Host> hostList = new ArrayList<>();
		hostList.add(new Host(0, new RamProvisionerSimple(4096), new BwProvisionerSimple(100000),
				1000000, peList, new VmSchedulerTimeShared(peList)));
		DatacenterCharacteristics ch = new DatacenterCharacteristics(
				"x86", "Linux", "Xen", hostList, 10.0, 3.0, 0.05, 0.001, 0.0);
		new Datacenter("Datacenter_0", ch, new VmAllocationPolicySimple(hostList), new LinkedList<Storage>(), 0);

		DatacenterBroker broker = new DatacenterBroker("Broker");
		int brokerId = broker.getId();

		// 3 VMs, 1000 MIPS, space-shared
		List<Vm> vmList = new ArrayList<>();
		for (int i = 0; i < 3; i++)
			vmList.add(new Vm(i, brokerId, 1000, 1, 512, 1000, 10000, "Xen", new CloudletSchedulerSpaceShared()));

		// 5 Cloudlets
		List<Cloudlet> cloudletList = new ArrayList<>();
		UtilizationModel um = new UtilizationModelFull();
		for (int i = 0; i < lengths.length; i++) {
			Cloudlet c = new Cloudlet(i, lengths[i], 1, 300, 300, um, um, um);
			c.setUserId(brokerId);
			cloudletList.add(c);
		}

		// SJF: sort by ascending length
		if (sjf)
			cloudletList.sort((a, b) -> Long.compare(a.getCloudletLength(), b.getCloudletLength()));

		// Policy: assign to VM with smallest current load, tie -> lowest VM id
		long[] load = new long[3];
		for (Cloudlet c : cloudletList) {
			int best = 0;
			for (int v = 1; v < 3; v++)
				if (load[v] < load[best])
					best = v;
			load[best] += c.getCloudletLength();
			c.setVmId(best);
		}

		broker.submitVmList(vmList);
		broker.submitCloudletList(cloudletList);

		CloudSim.startSimulation();
		List<Cloudlet> result = broker.getCloudletReceivedList();
		CloudSim.stopSimulation();

		double makespan = 0;
		System.out.println("Cloudlet  VM  Length  Start  Finish");
		for (Cloudlet c : result) {
			System.out.printf("C%-8d VM%d %6d  %5.2f  %6.2f%n", c.getCloudletId(), c.getVmId(),
					c.getCloudletLength(), c.getExecStartTime(), c.getFinishTime());
			makespan = Math.max(makespan, c.getFinishTime());
		}
		System.out.printf("Makespan = %.2f%n", makespan);
	}
}
