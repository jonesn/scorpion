# Power Systems And Simplex

## Terminology

### Generator And Load

A generator produces electricity. A load consumes electricity. The electricity is transmitted from the generation location to the load location via the transmission system.

### Power Voltage and Current

Electricity is the flow of electrons. The power of theelectricity is the product of the quantity of electrons
that flows (the current) and the pressure of that flow (the voltage).

### Transmission Voltage

As the electricity flows through the conductors (e.g., wires) the electrons interact with the material of the conductor (e.g., copper) and some of the power is lost as heat.

The more electrons that are flowing, i.e., the higher the current, the higher the losses. Therefore, before the power is sent over long distances its voltage is increased, which will allow the same amount of power to be sent with a lower current, thereby decreasing the losses.

High voltage electricity can jump across an air gap therefore it needs to be transported either by thick cables, or by towers where the wires are kept well away from the ground and from each other. This is expensive, hence high voltages are only used when long distances are involved and the corresponding reduction in losses is significant. The transport of
electricity over long distances at high voltages is referred to as transmission.

### Transformers

After the electricity is generated, it is increased to a higher voltage in preparation for transmission.

The voltage of the electricity is changed using transformers. Inside the transformer the electricity passes through a coil of wire, this creates an electromagnetic field that induces electricity in an adjacent coil of wire within the transformer.

If the adjacent coil has more windings then it will have a higher voltage across the end of its windings than the voltage across the windings of the first coil; if the adjacent coil has fewer windings then its voltage will be lower.

### Transmission system

The transmission system transmits the power to the load location, where a supply transformer decreases the voltage to a level that can be safely and easily distributed to the end consumer. The power is distributed to the end consumer via the
distribution network.

The transmission system consists of the lines, cables, and transformers that transmit the power from the generators to the distribution network.

### Scope of the Electricity Market

The electricity market models the generators, the transmission system and the load. The location of the load is not the location of the actual load but rather the point at which the power exits the transmission system and enters the distribution network.

### Branches

In the electricity market model the lines, cables and transformers that transmit the electricity are referred to as branches.

### Substations

The transmission system also includes substations. The substations are the nodes on the network and also where the transformers are located.

The generation is injected into the transmission system at a substation and the load exits to the distribution network at a substation. In between, the generation and load the power may travel through other substations that act as routing points.

### Buses

Within a substation there are one or more busbars that act as common connection points. Physically a busbar may consist of steel or copper bars, or it may be overhead cables running through a substation.

A busbar is usually referred to as a bus. Because a substation can contain several buses operating at different voltages, it is the buses rather than the substations themselves that function as nodes in the electricity market model.

### The Power System and the System Operator

The power system consists of the generators that produce the electricity, the transmission system that transports the electricity, and the load that consumes the electricity. The purpose of the power system is to meet the power requirements of the load.

The System Operator is the entity responsible for ensuring that the power system achieves its purpose. Hence, the System Operator is responsible for ensuring that there is always enough generation available to meet the power requirements of the load and that the transmission system is capable of transporting the necessary power.

### System Security

Part of ensuring that the power requirements can be met it is ensuring that the power system is secure. For example, the System Operator enforces system security by ensuring that the power flow does not exceed the maximum capacity of the branches, and that, as far as possible, there is no single event that could result in the power requirements of the load not being met. There are other aspects of system security, but these two are directly enforced by the electricity market model.

### Network Model and Power Flow

The System Operator assesses system security by using a network model. The network model is a computer representation of the power system, complete with generators, branches, buses and loads.

The network model is combined with power flow software that simulates the physical rules of power flow. The power flow software runs a power flow study to assess the security of the power system, e.g., by determining that branches do not exceed their limits.

## Electricity Markets

### Generator Offers

In an electricity market, generators submit offers to generate the power that will meet the requirements of the load. The offers consist of a price and a quantity.

### Load 

The majority of load will be consumed regardless of price. This load is referred to as required load. There is also some price-sensitive load, which will only be consumed if the price is below a certain level.

Price sensitive load is bid with a price and quantity. Required load can either be modelled as a constraint requiring that the load be supplied, or as a load with a bid price that is higher than any possible generation offer price.

### Units Of Measure

The generator’s offer quantity is specified in MW. The price is $/MWh, where one MWh is one MW supplied for an hour; this is because electricity is metered in terms of usage over time. Bids also use MW for quantity and $/MWh for price.

### Time Intervals and Schedules

Each electricity market model is built and solved to schedule generation to supply load over a specific interval of time. This time interval is referred to as a trading interval or a trading period.

For forecast schedules, i.e., schedules that predict future prices and quantities, a trading period of 30 minutes or 60 minutes is common, and the schedule will contain multiple trading periods, e.g. 48 half hour trading periods to cover a day.

For schedules that are solved to meet the load requirements in real time, a time interval of 5 minutes is common and the schedule only contains the one trading period.

### Nodal Electricity Markets

An electricity market could clear generation offers in order to meet load without considering how the generation would reach the load.

However, in nodal electricity markets the electricity market model includes a network model and a power flow. The generation offers that are cleared are modelled as reaching the load by obeying the mathematical rules of power flow.

When the nodal electricity market model is solved the result is a schedule of nodal prices and generation quantities. These prices and quantities are calculated based on the price of the generation offers combined with the requirements of the power flow.

# Generators Make Offers Consumers Make Bids