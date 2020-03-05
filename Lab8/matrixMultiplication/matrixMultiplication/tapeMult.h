#include <mpi.h>
#include <iostream>
#include <ctime>
#include <vector>
#include <windows.h>

namespace TapeMult {

	using std::cout;
	using std::cin;
	using std::endl;

	inline int** CreateMatrix(const int n, const int m)
	{
		int** Matrix = new int* [n];
		for (int i = 0; i < n; ++i)
			Matrix[i] = new int[m];

		srand(unsigned(time(0)));

		for (int i = 0; i < n; ++i)
		{
			for (int j = 0; j < m; ++j)
				Matrix[i][j] = (rand() % 100) - 50; 
		}

		return Matrix;
	}

	inline void ShowMatrix(int** Matrix, const int n, const int m)
	{
		for (int i = 0; i < n; ++i)
		{
			for (int j = 0; j < m; ++j)
				cout << Matrix[i][j] << " ";

			cout << endl;
		}
	}

	void tapeMatrixMultiplication(int matrixN)
	{
		MPI_Barrier(MPI_COMM_WORLD);

		int proc_id = 0, proc_num = 0;
		int** Matrix = nullptr;
		int* Matrix_vector = nullptr;
		int m = matrixN;
		int n = matrixN;
		int* receve_vector = nullptr;
		int work_size = 0;

		double time_start = 0;
		double parall_time_work = 0;
		double sequent_time_work = 0;
		int parallel_sum = 0;
		int sequential_sum = 0;
		int* partial_sum = 0;
		int* displs = nullptr;
		int* sendcounts = nullptr;
		int* partial_sum_part = nullptr;

		MPI_Comm_size(MPI_COMM_WORLD, &proc_num);
		MPI_Comm_rank(MPI_COMM_WORLD, &proc_id);



		displs = new int[proc_num];
		sendcounts = new int[proc_num];

		if (proc_id == 0)
		{
			Matrix = CreateMatrix(n, m);
			ShowMatrix(Matrix, n, m);
			partial_sum = new int[proc_num];
			for (int i = 0; i < proc_num; i++)
				partial_sum[i] = 0;


			time_start = clock();

			Matrix_vector = new int[n * m];
			for (int i = 0, k = 0; i < n; i++)
				for (int j = 0; j < m; j++, k++)
					Matrix_vector[k] = Matrix[i][j];

			partial_sum = new int[proc_num];

			for (int i = 0; i < proc_num; i++)
			{
				partial_sum[i] = 0;
				sendcounts[i] = (n * m) / proc_num;
				displs[i] = i * ((n * m) / proc_num);
			}

		}
		work_size = (n * m) / proc_num;

		MPI_Bcast(sendcounts, proc_num, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(displs, proc_num, MPI_INT, 0, MPI_COMM_WORLD);

		receve_vector = new int[work_size];
		partial_sum_part = new int[work_size];
		for (int i = 0; i < work_size; i++)
		{
			receve_vector[i] = 0;
			partial_sum_part[i] = 0;
		}

		MPI_Scatterv(Matrix_vector, sendcounts, displs, MPI_INT, receve_vector, work_size, MPI_INT, 0, MPI_COMM_WORLD);

		if (work_size * proc_id < m * n)
		{
			for (int i = 0; i < work_size; i++)
			{
				partial_sum_part[proc_id] += receve_vector[i];
				cout << proc_id << "   " << partial_sum_part[proc_id] << "  " << receve_vector[i] << endl;
			}
			cout << "partial_sum_part[" << proc_id << "] = " << partial_sum_part[proc_id] << endl;
		}
		MPI_Reduce(partial_sum_part, partial_sum, proc_num, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

		if (proc_id == 0)
		{
			for (int i = 0; i < proc_num; i++)
				parallel_sum += partial_sum[i];
			if (m * n % 2 != 0 && proc_num % 2 == 0)
			{
				parallel_sum += Matrix_vector[m * n - 1];
			}

			parall_time_work = clock() - time_start;

			cout << "Time of parallel algorithm = " << parall_time_work << " ms" << endl << endl;


			for (int i = 0; i < n; i++)
				delete[] Matrix[i];
			delete[] Matrix_vector;
			delete[] partial_sum;


		}
		delete[] receve_vector;
		delete[] partial_sum_part;
		delete[] sendcounts;
		delete[] displs;

		MPI_Barrier(MPI_COMM_WORLD);
	}
}