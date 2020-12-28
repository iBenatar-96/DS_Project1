public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int last;

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        last = 0;
    }

    @Override
    public Integer get(int index) {
        return arr[index];
    }

    @Override
    public Integer search(int x) {
        return recSearch(arr, x, 0, last - 1);
    } //binary searching through the array.

    private static int recSearch(int[] arr, int x, int low, int high) {
        int output = -1;
        if (low <= high) {
            int mid = (low + high) / 2;
            if (x < arr[mid])
                output = recSearch(arr, x, low, mid - 1);
            else if (x > arr[mid])
                output = recSearch(arr, x, mid + 1, high);
            else
                output = mid;
        }
        return output;
    }

    @Override
    public void insert(Integer x) {
        int c;
        for (c = last - 1; c >= 0 && arr[c] > x; c--) { //using a for loop to move all the elements until x is smaller than array[c].
            arr[c + 1] = arr[c];
        }
        arr[c + 1] = x;
        last = last + 1;
        stack.push(c + 1); //pushing location inserted.
        stack.push(x);
        stack.push(1); //1 means last action was insert.
    }


    @Override
    public void delete(Integer index) {
        if (index < last) {
            stack.push(index);
            stack.push(arr[index]);
            stack.push(0);
            int c;
            for (c = index; c < last - 1; c++) { //moving all the elements in the array.
                arr[c] = arr[c + 1];
            }
            last = last - 1;
        }
    }

    @Override
    public Integer minimum() {
        if (last == 0)
            return -1;
        return 0;
    }

    @Override
    public Integer maximum() {
        if (last == 0)
            return -1;
        return last-1;
    }

    @Override
    public Integer successor(Integer index) {
        for (int i = index + 1; i < last; i++) { //iteration until element is first larger than array[index].
            if (arr[index] < arr[i])
                return i;
        }
        return -1;
    }

    @Override
    public Integer predecessor(Integer index) { //iteration until element is first smaller than array[index].
        for (int i = index - 1; i >= 0; i--) {
            if (arr[index] > arr[i])
                return i;
        }
        return -1;
    }

    @Override
    public void backtrack() {
        if(!stack.isEmpty()) {
            int action = (int) stack.pop();
            int value = (int) stack.pop();
            int index = (int) stack.pop();
            if (action == 0) { //last action was delete
                int c;
                for (c = last - 1; c >= 0 && arr[c] > value; c--) { //moving all the elements.
                    arr[c + 1] = arr[c];
                }
                arr[c + 1] = value;
                last = last + 1;
            } else { //last action was insert.
                int c;
                for (c = index; c < last - 1; c++) { //moving all the elements.
                    arr[c] = arr[c + 1];
                }
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
        for (int c = 0; c < last; c++) {
            System.out.print(arr[c] + " ");
        }
    }
}