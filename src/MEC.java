import java.io.*;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Random;

public class MEC {

    private static int RAM = 163840;
    private static final int maximumRAM = 163840;
    private static final int hardDisk = 524288;
    private static int internalClock = 0;

    private static Probability probability[];
    private static ArrayList<PCB> processes = new ArrayList<>();
    private static PCB currentProcess;

    private static Queue<PCB> JobQueue = new LinkedList<>();
    private static Queue<PCB> ReadyQueue = new LinkedList<>();

    public static void main(String[] args){

        fillFileWithProcesses();

        readFile();

        fillProbability();

        while(JobQueue.peek() != null) {

            fillRAM();

            sorting();

            startMachineExecutionCycle();

            clearRAM();
        }

        outputFromTheSimulation();
    }

    private static void fillFileWithProcesses() {

        try {

            ArrayList<Integer> size = new  ArrayList<>();

            Random random = new Random();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Processes.txt"));

            int programmeSize = 0;

            while(programmeSize < hardDisk) {

                int newSize = random.nextInt(300) + 16;
                programmeSize += newSize;

                if(programmeSize >= hardDisk)
                    break;

                size.add(newSize);
            }

            Collections.sort(size);

            for(int i = 0 ; i < size.size(); i++){

                PCB process = new PCB(i,random.nextInt(512) + 16,size.get(i),random.nextInt(200) + 100, State.New);

                bufferedWriter.write(process.toString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(){

        String id, executionTime, size, IOTime; // process attributes

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("Processes.txt"));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
//              spilt the line we read it form the file and just take the values from the line
                String[] info = line.split(";");

                id = info[0].split(":")[1].trim();
                executionTime = info[1].split(":")[1].split("u")[0].trim();
                size = info[2].split(":")[1].split("k")[0].trim();
                IOTime = info[3].split(":")[1].split("u")[0].trim();

                PCB process = new PCB(id, executionTime, size, IOTime, State.New);

                processes.add(process);
                JobQueue.add(process);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private static void fillProbability(){

        probability = new Probability[100];

        for (int i = 0; i < probability.length; i++) {
            if (i < 5)
                probability[i] = Probability.TerminatesNormally;
            else if (i == 5)
                probability[i] = Probability.TerminatesAbnormally;
            else if (i >= 6 && i <= 15)
                probability[i] = Probability.Interrupt;
            else if (i >= 16 && i <= 35)
                probability[i] = Probability.IORequest;
            else if (i >= 36 && i <= 55)
                probability[i] = Probability.BusyIODevice;
            else
                probability[i] = Probability.DoNothing;
        }
    }

    private static void fillRAM(){

        while(RAM > 0 && JobQueue.peek() != null) {

            if((RAM - JobQueue.peek().size) <= 0)
                return;

            RAM -= JobQueue.peek().size;

            ReadyQueue.add(JobQueue.poll());
        }

    }

    private static void sorting(){

        ArrayList<PCB> arrayList = new ArrayList<>();
        PCB process;

        while((process = ReadyQueue.poll()) != null)
            arrayList.add(process); // fill the array of processes from the queue

        Collections.sort(arrayList); // then sorting the array

        for(PCB element : arrayList)
            ReadyQueue.add(element); // refill the readyQueue to start execution
    }

    private static void startMachineExecutionCycle() {

        while(ReadyQueue.peek() != null){

            currentProcess = ReadyQueue.poll();

            currentProcess.state = State.Running;
            processes.get(currentProcess.id).state = State.Running;

            for(int i = currentProcess.counterExecutionTime ; i <= currentProcess.expectedExecutionTime ; i++) {

                internalClock++;
                currentProcess.counterExecutionTime++;
                processes.get(currentProcess.id).counterExecutionTime++;

                Probability x = probability[new Random().nextInt(99)];

                switch(x) {

                    case Interrupt:

                        InterruptCase();

                        break;

                    case IORequest:

                        IORequestCase();

                        break;

                    case TerminatesNormally:

                        terminatesNormallyCase();

                        break;

                    case TerminatesAbnormally:

                        terminatesAbnormallyCase();

                        break;
                }

                if(x == Probability.TerminatesNormally || x == Probability.TerminatesAbnormally || x == Probability.Interrupt)
                    break;
            }

            isFinished();
        }
    }

    private static void InterruptCase() {

        ReadyQueue.add(currentProcess);
        currentProcess.state = State.Ready;
        processes.get(currentProcess.id).state = State.Ready;

        sorting();
    }

    private static void IORequestCase() {

        if(currentProcess.counterIOTime >= currentProcess.expectedIOTime)
            return;

        currentProcess.state = State.Waiting;
        processes.get(currentProcess.id).state = State.Waiting;

        while(currentProcess.counterIOTime < currentProcess.expectedIOTime) {

            internalClock++;
            currentProcess.counterIOTime++;
            processes.get(currentProcess.id).counterIOTime++;

            Probability x = probability[new Random().nextInt(99)];

            if(x == Probability.BusyIODevice)
                busyIODeviceCase();
        }

        currentProcess.state = State.Running;
        processes.get(currentProcess.id).state = State.Running;
    }

    private static void busyIODeviceCase() {

        currentProcess.counterIOTime = currentProcess.expectedIOTime;
        processes.get(currentProcess.id).counterIOTime = processes.get(currentProcess.id).expectedIOTime;
    }

    private static void terminatesNormallyCase() {

        currentProcess.state = State.TerminatedNormally;
        processes.get(currentProcess.id).state = State.TerminatedNormally;
    }

    private static void terminatesAbnormallyCase() {

        currentProcess.state = State.TerminatedAbnormally;
        processes.get(currentProcess.id).state = State.TerminatedAbnormally;
    }

    private static void isFinished() {

        if(currentProcess.counterExecutionTime >= currentProcess.expectedExecutionTime && currentProcess.state == State.Running){

            currentProcess.state = State.TerminatedNormally;
            processes.get(currentProcess.id).state = State.TerminatedNormally;
        }
    }

    private static void clearRAM() {

        RAM = maximumRAM;
    }

    private static void outputFromTheSimulation() {

        double programmeSize = 0;
        int processesNormally = 0;
        int processesAbnormally = 0;
        int processesCPUBound = 0;

        for (PCB element : processes){

            programmeSize += element.size;

            if(element.state == State.TerminatedNormally)
                processesNormally++;
            else if(element.state == State.TerminatedAbnormally)
                processesAbnormally++;

            if(element.counterExecutionTime > element.counterIOTime)
                processesCPUBound++;
        }

        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("Output from the simulation : ");
        System.out.println("1. The number of initially generated jobs stored on the H-disk : " + processes.size());
        System.out.println("2. The average program size of all jobs : " + (programmeSize / (processes.size() * 1.0)));
        System.out.println("3. The average number of jobs that have completed their execution normally : " + ( (1.0 * processesNormally) / (1.0 * processes.size())));
        System.out.println("4. The average number of jobs that have completed their execution abnormally : " + ( (1.0 * processesAbnormally) / (1.0 * processes.size())));
        System.out.println("5. The number of CPU bound jobs : " + processesCPUBound);
        System.out.println("---------------------------------------------------------------------------------------------");
    }
}