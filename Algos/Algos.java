import java.util.Arrays;

class Algos{

    static int count = 0;

    static void out(Object text){
        System.out.println(text);
    }

    public static void main(String args[]){
        try {
            int arr[] = {13,-3,-25,20,-3,-16,-23,18,20,-7,12,-5,-22,15,-4,7};
            //HeapSort(new int[]{4,1,3,2,16,9,10,14,8,7});
            HeapSort(arr);

            out(Arrays.toString(arr));
        } catch (Error e) {
            e.printStackTrace();       
        }
        
    }

    static void MergeSort(int arr[],int start,int end){
        if(start < end-1){
            int middle = (start + end) / 2;
            MergeSort(arr,start,middle);
            MergeSort(arr,middle,end);
            Merge(arr,start,end);
        }
    }
    
    static void Merge(int arr[],int start,int end){
        int middle = (start + end) / 2;
        int left[] = Arrays.copyOfRange(arr,start,middle);
        int right[] = Arrays.copyOfRange(arr,middle,end);
        for(int i=0,j=0,k=start;k<end;k++){
            if(i == left.length){
                arr[k] = right[j++];
                continue;
            }else if(j == right.length){
                arr[k] = left[i++];
                continue;
            }
            if(left[i] <= right[j]){
                arr[k] = left[i++];
            }else{
                arr[k] = right[j++];
            }
        }  
    }

    static int BinarySearch(int arr[],int start,int end,int val){
        if(start < end){
            int middle = (start + end) / 2;
            if(arr[middle] == val) return middle;
            else if(val < arr[middle]) return BinarySearch(arr,start,middle,val);
            else return BinarySearch(arr,middle+1,end,val);
        }
        return -1;
    }

    static int[] MaxSubArray(int arr[],int start,int end){
        if(end - start == 1) return new int[]{start,end,arr[start]};
        int middle = (start + end) / 2;
        int left[] = MaxSubArray(arr,start,middle);
        int right[] = MaxSubArray(arr,middle,end);
        int sum[] = SumSubArray(arr,start,end);
        if(left[2] > right[2] && left[2] > sum[2]) return left;
        else if(right[2] > left[2] && right[2] > sum[2]) return right;
        else return sum;
    }

    static int[] SumSubArray(int arr[],int start,int end){
        int middle = (start + end) / 2;
        int sum = 0;
        int lmax = -100;
        int rmax = -100;
        int left = middle-1;
        int right = middle;
        for(int i=middle-1;i>=start;i--) {
            sum += arr[i];
            if(sum > lmax){
                lmax = sum;
                left = i;
            }
        }
        sum = 0;
        for(int i=middle;i<end;i++) {
            sum += arr[i];
            if(sum > rmax){
                rmax = sum;
                right = i;
            }
        }
        return new int[]{left,right,lmax+rmax};
    }    

    static int[] MaxSubArray(int arr[]){
        int sum = 0;
        int max = -100;
        int left = 0;
        int tempLeft = 0;
        int right = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if(sum > max){
                max = sum;
                left = tempLeft;
                right = i;
            } 
            if(sum <= 0 && i+1 < arr.length && (arr[i+1] > 0 || arr[i+1] > max)) {
                sum = 0;
                tempLeft = i+1;
            }
        }
        return new int[]{left,right,max};
    }

    static void Heapify(int arr[],int i,int heapSize){
        int left = 2*i + 1;
        int right = 2*i + 2;
        int largest = i;
        if(left < heapSize && arr[i] < arr[left]){
            largest = left;
        }
        if(right < heapSize && arr[largest] < arr[right]){
            largest = right;
        }
        if(largest != i){   
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            Heapify(arr, largest,heapSize);
        }
    }

    static void BottomUpHeapify(int[] arr){
        int middle = arr.length / 2;
        for (int i = middle-1; i >= 0; i--) {
            Heapify(arr,i,arr.length);
        }
    }

    static void HeapSort(int arr[]){
        int heapSize= arr.length;
        BottomUpHeapify(arr);
        out(arr.length);
        out(Arrays.toString(arr));
        for (int i = arr.length-1; i >= 1; i--) {
            int temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            Heapify(arr,0,--heapSize);
        }
    }

}
