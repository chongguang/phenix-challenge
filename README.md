# Introduction
This solution is written in Scala with SBT as the packaging tool. Source code is in the `solution` folder.

I copied the provided data files into the `solution/src/main/resources` folder and duplicate all the files with another date `20170510` in order to show that the solution can also calculate asked indications for the last 7 days.

# Observation
I found that it is impossible to find the price of the product for certain transactions. It is because that those transactions have the `product` value at 0. However in the product reference files, product ids' value start with 1 and can never be 0.

For these transactions, I have decided to ignore them for all the aggregations. Of course it is also possible to assign other values for their prices.

# How to run it
To run this application, you need to have Scala and SBT installed on your machine.

## Clone the project from github
```
git clone https://github.com/chongguang/phenix-challenge.git
```

## Go to solution folder
```
cd solution
```

## Package the project with SBT
```
sbt package
```
A jar will be generate in the target/scala-2.12/ folder.

## Run the code and generate files
```
scala -classpath ./target/scala-2.12/phenix-challenge_2.12-0.1.0-SNAPSHOT.jar com.carrefour.App
```
Result files will be generate into the output folder. If the output folder does not exist, create it in advance.


