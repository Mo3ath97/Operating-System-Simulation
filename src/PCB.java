public class PCB implements Comparable {

    public int id, expectedExecutionTime, size , expectedIOTime, counterExecutionTime, counterIOTime;
    public State state;

    public PCB(int id, int expectedExecutionTime, int size, int expectedIOTime, State state) {

        this.id = id;
        this.expectedExecutionTime = expectedExecutionTime;
        this.size = size;
        this.expectedIOTime = expectedIOTime;
        this.state = state;
        this.counterExecutionTime = this.counterIOTime = 0;
    }

    public PCB(String id, String expectedExecutionTime, String size, String expectedIOTime, State state) {

        this.id = Integer.parseInt(id);
        this.expectedExecutionTime = Integer.parseInt(expectedExecutionTime);
        this.size = Integer.parseInt(size);
        this.expectedIOTime = Integer.parseInt(expectedIOTime);
        this.state = state;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\t;\t CPU: " + expectedExecutionTime + "ut \t;\t Size: " + size + "kb \t;\t IO: " + expectedIOTime + "ut";
    }

    @Override
    public int compareTo(Object o) {

        return this.expectedExecutionTime - ((PCB)o).expectedExecutionTime;
    }

}