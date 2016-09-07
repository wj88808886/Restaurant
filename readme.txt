Introduction:
This is a solution to this Operation System project. This is program that simulate a restaurant 6431. I implemented one thread for each arriving diner, which will compete for an available table. I implemented one thread for each cook, all of them active for the entire duration when the restaurant is open. Tables and machines for cooking the food are resources whose use must be coordinated among the threads.
Basically, I use something like lamport algorithm,which maintain each cook, dinner, table, and machine a local time that can sychronize time (here, we use the larger time) to solve the time problem. For each threads I use synchronized lock to lock some important things to avoid concurrency problem like tables, machines and waiting cooks.

Usage:
1. Import the whole project into Eclipse's workspace.You can Compile the Restaurant class and run. It will appear something like this:
enter the filename:
You are asked to enter the file.

2. Then for your input, put your files in the root directory and type the test input file name or the absolute path of your input file. Then press enter.

3. Then the program will automatically run to simulate the restaurant. Then when it is done. It will show below.
Done! please check the result file

4. Output is in the root directory, named: Result<whatever your input file name> For instance, if your input is TestData1.txt, Then your output file name will be ResultTestData1.txt.

5. My simulation output when each dinner arrived at the restaurant, was seated in which table and made what order.which cook processed their order, when which of the machines was used for their orders, and when the food was brought to their table. Finally, you should state the time when the last diner leaves the restaurant.

Test case:
All test cases information below and result was stored into corresponding output files.
1. Given test data1.
2. Given test data2
3. I copied from test data1 and make some swap among those dinners. So it does not maintain the arrival order.
4. I copied from test data1 and add a dinner who comes restaurant at 200 time. the restaurant has closed.
The output at console is below.

enter the filename:
TestData4.txt
dinner 8 arrives at the restaurant at time : 200
restaurant closed
Done! please check the result file
