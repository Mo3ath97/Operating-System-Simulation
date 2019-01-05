# Operating System Simulation
> "OS Course Project" : Developing an application that simulate OS works.

## Hardware:
> The computer hardware is assumed to have:
1. A hard disk of size of 10 GB where 1/5 of this size is used to store the user programs.
2. A RAM of size 192MB, where 32MB is used to store the OS.
3. A CPU that executes one instruction each unit of time.
4. An IO device for input and output operations.
5. An internal clock that ticks every unit of time.

## Operating System:
> The operating system is the multiprogramming OS. We would be interested in only 2 features in this simulation: The Job and CPU scheduling.
1. Job Scheduling: The program with the smallest size is first selected to be loaded in the main memory. We call this technique by SSPF.
2. CPU scheduling: The CPU is allocated to the program with the smallest expected running time. We call this technique by SETF.
