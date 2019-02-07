# Operating System Simulation
> "OS Course Project" : Developing an application that simulate OS works.

## Hardware:
```diff
The computer hardware is assumed to have:
```
* A hard disk of size of 10 GB where 1/5 of this size is used to store the user programs.
* A RAM of size 192MB, where 32MB is used to store the OS.
* A CPU that executes one instruction each unit of time.
* An IO device for input and output operations.
* An internal clock that ticks every unit of time.

## Operating System:
```diff
The operating system is the multiprogramming OS. We would be interested in only 2 features in this simulation: The Job and CPU scheduling.
```
* Job Scheduling: The program with the smallest size is first selected to be loaded in the main memory. We call this technique by SSPF.
* CPU scheduling: The CPU is allocated to the program with the smallest expected running time. We call this technique by SETF.

## Program specifications:
```diff
Each program has 2 main requirements: A *program size* in KB and an expected execution time. Of course, the expected execution time is greater or equal to the *exact execution time*. In addition, each program should have an *Id* and *state*. The student should specify any extra information that is required by the simulation.
```
* The memory sizes are uniformly distributed between 16KB and 16384KB
* The expected execution time is also distributed between 16ut and 512ut.
* The expected IO time is also distributed between 100ut and 200ut.

## Initialization phase:
```diff
You should perform the following steps before running the simulation:
```
* Generate enough programs with random memory size and random expected execution
time so to fill the hard disk. (Total program sizes <= 0.5GB)
* Load the RAM with the maximum number of user programs.
* Start the simulation which consists of a simulation of the Machine Execution Cycle.

## The Machine Execution Cycle:
```diff
The following algorithm simulates the machine Execution Cycle:
```
* MEC algorithm:

        While true do {
                Increments the simulated clock by one unit of time
                (* This assumes that one instruction is executed *)
                If there are interrupts
                    Then Interrupts the current program and calls the ISRi
                endif
        }
        
* Interrupts are also randomly generated:
  * The possibility that there are interrupts is 10%
  * The possibility that there is an IO request is 20%
  * The possibility that the busy IO device will terminate is 20%
  * The possibility that the program terminates normally is 5%
  * The possibility that the program terminates abnormally is 1%

* The main simulator program is like this:

      While there are jobs in the H-Disk do {
          Run the Machine Execution Cycle
      }
      Print the required statistics

## Output from the simulation:
```diff
At the end of the simulation, you should print the following results:
```
* The number of initially generated jobs stored on the H-disk.
* The average program size of all jobs.
* The average number of jobs that have completed their execution normally.
* The average number of jobs that have completed their execution abnormally.
* The number of CPU bound jobs.
