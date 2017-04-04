# ExcelParser.java  
`ExcelParser` takes a non-deterministic approach to parsing `.xlsx` files. It allows the customer to not only submit an `.xslx` file which they would prefer, but they can insert, delete, and rearange their content in any with a vast degree of feedom and get a reliably populated database. Here is a page explaining what `ExcelParser` needs from an `.xlsx` file to populate the database. 

This java class is responsible for converting from an excel file (`.xlsx`),
to our mongo database. This documentation was prepared to walk through how and why we implemented this class the way we did.    

### Setup: 
In order to use this parser, you will need to setup **Apachi POI** with your project. To do this, you will need edit your server level `build.gradle` file. First, add this line into your `dependencies` collection. 

```gradle
compile 'org.apache.poi:poi-ooxml:3.15'
```
You will then need to refresh gradle. In **IntelliJ IDEA**, you can go to the *gradle window* and in the top left, press the blue refresh button, and **BAM YOU WIN**. 

In our constructor we pass in a boolean, `test`. 
This boolean will change the excel file to our test spreadsheet and populate the database so our excelParser tests know what the outputs should be. 

## Step 1: Extracting data from the xlsx document into a 2D Array  
In our main method, the first thing we do is call `extractFromXLSX()`.
This method is the only method we use that uses *Apache POI*.
The back bone for this method is from an [Apache POI example](http://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/) that prints all the content from an `xlsx` file.
We heavily modified it to put that data in a 2D string array.
When playing around with this first method, we learned some important things:  

1. The default size for an `xlsx` from a Microsoft Excel document is **1000 rows, and 25 columns**.
As a consequence, our array is 1000 rows tall, and 25 columns wide, and each non-filled index is `null`.
We spend a lot of time shrinking the size of this array.  
2. General 2D array facts, for a 2D array named `cellValues`:    
  * `cellValues.length` gives us the height of the array   
  * `cellValues[i].length` gives us the length of a given row, `i`.  

## Step 2: Horizontally Collapse the Array  
Because most of our 2D array is null at this point, we horizontally collapse the array to get rid of all columns that are just filled with nulls.
We could have collapsed both vertically and horizontally at the same time, but for read and write simplicity, we opted for doing each of these steps individually. There are two steps involved in this proccess; locating the column to collapse the array at, and actually collapsing the array.  

### Locating the collapse point: `collapseHorizontally()`  
In the example `xlsx` file below, there are three rows that are grayed out. We designate these three rows (rows 1 through 3) as *key rows*. When collapsing horizontally, we start at row one at the rightmost part of our 2D array. We check to see if any of the three rows in the column are not null. If they are null, we will shift one column to the left and repeat. We keep doing this process until we reach a cell that is not null. 

### Collapsing the array: `trimArrayHorizontally()`  
This method starts where `collapseHorizontally()` leaves off. Because there is no built in method to trim arrays, let alone 2D arrays, we built one!  It simply makes a new 2D array of a size specified by `collapseHorizontally()`, copies the old array into the new one and returns it.   

![HorizontalCollapse](Documentation/Graphics/HorizontalCorrected.png)  


## Step 3: Vertically Collapse the Array  
Vertically collapsing the array is much easier than collapsing it horizontally. We still use two steps to do this process.
### Locating the collapse point: `collapseVertically()` 
Our assumption for finding the vertical collapse point is that `column 0` is consistently populated for every row we care about. To find the collapse point, we simply iterate on `column 0` from the bottom of the array until we find a non-null cell. We select this index as the collapse point. 
### Collapsing the array: `trimArrayVertically()`  
Once we know our collapse point we use `trimArrayVertically()` in a similar fashion to `trimArrayHorizontally()`. 
We make a new 2D array as tall as `collapseVertically()` specifies and copy the old elements into it. 

![VerticalCollapse](Documentation/Graphics/VerticalCorrected.png)  


## Step 4: Replace Nulls with Empty Strings: `replaceNulls()`  
The method simply iterates through our 2D array and replaces all nulls with empty strings.
This prevents any null pointer exceptions in the future. 

![ReplaceNulls](Documentation/Graphics/ReplaceNulls.png)

## Step 5: Using *Key Rows* to generate Keys for Mongo Collections  
To do this, we use the `getKeys()` method. This method accomplishes two things:  
1. It dynamically makes a `String[]` of keys.  
2. It filters the keys to match terms defined by the standards commite (eg, # becomes `id`) and not break things. 

In order to make a `String[]` of keys, we iterate, column by column, through our key rows. For every column, we concatenate all the strings from each cell. In the table below, the key for that column would be `Common Name`, in the following table, the key would be `HB=Hang BasketC=ContainerW=Wall`.  

|Common Name|
|:---------:|
|    ""     |
|    ""     |

|HB=Hang Basket|
|:-------------|
|C=Container   |
|W=Wall        |  

`HB=Hang BasketC=ContainerW=Wall` is not a great for users or programmers alike.
There are some points in our project where passing this around can break things. For this reason we filter the keys. 
We remove spaces, and equal signs. This is also a good oportunity to make our keys match what is specified by the standards committee. We change keys like `#` to `id`, and `Common Name` to `commonName`.  

## Step 6: Populating the Database: `populateDatabase()`  
This method starts at `row 4` (the first row after the key rows). It works by moving from left to right across each row, and at every cell, adding that cell's value as the value of a hashmap with its corresponding key as the key. The hashmap will be added into a document that gets added directly into the database. After this, the method moves to the next row, and repeats until it is at the bottom of the array.  

**And that! Is how you turn any XLSX document into a populating database!** 
