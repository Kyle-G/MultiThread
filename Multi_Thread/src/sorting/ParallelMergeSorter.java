package sorting;

import java.util.*;

/**
 * 
 * @author Jonathan Gonzalez and Kyle Greer
 *
 */

public class ParallelMergeSorter {
	/**
	 * Sorts an array, using the merge sort algorithm.
	 *
	 * @param a the array to sort
	 * @param comp the comparator to compare array elements
	 * @param availableThreads of threads that has been made available
	 */
	public static <E> void sortParallelMergeSort(E[] a, Comparator<? super E> comp, Integer availableThreads) {
		parallelMergeSort(a, 0, a.length - 1, comp, availableThreads);
	}

	/**
	 * Sorts a range of an array, using the merge sort algorithm.
	 *
	 * @param a the array to sort
	 * @param from the first index of the range to sort
	 * @param to the last index of the range to sort
	 * @param comp the comparator to compare array elements
	 * @param availableThreads of threads that has been made available
	 */
	public static <E> void parallelMergeSort(E[] a, int from, int to,
			Comparator<? super E> comp, Integer availableThreads) {

		if (from == to) {
			return;
		}

		//Find the middle point to divide the array into two halves: 
		int mid = (from + to) / 2;


		//Slide 42
		// Runnable implemented by Thread t1
		Runnable r1 = new Runnable(){
			@Override
			public void run(){
				//Call parallelMergeSort for first half in a new thread:
				parallelMergeSort(a, from, mid, comp, availableThreads/2);
			}
		};
		// Runnable implemented by Thread t2
		Runnable r2 = new Runnable(){
			@Override
			public void run(){
				// Call parallelMergeSort for second half in a new thread:
				parallelMergeSort(a, mid + 1, to, comp, availableThreads/2);
			}
		};

		// Call original non-parallel mergeSort
		if (availableThreads <= 1)
			MergeSorter.mergeSort(a, from, to, comp);
		else 
		{
			Thread t1 = new Thread(r1, "T1");
			Thread t2 = new Thread(r2, "T2");

			t1.start();
			t2.start();
			try{
				t1.join();
				t2.join();
			}catch(InterruptedException e){}
			
			//Merge the two halves sorted in previous steps:
			MergeSorter.merge(a, from, mid, to, comp);
		}
	}
}