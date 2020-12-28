public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int last;

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        last = 0;
    }

    @Override
    public Integer get(int index) {
        return arr[index];
    }

    @Override
    public Integer search(int x) { //searching for the element using a for loop.
        for (int i = 0; i < last; i++) {
            if (arr[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void insert(Integer x) { //inserting the element to last free cell in the array.
        arr[last] = x;
        stack.push(last); //pushing last place before inserting.
        stack.push(x); //element inserted.
        stack.push(1); //1 means last action was insert.
        last = last + 1;

    }

    @Override
    public void delete(Integer index) {
        stack.push(index);
        stack.push(arr[index]);
        stack.push(0);
        arr[index] = arr[last - 1]; //switching places with element in arr[last] and element index we want to delete
        last = last - 1;

    }

    @Override
    public Integer minimum() {
        if(last==0)
            return -1;
        int min = 0;
        for (int i = 0; i < last; i++) {
            if (arr[i] < arr[min]) //naive algorithm for searching minimum.
                min = i;
        }
        return min;
    }

    @Override
    public Integer maximum() {
        if(last==0)
            return -1;
        int max = 0;
        for (int i = 0; i < last; i++) { //naive algorithm for searching maximum.
            if (arr[i] > arr[max])
                max = i;
        }
        return max;
    }

    @Override
    public Integer successor(Integer index) {
        int succ = -1;
        if (index < last & index >=0) {
            for (int c = 0; c < last & succ == -1; c++) { //looking for first element that is bigger than array[index]
                if (arr[c] - arr[index] > 0) {
                    succ = c;
                }
            }
            for (int i = succ; i >= 0 && i < last; i++) {
                if (arr[i] - arr[index] < arr[succ] - arr[index] & (arr[i] - arr[index]) > 0) //checking the min diff between required successor.
                    succ = i;
            }
        }
        return succ;
    }

    @Override
    public Integer predecessor(Integer index) {
        int pred = -1;
        if (index < last & index >=0) {
            for (int c = 0; c < last & pred == -1; c++) { //looking for first element that is bigger than array[index]
                if (arr[index] - arr[c] > 0) {
                    pred = c;
                }
            }
            for (int i = pred; pred >= 0 && i < last; i++) { //checking the min diff between required array[index] and predecessor.
                if (arr[index] - arr[i] < arr[index] - arr[pred] & (arr[index] - arr[i]) > 0)
                    pred = i;
            }
        }
        return pred;
    }

    @Override
    public void backtrack() {
        if(!stack.isEmpty()) {
            int action = (int) stack.pop();
            int value = (int) stack.pop();
            int index = (int) stack.pop();

            if (action == 0) { //last action was delete
                arr[last]=arr[index]; //returning popped element to last
                arr[index] = value; //returning what we deleted
                last = last + 1;
            } else {
                last = last - 1;
            }
        }
        System.out.println("backtracking performed");
    }

    @Override
    public void retrack() {
        // Do not implement anything here!!
    }

    @Override
    public void print() {
        for (int i = 0; i < last; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}