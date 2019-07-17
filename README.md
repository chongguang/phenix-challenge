# Introduction
This solution is written in Scala with SBT as the packaging tool. Source code is in the `solution` folder.

I copied the provided data files into the `solution/src/main/resources` folder and duplicate all the files with another date `20170510` in order to show that the solution can also calculate asked indications for the last 7 days.

# How to run it
To run this application, you need to have Scala and SBT installed on your machine.

#### Clone the project from github
```
git clone https://github.com/chongguang/phenix-challenge.git
```

#### Go to solution folder
```
cd solution
```

#### Run unit tests
```
sbt clean test
```

#### Package the project
```
sbt clean package
```
A jar will be generate in the target/scala-2.12/ folder.

#### Set RAM of JVM to 512M as asked in the exercise
```
export JAVA_OPTS="-Xmx512m"
```

#### Run the code and generate files
```
scala -classpath ./target/scala-2.12/phenix-challenge_2.12-0.1.0-SNAPSHOT.jar com.carrefour.App
```
Result files will be generate into the output folder. If the output folder does not exist, create it in advance.

# Observation
I found that it is impossible to find the price of the product for certain transactions. It is because that those transactions have the `product` value at 0. However in the product reference files, product ids' value start with 1 and can never be 0.

For these transactions, I have decided to ignore them for all the aggregations. Of course it is also possible to assign other values for their prices.

# Algorithm and Performance
Given the constraint of 512M of JVM memory, it is difficult to parse and keep all transactions and product information in memory.

I parse the transaction file and aggregate by product Id before finding price from reference files. By doing this we reduce the usage of memory.

For calculating `ventes` or `ca` for the past 7 days, it is difficult to load 7 days' transactions into memory in the same time. So we aggregate directly from the calculated files of each day. This solution is not always accurate but use much less memory.

The object `DataGen.scala` has been created for generating big files for testing purpose. It programme has been tested and is able to deal with files of 5+ million transactions under the constraint of 512M of memory. One potential improvement is to aggregate a transaction once it is read from file. The reason is that the Source Object is lazy but applying the toList() function loads everything into memory. 